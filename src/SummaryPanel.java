/*
 * This is the summary tab panel.
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;

public class SummaryPanel extends JPanel implements ActionListener{

    Config config = new Config();

    SummaryPanel() {
        
        this.setLayout(null);
        this.setPreferredSize(new Dimension(config.DISPLAY_WIDTH, config.PANEL_HEIGHT));
        this.setBackground(config.SUMMARY_COLOR);

        this.setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        
    }
    
}
