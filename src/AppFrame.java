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
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class AppFrame extends JFrame implements ActionListener{

    Config config = new Config();
    MenuPanel menuPanel;
    ExpensesPanel expensesPanel;
    static JPanel currentPanel;
    ArrayList<DataEntry> dataList;
    static HashMap<Integer, ArrayList<DataEntry>> listsHashMap;

    final static String SUMMARY_ACTION_COMMAND = "summaryButton";
    final static String INCOME_ACTION_COMMAND = "incomeButton";
    final static String EXPENSES_ACTION_COMMAND = "expensesButton";
    final static String SAVINGS_ACTION_COMMAND = "savingsButton";
    final static String DATA_ACTION_COMMAND = "dataButton";

    AppFrame() {

        // LOADING DATA
        dataList = CSVHandler.readDataFromCSV("data.csv");
        listsHashMap = CSVHandler.createMonthLists(dataList);

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
        if (currentPanel instanceof ExpensesPanel) {
            
            // HANDLING YEAR CHANGE
            if (e.getSource() == expensesPanel.headerPanel.yearComboBox) {
                expensesPanel.remove(expensesPanel.dataPanel);  // remove old dataPanel.
                expensesPanel.dataPanel = new ExpensesSubPanel2((Integer) expensesPanel.headerPanel.yearComboBox.getSelectedItem());  // create new dataPanel.
                assignDeleteButtons();  // assign listeners to delete row buttons.
                expensesPanel.dataPanel.setPreferredSize(new Dimension(config.DISPLAY_WIDTH, config.PANEL_HEIGHT - ExpensesPanel.PANEL_HEADER_HEIGHT));
                expensesPanel.add(expensesPanel.dataPanel, BorderLayout.SOUTH);  // add the new dataPanel.
                expensesPanel.revalidate();
                expensesPanel.repaint();
            }

            // HANDLING NEW DATA BUTTON
            if (e.getSource() == expensesPanel.headerPanel.newDataButton) {
                this.remove(currentPanel);  // removes expenses panel.
                createNewExpensePanel();
                this.revalidate();
                this.repaint();
            }

            // HANDLING DELETE ROW BUTTONS
            if (actionCommand.substring(0, 6).equals("delete")) {
                deleteDataEntry(Integer.parseInt(actionCommand.substring(6)));  // calls the method for deleting DataEntry according to int id.
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

        expensesPanel = new ExpensesPanel();

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

        NewExpensePanel newExpensePanel = new NewExpensePanel();

        // SETTING ACTION LISTENERS

        // PAINTING PANEL
        currentPanel = newExpensePanel;
        currentPanel.setPreferredSize(new Dimension(config.DISPLAY_WIDTH, config.PANEL_HEIGHT));
        this.add(currentPanel);
        this.revalidate();
        this.repaint();

    }

    public void assignDeleteButtons() {

        // Assigns listeners to the delete buttons in the expenses panel. This is a separate method so that changing the year can trigger it.

        HashMap<String, Component> deleteButtonsMap = expensesPanel.dataPanel.deleteButtons;
        Object[] mapKeys = deleteButtonsMap.keySet().toArray();  // makes an array of the keySet.
        for (Object key : mapKeys) {
            JButton deleteButton = (JButton) deleteButtonsMap.get(key);
            deleteButton.addActionListener(this);
        }

    }

    public void deleteDataEntry(int id) {

        // Deletes the DataEntry corresponding to id.

        for (DataEntry dataEntry : dataList) {
            if (dataEntry.getId() == id) {
                dataList.remove(dataEntry);
                break;
            }
        }

        listsHashMap = CSVHandler.createMonthLists(dataList);  // remakes the listsHashMap
        this.remove(currentPanel);  // removes the old expenses panel.
        createExpensesPanel();  // recreates the expenses panel.

    }
    
}
