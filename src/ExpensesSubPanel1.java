/*
 * This is the first subpanel within the expenses panel. Contains the year selector and the add new data button.
 */

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ExpensesSubPanel1 extends JPanel {

    Config config = new Config();
    JLabel yearSelectLabel = new JLabel("Year:");
    JComboBox<Integer> yearComboBox = new JComboBox<>();

    ExpensesSubPanel1() {

        this.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        this.setBackground(config.EXPENSES_COLOR);

        // POPULATING YEAR SELECTOR
        List<Integer> monthKeys = new ArrayList<Integer>(AppFrame.listsHashMap.keySet().stream().toList());  // get list of monthTableIDs.
        Collections.sort(monthKeys, Collections.reverseOrder());  // sort it descending.

        List<Integer> yearsFromMonthKeys = monthKeys.stream().map(key -> ExpensesPanel.getYearFromMonthKey(key)).collect(Collectors.toList());  // extracts years from monthKeys.
        List<Integer> uniqueYears = new ArrayList<Integer>(new HashSet<Integer>(yearsFromMonthKeys));  // gets the unique years from the list.
        Collections.sort(uniqueYears, Collections.reverseOrder());  // sort it descending.
        uniqueYears.forEach(i -> yearComboBox.addItem(i));  // adding each unique year to JComboBox.
        yearComboBox.addItem(null);  // adding the null entry for when there is no data.

        // PAINTING PANEL
        yearSelectLabel.setFont(config.GENERAL_FONT);
        yearSelectLabel.setForeground(config.GENERAL_TEXT_COLOR);
        yearSelectLabel.setPreferredSize(new Dimension(100, ExpensesPanel.PANEL_HEADER_HEIGHT));
        this.add(yearSelectLabel);

        yearComboBox.setPreferredSize(new Dimension(100, ExpensesPanel.PANEL_HEADER_HEIGHT));
        this.add(yearComboBox);

    }
    
}
