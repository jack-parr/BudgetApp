/*
 * This is the summary tab panel.
 */

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class SummaryPanel extends JPanel {

    Config config = new Config();
    JComboBox<String> periodComboBox;
    JLabel startDateLabel;
    JTextField startDateInput;
    JLabel endDateLabel;
    JTextField endDateInput;
    JButton applyCustomPeriodButton;
    JLabel systemResponseLabel;
    GraphPanel graphPanel;

    SummaryPanel() {
        
        this.setLayout(new FlowLayout(FlowLayout.LEFT, config.PANEL_X_GAP, config.PANEL_Y_GAP));
        this.setPreferredSize(new Dimension(config.DISPLAY_WIDTH, config.PANEL_HEIGHT));
        this.setBackground(config.SUMMARY_PANEL_COLOR);

        JLabel periodSelectLabel = new JLabel("Period: ");
        periodSelectLabel.setFont(config.PRIMARY_FONT);
        periodSelectLabel.setForeground(config.PRIMARY_TEXT_COLOR);
        periodSelectLabel.setPreferredSize(new Dimension(55, config.PANEL_HEADER_HEIGHT));
        this.add(periodSelectLabel);

        periodComboBox = new JComboBox<>();
        periodComboBox.addItem("This Month");
        periodComboBox.addItem("Last Month");
        periodComboBox.addItem("Last 3 Months");
        periodComboBox.addItem("Last 6 Months");
        periodComboBox.addItem("Last 12 Months");
        periodComboBox.addItem("All-Time");
        periodComboBox.addItem("Custom");
        periodComboBox.setFont(config.PRIMARY_FONT);
        periodComboBox.setBackground(Color.white);
        periodComboBox.setPreferredSize(new Dimension(130, config.PANEL_HEADER_HEIGHT));
        this.add(periodComboBox);

        paintHeaderSpacer();

        startDateLabel = new JLabel("From: ");
        startDateLabel.setFont(config.PRIMARY_FONT);
        startDateLabel.setForeground(config.PRIMARY_TEXT_COLOR);
        startDateLabel.setPreferredSize(new Dimension(45, config.PANEL_HEADER_HEIGHT));
        this.add(startDateLabel);

        startDateInput = new JTextField();
        startDateInput.setFont(config.PRIMARY_FONT);
        startDateInput.setText(LocalDate.now().minusDays(LocalDate.now().getDayOfMonth() - 1).format(config.DATE_TIME_FORMATTER));
        startDateInput.setEnabled(false);
        startDateInput.setPreferredSize(new Dimension(100, config.PANEL_HEADER_HEIGHT));
        this.add(startDateInput);

        endDateLabel = new JLabel("Until: ");
        endDateLabel.setFont(config.PRIMARY_FONT);
        endDateLabel.setForeground(config.PRIMARY_TEXT_COLOR);
        endDateLabel.setPreferredSize(new Dimension(40, config.PANEL_HEADER_HEIGHT));
        this.add(endDateLabel);

        endDateInput = new JTextField();
        endDateInput.setFont(config.PRIMARY_FONT);
        endDateInput.setText(LocalDate.now().plusDays(LocalDate.now().lengthOfMonth() - LocalDate.now().getDayOfMonth()).format(config.DATE_TIME_FORMATTER));
        endDateInput.setEnabled(false);
        endDateInput.setPreferredSize(new Dimension(100, config.PANEL_HEADER_HEIGHT));
        this.add(endDateInput);

        applyCustomPeriodButton = new JButton("Apply");
        applyCustomPeriodButton.setFont(config.PRIMARY_FONT);
        applyCustomPeriodButton.setForeground(config.PRIMARY_TEXT_COLOR);
        applyCustomPeriodButton.setBackground(config.APPLY_BUTTON_COLOR);
        applyCustomPeriodButton.setFocusable(false);
        applyCustomPeriodButton.setEnabled(false);
        applyCustomPeriodButton.setPreferredSize(new Dimension(100, config.PANEL_HEADER_HEIGHT));
        this.add(applyCustomPeriodButton);

        paintHeaderSpacer();

        systemResponseLabel = new JLabel("Click Graph");
        systemResponseLabel.setFont(config.PRIMARY_FONT);
        systemResponseLabel.setForeground(config.PRIMARY_TEXT_COLOR);
        systemResponseLabel.setPreferredSize(new Dimension(200, config.PANEL_HEADER_HEIGHT));
        this.add(systemResponseLabel);

        graphPanel = new GraphPanel(startDateInput.getText(), endDateInput.getText());
        graphPanel.setPreferredSize(new Dimension(config.DISPLAY_WIDTH, config.PANEL_HEIGHT - config.PANEL_HEADER_HEIGHT - (2*config.PANEL_Y_GAP)));
        this.add(graphPanel);

        this.setVisible(true);

    }

    protected void graphClick(float xCoord, float yCoord) {

        // This handles a click on the graph and places the information into the header bar.

        float datePoint = Math.round(xCoord / graphPanel.graphDateStep);
        LocalDate clickDate = (LocalDate) graphPanel.graphDates.toArray()[(int) datePoint];

        if (clickDate.isBefore(LocalDate.now().plusDays(1))) {
            float clickYCoord = graphPanel.graphMap.get(clickDate);
            float clickValue = (clickYCoord - graphPanel.graphZeroCoord) / graphPanel.graphPoundStep;

            String signString = clickValue < 0 ? ": -£" : ": £";
            systemResponseLabel.setText(clickDate.format(config.DATE_TIME_FORMATTER) + signString + String.format("%.2f", Math.abs(clickValue)));
        }
            
    }

    private void paintHeaderSpacer() {

        // Paints a blank spacer label to separate elements within the header.

        JLabel spacerLabel = new JLabel();
        spacerLabel.setBackground(config.SUMMARY_PANEL_COLOR);
        spacerLabel.setOpaque(true);
        spacerLabel.setPreferredSize(new Dimension(50, config.PANEL_HEADER_HEIGHT));
        this.add(spacerLabel);

    }

}
