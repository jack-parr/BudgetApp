/*
 * This is the expenses tab panel.
 */

import javax.swing.*;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.*;

public class ExpensesPanel extends JPanel implements ActionListener {

    Config config = new Config();

    final static int PANEL_HEADER_HEIGHT = 40;
    
    ExpensesSubPanel1 headerPanel;
    ExpensesSubPanel2 dataPanel;

    ExpensesPanel() {

        this.setLayout(new BorderLayout());
        this.setBackground(config.EXPENSES_COLOR);

        // PAINTING THE PANEL
        headerPanel = new ExpensesSubPanel1();
        headerPanel.setPreferredSize(new Dimension(config.DISPLAY_WIDTH, PANEL_HEADER_HEIGHT));
        this.add(headerPanel, BorderLayout.NORTH);

        dataPanel = new ExpensesSubPanel2(2023);
        dataPanel.setPreferredSize(new Dimension(config.DISPLAY_WIDTH, config.PANEL_HEIGHT - PANEL_HEADER_HEIGHT));
        this.add(dataPanel, BorderLayout.SOUTH);
        
        this.setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        
    }

    public static int getYearFromMonthKey(int i) {

        // Rrturns the int year from a month key.

        return Integer.valueOf(Integer.toString(i).substring(0, 4));

    }

    public void newYearSelected(ActionEvent e) {

        // if (e.getActionCommand() == "comboBoxChanged") {  // detects if a new year is selected.
        //     this.remove(scrollablePanel);  // removes old subpanel.
        //     this.revalidate();
        //     this.repaint();

        //     // tablesPanel = new ExpensesSubPanel2((int) yearComboBox.getSelectedItem());  // creates new subpanel.
        //     // JScrollPane scrollablePanel = new JScrollPane(tablesPanel);
        //     // this.add(scrollablePanel);
        //     // this.setVisible(true);
        // }

    }
    
}
