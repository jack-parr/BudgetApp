/*
 * This is the subpanel that displays the monthly data tables in the expenses panel.
 */

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.LayoutManager;
import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;

public class ExpensesSubPanel2 extends JPanel {

    Config config = new Config();

    final int MONTH_HEADER_HEIGHT = 50;  // height of the month labels.
    final Font MONTH_HEADER_FONT = new Font("Arial Rounded MT Bold", Font.PLAIN, 40);

    ExpensesSubPanel2(int year) {

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBackground(config.EXPENSES_COLOR);

        List<Integer> monthKeys = new ArrayList<Integer>(TestAppFrame.tablesHashMap.keySet().stream().toList());  // get list of monthTableIDs.

        for (int monthTableKey : monthKeys) {
            if (ExpensesPanel.getYearFromMonthKey(monthTableKey) != year) {
                continue;  // passes if data table is not relevant to year.
            }
            String prevDate;
            String thisDate;
            // MONTH TITLE
            int monthInt = Integer.parseInt(Integer.toString(monthTableKey).substring(4));  // extract monthInt from monthTableID.
            String monthString = new DateFormatSymbols().getMonths()[monthInt-1];  // convert monthInt to String.
            JLabel monthHeader = new JLabel(monthString);
            monthHeader.setFont(MONTH_HEADER_FONT);
            monthHeader.setBackground(config.SAVINGS_COLOR);
            monthHeader.setOpaque(true);
            monthHeader.setForeground(config.GENERAL_TEXT_COLOR);
            monthHeader.setAlignmentX(Component.LEFT_ALIGNMENT);
            this.add(monthHeader);

            // HEADER ROW
            HeaderTable headerTable = new HeaderTable();
            this.add(headerTable);

            // TABLE OF DATA
            JTable table = TestAppFrame.tablesHashMap.get(monthTableKey);  // get JTable.
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
            this.add(table);  // add table to panel.
        }

    }
    
}
