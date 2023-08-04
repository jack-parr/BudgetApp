/*
 * This is the expenses tab panel.
 */

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.text.DateFormatSymbols;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class ExpensesPanel extends JPanel implements ActionListener {

    Config config = new Config();
    final int MONTH_HEADER_HEIGHT = 50;
    final Font MONTH_HEADER_FONT = new Font("Arial Rounded MT Bold", Font.PLAIN, 40);
    final Color MONTH_HEADER_COLOR = new Color(235, 235, 235);

    JComboBox<Integer> yearComboBox = new JComboBox<>();

    ExpensesPanel() {

        this.setLayout(null);
        this.setPreferredSize(new Dimension(config.DISPLAY_WIDTH, config.PANEL_HEIGHT));
        this.setBackground(config.EXPENSES_COLOR);

        // PAINTING THE PANEL
        paintExpensesPanel();

        this.setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        
    }

    public int getYearFromMonthKey(int i) {
        return Integer.valueOf(Integer.toString(i).substring(0, 4));
    }

    public void paintExpensesPanel() {

        // First draws the yearComboBox. Then draws all the data and headings onto the expenses panel grouped by monthly tables.

        List<Integer> monthKeys = new ArrayList<Integer>(AppFrame.tablesHashMap.keySet().stream().toList());  // get list of monthTableIDs.
        Collections.sort(monthKeys, Collections.reverseOrder());  // sort it descending.

        List<Integer> yearsFromMonthKeys = monthKeys.stream().map(key -> getYearFromMonthKey(key)).collect(Collectors.toList());  // extracts years from monthKeys.
        List<Integer> uniqueYears = new ArrayList<Integer>(new HashSet<Integer>(yearsFromMonthKeys));  // gets the unique years from the list.
        Collections.sort(uniqueYears, Collections.reverseOrder());  // sort it descending.

        uniqueYears.forEach(i -> yearComboBox.addItem(i));  // adding each unique year to JComboBox.
        int totalTablesHeight = 10;  // initial gap before displaying first element.
        yearComboBox.setBounds(10, totalTablesHeight, config.DISPLAY_WIDTH - 30, 20);
        this.add(yearComboBox);
        totalTablesHeight += 30;  // 20 height of JComboBox + 10 gap.

        // TO DO
        /*
         * add a JLabel to indicate the JComboBox is a year selector.
         * make it so only the data corrosponding to the selected year is displayed.
         * when a new year is selected, refresh the display of the data. Maybe add a new JScrollPanel contained within the expenses panel so that this can be refreshed without processing the JComboBox again.
         */

        // DISPLAYING DATA
        for (int monthTableKey : monthKeys) {
            String prevDate;
            String thisDate;
            // MONTH TITLE
            int monthInt = Integer.parseInt(Integer.toString(monthTableKey).substring(4));  // extract monthInt from monthTableID.
            String monthString = new DateFormatSymbols().getMonths()[monthInt-1];  // convert monthInt to String.
            JLabel monthHeader = new JLabel(monthString);
            monthHeader.setFont(MONTH_HEADER_FONT);
            monthHeader.setForeground(MONTH_HEADER_COLOR);
            monthHeader.setBounds(10, totalTablesHeight, config.DISPLAY_WIDTH - 30, MONTH_HEADER_HEIGHT);
            this.add(monthHeader);
            totalTablesHeight += MONTH_HEADER_HEIGHT + 10;

            // HEADER ROW
            HeaderTable headerTable = new HeaderTable();
            int headerTableHeight = headerTable.getRowCount() * headerTable.getRowHeight();
            headerTable.setBounds(10, totalTablesHeight, config.DISPLAY_WIDTH - 30, headerTableHeight);
            this.add(headerTable);
            totalTablesHeight += headerTableHeight;

            // TABLE OF DATA
            JTable table = AppFrame.tablesHashMap.get(monthTableKey);  // get JTable.
            prevDate = table.getValueAt(0, 0).toString();
            for (int i = 1; i < table.getRowCount(); i++) {
                thisDate = table.getValueAt(i, 0).toString();
                if (thisDate.equals(prevDate)) {
                    table.setValueAt("", i, 0);
                }
                else {
                    prevDate = thisDate;
                }
            }
            int tableHeight = table.getRowCount() * table.getRowHeight();  // work out the height of this table.
            table.setBounds(10, totalTablesHeight, config.DISPLAY_WIDTH - 30, tableHeight);  // set bounds of this table.
            this.add(table);  // add table to panel.
            totalTablesHeight += tableHeight + 10;  // add gap before next table is displayed.
        }

    }
    
}
