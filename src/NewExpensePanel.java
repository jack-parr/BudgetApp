import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.time.LocalDate;
import java.time.Year;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
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

    JComboBox<Integer> daySelect;
    JComboBox<Integer> monthSelect;
    JComboBox<Integer> yearSelect;
    JTextField categoryInput;
    JTextField valueInput;
    JButton cancelButton;
    JButton confirmButton;

    ArrayList<String> categoryShortcuts = new ArrayList<String>();
    HashMap<String, JButton> categoryShortcutButtonsMap = new HashMap<>();

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

        this.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 10));
        this.setBackground(config.EXPENSES_COLOR);

        JLabel panelHeading = new JLabel("Add Data");
        panelHeading.setFont(config.HEADING_FONT);
        panelHeading.setForeground(config.PRIMARY_TEXT_COLOR);
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

        // CANCEL / CONFIRM
        paintRowSeparator();
        paintCancelConfirm();

    }

    public void paintDateInput() {

        // Paints the date input row.

        LocalDate todayDate = LocalDate.now();

        JLabel dateHeading = new JLabel("Date:");
        dateHeading.setFont(config.PRIMARY_FONT);
        dateHeading.setForeground(config.PRIMARY_TEXT_COLOR);
        dateHeading.setPreferredSize(new Dimension(70, INPUT_ROW_HEIGHT));
        this.add(dateHeading);

        daySelect = new JComboBox<>();
        IntStream.range(1, 32).boxed().collect(Collectors.toList()).forEach(i -> daySelect.addItem(i));
        daySelect.setSelectedItem(todayDate.getDayOfMonth());
        daySelect.setFont(config.PRIMARY_FONT);
        daySelect.setPreferredSize(new Dimension(50, INPUT_ROW_HEIGHT));
        this.add(daySelect);

        paintDateSeparator();

        monthSelect = new JComboBox<>();
        IntStream.range(1, 13).boxed().collect(Collectors.toList()).forEach(i -> monthSelect.addItem(i));
        monthSelect.setSelectedItem(todayDate.getMonthValue());
        monthSelect.setFont(config.PRIMARY_FONT);
        monthSelect.setPreferredSize(new Dimension(50, INPUT_ROW_HEIGHT));
        this.add(monthSelect);

        paintDateSeparator();

        yearSelect = new JComboBox<>();
        Integer currentYear = Year.now().getValue();  // gets current year.
        IntStream.range(currentYear - 10, currentYear + 1).boxed().sorted(Collections.reverseOrder()).collect(Collectors.toList()).forEach(i -> yearSelect.addItem(i));  // populated yearSelect with last 10 years.
        yearSelect.setFont(config.PRIMARY_FONT);
        yearSelect.setPreferredSize(new Dimension(70, INPUT_ROW_HEIGHT));
        this.add(yearSelect);

    }

    public void paintCategoryInput() {

        // Paints the category input row and shortcut buttons.

        JLabel categoryHeading = new JLabel("Category:");
        categoryHeading.setFont(config.PRIMARY_FONT);
        categoryHeading.setForeground(config.PRIMARY_TEXT_COLOR);
        categoryHeading.setPreferredSize(new Dimension(100, INPUT_ROW_HEIGHT));
        this.add(categoryHeading);

        categoryInput = new JTextField();
        categoryInput.setFont(config.PRIMARY_FONT);
        categoryInput.setPreferredSize(new Dimension(200, INPUT_ROW_HEIGHT));
        this.add(categoryInput);

        // FILLING GAP IN REST OF ROW BEFORE SHORTCUT BUTTONS ARE ADDED
        JLabel categoryGapFiller = new JLabel();
        categoryGapFiller.setPreferredSize(new Dimension(config.DISPLAY_WIDTH - (categoryHeading.getPreferredSize().width + categoryInput.getPreferredSize().width), INPUT_ROW_HEIGHT));
        categoryGapFiller.setBackground(config.EXPENSES_COLOR);
        categoryGapFiller.setOpaque(true);
        this.add(categoryGapFiller);

        categoryShortcuts.forEach(s -> paintCategoryShortcutButton(s));  // painting the shortcut buttons

    }

    public void paintValueInput() {

        // Paints the value input row.

        JLabel valueHeading = new JLabel("Amount:   £");
        valueHeading.setFont(config.PRIMARY_FONT);
        valueHeading.setForeground(config.PRIMARY_TEXT_COLOR);
        valueHeading.setPreferredSize(new Dimension(100, INPUT_ROW_HEIGHT));
        this.add(valueHeading);

        valueInput = new JTextField();
        valueInput.setFont(config.PRIMARY_FONT);
        valueInput.setPreferredSize(new Dimension(100, INPUT_ROW_HEIGHT));
        this.add(valueInput);

    }

    public void paintCancelConfirm() {

        // Paints the cancel and confirm buttons.

        cancelButton = new JButton("Cancel");
        cancelButton.setFont(config.PRIMARY_FONT);
        this.add(cancelButton);

        confirmButton = new JButton("Confirm");
        confirmButton.setFont(config.PRIMARY_FONT);
        this.add(confirmButton);

    }

    public void paintRowSeparator() {

        // Paints the row separator between the input rows.

        JLabel rowSeparator = new JLabel();
        rowSeparator.setPreferredSize(new Dimension(config.DISPLAY_WIDTH, 3));
        rowSeparator.setBackground(config.SECONDARY_TEXT_COLOR);
        rowSeparator.setOpaque(true);
        this.add(rowSeparator);

    }

    public void paintDateSeparator() {

        // Paints the separator containing "/" within the date input row.

        JLabel dateSeparator = new JLabel(" / ");
        dateSeparator.setFont(config.PRIMARY_FONT);
        dateSeparator.setForeground(config.PRIMARY_TEXT_COLOR);
        dateSeparator.setPreferredSize(new Dimension(DATE_SEPARATOR_WIDTH, INPUT_ROW_HEIGHT));
        this.add(dateSeparator);

    }

    public void paintCategoryShortcutButton(String category) {

        // Paints a single category shortcut button containing the string argument. Also adds it the HashMap.

        // CREATE AND PAINT BUTTON
        JButton categoryButton = new JButton(category);
        categoryButton.setFont(config.PRIMARY_FONT);
        categoryButton.setActionCommand("category" + category);
        this.add(categoryButton);

        // ADD BUTTON TO HASHMAP
        categoryShortcutButtonsMap.put(category, categoryButton);

    }
    
}
