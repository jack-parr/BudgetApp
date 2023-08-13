/*
 * This is the panel for managing recurring entry generators.
 */

import javax.swing.*;
import java.awt.*;

public class GeneratorsPanel extends JPanel {

    Config config = new Config();

    final static int PANEL_HEADER_HEIGHT = 40;

    GeneratorsSubPanel1 headerPanel;
    GeneratorsSubPanel2 dataPanel;
    JScrollPane dataPanelScrollPane;

    GeneratorsPanel() {

        this.setLayout(new BorderLayout());
        this.setBackground(config.GENERATORS_PANEL_COLOR);
        
        // PAINTING THE PANEL
        headerPanel = new GeneratorsSubPanel1();
        headerPanel.setPreferredSize(new Dimension(config.DISPLAY_WIDTH, PANEL_HEADER_HEIGHT));
        this.add(headerPanel, BorderLayout.NORTH);

        dataPanel = new GeneratorsSubPanel2(headerPanel.typeComboBox.getSelectedItem().equals("Expense"));
        dataPanelScrollPane = new JScrollPane(dataPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        dataPanelScrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(config.SCROLLBAR_WIDTH, 0));
        dataPanelScrollPane.getVerticalScrollBar().setUnitIncrement(config.SCROLLBAR_INCREMENT);
        dataPanelScrollPane.setBorder(null);
        dataPanelScrollPane.setPreferredSize(new Dimension(config.DISPLAY_WIDTH, config.PANEL_HEIGHT - PANEL_HEADER_HEIGHT));
        this.add(dataPanelScrollPane, BorderLayout.SOUTH);
        
        this.setVisible(true);

    }

}
