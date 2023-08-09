import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.time.LocalDate;
import java.time.Year;
import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class NewExpensePanel extends JPanel {

    Config config = new Config();
    final int INPUT_ROW_HEIGHT = 30;
    final int DATE_SEPARATOR_WIDTH = 15;

    ArrayList<String> categoryShortcuts = new ArrayList<String>();

    NewExpensePanel() {

        // POPULATING categoryShortcuts
        categoryShortcuts.add("Groceries");
        categoryShortcuts.add("Social");
        categoryShortcuts.add("Eating Out");
        categoryShortcuts.add("Hobby");
        categoryShortcuts.add("Clothing");
        categoryShortcuts.add("House");
        categoryShortcuts.add("Gift");
        categoryShortcuts.add("Transport");
        categoryShortcuts.add("Medical");
        categoryShortcuts.add("Fitness");
        categoryShortcuts.add("Holiday");
        categoryShortcuts.add("Education");

        this.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        this.setBackground(config.EXPENSES_COLOR);

        JLabel panelHeading = new JLabel("Add Data");
        panelHeading.setFont(config.HEADING_FONT);
        panelHeading.setForeground(config.GENERAL_TEXT_COLOR);
        panelHeading.setPreferredSize(new Dimension(config.DISPLAY_WIDTH, 50));
        this.add(panelHeading);

        // DATE INPUT
        paintRowSeparator();
        paintDateInput();

        // CATEGORY INPUT
        paintRowSeparator();
        paintCategoryInput();

        // VALUE INPUT
        paintRowSeparator();
        paintValueInput();

        // cancel or confirm buttons. ActionListener in AppFrame.
        // will need input checks to make sure all inputs are present and valid
        paintRowSeparator();

    }

    public void paintDateInput() {

        // Paints the date input row.

        LocalDate todayDate = LocalDate.now();

        JLabel dateHeading = new JLabel("Date:");
        dateHeading.setFont(config.GENERAL_FONT);
        dateHeading.setForeground(config.GENERAL_TEXT_COLOR);
        dateHeading.setPreferredSize(new Dimension(70, INPUT_ROW_HEIGHT));
        this.add(dateHeading);

        JComboBox<Integer> daySelect = new JComboBox<>();
        IntStream.range(1, 32).boxed().collect(Collectors.toList()).forEach(i -> daySelect.addItem(i));
        daySelect.setSelectedItem(todayDate.getDayOfMonth());
        daySelect.setFont(config.GENERAL_FONT);
        daySelect.setPreferredSize(new Dimension(50, INPUT_ROW_HEIGHT));
        this.add(daySelect);

        paintDateSeparator();

        JComboBox<Integer> monthSelect = new JComboBox<>();
        IntStream.range(1, 13).boxed().collect(Collectors.toList()).forEach(i -> monthSelect.addItem(i));
        monthSelect.setSelectedItem(todayDate.getMonthValue());
        monthSelect.setFont(config.GENERAL_FONT);
        monthSelect.setPreferredSize(new Dimension(50, INPUT_ROW_HEIGHT));
        this.add(monthSelect);

        paintDateSeparator();

        JComboBox<Integer> yearSelect = new JComboBox<>();
        Integer currentYear = Year.now().getValue();  // gets current year.
        IntStream.range(currentYear - 10, currentYear + 1).boxed().sorted(Collections.reverseOrder()).collect(Collectors.toList()).forEach(i -> yearSelect.addItem(i));  // populated yearSelect with last 10 years.
        yearSelect.setFont(config.GENERAL_FONT);
        yearSelect.setPreferredSize(new Dimension(70, INPUT_ROW_HEIGHT));
        this.add(yearSelect);

        JLabel dateGapFiller = new JLabel();
        dateGapFiller.setPreferredSize(new Dimension(config.DISPLAY_WIDTH - (dateHeading.getPreferredSize().width + daySelect.getPreferredSize().width + 2*DATE_SEPARATOR_WIDTH + monthSelect.getPreferredSize().width + yearSelect.getPreferredSize().width), INPUT_ROW_HEIGHT));
        dateGapFiller.setBackground(Color.red);
        dateGapFiller.setOpaque(true);
        this.add(dateGapFiller);

    }

    public void paintCategoryInput() {

        // Paints the category input row and shortcut buttons.

        JLabel categoryHeading = new JLabel("Category:");
        categoryHeading.setFont(config.GENERAL_FONT);
        categoryHeading.setForeground(config.GENERAL_TEXT_COLOR);
        categoryHeading.setPreferredSize(new Dimension(100, INPUT_ROW_HEIGHT));
        this.add(categoryHeading);

        JTextField categoryInput = new JTextField();
        categoryInput.setFont(config.GENERAL_FONT);
        categoryInput.setPreferredSize(new Dimension(200, INPUT_ROW_HEIGHT));
        this.add(categoryInput);

        JLabel categoryGapFiller = new JLabel();
        categoryGapFiller.setPreferredSize(new Dimension(config.DISPLAY_WIDTH - (categoryHeading.getPreferredSize().width + categoryInput.getPreferredSize().width), INPUT_ROW_HEIGHT));
        categoryGapFiller.setBackground(Color.red);
        categoryGapFiller.setOpaque(true);
        this.add(categoryGapFiller);

        categoryShortcuts.forEach(s -> paintCategoryShortcutButton(s));  // painting the shortcut buttons

    }

    public void paintValueInput() {

        // Paints the value input row.

        // simple textfield input.

    }

    public void paintRowSeparator() {

        // Paints the row separator between the input rows.

        JLabel rowSeparator = new JLabel();
        rowSeparator.setPreferredSize(new Dimension(config.DISPLAY_WIDTH, 20));
        rowSeparator.setBackground(Color.green);
        rowSeparator.setOpaque(true);
        this.add(rowSeparator);
    }

    public void paintDateSeparator() {

        // Paints the separator containing "/" within the date input row.

        JLabel dateSeparator = new JLabel(" / ");
        dateSeparator.setFont(config.GENERAL_FONT);
        dateSeparator.setForeground(config.GENERAL_TEXT_COLOR);
        dateSeparator.setPreferredSize(new Dimension(DATE_SEPARATOR_WIDTH, INPUT_ROW_HEIGHT));
        this.add(dateSeparator);

    }

    public void paintCategoryShortcutButton(String category) {

        // Paints a single category shortcut button containing the string argument.

        JButton categoryButton = new JButton(category);
        categoryButton.setFont(config.GENERAL_FONT);
        this.add(categoryButton);
        // CREATE HASHMAP OF BUTTON COMPONENTS FOR ADDING LISTENERS IN AppFrame

    }
    
}
