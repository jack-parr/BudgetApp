/*
 * This is the subpanel that displays the monthly data tables in the expenses panel.
 */

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.text.DateFormatSymbols;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class ExpensesSubPanel2 extends JPanel {

    Config config = new Config();

    final int MONTH_HEADER_HEIGHT = 50;  // height of the month labels.
    final Font MONTH_HEADER_FONT = new Font("Arial Rounded MT Bold", Font.PLAIN, 40);
    final int DATA_ROW_HEIGHT = 20;
    final Font DATA_ROW_FONT = new Font("Arial Rounded MT Bold", Font.PLAIN, 14);
    final Font HEADER_ROW_FONT = new Font("Arial Rounded MT Bold", Font.PLAIN, 12);
    final Color HEADER_ROW_COLOR = new Color(150, 150, 150);
    

    ExpensesSubPanel2(int year) {

        this.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        this.setBackground(config.EXPENSES_COLOR);

        List<Integer> monthKeys = new ArrayList<Integer>(AppFrame.listsHashMap.keySet().stream().toList());  // get list of monthTableIDs.

        for (int monthListKey : monthKeys) {
            if (ExpensesPanel.getYearFromMonthKey(monthListKey) != year) {
                continue;  // passes if data table is not relevant to year.
            }

            // MONTH TITLE
            int monthInt = Integer.parseInt(Integer.toString(monthListKey).substring(4));  // extract monthInt from monthListID.
            String monthString = new DateFormatSymbols().getMonths()[monthInt-1];  // convert monthInt to String.
            JLabel monthHeader = new JLabel(monthString);
            monthHeader.setFont(MONTH_HEADER_FONT);
            monthHeader.setBackground(config.SAVINGS_COLOR);
            monthHeader.setOpaque(true);
            monthHeader.setForeground(config.GENERAL_TEXT_COLOR);
            monthHeader.setPreferredSize(new Dimension(config.DISPLAY_WIDTH, MONTH_HEADER_HEIGHT));
            this.add(monthHeader);

            // HEADER ROW
            ArrayList<Component> headerRowComponents = makeHeaderRow();  // makes a list containing all the components.

            Component headerDateLabel = headerRowComponents.get(0);
            headerDateLabel.setPreferredSize(new Dimension(config.DISPLAY_WIDTH / 3, DATA_ROW_HEIGHT));
            this.add(headerDateLabel);

            Component headerCategoryLabel = headerRowComponents.get(1);
            headerCategoryLabel.setPreferredSize(new Dimension(config.DISPLAY_WIDTH / 3, DATA_ROW_HEIGHT));
            this.add(headerCategoryLabel);

            Component headerValueLabel = headerRowComponents.get(2);
            headerValueLabel.setPreferredSize(new Dimension(config.DISPLAY_WIDTH / 3, DATA_ROW_HEIGHT));
            this.add(headerValueLabel);

            // DISPLAY DATA FROM LIST
            String prevDate = "";
            String thisDate;

            ArrayList<DataEntry> list = AppFrame.listsHashMap.get(monthListKey);  // get list.
            Collections.sort(list, Comparator.comparing(DataEntry::getSortCode));

            for (DataEntry dataEntry : list) {

                JLabel spacerLabel = makeSpacerLabel();
                spacerLabel.setPreferredSize(new Dimension(config.DISPLAY_WIDTH, 1));

                ArrayList<Component> dataRowComponents = makeDataRow(dataEntry);

                Component dataDateLabel = dataRowComponents.get(0);
                dataDateLabel.setPreferredSize(new Dimension(config.DISPLAY_WIDTH / 3, DATA_ROW_HEIGHT));

                Component dataCategoryLabel = dataRowComponents.get(1);
                dataCategoryLabel.setPreferredSize(new Dimension(config.DISPLAY_WIDTH / 3, DATA_ROW_HEIGHT));

                Component dataValueLabel = dataRowComponents.get(2);
                dataValueLabel.setPreferredSize(new Dimension(config.DISPLAY_WIDTH / 3, DATA_ROW_HEIGHT));

                thisDate = dateToLabelString(dataEntry.getDate());
                if (thisDate.equals(prevDate)) {
                    // no spacer, empty date label.
                    JLabel emptyDateLabel = new JLabel();
                    emptyDateLabel.setPreferredSize(new Dimension(config.DISPLAY_WIDTH / 3, DATA_ROW_HEIGHT));
                    this.add(emptyDateLabel);
                }
                else {
                    // gray spacer, populated date label.
                    this.add(spacerLabel);
                    this.add(dataDateLabel);
                }

                this.add(dataCategoryLabel);
                this.add(dataValueLabel);
                prevDate = thisDate;

            }
            
        }

    }

    public ArrayList<Component> makeHeaderRow() {

        // Makes a list of components of a header row for a month list.

        JLabel dateLabel = new JLabel("Date");
        dateLabel.setFont(HEADER_ROW_FONT);
        dateLabel.setForeground(HEADER_ROW_COLOR);

        JLabel categoryLabel = new JLabel("Category");
        categoryLabel.setFont(HEADER_ROW_FONT);
        categoryLabel.setForeground(HEADER_ROW_COLOR);

        JLabel valueLabel = new JLabel("Value");
        valueLabel.setFont(HEADER_ROW_FONT);
        valueLabel.setForeground(HEADER_ROW_COLOR);

        ArrayList<Component> componentList = new ArrayList<>();
        componentList.add(dateLabel);
        componentList.add(categoryLabel);
        componentList.add(valueLabel);

        return componentList;

    }

    public ArrayList<Component> makeDataRow(DataEntry dataEntry) {

        // Makes a list of components of a data row from a DataEntry.

        JLabel dateLabel = new JLabel(dateToLabelString(dataEntry.getDate()));
        dateLabel.setFont(DATA_ROW_FONT);
        dateLabel.setForeground(config.GENERAL_TEXT_COLOR);

        JLabel categoryLabel = new JLabel(dataEntry.getCategory());
        categoryLabel.setFont(DATA_ROW_FONT);
        categoryLabel.setForeground(config.GENERAL_TEXT_COLOR);

        JLabel valueLabel = new JLabel(String.format("%.2f", dataEntry.getValue()));
        valueLabel.setFont(DATA_ROW_FONT);
        valueLabel.setForeground(config.GENERAL_TEXT_COLOR);

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
        spacerLabel.setBackground(HEADER_ROW_COLOR);
        spacerLabel.setOpaque(true);

        return spacerLabel;

    }
    
}
