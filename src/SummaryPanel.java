/*
 * This is the summary tab panel.
 */

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class SummaryPanel extends JPanel {

    Config config = new Config();
    JComboBox<String> periodComboBox;
    JTextField startDateInput;
    JTextField endDateInput;
    JButton applyCustomPeriodButton;
    JLabel clickDataLabel;
    GraphPanel graphPanel;

    final int PANEL_HEADER_HEIGHT = 40;

    SummaryPanel() {
        
        this.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        this.setPreferredSize(new Dimension(config.DISPLAY_WIDTH, config.PANEL_HEIGHT));
        this.setBackground(config.SUMMARY_PANEL_COLOR);

        JLabel periodSelectLabel = new JLabel("Period: ");
        periodSelectLabel.setFont(config.PRIMARY_FONT);
        periodSelectLabel.setForeground(config.PRIMARY_TEXT_COLOR);
        periodSelectLabel.setPreferredSize(new Dimension(70, PANEL_HEADER_HEIGHT));
        this.add(periodSelectLabel);

        periodComboBox = new JComboBox<>();
        periodComboBox.addItem("This Month");
        periodComboBox.addItem("Last Month");
        periodComboBox.addItem("Last 3 Months");
        periodComboBox.addItem("Last 6 Months");
        periodComboBox.addItem("Last 12 Months");
        periodComboBox.addItem("All-Time");
        periodComboBox.addItem("Custom");
        periodComboBox.setPreferredSize(new Dimension(120, PANEL_HEADER_HEIGHT));
        this.add(periodComboBox);

        startDateInput = new JTextField();
        startDateInput.setText(LocalDate.now().minusDays(LocalDate.now().getDayOfMonth() - 1).format(config.DATE_TIME_FORMATTER));
        startDateInput.setEnabled(false);
        startDateInput.setPreferredSize(new Dimension(100, PANEL_HEADER_HEIGHT));
        this.add(startDateInput);

        endDateInput = new JTextField();
        endDateInput.setText(LocalDate.now().plusDays(LocalDate.now().lengthOfMonth() - LocalDate.now().getDayOfMonth()).format(config.DATE_TIME_FORMATTER));
        endDateInput.setEnabled(false);
        endDateInput.setPreferredSize(new Dimension(100, PANEL_HEADER_HEIGHT));
        this.add(endDateInput);

        applyCustomPeriodButton = new JButton("Apply");
        applyCustomPeriodButton.setEnabled(false);
        applyCustomPeriodButton.setPreferredSize(new Dimension(100, PANEL_HEADER_HEIGHT));
        this.add(applyCustomPeriodButton);

        clickDataLabel = new JLabel();
        clickDataLabel.setFont(config.PRIMARY_FONT);
        clickDataLabel.setForeground(config.PRIMARY_TEXT_COLOR);
        clickDataLabel.setPreferredSize(new Dimension(500, PANEL_HEADER_HEIGHT));
        this.add(clickDataLabel);

        graphPanel = new GraphPanel(startDateInput.getText(), endDateInput.getText());
        graphPanel.setPreferredSize(new Dimension(config.DISPLAY_WIDTH, config.PANEL_HEIGHT - PANEL_HEADER_HEIGHT));
        this.add(graphPanel);

        this.setVisible(true);

    }

    public void graphClick(float xCoord, float yCoord) {

        // This handles a click on the graph and places the information into the header bar.

        float datePoint = Math.round(xCoord / graphPanel.graphDateStep);
        LocalDate clickDate = (LocalDate) graphPanel.graphDates.toArray()[(int) datePoint];

        if (clickDate.isBefore(LocalDate.now().plusDays(1))) {
            float clickYCoord = graphPanel.graphMap.get(clickDate);
            float clickValue = (clickYCoord - graphPanel.graphZeroCoord) / graphPanel.graphPoundStep;

            clickDataLabel.setText(clickDate.format(config.DATE_TIME_FORMATTER) + ": £" + String.format("$%.2f", Math.abs(clickValue)));
        }
            
        }


    }
