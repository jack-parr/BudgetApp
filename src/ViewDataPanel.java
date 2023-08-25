/*
 * This is the panel for managing one-off data entries.
 */

import javax.swing.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class ViewDataPanel extends JPanel {

    Config config = new Config();
    
    JLabel yearSelectLabel;
    JComboBox<Integer> yearComboBox;
    JButton newDataButton;
    JLabel filterLabel;
    JTextField filterInput;
    JButton applyFilterButton;
    JButton resetFilterButton;

    DataPanel dataPanel;
    JScrollPane dataPanelScrollPane;

    ViewDataPanel(int selectedYear) {

        this.setLayout(new FlowLayout(FlowLayout.LEFT, config.PANEL_X_GAP, config.PANEL_Y_GAP));
        this.setBackground(config.VIEW_DATA_PANEL_COLOR);

        // POPULATING YEAR SELECTOR
        yearComboBox = new JComboBox<>();
        List<Integer> monthKeys = new ArrayList<Integer>(AppFrame.listsHashMap.keySet().stream().toList());  // get list of monthListIDs.
        Collections.sort(monthKeys, Collections.reverseOrder());  // sort it descending.

        List<Integer> yearsFromMonthKeys = monthKeys.stream().map(key -> ViewDataPanel.getYearFromMonthKey(key)).collect(Collectors.toList());  // extracts years from monthKeys.
        List<Integer> uniqueYears = new ArrayList<Integer>(new HashSet<Integer>(yearsFromMonthKeys));  // gets the unique years from the list.
        Collections.sort(uniqueYears, Collections.reverseOrder());  // sort it descending.
        uniqueYears.forEach(i -> yearComboBox.addItem(i));  // adding each unique year to JComboBox.
        yearComboBox.addItem(null);  // adding the null entry for when there is no data.

        // PAINTING THE HEADER
        yearSelectLabel = new JLabel("Year:");
        yearSelectLabel.setFont(config.PRIMARY_FONT);
        yearSelectLabel.setForeground(config.PRIMARY_TEXT_COLOR);
        yearSelectLabel.setPreferredSize(new Dimension(40, config.PANEL_HEADER_HEIGHT));
        this.add(yearSelectLabel);

        yearComboBox.setFont(config.PRIMARY_FONT);
        yearComboBox.setBackground(Color.white);
        yearComboBox.setSelectedItem(selectedYear);
        yearComboBox.setPreferredSize(new Dimension(100, config.PANEL_HEADER_HEIGHT));
        this.add(yearComboBox);

        paintHeaderSpacer();

        filterLabel = new JLabel("Filter:");
        filterLabel.setFont(config.PRIMARY_FONT);
        filterLabel.setForeground(config.PRIMARY_TEXT_COLOR);
        filterLabel.setPreferredSize(new Dimension(40, config.PANEL_HEADER_HEIGHT));
        this.add(filterLabel);

        filterInput = new JTextField();
        filterInput.setFont(config.PRIMARY_FONT);
        filterInput.setPreferredSize(new Dimension(100, config.PANEL_HEADER_HEIGHT));
        this.add(filterInput);

        applyFilterButton = new JButton("Apply");
        applyFilterButton.setFont(config.PRIMARY_FONT);
        applyFilterButton.setForeground(config.PRIMARY_TEXT_COLOR);
        applyFilterButton.setBackground(config.APPLY_BUTTON_COLOR);
        applyFilterButton.setPreferredSize(new Dimension(80, config.PANEL_HEADER_HEIGHT));
        this.add(applyFilterButton);

        resetFilterButton = new JButton("Reset");
        resetFilterButton.setFont(config.PRIMARY_FONT);
        resetFilterButton.setForeground(config.PRIMARY_TEXT_COLOR);
        resetFilterButton.setBackground(config.RESET_BUTTON_COLOR);
        resetFilterButton.setPreferredSize(new Dimension(80, config.PANEL_HEADER_HEIGHT));
        this.add(resetFilterButton);

        paintHeaderSpacer();

        newDataButton = new JButton("Add Data");
        newDataButton.setBackground(config.ADD_NEW_DATA_BUTTON_COLOR);
        newDataButton.setFont(config.PRIMARY_FONT);
        newDataButton.setForeground(config.PRIMARY_TEXT_COLOR);
        newDataButton.setPreferredSize(new Dimension(100, config.PANEL_HEADER_HEIGHT));
        this.add(newDataButton);

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
        dataPanelScrollPane.setPreferredSize(new Dimension(config.DISPLAY_WIDTH - (2*config.PANEL_X_GAP), config.PANEL_HEIGHT - config.PANEL_HEADER_HEIGHT - (2*config.PANEL_Y_GAP)));
        this.add(dataPanelScrollPane);
        
        this.setVisible(true);

    }

    protected static int getYearFromMonthKey(int i) {

        // Rrturns the int year from a month key.

        return Integer.valueOf(Integer.toString(i).substring(0, 4));

    }

    private void paintHeaderSpacer() {

        // Paints a blank spacer label to separate elements within the header.

        JLabel spacerLabel = new JLabel();
        spacerLabel.setBackground(config.VIEW_DATA_PANEL_COLOR);
        spacerLabel.setOpaque(true);
        spacerLabel.setPreferredSize(new Dimension(50, config.PANEL_HEADER_HEIGHT));
        this.add(spacerLabel);

    }
    
}
