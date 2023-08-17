/*
 * This is the panel for managing one-off data entries.
 */

import javax.swing.*;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class ViewDataPanel extends JPanel {

    Config config = new Config();

    final static int PANEL_HEADER_HEIGHT = 40;  // height of the first subpanel.
    
    JLabel yearSelectLabel = new JLabel("Year:");
    JComboBox<Integer> yearComboBox = new JComboBox<>();
    JButton newDataButton;
    JLabel filterLabel = new JLabel("Filter:");
    JTextField filterInput;
    JButton applyFilterButton;
    JButton resetFilterButton;

    DataPanel dataPanel;
    JScrollPane dataPanelScrollPane;

    ViewDataPanel(int selectedYear) {

        this.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        this.setBackground(config.VIEW_DATA_PANEL_COLOR);

        // POPULATING YEAR SELECTOR
        List<Integer> monthKeys = new ArrayList<Integer>(AppFrame.listsHashMap.keySet().stream().toList());  // get list of monthListIDs.
        Collections.sort(monthKeys, Collections.reverseOrder());  // sort it descending.

        List<Integer> yearsFromMonthKeys = monthKeys.stream().map(key -> ViewDataPanel.getYearFromMonthKey(key)).collect(Collectors.toList());  // extracts years from monthKeys.
        List<Integer> uniqueYears = new ArrayList<Integer>(new HashSet<Integer>(yearsFromMonthKeys));  // gets the unique years from the list.
        Collections.sort(uniqueYears, Collections.reverseOrder());  // sort it descending.
        uniqueYears.forEach(i -> yearComboBox.addItem(i));  // adding each unique year to JComboBox.
        yearComboBox.addItem(null);  // adding the null entry for when there is no data.

        // PAINTING THE HEADER
        yearSelectLabel.setFont(config.PRIMARY_FONT);
        yearSelectLabel.setForeground(config.PRIMARY_TEXT_COLOR);
        yearSelectLabel.setPreferredSize(new Dimension(50, ViewDataPanel.PANEL_HEADER_HEIGHT));
        this.add(yearSelectLabel);

        yearComboBox.setSelectedItem(selectedYear);
        yearComboBox.setPreferredSize(new Dimension(100, ViewDataPanel.PANEL_HEADER_HEIGHT));
        this.add(yearComboBox);

        newDataButton = new JButton("Add Data");
        newDataButton.setPreferredSize(new Dimension(100, ViewDataPanel.PANEL_HEADER_HEIGHT));
        this.add(newDataButton);

        filterLabel.setFont(config.PRIMARY_FONT);
        filterLabel.setForeground(config.PRIMARY_TEXT_COLOR);
        filterLabel.setPreferredSize(new Dimension(60, ViewDataPanel.PANEL_HEADER_HEIGHT));
        this.add(filterLabel);

        filterInput = new JTextField();
        filterInput.setFont(config.PRIMARY_FONT);
        filterInput.setPreferredSize(new Dimension(100, ViewDataPanel.PANEL_HEADER_HEIGHT));
        this.add(filterInput);

        applyFilterButton = new JButton("Apply");
        applyFilterButton.setPreferredSize(new Dimension(80, ViewDataPanel.PANEL_HEADER_HEIGHT));
        this.add(applyFilterButton);

        resetFilterButton = new JButton("Reset");
        resetFilterButton.setPreferredSize(new Dimension(80, ViewDataPanel.PANEL_HEADER_HEIGHT));
        this.add(resetFilterButton);

        // MAKING DATA PANEL
        int checkedYear = 0;
        int comboBoxSize = yearComboBox.getItemCount();
        for (int i = 0; i < comboBoxSize - 1; i++) {
            int item = yearComboBox.getItemAt(i);
            if (item == selectedYear) {
                checkedYear = selectedYear;
            }
        }

        if (checkedYear != 0) {
            dataPanel = new DataPanel(selectedYear, filterInput.getText());
        }
        else {
            dataPanel = new DataPanel((Integer) yearComboBox.getSelectedItem(), filterInput.getText());
        }
        dataPanelScrollPane = new JScrollPane(dataPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        dataPanelScrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(config.SCROLLBAR_WIDTH, 0));
        dataPanelScrollPane.getVerticalScrollBar().setUnitIncrement(config.SCROLLBAR_INCREMENT);
        dataPanelScrollPane.setBorder(null);
        dataPanelScrollPane.setPreferredSize(new Dimension(config.DISPLAY_WIDTH, config.PANEL_HEIGHT - PANEL_HEADER_HEIGHT));
        this.add(dataPanelScrollPane);
        
        this.setVisible(true);

    }

    public static int getYearFromMonthKey(int i) {

        // Rrturns the int year from a month key.

        return Integer.valueOf(Integer.toString(i).substring(0, 4));

    }
    
}
