/*
 * This is the expenses tab panel.
 */

import javax.swing.*;

import java.awt.BorderLayout;
import java.awt.Dimension;

public class ViewDataPanel extends JPanel {

    Config config = new Config();

    final static int PANEL_HEADER_HEIGHT = 40;  // height of the first subpanel.
    
    ViewDataSubPanel1 headerPanel;
    ViewDataSubPanel2 dataPanel;
    JScrollPane dataPanelScrollPane;

    ViewDataPanel(int selectedYear) {

        this.setLayout(new BorderLayout());
        this.setBackground(config.VIEW_DATA_PANEL_COLOR);

        // PAINTING THE PANEL
        headerPanel = new ViewDataSubPanel1();
        headerPanel.yearComboBox.setSelectedItem(selectedYear);
        headerPanel.setPreferredSize(new Dimension(config.DISPLAY_WIDTH, PANEL_HEADER_HEIGHT));
        this.add(headerPanel, BorderLayout.NORTH);

        int checkedYear = 0;
        int comboBoxSize = headerPanel.yearComboBox.getItemCount();
        for (int i = 0; i < comboBoxSize - 1; i++) {
            int item = headerPanel.yearComboBox.getItemAt(i);
            if (item == selectedYear) {
                checkedYear = selectedYear;
            }
        }

        if (checkedYear != 0) {
            dataPanel = new ViewDataSubPanel2(selectedYear, headerPanel.filterInput.getText());
        }
        else {
            dataPanel = new ViewDataSubPanel2((Integer) headerPanel.yearComboBox.getSelectedItem(), headerPanel.filterInput.getText());
        }
        dataPanelScrollPane = new JScrollPane(dataPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        dataPanelScrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(config.SCROLLBAR_WIDTH, 0));
        dataPanelScrollPane.getVerticalScrollBar().setUnitIncrement(config.SCROLLBAR_INCREMENT);
        dataPanelScrollPane.setBorder(null);
        dataPanelScrollPane.setPreferredSize(new Dimension(config.DISPLAY_WIDTH, config.PANEL_HEIGHT - PANEL_HEADER_HEIGHT));
        this.add(dataPanelScrollPane, BorderLayout.SOUTH);
        
        this.setVisible(true);

    }

    public static int getYearFromMonthKey(int i) {

        // Rrturns the int year from a month key.

        return Integer.valueOf(Integer.toString(i).substring(0, 4));

    }
    
}
