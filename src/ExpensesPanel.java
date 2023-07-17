/*
 * This is the expenses tab panel.
 */

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.text.DateFormatSymbols;
import java.util.*;
import java.util.List;

public class ExpensesPanel extends JPanel implements ActionListener {

    Config config = new Config();
    HashMap<Integer, JTable> tablesHashMap = AppFrame.tablesHashMap;
    final int MONTH_HEADER_HEIGHT = 50;
    final Font MONTH_HEADER_FONT = new Font("Arial Rounded MT Bold", Font.PLAIN, 40);

    ExpensesPanel() {

        // REMOVE DATE ENTRY FOR EACH ENTRY AFTER FIRST OCCURENCE IN EACH TABLE.
        // REMOVE HORIZONTAL LINES FOR ENTRIES ON SAME DAY TOO IF POSSIBLE.
        // CREATE SUMMARY IN AND OUT FOR EACH MONTH NEXT TO HEADING.
        // FORMAT EXPENSES PANEL. (TABLE CELL RENDERER)
        // JSCROLLPANE WITH SIZE BASED ON TOTAL SIZE NEEDED.
        // REMOVE ITEM CAPABILITY.
        // ADD ITEM CAPABILITY.

        this.setLayout(null);
        this.setPreferredSize(new Dimension(config.DISPLAY_WIDTH, config.PANEL_HEIGHT));
        this.setBackground(config.EXPENSES_COLOR);

        // DISPLAYING TABLES BY MONTH.
        paintMonthTables();

        this.setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        
    }

    public void paintMonthTables() {

        // Draws all the data onto the expenses panel grouped by monthly tables.

        List<Integer> monthKeys = new ArrayList<Integer>(tablesHashMap.keySet().stream().toList());  // get list of monthTableIDs.
        Collections.sort(monthKeys, Collections.reverseOrder());  // sort it descending.
        int totalTablesHeight = 10;  // initial gap before displaying first table.
        for (int monthTableKey : monthKeys) {
            int monthInt = Integer.parseInt(Integer.toString(monthTableKey).substring(4));  // extract monthInt from monthTableID.
            String monthString = new DateFormatSymbols().getMonths()[monthInt-1];  // convert monthInt to String.
            JLabel monthHeader = new JLabel(monthString);
            monthHeader.setFont(MONTH_HEADER_FONT);
            monthHeader.setBounds(10, totalTablesHeight, config.DISPLAY_WIDTH - 30, MONTH_HEADER_HEIGHT);
            this.add(monthHeader);
            totalTablesHeight += MONTH_HEADER_HEIGHT + 10;

            HeaderTable headerTable = new HeaderTable();
            int headerTableHeight = headerTable.getRowCount() * headerTable.getRowHeight();
            headerTable.setBounds(10, totalTablesHeight, config.DISPLAY_WIDTH - 30, headerTableHeight);
            this.add(headerTable);
            totalTablesHeight += headerTableHeight + 10;

            JTable table = tablesHashMap.get(monthTableKey);  // get JTable.
            int tableHeight = table.getRowCount() * table.getRowHeight();  // work out the height of this table.
            table.setBounds(10, totalTablesHeight, config.DISPLAY_WIDTH - 30, tableHeight);  // set bounds of this table.
            this.add(table);  // add table to panel.
            totalTablesHeight += tableHeight + 10;  // add gap before next table is displayed.
        }

    }
    
}
