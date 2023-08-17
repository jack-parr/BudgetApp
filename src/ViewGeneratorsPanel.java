/*
 * This is the panel for managing recurring entry generators.
 */

import javax.swing.*;
import java.awt.*;

public class ViewGeneratorsPanel extends JPanel {

    Config config = new Config();

    final static int PANEL_HEADER_HEIGHT = 40;

    JLabel typeSelectLabel = new JLabel("Type:");
    JComboBox<String> typeComboBox = new JComboBox<>();
    JButton newDataButton;

    GeneratorsPanel generatorsPanel;
    JScrollPane generatorsPanelScrollPane;

    ViewGeneratorsPanel(String selectedType) {

        this.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        this.setBackground(config.GENERATORS_PANEL_COLOR);

        // POPULATING TYPE SELECTOR
        typeComboBox.addItem("Expense");
        typeComboBox.addItem("Income");
        
        // PAINTING THE HEADER
        typeSelectLabel.setFont(config.PRIMARY_FONT);
        typeSelectLabel.setForeground(config.PRIMARY_TEXT_COLOR);
        typeSelectLabel.setPreferredSize(new Dimension(100, PANEL_HEADER_HEIGHT));
        this.add(typeSelectLabel);

        typeComboBox.setPreferredSize(new Dimension(100, PANEL_HEADER_HEIGHT));
        this.add(typeComboBox);

        newDataButton = new JButton("Add Data");
        newDataButton.setPreferredSize(new Dimension(100, PANEL_HEADER_HEIGHT));
        this.add(newDataButton);

        // MAKING GENERATORS PANEL
        generatorsPanel = new GeneratorsPanel(typeComboBox.getSelectedItem().equals("Expense"));
        generatorsPanelScrollPane = new JScrollPane(generatorsPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        generatorsPanelScrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(config.SCROLLBAR_WIDTH, 0));
        generatorsPanelScrollPane.getVerticalScrollBar().setUnitIncrement(config.SCROLLBAR_INCREMENT);
        generatorsPanelScrollPane.setBorder(null);
        generatorsPanelScrollPane.setPreferredSize(new Dimension(config.DISPLAY_WIDTH, config.PANEL_HEIGHT - PANEL_HEADER_HEIGHT));
        this.add(generatorsPanelScrollPane);
        
        this.setVisible(true);

    }

}
