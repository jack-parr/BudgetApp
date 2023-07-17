/*
 * This is the income tab panel.
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class IncomePanel extends JPanel implements ActionListener {

    Config config = new Config();

    IncomePanel() {

        this.setLayout(null);
        this.setPreferredSize(new Dimension(config.DISPLAY_WIDTH, config.PANEL_HEIGHT));
        this.setBackground(config.INCOME_COLOR);

        this.setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        
    }
    
}
