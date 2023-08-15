/*
 * This is the summary tab panel.
 * 
 * NOTES:
 * - Need a method for creating a List<Float> of monthly plot points from dataList.
 * - Need a method for creating a graph panel.
 * - Need a JPanel class that will be the one the graph is drawn onto, based on inputs: datapoints, timeframe, and savings goal.
 * - Tutorial for graph drawing: https://stackoverflow.com/questions/8693342/drawing-a-simple-line-graph-in-java
 * - Another tutorial: https://www.codespeedy.com/plot-graph-in-java/
 */

import javax.swing.*;
import java.awt.*;

public class SummaryPanel extends JPanel {

    Config config = new Config();
    JComboBox<String> periodComboBox;

    final int PANEL_HEADER_HEIGHT = 40;

    SummaryPanel() {
        
        this.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        this.setPreferredSize(new Dimension(config.DISPLAY_WIDTH, config.PANEL_HEIGHT));
        this.setBackground(config.SUMMARY_PANEL_COLOR);

        JLabel periodSelectLabel = new JLabel("Period: ");
        periodSelectLabel.setFont(config.PRIMARY_FONT);
        periodSelectLabel.setForeground(config.PRIMARY_TEXT_COLOR);
        periodSelectLabel.setPreferredSize(new Dimension(100, PANEL_HEADER_HEIGHT));
        this.add(periodSelectLabel);

        periodComboBox = new JComboBox<>();
        periodComboBox.addItem("Savings Goal");
        periodComboBox.addItem("Last 6 Months");
        periodComboBox.addItem("Last 12 Months");
        periodComboBox.addItem("All-Time");
        periodComboBox.setPreferredSize(new Dimension(150, PANEL_HEADER_HEIGHT));
        this.add(periodComboBox);

        JLabel summmaryPanelHeaderGapFiller = new JLabel();
        summmaryPanelHeaderGapFiller.setBackground(config.SUMMARY_PANEL_COLOR);
        summmaryPanelHeaderGapFiller.setOpaque(true);
        summmaryPanelHeaderGapFiller.setPreferredSize(new Dimension(config.DISPLAY_WIDTH - (periodSelectLabel.getPreferredSize().width + periodComboBox.getPreferredSize().width), PANEL_HEADER_HEIGHT));
        this.add(summmaryPanelHeaderGapFiller);

        GraphPanel graphPanel = new GraphPanel();
        graphPanel.setPreferredSize(new Dimension(config.DISPLAY_WIDTH, config.PANEL_HEIGHT - PANEL_HEADER_HEIGHT));
        this.add(graphPanel);

        this.setVisible(true);

    }
    
}
