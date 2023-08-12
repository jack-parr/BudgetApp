/*
 * This is the expenses tab panel.
 */

import javax.swing.*;
import javax.xml.crypto.Data;

import java.awt.BorderLayout;
import java.awt.Dimension;

public class ViewDataPanel extends JPanel {

    Config config = new Config();

    final static int PANEL_HEADER_HEIGHT = 40;  // height of the first subpanel.
    
    ViewDataSubPanel1 headerPanel;
    ViewDataSubPanel2 dataPanel;

    ViewDataPanel() {

        this.setLayout(new BorderLayout());
        this.setBackground(config.EXPENSES_COLOR);

        // PAINTING THE PANEL
        headerPanel = new ViewDataSubPanel1();
        headerPanel.setPreferredSize(new Dimension(config.DISPLAY_WIDTH, PANEL_HEADER_HEIGHT));
        this.add(headerPanel, BorderLayout.NORTH);

        dataPanel = new ViewDataSubPanel2((Integer) headerPanel.yearComboBox.getSelectedItem());
        dataPanel.setPreferredSize(new Dimension(config.DISPLAY_WIDTH, config.PANEL_HEIGHT - PANEL_HEADER_HEIGHT));
        this.add(dataPanel, BorderLayout.SOUTH);
        
        this.setVisible(true);

    }

    public static int getYearFromMonthKey(int i) {

        // Rrturns the int year from a month key.

        return Integer.valueOf(Integer.toString(i).substring(0, 4));

    }
    
}
