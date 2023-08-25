/*
 * This is the panel for managing recurring entry generators.
 */

import javax.swing.*;
import java.awt.*;

public class ViewGeneratorsPanel extends JPanel {

    Config config = new Config();

    JLabel typeSelectLabel;
    JComboBox<String> typeComboBox;
    JButton newDataButton;

    GeneratorsPanel generatorsPanel;
    JScrollPane generatorsPanelScrollPane;

    ViewGeneratorsPanel(String selectedType) {

        this.setLayout(new FlowLayout(FlowLayout.LEFT, config.PANEL_X_GAP, config.PANEL_Y_GAP));
        this.setBackground(config.VIEW_GENERATORS_PANEL_COLOR);

        // POPULATING TYPE SELECTOR
        typeComboBox = new JComboBox<>();
        typeComboBox.addItem("Expense");
        typeComboBox.addItem("Income");
        
        // PAINTING THE HEADER
        typeSelectLabel = new JLabel("Type:");
        typeSelectLabel.setFont(config.PRIMARY_FONT);
        typeSelectLabel.setForeground(config.PRIMARY_TEXT_COLOR);
        typeSelectLabel.setPreferredSize(new Dimension(40, config.PANEL_HEADER_HEIGHT));
        this.add(typeSelectLabel);

        typeComboBox.setFont(config.PRIMARY_FONT);
        typeComboBox.setBackground(Color.white);
        typeComboBox.setPreferredSize(new Dimension(100, config.PANEL_HEADER_HEIGHT));
        this.add(typeComboBox);

        paintHeaderSpacer();

        newDataButton = new JButton("Add Data");
        newDataButton.setBackground(config.ADD_NEW_DATA_BUTTON_COLOR);
        newDataButton.setFont(config.PRIMARY_FONT);
        newDataButton.setForeground(config.PRIMARY_TEXT_COLOR);
        newDataButton.setPreferredSize(new Dimension(100, config.PANEL_HEADER_HEIGHT));
        this.add(newDataButton);

        // MAKING GENERATORS PANEL
        generatorsPanel = new GeneratorsPanel(typeComboBox.getSelectedItem().equals("Expense"));
        generatorsPanelScrollPane = new JScrollPane(generatorsPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        generatorsPanelScrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(config.SCROLLBAR_WIDTH, 0));
        generatorsPanelScrollPane.getVerticalScrollBar().setUnitIncrement(config.SCROLLBAR_INCREMENT);
        generatorsPanelScrollPane.setBorder(null);
        generatorsPanelScrollPane.setPreferredSize(new Dimension(config.DISPLAY_WIDTH, config.PANEL_HEIGHT - config.PANEL_HEADER_HEIGHT - (2*config.PANEL_Y_GAP)));
        this.add(generatorsPanelScrollPane);
        
        this.setVisible(true);

    }

    private void paintHeaderSpacer() {

        // Paints a blank spacer label to separate elements within the header.

        JLabel spacerLabel = new JLabel();
        spacerLabel.setBackground(config.VIEW_GENERATORS_PANEL_COLOR);
        spacerLabel.setOpaque(true);
        spacerLabel.setPreferredSize(new Dimension(50, config.PANEL_HEADER_HEIGHT));
        this.add(spacerLabel);

    }

}
