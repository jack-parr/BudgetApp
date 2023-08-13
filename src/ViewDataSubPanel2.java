/*
 * This is the subpanel that displays the monthly data in the expenses panel.
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
    final Font HEADER_ROW_FONT = new Font("Arial Rounded MT Bold", Font.PLAIN, 12);
    final int DATA_ROW_HEIGHT = 30;
    final Font DATA_ROW_FONT = new Font("Arial Rounded MT Bold", Font.PLAIN, 14);
    final int DELETE_BUTTON_WIDTH = 50;
    final int ADJUSTED_DISPLAY_WIDTH = config.DISPLAY_WIDTH - 10;

    HashMap<String, Component> deleteButtonsMap = new HashMap<>();

    ViewDataSubPanel2(Integer year) {

        this.setLayout(new GridBagLayout());
        GridBagConstraints layoutConstraints = new GridBagConstraints();
        layoutConstraints.gridx = 0;  // makes it so every added panel is added vertically.
        this.setBackground(config.EXPENSES_COLOR);

        if (year == null) {
            JLabel nullLabel = new JLabel("No Data", SwingConstants.CENTER);
            nullLabel.setFont(config.PRIMARY_FONT);
            nullLabel.setForeground(config.SECONDARY_TEXT_COLOR);       
            nullLabel.setPreferredSize(new Dimension(ADJUSTED_DISPLAY_WIDTH, config.PANEL_HEIGHT - ViewDataPanel.PANEL_HEADER_HEIGHT));
            this.add(nullLabel);
        }
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
                totalLabel.setFont(DATA_ROW_FONT);
                totalLabel.setPreferredSize(new Dimension(ADJUSTED_DISPLAY_WIDTH - monthHeader.getPreferredSize().width, MONTH_HEADER_HEIGHT));
                rowPanelMonth.add(totalLabel);

                this.add(rowPanelMonth, layoutConstraints);


                // COLUMNS ROW
                RowPanel rowPanelColumns = new RowPanel();

                ArrayList<Component> headerRowComponents = makeHeaderRow();  // makes a list containing all the components.

                Component headerDateLabel = headerRowComponents.get(0);
                headerDateLabel.setPreferredSize(new Dimension(ADJUSTED_DISPLAY_WIDTH / 3, DATA_ROW_HEIGHT));
                rowPanelColumns.add(headerDateLabel);

                Component headerCategoryLabel = headerRowComponents.get(1);
                headerCategoryLabel.setPreferredSize(new Dimension(ADJUSTED_DISPLAY_WIDTH / 3, DATA_ROW_HEIGHT));
                rowPanelColumns.add(headerCategoryLabel);

                Component headerValueLabel = headerRowComponents.get(2);
                headerValueLabel.setPreferredSize(new Dimension(ADJUSTED_DISPLAY_WIDTH / 3, DATA_ROW_HEIGHT));
                rowPanelColumns.add(headerValueLabel);

                this.add(rowPanelColumns, layoutConstraints);


                // DISPLAY DATA FROM LIST
                String prevDate = "";
                String thisDate;

                Collections.sort(monthList, Comparator.comparing(DataEntry::getSortCode));

                for (DataEntry dataEntry : monthList) {

                    RowPanel rowPanelDataEntry = new RowPanel();

                    JLabel spacerLabel = makeSpacerLabel();
                    spacerLabel.setPreferredSize(new Dimension(ADJUSTED_DISPLAY_WIDTH, 1));

                    ArrayList<Component> dataRowComponents = makeDataRow(dataEntry);

                    Component dataDateLabel = dataRowComponents.get(0);
                    dataDateLabel.setPreferredSize(new Dimension((ADJUSTED_DISPLAY_WIDTH - DELETE_BUTTON_WIDTH) / 3, DATA_ROW_HEIGHT));

                    Component dataCategoryLabel = dataRowComponents.get(1);
                    dataCategoryLabel.setPreferredSize(new Dimension((ADJUSTED_DISPLAY_WIDTH - DELETE_BUTTON_WIDTH) / 3, DATA_ROW_HEIGHT));

                    Component dataValueLabel = dataRowComponents.get(2);
                    dataValueLabel.setPreferredSize(new Dimension((ADJUSTED_DISPLAY_WIDTH - DELETE_BUTTON_WIDTH) / 3, DATA_ROW_HEIGHT));

                    thisDate = dateToLabelString(dataEntry.getStartDate());
                    if (thisDate.equals(prevDate)) {
                        // no spacer, empty date label.
                        JLabel emptyDateLabel = new JLabel();
                        emptyDateLabel.setPreferredSize(new Dimension((ADJUSTED_DISPLAY_WIDTH - DELETE_BUTTON_WIDTH) / 3, DATA_ROW_HEIGHT));
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
                    JButton deleteRowButton = new JButton();
                    deleteRowButton.setFocusable(false);
                    deleteRowButton.setBorderPainted(false);
                    deleteRowButton.setBackground(Color.RED);
                    deleteRowButton.setName("deleteButton" + dataEntry.getId());
                    deleteRowButton.setActionCommand("delete" + dataEntry.getId());
                    deleteRowButton.setPreferredSize(new Dimension(DELETE_BUTTON_WIDTH, DATA_ROW_HEIGHT));
                    rowPanelDataEntry.add(deleteRowButton);

                    // ADD DELETE BUTTON TO HASHMAP
                    deleteButtonsMap.put(deleteRowButton.getName(), deleteRowButton);

                    // ADD ROWPANEL TO SUBPANEL
                    this.add(rowPanelDataEntry, layoutConstraints);

                }
                
            }

            // ADD PANEL FILLER TO ALIGN TO TOP OF JSCROLLPANE WHEN NOT OVERFLOWING
            int remainingVerticalSpace = (config.PANEL_HEIGHT - ViewDataPanel.PANEL_HEADER_HEIGHT) - (this.getPreferredSize().height);
            if (remainingVerticalSpace > 0) {
                JPanel panelFillerLabel = new JPanel();
                panelFillerLabel.setBackground(config.EXPENSES_COLOR);
                panelFillerLabel.setPreferredSize(new Dimension(ADJUSTED_DISPLAY_WIDTH, remainingVerticalSpace));
                this.add(panelFillerLabel, layoutConstraints);
            }

        }

    }

    public ArrayList<Component> makeHeaderRow() {

        // Makes a list of components of a header row for a month list.

        JLabel dateLabel = new JLabel("Date");
        dateLabel.setFont(HEADER_ROW_FONT);
        dateLabel.setForeground(config.SECONDARY_TEXT_COLOR);

        JLabel categoryLabel = new JLabel("Category");
        categoryLabel.setFont(HEADER_ROW_FONT);
        categoryLabel.setForeground(config.SECONDARY_TEXT_COLOR);

        JLabel valueLabel = new JLabel("Value");
        valueLabel.setFont(HEADER_ROW_FONT);
        valueLabel.setForeground(config.SECONDARY_TEXT_COLOR);

        ArrayList<Component> componentList = new ArrayList<>();
        componentList.add(dateLabel);
        componentList.add(categoryLabel);
        componentList.add(valueLabel);

        return componentList;

    }

    public ArrayList<Component> makeDataRow(DataEntry dataEntry) {

        // Makes a list of components of a data row from a DataEntry.

        JLabel dateLabel = new JLabel(dateToLabelString(dataEntry.getStartDate()));
        dateLabel.setFont(DATA_ROW_FONT);
        dateLabel.setForeground(config.PRIMARY_TEXT_COLOR);

        JLabel categoryLabel = new JLabel(dataEntry.getCategory());
        categoryLabel.setFont(DATA_ROW_FONT);
        categoryLabel.setForeground(config.PRIMARY_TEXT_COLOR);

        JLabel valueLabel = new JLabel();
        valueLabel.setFont(DATA_ROW_FONT);
        if (dataEntry.getIsExpense()) {
            valueLabel.setText("- £" + String.format("%.2f", Math.abs(dataEntry.getValue())));
            valueLabel.setForeground(Color.RED);
        }
        else {
            valueLabel.setText("+ £" + String.format("%.2f", Math.abs(dataEntry.getValue())));
            valueLabel.setForeground(Color.GREEN);
        }

        ArrayList<Component> componentList = new ArrayList<>();
        componentList.add(dateLabel);
        componentList.add(categoryLabel);
        componentList.add(valueLabel);

        return componentList;

    }

    public String dateToLabelString(LocalDate date) {

        // Returns a string of day/month based on the LocalDate 'date'.

        String dateString = date.getDayOfMonth() + "/" + date.getMonthValue();
        return dateString;

    }

    public JLabel makeSpacerLabel() {

        // Makes a colored label to insert throughout the panel.

        JLabel spacerLabel = new JLabel();
        spacerLabel.setBackground(config.SECONDARY_TEXT_COLOR);
        spacerLabel.setOpaque(true);

        return spacerLabel;

    }
    
}

class RowPanel extends JPanel {

    // This uses FlowLayout to construct every row added to the subpanel.

    Config config = new Config();

    RowPanel() {

        this.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        this.setBackground(config.EXPENSES_COLOR);

    }

}
