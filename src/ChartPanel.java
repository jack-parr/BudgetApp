/*
 * This is the data visualiser tab panel.
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ChartPanel extends JPanel implements ActionListener {

    Config config = new Config();

    ChartPanel() {

        this.setLayout(null);
        this.setPreferredSize(new Dimension(config.DISPLAY_WIDTH, config.PANEL_HEIGHT));
        this.setBackground(config.CHART_COLOR);

        this.setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        
    }
    
}
