/*
 * This is the initial JFrame that contains the menu bar and the current panel.
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.stream.Collectors;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class AppFrame extends JFrame implements ActionListener, MouseListener{

    Config config = new Config();
    static ArrayList<DataEntry> dataList;
    static HashMap<Integer, ArrayList<DataEntry>> listsHashMap;

    static JPanel currentPanel;
    MenuPanel menuPanel;
    SummaryPanel summaryPanel;
    ViewGeneratorsPanel viewGeneratorsPanel;
    ViewDataPanel viewDataPanel;
    AddNewDataPanel addNewDataPanel;
    
    final static String SUMMARY_ACTION_COMMAND = "summaryButton";
    final static String VIEW_GENERATORS_ACTION_COMMAND = "viewGeneratorsButton";
    final static String VIEW_DATA_ACTION_COMMAND = "viewDataButton";

    AppFrame() {

        // LOADING DATA
        dataList = DataHandler.readDataFromCSV(config.DATA_FILEPATH);
        DataHandler.assignIds(dataList);  // assigning IDs.
        checkRecurringEntryGenerators(dataList, true);  // checking the generators.
        listsHashMap = DataHandler.createMonthLists(dataList);  // creating the listsHashMap.

        // SAVE DATA ON CLOSE
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {  // X window button is pressed.
                DataHandler.saveDataToCSV(dataList, config.DATA_FILEPATH);  // saves data.
                System.exit(0);  // exits programme.
            }
        });
        
        this.setTitle("Finances Tracker");
        this.setResizable(false);
        this.setLayout(new BorderLayout());

        // CREATING MENU PANEL
        menuPanel = new MenuPanel();

        menuPanel.summaryButton.addActionListener(this);
        menuPanel.summaryButton.setActionCommand(SUMMARY_ACTION_COMMAND);
        menuPanel.viewGeneratorsButton.addActionListener(this);
        menuPanel.viewGeneratorsButton.setActionCommand(VIEW_GENERATORS_ACTION_COMMAND);
        menuPanel.viewDataButton.addActionListener(this);
        menuPanel.viewDataButton.setActionCommand(VIEW_DATA_ACTION_COMMAND);

        menuPanel.setPreferredSize(new Dimension(config.DISPLAY_WIDTH, config.MENU_HEIGHT));
        this.add(menuPanel, BorderLayout.NORTH);

        // CREATING CURRENT PANEL
        createSummaryPanel();  // default is SummaryPanel.

        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);

    }

    @Override
    public void mouseClicked(MouseEvent e) {

        // Handles the mouse click on the GraphFrontPane.

        float xCoord = e.getX() - summaryPanel.graphPanel.MARGIN;  // into cartesian coords.
        float yCoord = summaryPanel.graphPanel.MARGIN + summaryPanel.graphPanel.graphHeight - e.getY();
        if (xCoord>0 && xCoord<summaryPanel.graphPanel.graphWidth && yCoord>0 && yCoord<summaryPanel.graphPanel.graphHeight) {
            summaryPanel.graphClick(xCoord, yCoord);
        }

    }

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    @Override
    public void actionPerformed(ActionEvent e) {

        String actionCommand = e.getActionCommand();

        // HANDLING CLICKING OF TAB BUTTONS
        switch (actionCommand) {
        
        case SUMMARY_ACTION_COMMAND:
            this.remove(currentPanel);
            createSummaryPanel();
            break;

        case VIEW_GENERATORS_ACTION_COMMAND:
            this.remove(currentPanel);
            createViewGeneratorsPanel("Expense");
            break;
            
        case VIEW_DATA_ACTION_COMMAND:
            this.remove(currentPanel);
            createViewDataPanel(0);
            break;

        default: 
            break;

        }

        // SUMMARY PANEL ACTIONS
        if (currentPanel instanceof SummaryPanel) {

            // HANDLING PERIOD CHANGE
            if (e.getSource() == summaryPanel.periodComboBox) {
                // RESETTING ALL LABEL COLORS
                summaryPanel.startDateLabel.setForeground(config.PRIMARY_TEXT_COLOR);
                summaryPanel.endDateLabel.setForeground(config.PRIMARY_TEXT_COLOR);
                summaryPanel.systemResponseLabel.setText("Click Graph");
                
                // PROCESSING DECISION
                String todayDateString = LocalDate.now().format(config.DATE_TIME_FORMATTER);
                switch ((String) summaryPanel.periodComboBox.getSelectedItem()) {
                case "This Month":
                    setCustomPeriodComponents(false);
                    summaryPanel.startDateInput.setText(LocalDate.now().minusDays(LocalDate.now().getDayOfMonth() - 1).format(config.DATE_TIME_FORMATTER));
                    summaryPanel.endDateInput.setText(LocalDate.now().plusDays(LocalDate.now().lengthOfMonth() - LocalDate.now().getDayOfMonth()).format(config.DATE_TIME_FORMATTER));
                    break;
                case "Last Month":
                    setCustomPeriodComponents(false);
                    summaryPanel.startDateInput.setText(LocalDate.now().minusMonths(1).format(config.DATE_TIME_FORMATTER));
                    summaryPanel.endDateInput.setText(todayDateString);
                    break;
                case "Last 3 Months":
                    setCustomPeriodComponents(false);
                    summaryPanel.startDateInput.setText(LocalDate.now().minusMonths(3).format(config.DATE_TIME_FORMATTER));
                    summaryPanel.endDateInput.setText(todayDateString);
                    break;
                case "Last 6 Months":
                    setCustomPeriodComponents(false);
                    summaryPanel.startDateInput.setText(LocalDate.now().minusMonths(6).format(config.DATE_TIME_FORMATTER));
                    summaryPanel.endDateInput.setText(todayDateString);
                    break;
                case "Last 12 Months":
                    setCustomPeriodComponents(false);
                    summaryPanel.startDateInput.setText(LocalDate.now().minusYears(1).format(config.DATE_TIME_FORMATTER));
                    summaryPanel.endDateInput.setText(todayDateString);
                    break;
                case "All-Time":
                    setCustomPeriodComponents(false);
                    summaryPanel.startDateInput.setText(Collections.min(dataList.stream().map(de -> de.getStartDate()).collect(Collectors.toList())).format(config.DATE_TIME_FORMATTER));
                    summaryPanel.endDateInput.setText(todayDateString);
                    break;
                case "Custom":
                    setCustomPeriodComponents(true);
                    break;
                }
                summaryPanel.remove(summaryPanel.graphPanel);  // remove old graphPanel.
                summaryPanel.graphPanel = new GraphPanel(summaryPanel.startDateInput.getText(), summaryPanel.endDateInput.getText());
                summaryPanel.graphPanel.addMouseListener(this);
                summaryPanel.graphPanel.setPreferredSize(new Dimension(config.DISPLAY_WIDTH, config.PANEL_HEIGHT - config.PANEL_HEADER_HEIGHT - (2*config.PANEL_Y_GAP)));
                summaryPanel.add(summaryPanel.graphPanel);
                summaryPanel.revalidate();
            }

            // HANDLING APPLY CUSTOM DATES BUTTON
            if (e.getSource() == summaryPanel.applyCustomPeriodButton) {
                // INPUT CHECKS
                boolean checksPassed = false;
                checksPassed = checkUserInputsCustomDates();
                if (checksPassed) {
                    summaryPanel.remove(summaryPanel.graphPanel);  // remove old graphPanel.
                    summaryPanel.graphPanel = new GraphPanel(summaryPanel.startDateInput.getText(), summaryPanel.endDateInput.getText());
                    summaryPanel.graphPanel.addMouseListener(this);
                    summaryPanel.graphPanel.setPreferredSize(new Dimension(config.DISPLAY_WIDTH, config.PANEL_HEIGHT - config.PANEL_HEADER_HEIGHT - (2*config.PANEL_Y_GAP)));
                    summaryPanel.add(summaryPanel.graphPanel);
                    summaryPanel.revalidate();
                }
            }
        
        }

        // GENERATORS PANEL ACTIONS
        if (currentPanel instanceof ViewGeneratorsPanel) {

            // HANDLING YEAR CHANGE
            if (e.getSource() == viewGeneratorsPanel.typeComboBox) {
                viewGeneratorsPanel.remove(viewGeneratorsPanel.generatorsPanelScrollPane);  // remove old dataPanel.
                viewGeneratorsPanel.generatorsPanel = new GeneratorsPanel(viewGeneratorsPanel.typeComboBox.getSelectedItem().equals("Expense"));
                viewGeneratorsPanel.generatorsPanelScrollPane = new JScrollPane(viewGeneratorsPanel.generatorsPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                viewGeneratorsPanel.generatorsPanelScrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(config.SCROLLBAR_WIDTH, 0));
                viewGeneratorsPanel.generatorsPanelScrollPane.getVerticalScrollBar().setUnitIncrement(config.SCROLLBAR_INCREMENT);
                viewGeneratorsPanel.generatorsPanelScrollPane.setBorder(null);
                assignDeleteButtons(viewGeneratorsPanel);  // assign listeners to delete row buttons.
                viewGeneratorsPanel.generatorsPanelScrollPane.setPreferredSize(new Dimension(config.DISPLAY_WIDTH, config.PANEL_HEIGHT - config.PANEL_HEADER_HEIGHT - (2*config.PANEL_Y_GAP)));
                viewGeneratorsPanel.add(viewGeneratorsPanel.generatorsPanelScrollPane, BorderLayout.SOUTH);
                viewGeneratorsPanel.revalidate();
            }

            // HANDLING NEW DATA BUTTON
            else if (e.getSource() == viewGeneratorsPanel.newDataButton) {
                this.remove(currentPanel);
                createAddNewDataPanel(currentPanel);
                this.revalidate();
                this.repaint();
            }

            // HANDLING DELETE ROW BUTTONS
            else if (actionCommand.startsWith("delete")) {
                deleteDataEntry(actionCommand.substring(6), viewGeneratorsPanel, (String) viewGeneratorsPanel.typeComboBox.getSelectedItem(), false);  // calls the method for deleting DataEntry according to int id.
            }

        }

        // VIEWDATA PANEL ACTIONS
        if (currentPanel instanceof ViewDataPanel) {
            
            // HANDLING YEAR CHANGE
            if (e.getSource() == viewDataPanel.yearComboBox) {
                remakeDataPanel();
            }

            // HANDLING NEW DATA BUTTON
            else if (e.getSource() == viewDataPanel.newDataButton) {
                this.remove(currentPanel);
                createAddNewDataPanel(currentPanel);
                this.revalidate();
                this.repaint();
            }

            // HANDLING APPLY FILTER BUTTON
            else if (e.getSource() == viewDataPanel.applyFilterButton) {
                viewDataPanel.remove(viewDataPanel.dataPanelScrollPane);  // remove old dataPanel.
                remakeDataPanel();
            }

            // HANDLING RESET FILTER BUTTON
            else if (e.getSource() == viewDataPanel.resetFilterButton) {
                viewDataPanel.filterInput.setText("");
                remakeDataPanel();
            }

            // HANDLING DELETE ROW BUTTONS
            else if (actionCommand.startsWith("delete")) {
                deleteDataEntry(actionCommand.substring(6), viewDataPanel, Integer.toString((Integer) viewDataPanel.yearComboBox.getSelectedItem()), false);  // calls the method for deleting DataEntry according to int id.
            }

        }

        // ADD NEW DATA PANEL ACTIONS
        if (currentPanel instanceof AddNewDataPanel) {

            if (e.getSource() == addNewDataPanel.incomeButton) {
                addNewDataPanel.incomeButton.setBorder(config.SELECTED_BUTTON_BORDER);
                addNewDataPanel.expenseButton.setBorder(null);
            }

            else if (e.getSource() == addNewDataPanel.expenseButton) {
                addNewDataPanel.incomeButton.setBorder(null);
                addNewDataPanel.expenseButton.setBorder(config.SELECTED_BUTTON_BORDER);
            }
            
            else if (e.getSource() == addNewDataPanel.oneOffButton) {
                addNewDataPanel.oneOffButton.setBorder(config.SELECTED_BUTTON_BORDER);
                addNewDataPanel.recurringButton.setBorder(null);
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
                
                // CATEGORY INPUT
                addNewDataPanel.remove(addNewDataPanel.categoryInputPanel);
                addNewDataPanel.paintCategoryInput(false);
                HashMap<String, JButton> shortcutButtonsMap = addNewDataPanel.categoryShortcutButtonsMap;
                Object[] mapKeys = shortcutButtonsMap.keySet().toArray();  // makes an array of the keySet.
                for (Object key : mapKeys) {
                    JButton categoryShortcutButton = (JButton) shortcutButtonsMap.get(key);
                    categoryShortcutButton.addActionListener(this);
                }
            }

            else if (e.getSource() == addNewDataPanel.recurringButton) {
                addNewDataPanel.oneOffButton.setBorder(null);
                addNewDataPanel.recurringButton.setBorder(config.SELECTED_BUTTON_BORDER);
                // Enables frequencyInput and endDateInput.
                addNewDataPanel.frequencyHeading.setForeground(config.PRIMARY_TEXT_COLOR);
                addNewDataPanel.frequencyInput.setEnabled(true);
                addNewDataPanel.frequencyInput.setSelectedIndex(0);  // sets selection to first option.
                addNewDataPanel.startDateHeading.setText("Start Date: ");
                addNewDataPanel.endDateHeading.setForeground(config.PRIMARY_TEXT_COLOR);
                addNewDataPanel.endDateDayInput.setEnabled(true);
                addNewDataPanel.endDateMonthInput.setEnabled(true);
                addNewDataPanel.endDateYearInput.setEnabled(true);

                // CATEGORY INPUT
                addNewDataPanel.remove(addNewDataPanel.categoryInputPanel);
                addNewDataPanel.paintCategoryInput(true);
                HashMap<String, JButton> shortcutButtonsMap = addNewDataPanel.categoryShortcutButtonsMap;
                Object[] mapKeys = shortcutButtonsMap.keySet().toArray();  // makes an array of the keySet.
                for (Object key : mapKeys) {
                    JButton categoryShortcutButton = (JButton) shortcutButtonsMap.get(key);
                    categoryShortcutButton.addActionListener(this);
                }
            }

            // HANDLING CLOSE BUTTON
            else if (e.getSource() == addNewDataPanel.closeButton) {
                this.remove(currentPanel);
                if (addNewDataPanel.sourcePanel instanceof ViewDataPanel) {
                    createViewDataPanel(0);
                }
                else if (addNewDataPanel.sourcePanel instanceof ViewGeneratorsPanel) {
                    createViewGeneratorsPanel("Expense");
                }
                
            }

            // HANDLING CONFIRM BUTTON
            else if (e.getSource() == addNewDataPanel.confirmButton) {
                // FIXING END DATE IF ONE-OFF ENTRY
                if (!Boolean.parseBoolean(addNewDataPanel.isRecurringButtonGroup.getSelection().getActionCommand())) {
                    addNewDataPanel.endDateDayInput.setSelectedItem(addNewDataPanel.startDateDayInput.getSelectedItem());
                    addNewDataPanel.endDateMonthInput.setSelectedItem(addNewDataPanel.startDateMonthInput.getSelectedItem());
                    addNewDataPanel.endDateYearInput.setSelectedItem(addNewDataPanel.startDateYearInput.getSelectedItem());
                }
                addNewDataEntryFromUser();
            }

            // HANDLING CATEGORY SHORTCUT BUTTONS
            else if (actionCommand.substring(0, 8).equals("category")) {
                ((AddNewDataPanel) currentPanel).categoryInput.setText(actionCommand.substring(8));
            }

        }

    }

    private void createSummaryPanel() {

        // Creates and paints the summary panel.

        summaryPanel = new SummaryPanel();

        // SETTING ACTION LISTENERS
        summaryPanel.periodComboBox.addActionListener(this);
        summaryPanel.applyCustomPeriodButton.addActionListener(this);
        summaryPanel.graphPanel.addMouseListener(this);

        currentPanel = summaryPanel;
        currentPanel.setPreferredSize(new Dimension(config.DISPLAY_WIDTH, config.PANEL_HEIGHT));
        this.add(currentPanel, BorderLayout.SOUTH);
        this.pack();
        this.repaint();

    }

    private void setCustomPeriodComponents(boolean bool) {

        // This toggles the custom period components within the SummaryPanel.

        summaryPanel.startDateInput.setEnabled(bool);
        summaryPanel.endDateInput.setEnabled(bool);
        summaryPanel.applyCustomPeriodButton.setEnabled(bool);

    }

    private boolean checkUserInputsCustomDates() {

        // Checks the user inputs when inputting a custom time period to view on the graph in SummaryPanel.

        String negativeReponse = "Error - Please Review Dates";
        LocalDate startDateInput;
        LocalDate endDateInput;

        // RESETTING ALL LABEL COLORS
        summaryPanel.startDateLabel.setForeground(config.PRIMARY_TEXT_COLOR);
        summaryPanel.endDateLabel.setForeground(config.PRIMARY_TEXT_COLOR);

        // START DATE VALID
        try {
            startDateInput = LocalDate.parse(summaryPanel.startDateInput.getText(), config.DATE_TIME_FORMATTER);
        } catch (Exception e) {
            summaryPanel.startDateLabel.setForeground(Color.red);
            summaryPanel.systemResponseLabel.setText(negativeReponse);
            return false;
        }

        // END DATE VALID
        try {
            endDateInput = LocalDate.parse(summaryPanel.endDateInput.getText(), config.DATE_TIME_FORMATTER);
        } catch (Exception e) {
            summaryPanel.endDateLabel.setForeground(Color.red);
            summaryPanel.systemResponseLabel.setText(negativeReponse);
            return false;
        }

        // START DATE BEFORE END DATE
        if (!startDateInput.isBefore(endDateInput)) {
            summaryPanel.startDateLabel.setForeground(Color.red);
            summaryPanel.endDateLabel.setForeground(Color.red);
            summaryPanel.systemResponseLabel.setText(negativeReponse);
            return false;
        }
        
        // ALL CHECKS PASSED
        summaryPanel.systemResponseLabel.setText("Click Graph");
        return true;
        
    }

    private void createViewGeneratorsPanel(String selectedType) {

        // Creates and paints the viewGeneratorsPanel.

        viewGeneratorsPanel = new ViewGeneratorsPanel(selectedType);

        // SETTING ACTION LISTENERS
        viewGeneratorsPanel.typeComboBox.addActionListener(this);
        viewGeneratorsPanel.newDataButton.addActionListener(this);
        assignDeleteButtons(viewGeneratorsPanel);

        currentPanel = viewGeneratorsPanel;
        currentPanel.setPreferredSize(new Dimension(config.DISPLAY_WIDTH, config.PANEL_HEIGHT));
        this.add(currentPanel, BorderLayout.SOUTH);
        this.pack();
        this.repaint();
        
    }

    private void createViewDataPanel(int selectedYear) {

        // Creates and paints the viewDataPanel.

        viewDataPanel = new ViewDataPanel(selectedYear);

        // SETTING ACTION LISTENERS
        viewDataPanel.yearComboBox.addActionListener(this);
        viewDataPanel.newDataButton.addActionListener(this);
        viewDataPanel.applyFilterButton.addActionListener(this);
        viewDataPanel.resetFilterButton.addActionListener(this);
        assignDeleteButtons(viewDataPanel);

        // PAINTING PANEL
        currentPanel = viewDataPanel;
        currentPanel.setPreferredSize(new Dimension(config.DISPLAY_WIDTH, config.PANEL_HEIGHT));
        this.add(currentPanel, BorderLayout.SOUTH);
        this.pack();
        this.repaint();
        
    }

    private void remakeDataPanel() {

        // Removes the old dataPanel and replaces it with newly specified conditions.

        viewDataPanel.remove(viewDataPanel.dataPanelScrollPane);  // remove old dataPanel.
        viewDataPanel.dataPanel = new DataPanel((Integer) viewDataPanel.yearComboBox.getSelectedItem(), viewDataPanel.filterInput.getText());
        viewDataPanel.dataPanelScrollPane = new JScrollPane(viewDataPanel.dataPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        viewDataPanel.dataPanelScrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(config.SCROLLBAR_WIDTH, 0));
        viewDataPanel.dataPanelScrollPane.getVerticalScrollBar().setUnitIncrement(config.SCROLLBAR_INCREMENT);
        viewDataPanel.dataPanelScrollPane.setBorder(null);
        assignDeleteButtons(viewDataPanel);  // assign listeners to delete row buttons.
        viewDataPanel.dataPanelScrollPane.setPreferredSize(new Dimension(config.DISPLAY_WIDTH, config.PANEL_HEIGHT - config.PANEL_HEADER_HEIGHT - (2*config.PANEL_Y_GAP)));
        viewDataPanel.add(viewDataPanel.dataPanelScrollPane, BorderLayout.SOUTH);
        viewDataPanel.revalidate();

    }

    private void assignDeleteButtons(JPanel createdPanel) {

        // Assigns listeners to the delete buttons in the created panel. This is a separate method so that JComboBox can also trigger it.

        HashMap<String, Component> deleteButtonsMap = new HashMap<>();  // temporary initialise to remove errors.

        if (createdPanel instanceof ViewGeneratorsPanel) {
            deleteButtonsMap = ((ViewGeneratorsPanel) createdPanel).generatorsPanel.deleteButtonsMap;
        }
        else if (createdPanel instanceof ViewDataPanel) {
            deleteButtonsMap = ((ViewDataPanel) createdPanel).dataPanel.deleteButtonsMap;
        }
        
        Object[] mapKeys = deleteButtonsMap.keySet().toArray();  // makes an array of the keySet.
        for (Object key : mapKeys) {
            JButton deleteButton = (JButton) deleteButtonsMap.get(key);
            deleteButton.addActionListener(this);
        }

    }

    private void deleteDataEntry(String id, JPanel sourcePanel, String comboBoxSelection, boolean startUp) {

        // Deletes the DataEntry corresponding to id.

        for (DataEntry dataEntry : dataList) {
            if (dataEntry.getId().equals(id)) {
                dataList.remove(dataEntry);
                break;
            }
        }

        listsHashMap = DataHandler.createMonthLists(dataList);  // remakes the listsHashMap

        if (!startUp) {

            if (sourcePanel instanceof SummaryPanel) {
                this.remove(currentPanel);  // removes the old panel.
                createSummaryPanel();  // recreates summary panel if occuring during startup.
            }

            if (sourcePanel instanceof ViewGeneratorsPanel) {
                this.remove(currentPanel);  // removes the old panel.
                createViewGeneratorsPanel(comboBoxSelection);  // recreates ViewGeneratorsPanel.
            }
            else if (sourcePanel instanceof ViewDataPanel) {
                viewDataPanel.remove(viewDataPanel.dataPanelScrollPane);  // remove old dataPanel.
                remakeDataPanel();
            }
        }
        
        

    }

    private void createAddNewDataPanel(JPanel sourcePanel) {

        // Creates and paints the new data panel.

        addNewDataPanel = new AddNewDataPanel(sourcePanel);

        // SETTING ACTION LISTENERS
        addNewDataPanel.incomeButton.addActionListener(this);
        addNewDataPanel.expenseButton.addActionListener(this);
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

    private void addNewDataEntryFromUser() {

        // Adds a DataEntry based on information currently in the newExpensesPanel.

        // INPUT CHECKS
        boolean checksPassed = false;
        checksPassed = checkUserInputsAddNewData();
        if (checksPassed) {

            // ADDING DATA ENTRY
            String[] metadata = new String[9];  // creating metadata.
            metadata[0] = "0";  // placeholder id.
            metadata[1] = addNewDataPanel.isExpenseButtonGroup.getSelection().getActionCommand();
            metadata[2] = addNewDataPanel.isRecurringButtonGroup.getSelection().getActionCommand();
            metadata[3] = (String) addNewDataPanel.frequencyInput.getSelectedItem();
            metadata[4] = addNewDataPanel.startDateYearInput.getSelectedItem() + "-" + String.format("%02d", addNewDataPanel.startDateMonthInput.getSelectedItem()) + "-" + String.format("%02d", addNewDataPanel.startDateDayInput.getSelectedItem());  // date in correct format.
            metadata[5] = addNewDataPanel.endDateYearInput.getSelectedItem() + "-" + String.format("%02d", addNewDataPanel.endDateMonthInput.getSelectedItem()) + "-" + String.format("%02d", addNewDataPanel.endDateDayInput.getSelectedItem());  // date in correct format.
            metadata[6] = metadata[4];  // this is so that recurring entry generators start with the first startDate.
            metadata[7] = addNewDataPanel.categoryInput.getText();  // category.
            metadata[8] = addNewDataPanel.valueInput.getText();  // value.

            DataEntry newDataEntry = DataHandler.createDataEntry(metadata);
            dataList.add(newDataEntry);

            // POST PROCESSING
            DataHandler.assignIds(dataList);  // reassigning IDs.
            checkRecurringEntryGenerators(dataList, false);  // check for any due generators and handle these.
            listsHashMap = DataHandler.createMonthLists(dataList);   // create monthLists.

            // change this for something in the app.
            addNewDataPanel.categoryInput.setText("");
            addNewDataPanel.valueInput.setText("");

        }

    }

    private boolean checkUserInputsAddNewData() {

        // Checks the user inputs when attempting to add a new data entry from the AddNewDataPanel. 

        String positiveResponse = "New Data Entry Successfully Added";
        String negativeResponse = "Error - Please Review Inputs";
        LocalDate startDateInput;
        LocalDate endDateInput;
        Float valueInput;

        // RESETTING ALL LABEL COLORS
        addNewDataPanel.startDateHeading.setForeground(config.PRIMARY_TEXT_COLOR);
        if (addNewDataPanel.recurringButton.isSelected()) {
            addNewDataPanel.endDateHeading.setForeground(config.PRIMARY_TEXT_COLOR);
        }
        else {addNewDataPanel.endDateHeading.setForeground(config.SECONDARY_TEXT_COLOR);}
        addNewDataPanel.categoryHeading.setForeground(config.PRIMARY_TEXT_COLOR);
        addNewDataPanel.valueHeading.setForeground(config.PRIMARY_TEXT_COLOR);

        // START DATE VALID
        try {
            startDateInput = LocalDate.of((int) addNewDataPanel.startDateYearInput.getSelectedItem(), (int) addNewDataPanel.startDateMonthInput.getSelectedItem(), (int) addNewDataPanel.startDateDayInput.getSelectedItem());
        } catch (Exception e) {
            addNewDataPanel.systemResponseLabel.setText(negativeResponse);
            addNewDataPanel.startDateHeading.setForeground(Color.red);
            return false;
        }
        
        // END DATE VALID
        try {
            endDateInput = LocalDate.of((int) addNewDataPanel.endDateYearInput.getSelectedItem(), (int) addNewDataPanel.endDateMonthInput.getSelectedItem(), (int) addNewDataPanel.endDateDayInput.getSelectedItem());
        } catch (Exception e) {
            addNewDataPanel.systemResponseLabel.setText(negativeResponse);
            addNewDataPanel.endDateHeading.setForeground(Color.red);
            return false;
        }

        // START DATE BEFORE OR ON END DATE
        if (!startDateInput.isBefore(endDateInput.plusDays(1))) {
            addNewDataPanel.systemResponseLabel.setText(negativeResponse);
            addNewDataPanel.startDateHeading.setForeground(Color.red);
            addNewDataPanel.endDateHeading.setForeground(Color.red);
            return false;
        }

        // CATEGORY STRING POPULATED
        if (addNewDataPanel.categoryInput.getText().isEmpty()) {
            addNewDataPanel.systemResponseLabel.setText(negativeResponse);
            addNewDataPanel.categoryHeading.setForeground(Color.red);
            return false;
        }

        // VALUE VALID
        try {
            valueInput = Float.parseFloat(addNewDataPanel.valueInput.getText());
        } catch (Exception e) {
            addNewDataPanel.systemResponseLabel.setText(negativeResponse);
            addNewDataPanel.valueHeading.setForeground(Color.red);
            return false;
        }

        // VALUE POSITIVE
        if (valueInput < 0) {
            addNewDataPanel.systemResponseLabel.setText(negativeResponse);
            addNewDataPanel.valueHeading.setForeground(Color.red);
            return false;
        }

        // ALL CHECKS PASSED
        addNewDataPanel.systemResponseLabel.setText(positiveResponse);
        return true;

    }

    private void checkRecurringEntryGenerators(ArrayList<DataEntry> dataList, boolean startUp) {

        // This checks all the recurring entry generators for if they're due a new DataEntry, and handles it.

        ArrayList<DataEntry> tempAddList = new ArrayList<>();
        tempAddList.add(new DataEntry("TEMP", false, false, "", LocalDate.now(), LocalDate.now(), LocalDate.now(), "TEMP", 0));  // add one to begin the while loop.
        ArrayList<DataEntry> tempDeleteList = new ArrayList<>();

        while (!tempAddList.isEmpty()) {  // will end once a pass has been made with no additions.

            tempAddList.clear();

            for (DataEntry dataEntry : dataList) {
                if (dataEntry.isRecurring && dataEntry.getNextDueDate().isBefore(Collections.min(Arrays.asList(LocalDate.now().plusDays(1), dataEntry.getEndDate().plusDays(1))))) {
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

                    DataEntry newDataEntry = DataHandler.createDataEntry(metadata);
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
                else if (dataEntry.isRecurring && dataEntry.getEndDate().isBefore(LocalDate.now())) {
                    
                    // PREPARING TO DELETE THE EXPIRED DATAENTRY
                    tempDeleteList.add(dataEntry);
                    
                }
            }

            if (!tempAddList.isEmpty()) {
                tempAddList.forEach(de -> dataList.add(de));
            }

            if (!tempDeleteList.isEmpty()) {
                tempDeleteList.forEach(de -> deleteDataEntry(de.getId(), new JPanel(), "", startUp));
            }

        }

        DataHandler.assignIds(dataList);  // reassigning IDs after all the new entries.

    }

}

