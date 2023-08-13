import java.awt.Dimension;
import java.awt.FlowLayout;
import java.time.LocalDate;
import java.time.Year;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToggleButton;

public class AddNewDataPanel extends JPanel {

    Config config = new Config();
    final int INPUT_ROW_HEIGHT = 30;
    final int DATE_SEPARATOR_WIDTH = 15;

    ButtonGroup isExpenseButtonGroup;
    ButtonGroup isRecurringButtonGroup;
    JToggleButton oneOffButton;
    JToggleButton recurringButton;
    JLabel frequencyHeading;
    JComboBox<String> frequencyInput;
    JLabel startDateHeading = new JLabel("Date: ");
    JComboBox<Integer> startDateDayInput = new JComboBox<>();
    JComboBox<Integer> startDateMonthInput = new JComboBox<>();
    JComboBox<Integer> startDateYearInput = new JComboBox<>();
    JLabel endDateHeading = new JLabel("Until: ");
    JComboBox<Integer> endDateDayInput = new JComboBox<>();
    JComboBox<Integer> endDateMonthInput = new JComboBox<>();
    JComboBox<Integer> endDateYearInput = new JComboBox<>();
    JTextField categoryInput;
    JTextField valueInput;
    JButton closeButton;
    JButton confirmButton;

    ArrayList<String> categoryShortcuts = new ArrayList<>();
    HashMap<String, JButton> categoryShortcutButtonsMap = new HashMap<>();
    ArrayList<String> frequencyList = new ArrayList<>();

    AddNewDataPanel() {

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

        // POPULATING frequencyList
        frequencyList.add("Daily");
        frequencyList.add("Monthly");
        frequencyList.add("Annually");

        this.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 8));
        this.setBackground(config.EXPENSES_COLOR);

        JLabel panelHeading = new JLabel("Add Data");
        panelHeading.setFont(config.HEADING_FONT);
        panelHeading.setForeground(config.PRIMARY_TEXT_COLOR);
        panelHeading.setPreferredSize(new Dimension(config.DISPLAY_WIDTH, 40));
        this.add(panelHeading);

        // SIGN INPUT
        paintRowSeparator();
        paintSignInput();
        
        // TYPE INPUT
        paintRowSeparator();
        paintIsRecurringInput();

        // STARTDATE INPUT
        paintRowSeparator();
        paintDateInput(true, startDateHeading, startDateDayInput, startDateMonthInput, startDateYearInput);

        // FREQUENCY INPUT
        paintRowSeparator();
        paintFrequencyInput();

        // ENDDATE INPUT
        paintRowSeparator();
        paintDateInput(false, endDateHeading, endDateDayInput, endDateMonthInput, endDateYearInput);

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

    public void paintSignInput() {

        // Paints the income or expense select row.

        isExpenseButtonGroup = new ButtonGroup();

        JToggleButton incomeButton = new JToggleButton("Income");
        incomeButton.setActionCommand("false");
        isExpenseButtonGroup.add(incomeButton);
        incomeButton.setPreferredSize(new Dimension(100, INPUT_ROW_HEIGHT));
        this.add(incomeButton);

        JToggleButton expenseButton = new JToggleButton("Expense");
        expenseButton.setActionCommand("true");
        expenseButton.setSelected(true);
        isExpenseButtonGroup.add(expenseButton);
        expenseButton.setPreferredSize(new Dimension(100, INPUT_ROW_HEIGHT));
        this.add(expenseButton);

    }

    public void paintIsRecurringInput() {

        // Paints the isRecurring select row.

        isRecurringButtonGroup = new ButtonGroup();

        oneOffButton = new JToggleButton("One-Off");
        oneOffButton.setActionCommand("false");
        oneOffButton.setSelected(true);
        isRecurringButtonGroup.add(oneOffButton);
        oneOffButton.setPreferredSize(new Dimension(100, INPUT_ROW_HEIGHT));
        this.add(oneOffButton);

        recurringButton = new JToggleButton("Recurring");
        recurringButton.setActionCommand("true");
        isRecurringButtonGroup.add(recurringButton);
        recurringButton.setPreferredSize(new Dimension(100, INPUT_ROW_HEIGHT));
        this.add(recurringButton);

    }
    
    public void paintFrequencyInput() {

        // Paints the frequency select row.
        // On generation, isRecurring = false, so this is disabled.

        frequencyHeading = new JLabel("Frequency: ");
        frequencyHeading.setFont(config.PRIMARY_FONT);
        frequencyHeading.setForeground(config.SECONDARY_TEXT_COLOR);
        frequencyHeading.setPreferredSize(new Dimension(100, INPUT_ROW_HEIGHT));
        this.add(frequencyHeading);

        frequencyInput = new JComboBox<>();
        frequencyList.forEach(i -> frequencyInput.addItem(i));
        frequencyInput.setSelectedIndex(-1);
        frequencyInput.setEnabled(false);
        frequencyInput.setPreferredSize(new Dimension(100, INPUT_ROW_HEIGHT));
        this.add(frequencyInput);

    }

    public void paintDateInput(boolean defaultEnabled, JLabel dateHeading, JComboBox<Integer> dayInput, JComboBox<Integer> monthInput, JComboBox<Integer> yearInput) {

        // Paints the date input row.

        LocalDate todayDate = LocalDate.now();

        dateHeading.setFont(config.PRIMARY_FONT);
        dateHeading.setForeground(config.PRIMARY_TEXT_COLOR);
        dateHeading.setPreferredSize(new Dimension(100, INPUT_ROW_HEIGHT));
        this.add(dateHeading);

        IntStream.range(1, 32).boxed().collect(Collectors.toList()).forEach(i -> dayInput.addItem(i));
        dayInput.setSelectedItem(todayDate.getDayOfMonth());
        dayInput.setFont(config.PRIMARY_FONT);
        dayInput.setPreferredSize(new Dimension(50, INPUT_ROW_HEIGHT));
        this.add(dayInput);

        paintDateSeparator();

        IntStream.range(1, 13).boxed().collect(Collectors.toList()).forEach(i -> monthInput.addItem(i));
        monthInput.setSelectedItem(todayDate.getMonthValue());
        monthInput.setFont(config.PRIMARY_FONT);
        monthInput.setPreferredSize(new Dimension(50, INPUT_ROW_HEIGHT));
        this.add(monthInput);

        paintDateSeparator();

        Integer currentYear = Year.now().getValue();  // gets current year.
        IntStream.range(currentYear - 10, currentYear + 11).boxed().sorted(Collections.reverseOrder()).collect(Collectors.toList()).forEach(i -> yearInput.addItem(i));  // populates yearInput with +- 10 years from current year.
        yearInput.setSelectedItem(currentYear);
        yearInput.setFont(config.PRIMARY_FONT);
        yearInput.setPreferredSize(new Dimension(70, INPUT_ROW_HEIGHT));
        this.add(yearInput);

        // MODIFYING IF DEFAULT STATE IS DISABLED
        if (!defaultEnabled) {
            dateHeading.setForeground(config.SECONDARY_TEXT_COLOR);
            dayInput.setEnabled(false);
            monthInput.setEnabled(false);
            yearInput.setEnabled(false);
        }

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

        closeButton = new JButton("Close");
        closeButton.setFont(config.PRIMARY_FONT);
        this.add(closeButton);

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
