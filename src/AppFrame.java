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
import javax.swing.JScrollPane;

public class AppFrame extends JFrame implements ActionListener{

    Config config = new Config();
    ArrayList<DataEntry> dataList;
    static HashMap<Integer, ArrayList<DataEntry>> listsHashMap;

    static JPanel currentPanel;
    MenuPanel menuPanel;
    ViewDataPanel viewDataPanel;
    AddNewDataPanel addNewDataPanel;
    
    final static String SUMMARY_ACTION_COMMAND = "summaryButton";
    final static String INCOME_ACTION_COMMAND = "incomeButton";
    final static String VIEWDATA_ACTION_COMMAND = "viewDataButton";
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
        menuPanel.expensesButton.setActionCommand(VIEWDATA_ACTION_COMMAND);

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
            
        case VIEWDATA_ACTION_COMMAND:
            this.remove(currentPanel);
            createViewDataPanel();
            break;

        default: 
            break;
        }

        // VIEWDATA PANEL ACTIONS
        if (currentPanel instanceof ViewDataPanel) {
            
            // HANDLING YEAR CHANGE
            if (e.getSource() == viewDataPanel.headerPanel.yearComboBox) {
                viewDataPanel.remove(viewDataPanel.dataPanelScrollPane);  // remove old dataPanel.
                viewDataPanel.dataPanel = new ViewDataSubPanel2((Integer) viewDataPanel.headerPanel.yearComboBox.getSelectedItem());
                viewDataPanel.dataPanelScrollPane = new JScrollPane(viewDataPanel.dataPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                viewDataPanel.dataPanelScrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(viewDataPanel.dataPanel.SCROLL_BAR_WIDTH, 0));
                viewDataPanel.dataPanelScrollPane.getVerticalScrollBar().setUnitIncrement(10);
                viewDataPanel.dataPanelScrollPane.setBorder(null);
                assignDeleteButtons();  // assign listeners to delete row buttons.
                viewDataPanel.dataPanelScrollPane.setPreferredSize(new Dimension(config.DISPLAY_WIDTH, config.PANEL_HEIGHT - ViewDataPanel.PANEL_HEADER_HEIGHT));
                viewDataPanel.add(viewDataPanel.dataPanelScrollPane, BorderLayout.SOUTH);
                viewDataPanel.revalidate();
            }

            // HANDLING NEW DATA BUTTON
            else if (e.getSource() == viewDataPanel.headerPanel.newDataButton) {
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
            
            if (e.getSource() == addNewDataPanel.oneOffButton) {
                // Disables frequencyInput and endDateInput.
                addNewDataPanel.frequencyHeading.setForeground(config.SECONDARY_TEXT_COLOR);
                addNewDataPanel.frequencyInput.setSelectedIndex(-1);  // clears selection.
                addNewDataPanel.frequencyInput.setEnabled(false);  // disables.
                addNewDataPanel.startDateHeading.setText("Date: ");
                addNewDataPanel.endDateHeading.setForeground(config.SECONDARY_TEXT_COLOR);
                addNewDataPanel.endDateDayInput.setSelectedItem(addNewDataPanel.startDateDayInput.getSelectedItem());
                addNewDataPanel.endDateDayInput.setEnabled(false);
                addNewDataPanel.endDateMonthInput.setSelectedItem(addNewDataPanel.startDateMonthInput.getSelectedItem());
                addNewDataPanel.endDateMonthInput.setEnabled(false);
                addNewDataPanel.endDateYearInput.setSelectedItem(addNewDataPanel.startDateYearInput.getSelectedItem());
                addNewDataPanel.endDateYearInput.setEnabled(false);
            }

            else if (e.getSource() == addNewDataPanel.recurringButton) {
                // Enables frequencyInput and endDateInput.
                addNewDataPanel.frequencyHeading.setForeground(config.PRIMARY_TEXT_COLOR);
                addNewDataPanel.frequencyInput.setEnabled(true);
                addNewDataPanel.frequencyInput.setSelectedIndex(0);  // sets selection to first option.
                addNewDataPanel.startDateHeading.setText("Start Date: ");
                addNewDataPanel.endDateHeading.setForeground(config.PRIMARY_TEXT_COLOR);
                addNewDataPanel.endDateDayInput.setEnabled(true);
                addNewDataPanel.endDateMonthInput.setEnabled(true);
                addNewDataPanel.endDateYearInput.setEnabled(true);
            }

            else if (e.getSource() == addNewDataPanel.closeButton) {
                this.remove(currentPanel);
                createViewDataPanel();
            }

            else if (e.getSource() == addNewDataPanel.confirmButton) {
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

    public void createViewDataPanel() {

        // Creates and paints the expenses panel.

        viewDataPanel = new ViewDataPanel();

        // SETTING ACTION LISTENERS
        viewDataPanel.headerPanel.yearComboBox.addActionListener(this);
        viewDataPanel.headerPanel.newDataButton.addActionListener(this);
        assignDeleteButtons();

        // PAINTING PANEL
        currentPanel = viewDataPanel;
        currentPanel.setPreferredSize(new Dimension(config.DISPLAY_WIDTH, config.PANEL_HEIGHT));
        this.add(currentPanel, BorderLayout.SOUTH);
        this.pack();
        this.repaint();
        
    }

    public void createNewExpensePanel() {

        // Creates and paints the new data panel.

        addNewDataPanel = new AddNewDataPanel();

        // SETTING ACTION LISTENERS
        addNewDataPanel.oneOffButton.addActionListener(this);
        addNewDataPanel.recurringButton.addActionListener(this);

        HashMap<String, JButton> shortcutButtonsMap = addNewDataPanel.categoryShortcutButtonsMap;
        Object[] mapKeys = shortcutButtonsMap.keySet().toArray();  // makes an array of the keySet.
        for (Object key : mapKeys) {
            JButton categoryShortcutButton = (JButton) shortcutButtonsMap.get(key);
            categoryShortcutButton.addActionListener(this);
        }

        addNewDataPanel.closeButton.addActionListener(this);
        addNewDataPanel.confirmButton.addActionListener(this);

        // PAINTING PANEL
        currentPanel = addNewDataPanel;
        currentPanel.setPreferredSize(new Dimension(config.DISPLAY_WIDTH, config.PANEL_HEIGHT));
        this.add(currentPanel);
        this.revalidate();
        this.repaint();

    }

    public void assignDeleteButtons() {

        // Assigns listeners to the delete buttons in the expenses panel. This is a separate method so that changing the year can trigger it.

        HashMap<String, Component> deleteButtonsMap = viewDataPanel.dataPanel.deleteButtonsMap;
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
        createViewDataPanel();  // recreates the expenses panel.

    }

    public void addNewDataEntryFromUser() {

        // Adds a DataEntry based on information currently in the newExpensesPanel.

        // INPUT CHECKS
        System.out.println("UNIMPLEMENTED INPUT CHECKS");

        // ADDING DATA ENTRY
        String[] metadata = new String[9];  // creating metadata.
        metadata[0] = "0";  // placeholder id.
        metadata[1] = addNewDataPanel.isExpenseButtonGroup.getSelection().getActionCommand();
        metadata[2] = addNewDataPanel.isRecurringButtonGroup.getSelection().getActionCommand();
        metadata[3] = (String) addNewDataPanel.frequencyInput.getSelectedItem();
        metadata[4] = addNewDataPanel.startDateYearInput.getSelectedItem() + "-" + String.format("%02d", addNewDataPanel.startDateMonthInput.getSelectedItem()) + "-" + String.format("%02d", addNewDataPanel.startDateDayInput.getSelectedItem());  // date in correct format.
        metadata[5] = addNewDataPanel.endDateYearInput.getSelectedItem() + "-" + String.format("%02d", addNewDataPanel.endDateMonthInput.getSelectedItem()) + "-" + String.format("%02d", addNewDataPanel.endDateDayInput.getSelectedItem());  // date in correct format.
        metadata[6] = metadata[4];  // this is irrelevant, just needs to be a readable date.
        metadata[7] = addNewDataPanel.categoryInput.getText();  // category.
        metadata[8] = addNewDataPanel.valueInput.getText();  // value.

        DataEntry newDataEntry = CSVHandler.createDataEntry(metadata);
        dataList.add(newDataEntry);

        // POST PROCESSING
        CSVHandler.assignIds(dataList);  // reassigning IDs.
        checkRecurringEntryGenerators(dataList);  // check for any due generators and handle these.
        listsHashMap = CSVHandler.createMonthLists(dataList);   // create monthLists.

        // change this for something in the app.
        System.out.println("New Data Successfully Added.");
        addNewDataPanel.categoryInput.setText("");
        addNewDataPanel.valueInput.setText("");

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

