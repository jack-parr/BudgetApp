/*
 * This is the savings tab panel.
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SavingsPanel extends JPanel implements ActionListener {

    Config config = new Config();

    SavingsPanel() {

        this.setLayout(null);
        this.setPreferredSize(new Dimension(config.DISPLAY_WIDTH, config.PANEL_HEIGHT));
        this.setBackground(config.SAVINGS_COLOR);

        this.setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        
    }
    
}
