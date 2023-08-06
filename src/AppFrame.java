/*
 * This is the initial JFrame that contains the menu bar and the current panel.
 */

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class AppFrame extends JFrame implements ActionListener{

    Config config = new Config();
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
        ArrayList<DataEntry> dataList = CSVHandler.readDataFromCSV("data.csv");
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
        MenuPanel menuPanel = new MenuPanel();

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

        // HANDLE CLICKING OF TAB BUTTONS
        
        switch (e.getActionCommand()) {

        case SUMMARY_ACTION_COMMAND:
            this.remove(currentPanel);
            SummaryPanel summaryPanel = new SummaryPanel();
            currentPanel = summaryPanel;
            currentPanel.setPreferredSize(new Dimension(config.DISPLAY_WIDTH, config.PANEL_HEIGHT));
            this.add(currentPanel, BorderLayout.SOUTH);
            this.pack();
            this.repaint();
            break;

        case INCOME_ACTION_COMMAND:
            this.remove(currentPanel);
            IncomePanel incomePanel = new IncomePanel();
            currentPanel = incomePanel;
            currentPanel.setPreferredSize(new Dimension(config.DISPLAY_WIDTH, config.PANEL_HEIGHT));
            this.add(currentPanel, BorderLayout.SOUTH);
            this.pack();
            this.repaint();
            break;
            
        case EXPENSES_ACTION_COMMAND:
            this.remove(currentPanel);
            ExpensesPanel expensesPanel = new ExpensesPanel();
            currentPanel = expensesPanel;
            currentPanel.setPreferredSize(new Dimension(config.DISPLAY_WIDTH, config.PANEL_HEIGHT));
            this.add(currentPanel, BorderLayout.SOUTH);
            this.pack();
            this.repaint();
            break;

        default: 
            break;
        }

    }
    
}
