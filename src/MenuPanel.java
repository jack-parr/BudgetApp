import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

public class MenuPanel extends JPanel {

    Config config = new Config();

    JButton summaryButton;
    JButton incomeButton;
    JButton expensesButton;
    JButton savingsButton;
    JButton chartButton;

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

        incomeButton = new JButton("Generators");
        incomeButton.setPreferredSize(new Dimension(config.DISPLAY_WIDTH / 3, config.MENU_HEIGHT));
        incomeButton.setFocusable(false);
        incomeButton.setBackground(config.GENERATORS_PANEL_COLOR);
        incomeButton.setFont(config.MENU_FONT);
        incomeButton.setForeground(config.PRIMARY_TEXT_COLOR);
        incomeButton.setBorderPainted(false);
        
        expensesButton = new JButton("Data");
        expensesButton.setPreferredSize(new Dimension(config.DISPLAY_WIDTH / 3, config.MENU_HEIGHT));
        expensesButton.setFocusable(false);
        expensesButton.setBackground(config.VIEW_DATA_PANEL_COLOR);
        expensesButton.setFont(config.MENU_FONT);
        expensesButton.setForeground(config.PRIMARY_TEXT_COLOR);
        expensesButton.setBorderPainted(false);

        this.add(summaryButton);
        this.add(incomeButton);
        this.add(expensesButton);
        
    }
    
}
