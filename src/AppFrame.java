/*
 * This is the JFrame that acts as the window.
 * The JFrame is used as a container for panels, which contain the actual functionality.
 * The JFrame contains a bar of buttons that goes along the top, enabling switching between tabs.
 */


import javax.swing.*;
import java.awt.event.*;
import java.util.*;

public class AppFrame extends JFrame implements ActionListener{

    CSVHandler csvReader = new CSVHandler();;
    Config config = new Config();
    ExpensesPanel expensesPanel;
    JPanel currentPanel;

    JButton summaryButton;
    JButton incomeButton;
    JButton expensesButton;
    JButton savingsButton;
    JButton chartButton;

    ArrayList<DataEntry> dataList;
    static HashMap<Integer, JTable> tablesHashMap;

    final String SUMMARY_ACTION_COMMAND = "summaryButton";
    final String INCOME_ACTION_COMMAND = "incomeButton";
    final String EXPENSES_ACTION_COMMAND = "expensesButton";
    final String SAVINGS_ACTION_COMMAND = "savingsButton";
    final String DATA_ACTION_COMMAND = "dataButton";

    AppFrame() {

        // LOADING DATA
        ArrayList<DataEntry> dataList = CSVHandler.readDataFromCSV("data.csv");
        for (DataEntry dataEntry : dataList) {
            System.out.println(dataEntry);
        }

        // GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        // Font[] allFonts = ge.getAllFonts();
        // for (Font font : allFonts) {
        //     System.out.println(font.getFontName(Locale.US));
        // }

        tablesHashMap = CSVHandler.createMonthTables(dataList);
        // System.out.println(tablesHashMap);
        
        // CSVHandler.saveDataToCSV(dataList, "testFile.csv");

        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {  // X window button is pressed.
                CSVHandler.saveDataToCSV(dataList, config.DATA_FILEPATH);  // saves data.
                System.exit(0);  // exits programme.
            }
        });

        dataList.add(CSVHandler.createDataEntry(new String[] {"2023-04-01", "Education", "12"}));

        this.setTitle("Expenses Tracker");
        this.setSize(config.DISPLAY_WIDTH, config.DISPLAY_HEIGHT);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setLayout(null);

        // TAB BUTTONS TO NAVIGATE BETWEEN PANELS
        summaryButton = new JButton("Summary");
        summaryButton.setBounds(0, 0, config.DISPLAY_WIDTH/5, config.MENU_HEIGHT);
        summaryButton.setFocusable(false);
        summaryButton.setBackground(config.SUMMARY_COLOR);
        summaryButton.addActionListener(this);
        summaryButton.setActionCommand(SUMMARY_ACTION_COMMAND);
        summaryButton.setBorderPainted(false);

        incomeButton = new JButton("Income");
        incomeButton.setBounds(config.DISPLAY_WIDTH * 1/5, 0, config.DISPLAY_WIDTH/5, config.MENU_HEIGHT);
        incomeButton.setFocusable(false);
        incomeButton.setBackground(config.INCOME_COLOR);
        incomeButton.addActionListener(this);
        incomeButton.setActionCommand(INCOME_ACTION_COMMAND);
        incomeButton.setBorderPainted(false);
        
        expensesButton = new JButton("Expenses");
        expensesButton.setBounds(config.DISPLAY_WIDTH * 2/5, 0, config.DISPLAY_WIDTH/5, config.MENU_HEIGHT);
        expensesButton.setFocusable(false);
        expensesButton.setBackground(config.EXPENSES_COLOR);
        expensesButton.addActionListener(this);
        expensesButton.setActionCommand(EXPENSES_ACTION_COMMAND);
        expensesButton.setBorderPainted(false);
        
        savingsButton = new JButton("Savings");
        savingsButton.setBounds(config.DISPLAY_WIDTH * 3/5, 0, config.DISPLAY_WIDTH/5, config.MENU_HEIGHT);
        savingsButton.setFocusable(false);
        savingsButton.setBackground(config.SAVINGS_COLOR);
        savingsButton.addActionListener(this);
        savingsButton.setActionCommand(SAVINGS_ACTION_COMMAND);
        savingsButton.setBorderPainted(false);
        
        chartButton = new JButton("Charts");
        chartButton.setBounds(config.DISPLAY_WIDTH * 4/5, 0, config.DISPLAY_WIDTH/5, config.MENU_HEIGHT);
        chartButton.setFocusable(false);
        chartButton.setBackground(config.CHART_COLOR);
        chartButton.addActionListener(this);
        chartButton.setActionCommand(DATA_ACTION_COMMAND);
        chartButton.setBorderPainted(false);

        // summaryPanel = new SummaryPanel();  // start by presenting the summary panel.
        // summaryPanel.setBounds(0, config.MENU_HEIGHT, config.DISPLAY_WIDTH, config.PANEL_HEIGHT);
        // currentPanel = summaryPanel;
        expensesPanel = new ExpensesPanel();  // start by presenting the summary panel.
        expensesPanel.setBounds(0, config.MENU_HEIGHT, config.DISPLAY_WIDTH, config.PANEL_HEIGHT);
        currentPanel = expensesPanel;

        // ADDING TO FRAME
        this.add(summaryButton);
        this.add(incomeButton);
        this.add(expensesButton);
        this.add(savingsButton);
        this.add(chartButton);
        this.add(expensesPanel);
        this.setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // HANDLE CLICKING OF TAB BUTTONS
        
        switch (e.getActionCommand()) {

        case SUMMARY_ACTION_COMMAND:
            this.remove(currentPanel);
            SummaryPanel summaryPanel = new SummaryPanel();
            summaryPanel.setBounds(0, config.MENU_HEIGHT, config.DISPLAY_WIDTH, config.PANEL_HEIGHT);
            currentPanel = summaryPanel;
            this.add(summaryPanel);
            this.repaint();
            break;

        case INCOME_ACTION_COMMAND:
            this.remove(currentPanel);
            IncomePanel incomePanel = new IncomePanel();
            incomePanel.setBounds(0, config.MENU_HEIGHT, config.DISPLAY_WIDTH, config.PANEL_HEIGHT);
            currentPanel = incomePanel;
            this.add(incomePanel);
            this.repaint();
            break;
            
        case EXPENSES_ACTION_COMMAND:
            this.remove(currentPanel);
            ExpensesPanel expensesPanel = new ExpensesPanel();
            expensesPanel.setBounds(0, config.MENU_HEIGHT, config.DISPLAY_WIDTH, config.PANEL_HEIGHT);
            currentPanel = expensesPanel;
            this.add(expensesPanel);
            this.repaint();
            break;

        case SAVINGS_ACTION_COMMAND:
            this.remove(currentPanel);
            SavingsPanel savingsPanel = new SavingsPanel();
            savingsPanel.setBounds(0, config.MENU_HEIGHT, config.DISPLAY_WIDTH, config.PANEL_HEIGHT);
            currentPanel = savingsPanel;
            this.add(savingsPanel);
            this.repaint();
            break;

        case DATA_ACTION_COMMAND:
            this.remove(currentPanel);
            ChartPanel dataPanel = new ChartPanel();
            dataPanel.setBounds(0, config.MENU_HEIGHT, config.DISPLAY_WIDTH, config.PANEL_HEIGHT);
            currentPanel = dataPanel;
            this.add(dataPanel);
            this.repaint();
            break;

        default: 
            break;
        }

    }
    
}
