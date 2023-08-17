import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

public class MenuPanel extends JPanel {

    Config config = new Config();

    JButton summaryButton;
    JButton viewGeneratorsButton;
    JButton viewDataButton;

    MenuPanel() {

        this.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));

        // TAB BUTTONS TO NAVIGATE BETWEEN PANELS
        summaryButton = new JButton("Summary");
        summaryButton.setPreferredSize(new Dimension(config.DISPLAY_WIDTH / 3, config.MENU_HEIGHT));
        summaryButton.setFocusable(false);
        summaryButton.setBackground(config.SUMMARY_PANEL_COLOR);
        summaryButton.setFont(config.MENU_FONT);
        summaryButton.setForeground(config.PRIMARY_TEXT_COLOR);
        summaryButton.setBorderPainted(false);

        viewGeneratorsButton = new JButton("Generators");
        viewGeneratorsButton.setPreferredSize(new Dimension(config.DISPLAY_WIDTH / 3, config.MENU_HEIGHT));
        viewGeneratorsButton.setFocusable(false);
        viewGeneratorsButton.setBackground(config.GENERATORS_PANEL_COLOR);
        viewGeneratorsButton.setFont(config.MENU_FONT);
        viewGeneratorsButton.setForeground(config.PRIMARY_TEXT_COLOR);
        viewGeneratorsButton.setBorderPainted(false);
        
        viewDataButton = new JButton("Data");
        viewDataButton.setPreferredSize(new Dimension(config.DISPLAY_WIDTH / 3, config.MENU_HEIGHT));
        viewDataButton.setFocusable(false);
        viewDataButton.setBackground(config.VIEW_DATA_PANEL_COLOR);
        viewDataButton.setFont(config.MENU_FONT);
        viewDataButton.setForeground(config.PRIMARY_TEXT_COLOR);
        viewDataButton.setBorderPainted(false);

        this.add(summaryButton);
        this.add(viewGeneratorsButton);
        this.add(viewDataButton);
        
    }
    
}
