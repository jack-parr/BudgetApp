/*
 * This is the initial JFrame that contains the menu bar and the current panel.
 */

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class AppFrame extends JFrame implements ActionListener{

    Config config = new Config();
    ArrayList<DataEntry> dataList;
    static HashMap<Integer, ArrayList<DataEntry>> listsHashMap;

    static JPanel currentPanel;
    MenuPanel menuPanel;
    ViewDataPanel expensesPanel;
    AddNewDataPanel newExpensePanel;
    
    final static String SUMMARY_ACTION_COMMAND = "summaryButton";
    final static String INCOME_ACTION_COMMAND = "incomeButton";
    final static String EXPENSES_ACTION_COMMAND = "expensesButton";
    final static String SAVINGS_ACTION_COMMAND = "savingsButton";
    final static String DATA_ACTION_COMMAND = "dataButton";

    AppFrame() {

        // TEMP TESTING - DELETE WHEN NOT NEEDED ANYMORE
        // ArrayList<Integer> testList = new ArrayList<>();
        // testList.add(1);
        // while (!testList.isEmpty()) {
        //     testList.removeAll(testList);
        //     System.out.println("Still running");
        //     System.out.println("Here too");
        // }

        // LOADING DATA
        dataList = CSVHandler.readDataFromCSV("tempDataIn.csv");
        // for (DataEntry dataEntry : dataList) {
        //     System.out.println(dataEntry);
        // }
        CSVHandler.assignIds(dataList);  // assigning IDs.
        listsHashMap = CSVHandler.createMonthLists(dataList);  // creating the listsHashMap.
        checkRecurringEntryGenerators(dataList);  // checking the generators.
        //System.out.println(dataList.get(0).getStartDate().plusDays(1));

        // SAVE DATA ON CLOSE
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {  // X window button is pressed.
                CSVHandler.saveDataToCSV(dataList, config.DATA_FILEPATH);  // saves data.
                System.exit(0);  // exits programme.
            }
        });
        
        this.setTitle("Expenses Tracker");
        this.setResizable(false);
        this.setLayout(new BorderLayout());

        // CREATING MENU PANEL
        menuPanel = new MenuPanel();

        menuPanel.summaryButton.addActionListener(this);
        menuPanel.summaryButton.setActionCommand(SUMMARY_ACTION_COMMAND);
        menuPanel.incomeButton.addActionListener(this);
        menuPanel.incomeButton.setActionCommand(INCOME_ACTION_COMMAND);
        menuPanel.expensesButton.addActionListener(this);
        menuPanel.expensesButton.setActionCommand(EXPENSES_ACTION_COMMAND);

        menuPanel.setPreferredSize(new Dimension(config.DISPLAY_WIDTH, config.MENU_HEIGHT));
        this.add(menuPanel, BorderLayout.NORTH);

        // CREATING CURRENT PANEL
        SummaryPanel summaryPanel = new SummaryPanel();  // defaults to summary panel.
        currentPanel = summaryPanel;
        currentPanel.setPreferredSize(new Dimension(config.DISPLAY_WIDTH, config.PANEL_HEIGHT));
        this.add(currentPanel, BorderLayout.SOUTH);

        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        String actionCommand = e.getActionCommand();

        // HANDLE CLICKING OF TAB BUTTONS

        switch (actionCommand) {
        
        case SUMMARY_ACTION_COMMAND:
            this.remove(currentPanel);
            createSummaryPanel();
            break;

        case INCOME_ACTION_COMMAND:
            this.remove(currentPanel);
            createIncomePanel();
            break;
            
        case EXPENSES_ACTION_COMMAND:
            this.remove(currentPanel);
            createExpensesPanel();
            break;

        default: 
            break;
        }

        // EXPENSES PANEL ACTIONS
        if (currentPanel instanceof ViewDataPanel) {
            
            // HANDLING YEAR CHANGE
            if (e.getSource() == expensesPanel.headerPanel.yearComboBox) {
                expensesPanel.remove(expensesPanel.dataPanel);  // remove old dataPanel.
                expensesPanel.dataPanel = new ViewDataSubPanel2((Integer) expensesPanel.headerPanel.yearComboBox.getSelectedItem());  // create new dataPanel.
                assignDeleteButtons();  // assign listeners to delete row buttons.
                expensesPanel.dataPanel.setPreferredSize(new Dimension(config.DISPLAY_WIDTH, config.PANEL_HEIGHT - ViewDataPanel.PANEL_HEADER_HEIGHT));
                expensesPanel.add(expensesPanel.dataPanel, BorderLayout.SOUTH);  // add the new dataPanel.
                expensesPanel.revalidate();
                expensesPanel.repaint();
            }

            // HANDLING NEW DATA BUTTON
            else if (e.getSource() == expensesPanel.headerPanel.newDataButton) {
                this.remove(currentPanel);  // removes expenses panel.
                createNewExpensePanel();
                this.revalidate();
                this.repaint();
            }

            // HANDLING DELETE ROW BUTTONS
            else if (actionCommand.startsWith("delete")) {
                deleteDataEntry(actionCommand.substring(6));  // calls the method for deleting DataEntry according to int id.
            }

        }

        // NEW EXPENSE PANEL ACTIONS
        if (currentPanel instanceof AddNewDataPanel) {
            
            if (e.getSource() == newExpensePanel.oneOffButton) {
                // Disables frequencyInput and endDateInput.
                newExpensePanel.frequencyHeading.setForeground(config.SECONDARY_TEXT_COLOR);
                newExpensePanel.frequencyInput.setSelectedIndex(-1);  // clears selection.
                newExpensePanel.frequencyInput.setEnabled(false);  // disables.
                newExpensePanel.startDateHeading.setText("Date: ");
                newExpensePanel.endDateHeading.setForeground(config.SECONDARY_TEXT_COLOR);
                newExpensePanel.endDateDayInput.setSelectedItem(newExpensePanel.startDateDayInput.getSelectedItem());
                newExpensePanel.endDateDayInput.setEnabled(false);
                newExpensePanel.endDateMonthInput.setSelectedItem(newExpensePanel.startDateMonthInput.getSelectedItem());
                newExpensePanel.endDateMonthInput.setEnabled(false);
                newExpensePanel.endDateYearInput.setSelectedItem(newExpensePanel.startDateYearInput.getSelectedItem());
                newExpensePanel.endDateYearInput.setEnabled(false);
            }

            else if (e.getSource() == newExpensePanel.recurringButton) {
                // Enables frequencyInput and endDateInput.
                newExpensePanel.frequencyHeading.setForeground(config.PRIMARY_TEXT_COLOR);
                newExpensePanel.frequencyInput.setEnabled(true);
                newExpensePanel.frequencyInput.setSelectedIndex(0);  // sets selection to first option.
                newExpensePanel.startDateHeading.setText("Start Date: ");
                newExpensePanel.endDateHeading.setForeground(config.PRIMARY_TEXT_COLOR);
                newExpensePanel.endDateDayInput.setEnabled(true);
                newExpensePanel.endDateMonthInput.setEnabled(true);
                newExpensePanel.endDateYearInput.setEnabled(true);
            }

            else if (e.getSource() == newExpensePanel.cancelButton) {
                this.remove(currentPanel);
                createExpensesPanel();
            }

            else if (e.getSource() == newExpensePanel.confirmButton) {
                addNewDataEntryFromUser();
            }

            // will need input checks to make sure all inputs are present and valid

            else if (actionCommand.substring(0, 8).equals("category")) {
                ((AddNewDataPanel) currentPanel).categoryInput.setText(actionCommand.substring(8));
            }

        }

    }

    public void createSummaryPanel() {

        // Creates and paints the summary panel.

        SummaryPanel summaryPanel = new SummaryPanel();
        currentPanel = summaryPanel;
        currentPanel.setPreferredSize(new Dimension(config.DISPLAY_WIDTH, config.PANEL_HEIGHT));
        this.add(currentPanel, BorderLayout.SOUTH);
        this.pack();
        this.repaint();

    }

    public void createIncomePanel() {

        // Creates and paints the income panel.

        IncomePanel incomePanel = new IncomePanel();
        currentPanel = incomePanel;
        currentPanel.setPreferredSize(new Dimension(config.DISPLAY_WIDTH, config.PANEL_HEIGHT));
        this.add(currentPanel, BorderLayout.SOUTH);
        this.pack();
        this.repaint();
        
    }

    public void createExpensesPanel() {

        // Creates and paints the expenses panel.

        expensesPanel = new ViewDataPanel();

        // SETTING ACTION LISTENERS
        expensesPanel.headerPanel.yearComboBox.addActionListener(this);
        expensesPanel.headerPanel.newDataButton.addActionListener(this);
        assignDeleteButtons();

        // PAINTING PANEL
        currentPanel = expensesPanel;
        currentPanel.setPreferredSize(new Dimension(config.DISPLAY_WIDTH, config.PANEL_HEIGHT));
        this.add(currentPanel, BorderLayout.SOUTH);
        this.pack();
        this.repaint();
        
    }

    public void createNewExpensePanel() {

        // Creates and paints the new data panel.

        newExpensePanel = new AddNewDataPanel();

        // SETTING ACTION LISTENERS
        newExpensePanel.oneOffButton.addActionListener(this);
        newExpensePanel.recurringButton.addActionListener(this);

        HashMap<String, JButton> shortcutButtonsMap = newExpensePanel.categoryShortcutButtonsMap;
        Object[] mapKeys = shortcutButtonsMap.keySet().toArray();  // makes an array of the keySet.
        for (Object key : mapKeys) {
            JButton categoryShortcutButton = (JButton) shortcutButtonsMap.get(key);
            categoryShortcutButton.addActionListener(this);
        }

        newExpensePanel.cancelButton.addActionListener(this);
        newExpensePanel.confirmButton.addActionListener(this);

        // PAINTING PANEL
        currentPanel = newExpensePanel;
        currentPanel.setPreferredSize(new Dimension(config.DISPLAY_WIDTH, config.PANEL_HEIGHT));
        this.add(currentPanel);
        this.revalidate();
        this.repaint();

    }

    public void assignDeleteButtons() {

        // Assigns listeners to the delete buttons in the expenses panel. This is a separate method so that changing the year can trigger it.

        HashMap<String, Component> deleteButtonsMap = expensesPanel.dataPanel.deleteButtonsMap;
        Object[] mapKeys = deleteButtonsMap.keySet().toArray();  // makes an array of the keySet.
        for (Object key : mapKeys) {
            JButton deleteButton = (JButton) deleteButtonsMap.get(key);
            deleteButton.addActionListener(this);
        }

    }

    public void deleteDataEntry(String id) {

        // Deletes the DataEntry corresponding to id.

        for (DataEntry dataEntry : dataList) {
            if (dataEntry.getId().equals(id)) {
                dataList.remove(dataEntry);
                break;
            }
        }

        listsHashMap = CSVHandler.createMonthLists(dataList);  // remakes the listsHashMap
        this.remove(currentPanel);  // removes the old expenses panel.
        createExpensesPanel();  // recreates the expenses panel.

    }

    public void addNewDataEntryFromUser() {

        // Adds a DataEntry based on information currently in the newExpensesPanel.

        // INPUT CHECKS
        System.out.println("UNIMPLEMENTED INPUT CHECKS");

        // ADDING DATA ENTRY
        String[] metadata = new String[9];  // creating metadata.
        metadata[0] = "0";  // placeholder id.
        metadata[1] = newExpensePanel.isExpenseButtonGroup.getSelection().getActionCommand();
        metadata[2] = newExpensePanel.isRecurringButtonGroup.getSelection().getActionCommand();
        metadata[3] = (String) newExpensePanel.frequencyInput.getSelectedItem();
        metadata[4] = newExpensePanel.startDateYearInput.getSelectedItem() + "-" + String.format("%02d", newExpensePanel.startDateMonthInput.getSelectedItem()) + "-" + String.format("%02d", newExpensePanel.startDateDayInput.getSelectedItem());  // date in correct format.
        metadata[5] = newExpensePanel.endDateYearInput.getSelectedItem() + "-" + String.format("%02d", newExpensePanel.endDateMonthInput.getSelectedItem()) + "-" + String.format("%02d", newExpensePanel.endDateDayInput.getSelectedItem());  // date in correct format.
        metadata[6] = metadata[4];  // this is irrelevant, just needs to be a readable date.
        metadata[7] = newExpensePanel.categoryInput.getText();  // category.
        metadata[8] = newExpensePanel.valueInput.getText();  // value.

        DataEntry newDataEntry = CSVHandler.createDataEntry(metadata);
        dataList.add(newDataEntry);

        // POST PROCESSING
        CSVHandler.assignIds(dataList);  // reassigning IDs.
        checkRecurringEntryGenerators(dataList);  // check for any due generators and handle these.
        listsHashMap = CSVHandler.createMonthLists(dataList);   // create monthLists.

        // change this for something in the app.
        System.out.println("New Data Successfully Added.");
        newExpensePanel.categoryInput.setText("");
        newExpensePanel.valueInput.setText("");

    }

    public void checkRecurringEntryGenerators(ArrayList<DataEntry> dataList) {

        // This checks all the recurring entry generators for if they're due a new DataEntry, and handles it.

        ArrayList<DataEntry> tempAddList = new ArrayList<>();
        tempAddList.add(dataList.get(0));  // add one to begin the while loop.

        while (!tempAddList.isEmpty()) {  // will end once a pass has been made with no additions.

            tempAddList.clear();

            for (DataEntry dataEntry : dataList) {
                if (dataEntry.isRecurring && dataEntry.getNextDueDate().isBefore(Collections.min(Arrays.asList(LocalDate.now().plusDays(1), dataEntry.getEndDate())))) {

                    // CREATE NEW ONE-OFF DataEntry
                    String[] metadata = new String[9];
                    metadata[0] = "0";  // placeholder id.
                    metadata[1] = Boolean.toString(dataEntry.getIsExpense());
                    metadata[2] = "false";  // makes it a one-off entry instead of a generator.
                    metadata[3] = "";  // frequency.
                    metadata[4] = dataEntry.getNextDueDate().toString();  // the due date in correct format.
                    metadata[5] = dataEntry.getNextDueDate().toString();  // the due date in correct format.
                    metadata[6] = metadata[4];  // this is irrelevant, just needs to be a readable date.
                    metadata[7] = dataEntry.getCategory();  // category.
                    metadata[8] = Float.toString(dataEntry.getValue());  // value.

                    DataEntry newDataEntry = CSVHandler.createDataEntry(metadata);
                    tempAddList.add(newDataEntry);

                    // UPDATING GENERATOR
                    switch (dataEntry.getFrequency()) {
                    case "Daily":
                        dataEntry.setStartDate(dataEntry.getNextDueDate());
                        dataEntry.setNextDueDate(dataEntry.getStartDate().plusDays(1));
                        break;
                    case "Weekly":
                        dataEntry.setStartDate(dataEntry.getNextDueDate());
                        dataEntry.setNextDueDate(dataEntry.getStartDate().plusWeeks(1));
                        break;
                    case "Fortnightly":
                        dataEntry.setStartDate(dataEntry.getNextDueDate());
                        dataEntry.setNextDueDate(dataEntry.getStartDate().plusWeeks(2));
                        break;
                    case "Monthly":
                        dataEntry.setStartDate(dataEntry.getNextDueDate());
                        dataEntry.setNextDueDate(dataEntry.getStartDate().plusMonths(1));
                        break;
                    case "Annually":
                        dataEntry.setStartDate(dataEntry.getNextDueDate());
                        dataEntry.setNextDueDate(dataEntry.getStartDate().plusYears(1));
                        break;
                    default:
                        break;
                    }
                }
            }

            if (!tempAddList.isEmpty()) {
                tempAddList.forEach(de -> dataList.add(de));
            }

        }

        CSVHandler.assignIds(dataList);  // reassigning IDs after all the new entries.

    }

}

