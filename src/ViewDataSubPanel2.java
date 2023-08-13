/*
 * This is the subpanel that displays the monthly data in the ViewDataPanel.
 * The panel uses a GridBagLayout, adding every new component vertically down.
 * The added components are JPanel with FlowLayout for each row.
 */

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.text.DateFormatSymbols;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class ViewDataSubPanel2 extends JPanel {

    Config config = new Config();

    final int MONTH_HEADER_HEIGHT = 50;  // height of the month labels.
    final Font MONTH_HEADER_FONT = new Font("Arial Rounded MT Bold", Font.PLAIN, 40);

    HashMap<String, Component> deleteButtonsMap = new HashMap<>();

    ViewDataSubPanel2(Integer year) {

        this.setLayout(new GridBagLayout());
        GridBagConstraints layoutConstraints = new GridBagConstraints();
        layoutConstraints.gridx = 0;  // makes it so every added panel is added vertically.
        this.setBackground(config.VIEW_DATA_PANEL_COLOR);

        // HANDLING NO DATA
        if (year == null) {
            JLabel nullLabel = new JLabel("No Data", SwingConstants.CENTER);
            nullLabel.setFont(config.PRIMARY_FONT);
            nullLabel.setForeground(config.SECONDARY_TEXT_COLOR);       
            nullLabel.setPreferredSize(new Dimension(config.SCROLLABLE_DISPLAY_WIDTH, config.PANEL_HEIGHT - ViewDataPanel.PANEL_HEADER_HEIGHT));
            this.add(nullLabel);
        }
        // HANDLING DATA
        else {

            List<Integer> monthKeys = new ArrayList<Integer>(AppFrame.listsHashMap.keySet().stream().toList());  // get list of monthListIDs.
            Collections.sort(monthKeys, Collections.reverseOrder());  // sort it descending.

            for (int monthListKey : monthKeys) {
                if (ViewDataPanel.getYearFromMonthKey(monthListKey) != year) {
                    continue;  // passes if data list is not relevant to year.
                }

                ArrayList<DataEntry> monthList = AppFrame.listsHashMap.get(monthListKey);  // get list.


                // MONTH HEADER ROW
                RowPanel rowPanelMonth = new RowPanel();
                rowPanelMonth.setBackground(config.VIEW_DATA_PANEL_COLOR);

                // MONTH TITLE
                int monthInt = Integer.parseInt(Integer.toString(monthListKey).substring(4));  // extract monthInt from monthListID.
                String monthString = new DateFormatSymbols().getMonths()[monthInt-1];  // convert monthInt to String.
                JLabel monthHeader = new JLabel(monthString);
                monthHeader.setFont(MONTH_HEADER_FONT);
                monthHeader.setForeground(config.PRIMARY_TEXT_COLOR);
                monthHeader.setPreferredSize(new Dimension(250, MONTH_HEADER_HEIGHT));
                rowPanelMonth.add(monthHeader);

                // MONTH TOTAL
                float monthTotal = 0;
                int sign = 0;
                for (DataEntry dataEntry : monthList) {
                    if (dataEntry.getIsExpense()) {sign = -1;} 
                    else {sign = 1;}
                    monthTotal += sign * dataEntry.getValue();
                }

                JLabel totalLabel = new JLabel();
                if (monthTotal < 0) {
                    totalLabel.setText("Month Total:   - £" + String.format("%.2f", Math.abs(monthTotal)));
                    totalLabel.setForeground(Color.RED);
                }
                else {
                    totalLabel.setText("Month Total:   + £" + String.format("%.2f", Math.abs(monthTotal)));
                    totalLabel.setForeground(Color.GREEN);
                }
                totalLabel.setFont(config.DATA_ROW_FONT);
                totalLabel.setPreferredSize(new Dimension(config.SCROLLABLE_DISPLAY_WIDTH - monthHeader.getPreferredSize().width, MONTH_HEADER_HEIGHT));
                rowPanelMonth.add(totalLabel);

                this.add(rowPanelMonth, layoutConstraints);


                // COLUMNS ROW
                RowPanel rowPanelColumns = new RowPanel();
                rowPanelColumns.setBackground(config.VIEW_DATA_PANEL_COLOR);

                ArrayList<Component> headerRowComponents = config.makeDataHeaderRowComponents();  // makes a list containing all the components.

                Component headerDateLabel = headerRowComponents.get(0);
                headerDateLabel.setPreferredSize(new Dimension((config.SCROLLABLE_DISPLAY_WIDTH - config.DELETE_BUTTON_WIDTH) / 3, config.DATA_ROW_HEIGHT));
                rowPanelColumns.add(headerDateLabel);

                Component headerCategoryLabel = headerRowComponents.get(1);
                headerCategoryLabel.setPreferredSize(new Dimension((config.SCROLLABLE_DISPLAY_WIDTH - config.DELETE_BUTTON_WIDTH) / 3, config.DATA_ROW_HEIGHT));
                rowPanelColumns.add(headerCategoryLabel);

                Component headerValueLabel = headerRowComponents.get(2);
                headerValueLabel.setPreferredSize(new Dimension((config.SCROLLABLE_DISPLAY_WIDTH - config.DELETE_BUTTON_WIDTH) / 3, config.DATA_ROW_HEIGHT));
                rowPanelColumns.add(headerValueLabel);

                Component headerDeleteLabel = headerRowComponents.get(5);
                rowPanelColumns.add(headerDeleteLabel);

                this.add(rowPanelColumns, layoutConstraints);


                // DISPLAY DATA FROM LIST
                String prevDate = "";
                String thisDate;

                Collections.sort(monthList, Comparator.comparing(DataEntry::getSortCode));

                for (DataEntry dataEntry : monthList) {

                    RowPanel rowPanelDataEntry = new RowPanel();
                    rowPanelDataEntry.setBackground(config.VIEW_DATA_PANEL_COLOR);

                    JLabel spacerLabel = config.makeDataSpacerLabel();
                    spacerLabel.setPreferredSize(new Dimension(config.SCROLLABLE_DISPLAY_WIDTH, 1));

                    ArrayList<Component> dataDisplayRowComponents = config.makeDataDisplayRowComponents(dataEntry);

                    Component dataDateLabel = dataDisplayRowComponents.get(0);
                    dataDateLabel.setPreferredSize(new Dimension((config.SCROLLABLE_DISPLAY_WIDTH - config.DELETE_BUTTON_WIDTH) / 3, config.DATA_ROW_HEIGHT));

                    Component dataCategoryLabel = dataDisplayRowComponents.get(1);
                    dataCategoryLabel.setPreferredSize(new Dimension((config.SCROLLABLE_DISPLAY_WIDTH - config.DELETE_BUTTON_WIDTH) / 3, config.DATA_ROW_HEIGHT));

                    Component dataValueLabel = dataDisplayRowComponents.get(2);
                    dataValueLabel.setPreferredSize(new Dimension((config.SCROLLABLE_DISPLAY_WIDTH - config.DELETE_BUTTON_WIDTH) / 3, config.DATA_ROW_HEIGHT));

                    thisDate = config.dateToLabelString(dataEntry.getStartDate(), false);
                    if (thisDate.equals(prevDate)) {
                        // no spacer, empty date label.
                        JLabel emptyDateLabel = new JLabel();
                        emptyDateLabel.setPreferredSize(new Dimension((config.SCROLLABLE_DISPLAY_WIDTH - config.DELETE_BUTTON_WIDTH) / 3, config.DATA_ROW_HEIGHT));
                        rowPanelDataEntry.add(emptyDateLabel);
                    }
                    else {
                        // gray spacer, populated date label.
                        this.add(spacerLabel, layoutConstraints);
                        rowPanelDataEntry.add(dataDateLabel);
                    }

                    rowPanelDataEntry.add(dataCategoryLabel);
                    rowPanelDataEntry.add(dataValueLabel);
                    prevDate = thisDate;

                    // CREATE AND PAINT DELETE BUTTON
                    JButton deleteDataEntryButton = new JButton();
                    deleteDataEntryButton.setFocusable(false);
                    deleteDataEntryButton.setBorderPainted(false);
                    deleteDataEntryButton.setBackground(Color.RED);
                    deleteDataEntryButton.setName("deleteButton" + dataEntry.getId());
                    deleteDataEntryButton.setActionCommand("delete" + dataEntry.getId());
                    deleteDataEntryButton.setPreferredSize(new Dimension(config.DELETE_BUTTON_WIDTH, config.DATA_ROW_HEIGHT));
                    rowPanelDataEntry.add(deleteDataEntryButton);

                    // ADD DELETE BUTTON TO HASHMAP
                    deleteButtonsMap.put(deleteDataEntryButton.getName(), deleteDataEntryButton);

                    // ADD ROWPANEL TO SUBPANEL
                    this.add(rowPanelDataEntry, layoutConstraints);

                }
                
            }

            // ADD PANEL FILLER TO ALIGN TO TOP OF JSCROLLPANE WHEN NOT OVERFLOWING
            int remainingVerticalSpace = (config.PANEL_HEIGHT - ViewDataPanel.PANEL_HEADER_HEIGHT) - (this.getPreferredSize().height);
            if (remainingVerticalSpace > 0) {
                JPanel panelFillerLabel = new JPanel();
                panelFillerLabel.setBackground(config.VIEW_DATA_PANEL_COLOR);
                panelFillerLabel.setPreferredSize(new Dimension(config.SCROLLABLE_DISPLAY_WIDTH, remainingVerticalSpace));
                this.add(panelFillerLabel, layoutConstraints);
            }

        }

    }
    
}
