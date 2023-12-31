/*
 * Contains all the configuration variables.
 */

import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.border.Border;

public class Config {

    // DIMENSIONS
    public final int DISPLAY_WIDTH = Toolkit.getDefaultToolkit().getScreenSize().width - 50;
    public final int DISPLAY_HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().height - 100;
    public final int MENU_HEIGHT = 40;
    public final int PANEL_HEIGHT = DISPLAY_HEIGHT - MENU_HEIGHT;
    public final int PANEL_HEADER_HEIGHT = 35;
    public final int PANEL_Y_GAP = 10;
    public final int PANEL_X_GAP = 5;
    public final int DATA_ROW_HEIGHT = 30;
    public final int DELETE_BUTTON_WIDTH = 40;
    public final int SCROLLBAR_WIDTH = 10;
    public final int SCROLLBAR_INCREMENT = 10;
    public final int SCROLLABLE_DISPLAY_WIDTH = DISPLAY_WIDTH - SCROLLBAR_WIDTH - (2*PANEL_X_GAP);

    // PANEL COLORS
    public final Color SUMMARY_PANEL_COLOR = new Color(80, 80, 80);
    public final Color VIEW_GENERATORS_PANEL_COLOR = new Color(70, 70, 70);
    public final Color VIEW_DATA_PANEL_COLOR = new Color(60, 60, 60);
    public final Color ADD_NEW_DATA_PANEL_COLOR = new Color(50, 50, 50);
    public final Color PANEL_BACKGROUND_COLOR = new Color(50, 50, 50);

    // TEXT FONT AND COLORS
    public final Color PRIMARY_TEXT_COLOR = new Color(235, 235, 235);
    public final Font PRIMARY_FONT = new Font("Arial Rounded MT Bold", Font.PLAIN, 14);
    public final Color SECONDARY_TEXT_COLOR = new Color(150, 150, 150);
    public final Font MENU_FONT = new Font("Arial Rounded MT Bold", Font.PLAIN, 14);
    public final Font HEADINGS_FONT = new Font("Arial Rounded MT Bold", Font.PLAIN, 24);
    public final Font HEADER_ROW_FONT = new Font("Arial Rounded MT Bold", Font.PLAIN, 12);
    public final Font DATA_ROW_FONT = new Font("Arial Rounded MT Bold", Font.PLAIN, 14);

    // OTHER COLORS
    public final Color APPLY_BUTTON_COLOR = new Color(50, 120, 0);
    public final Color RESET_BUTTON_COLOR = new Color(117, 0, 0);
    public final Color ADD_NEW_DATA_BUTTON_COLOR = new Color(0, 57, 117);
    public final Color INCOME_COLOR = Color.green;
    public final Color EXPENSE_COLOR = Color.red;
    public final Border SELECTED_BUTTON_BORDER = BorderFactory.createLineBorder(Color.white, 3, true);
    
    // DATA SOURCE
    public final String DATA_FILEPATH = "data.csv";
    final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    protected ArrayList<Component> makeDataHeaderRowComponents() {

        // Makes a list of components of a header row for a month list.
        // Contents:
        // [0]: Date
        // [1]: Category
        // [2]: Value
        // [3]: Frequency
        // [4]: Terminates
        // [5]: Delete

        JLabel dateLabel = new JLabel("Date");
        dateLabel.setFont(HEADER_ROW_FONT);
        dateLabel.setForeground(SECONDARY_TEXT_COLOR);

        JLabel categoryLabel = new JLabel("Category");
        categoryLabel.setFont(HEADER_ROW_FONT);
        categoryLabel.setForeground(SECONDARY_TEXT_COLOR);

        JLabel valueLabel = new JLabel("Value");
        valueLabel.setFont(HEADER_ROW_FONT);
        valueLabel.setForeground(SECONDARY_TEXT_COLOR);

        JLabel frequencyLabel = new JLabel("Frequency");
        frequencyLabel.setFont(HEADER_ROW_FONT);
        frequencyLabel.setForeground(SECONDARY_TEXT_COLOR);

        JLabel terminatesLabel = new JLabel("Terminates");
        terminatesLabel.setFont(HEADER_ROW_FONT);
        terminatesLabel.setForeground(SECONDARY_TEXT_COLOR);

        JLabel deleteLabel = new JLabel("Delete");
        deleteLabel.setFont(HEADER_ROW_FONT);
        deleteLabel.setForeground(SECONDARY_TEXT_COLOR);
        deleteLabel.setPreferredSize(new Dimension(DELETE_BUTTON_WIDTH, DATA_ROW_HEIGHT));

        ArrayList<Component> componentList = new ArrayList<>();
        componentList.add(dateLabel);
        componentList.add(categoryLabel);
        componentList.add(valueLabel);
        componentList.add(frequencyLabel);
        componentList.add(terminatesLabel);
        componentList.add(deleteLabel);

        return componentList;

    }

    protected ArrayList<Component> makeDataDisplayRowComponents(DataEntry dataEntry) {

        // Makes a list of components of a data row from a DataEntry.
        // Contents:
        // [0]: Date
        // [1]: Category
        // [2]: Value
        // [3]: Frequency
        // [4]: Terminates

        JLabel dateLabel = new JLabel(dateToLabelString(dataEntry.getStartDate(), false));
        dateLabel.setFont(DATA_ROW_FONT);
        dateLabel.setForeground(PRIMARY_TEXT_COLOR);

        JLabel categoryLabel = new JLabel(dataEntry.getCategory());
        categoryLabel.setFont(DATA_ROW_FONT);
        categoryLabel.setForeground(PRIMARY_TEXT_COLOR);

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

        JLabel frequencyLabel = new JLabel(dataEntry.getFrequency());
        frequencyLabel.setFont(DATA_ROW_FONT);
        frequencyLabel.setForeground(PRIMARY_TEXT_COLOR);

        JLabel terminatesLabel = new JLabel(dateToLabelString(dataEntry.getEndDate(), true));
        terminatesLabel.setFont(DATA_ROW_FONT);
        terminatesLabel.setForeground(PRIMARY_TEXT_COLOR);

        ArrayList<Component> componentList = new ArrayList<>();
        componentList.add(dateLabel);
        componentList.add(categoryLabel);
        componentList.add(valueLabel);
        componentList.add(frequencyLabel);
        componentList.add(terminatesLabel);

        return componentList;

    }

    protected JLabel makeDataSpacerLabel() {

        // Makes a colored row separator label to insert throughout the ViewDataPanel and GeneratorsPanel.

        JLabel spacerLabel = new JLabel();
        spacerLabel.setBackground(SECONDARY_TEXT_COLOR);
        spacerLabel.setOpaque(true);

        return spacerLabel;

    }

    protected String dateToLabelString(LocalDate date, boolean includeYear) {

        // Returns a string of day/month based on the LocalDate 'date'.

        String dateString = date.getDayOfMonth() + "/" + date.getMonthValue();
        if (includeYear) {
            dateString = dateString + "/" + date.getYear();
        }
        return dateString;

    }
    
}
