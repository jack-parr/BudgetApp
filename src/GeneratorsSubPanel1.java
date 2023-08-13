/*
 * This is the first subpanel within the GeneratorsPanel.
 */

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GeneratorsSubPanel1 extends JPanel {

    Config config = new Config();
    JLabel typeSelectLabel = new JLabel("Type:");
    JComboBox<String> typeComboBox = new JComboBox<>();
    JButton newDataButton;

    GeneratorsSubPanel1() {

        this.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        this.setBackground(config.GENERATORS_PANEL_COLOR);

        // POPULATING TYPE SELECTOR
        typeComboBox.addItem("Expense");
        typeComboBox.addItem("Income");

        // PAINTING PANEL
        typeSelectLabel.setFont(config.PRIMARY_FONT);
        typeSelectLabel.setForeground(config.PRIMARY_TEXT_COLOR);
        typeSelectLabel.setPreferredSize(new Dimension(100, ViewDataPanel.PANEL_HEADER_HEIGHT));
        this.add(typeSelectLabel);

        typeComboBox.setPreferredSize(new Dimension(100, ViewDataPanel.PANEL_HEADER_HEIGHT));
        this.add(typeComboBox);

        newDataButton = new JButton("Add Data");
        newDataButton.setPreferredSize(new Dimension(100, ViewDataPanel.PANEL_HEADER_HEIGHT));
        this.add(newDataButton);

    }
    
}
