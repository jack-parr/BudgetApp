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
    final Color[] CATEGORY_BUTTON_COLORS = {new Color(80, 80, 160), new Color(60, 60, 140)};

    JPanel sourcePanel;
    ButtonGroup isExpenseButtonGroup;
    JToggleButton incomeButton;
    JToggleButton expenseButton;
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
    JLabel categoryHeading;
    JPanel categoryInputPanel;
    JTextField categoryInput;
    JLabel valueHeading;
    JTextField valueInput;
    JButton closeButton;
    JButton confirmButton;
    JLabel systemResponseLabel;

    ArrayList<String> categoryShortcutsOneOff = new ArrayList<>();
    ArrayList<String> categoryShortcutsRecurring = new ArrayList<>();
    HashMap<String, JButton> categoryShortcutButtonsMap = new HashMap<>();
    int categoryButtonColorSelect = 0;
    ArrayList<String> frequencyList = new ArrayList<>();

    AddNewDataPanel(JPanel sourcePanel) {

        this.sourcePanel = sourcePanel;

        // POPULATING categoryShortcuts
        categoryShortcutsOneOff.add("Clothes");
        categoryShortcutsOneOff.add("Eating Out");
        categoryShortcutsOneOff.add("Education");
        categoryShortcutsOneOff.add("Fitness");
        categoryShortcutsOneOff.add("Gift");
        categoryShortcutsOneOff.add("Groceries");
        categoryShortcutsOneOff.add("Hobby");
        categoryShortcutsOneOff.add("Holiday");
        categoryShortcutsOneOff.add("Home");
        categoryShortcutsOneOff.add("Medical");
        categoryShortcutsOneOff.add("Misc");
        categoryShortcutsOneOff.add("Payslip");
        categoryShortcutsOneOff.add("Selling");
        categoryShortcutsOneOff.add("Shops");
        categoryShortcutsOneOff.add("Social");
        categoryShortcutsOneOff.add("Transport");

        categoryShortcutsRecurring.add("Bill");
        categoryShortcutsRecurring.add("Charity");
        categoryShortcutsRecurring.add("Entertainment");
        categoryShortcutsRecurring.add("Gym");
        categoryShortcutsRecurring.add("Insurance");
        categoryShortcutsRecurring.add("Misc");
        categoryShortcutsRecurring.add("Payslip");
        categoryShortcutsRecurring.add("Rent");
        categoryShortcutsRecurring.add("SFE");

        // POPULATING frequencyList
        frequencyList.add("Daily");
        frequencyList.add("Fortnightly");
        frequencyList.add("Monthly");
        frequencyList.add("Annually");

        this.setLayout(new FlowLayout(FlowLayout.LEFT, config.PANEL_X_GAP, config.PANEL_Y_GAP));
        this.setBackground(config.ADD_NEW_DATA_PANEL_COLOR);

        JLabel panelHeading = new JLabel("Add Data");
        panelHeading.setFont(config.HEADINGS_FONT);
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
        paintCategoryInput(false);  // generates in the one-off state.

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

        incomeButton = new JToggleButton("Income");
        incomeButton.setActionCommand("false");
        incomeButton.setContentAreaFilled(false);
        incomeButton.setOpaque(true);
        incomeButton.setFont(config.PRIMARY_FONT);
        incomeButton.setForeground(config.PRIMARY_TEXT_COLOR);
        incomeButton.setBackground(config.APPLY_BUTTON_COLOR);
        incomeButton.setBorder(null);
        incomeButton.setFocusable(false);
        isExpenseButtonGroup.add(incomeButton);
        incomeButton.setPreferredSize(new Dimension(100, INPUT_ROW_HEIGHT));
        this.add(incomeButton);

        expenseButton = new JToggleButton("Expense");
        expenseButton.setActionCommand("true");
        expenseButton.setContentAreaFilled(false);
        expenseButton.setOpaque(true);
        expenseButton.setFont(config.PRIMARY_FONT);
        expenseButton.setForeground(config.PRIMARY_TEXT_COLOR);
        expenseButton.setBackground(config.RESET_BUTTON_COLOR);
        expenseButton.setBorder(config.SELECTED_BUTTON_BORDER);
        expenseButton.setFocusable(false);
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
        oneOffButton.setContentAreaFilled(false);
        oneOffButton.setOpaque(true);
        oneOffButton.setFont(config.PRIMARY_FONT);
        oneOffButton.setForeground(config.PRIMARY_TEXT_COLOR);
        oneOffButton.setBackground(config.ADD_NEW_DATA_BUTTON_COLOR);
        oneOffButton.setBorder(config.SELECTED_BUTTON_BORDER);
        oneOffButton.setFocusable(false);
        oneOffButton.setSelected(true);
        isRecurringButtonGroup.add(oneOffButton);
        oneOffButton.setPreferredSize(new Dimension(100, INPUT_ROW_HEIGHT));
        this.add(oneOffButton);

        recurringButton = new JToggleButton("Recurring");
        recurringButton.setActionCommand("true");
        recurringButton.setContentAreaFilled(false);
        recurringButton.setOpaque(true);
        recurringButton.setFont(config.PRIMARY_FONT);
        recurringButton.setForeground(config.PRIMARY_TEXT_COLOR);
        recurringButton.setBackground(new Color(60, 100, 160));
        recurringButton.setBorder(null);
        recurringButton.setFocusable(false);
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
        frequencyInput.setBackground(Color.white);
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
        dayInput.setBackground(Color.white);
        dayInput.setFont(config.PRIMARY_FONT);
        dayInput.setPreferredSize(new Dimension(50, INPUT_ROW_HEIGHT));
        this.add(dayInput);

        paintDateSeparator();

        IntStream.range(1, 13).boxed().collect(Collectors.toList()).forEach(i -> monthInput.addItem(i));
        monthInput.setSelectedItem(todayDate.getMonthValue());
        monthInput.setBackground(Color.white);
        monthInput.setFont(config.PRIMARY_FONT);
        monthInput.setPreferredSize(new Dimension(50, INPUT_ROW_HEIGHT));
        this.add(monthInput);

        paintDateSeparator();

        Integer currentYear = Year.now().getValue();  // gets current year.
        IntStream.range(currentYear - 10, currentYear + 11).boxed().sorted(Collections.reverseOrder()).collect(Collectors.toList()).forEach(i -> yearInput.addItem(i));  // populates yearInput with +- 10 years from current year.
        yearInput.setSelectedItem(currentYear);
        yearInput.setBackground(Color.white);
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

    public void paintCategoryInput(boolean isRecurring) {

        // Paints the category input row and shortcut buttons.
        // This is done as a separate panel so that it can be modified by the Recurring button.

        categoryInputPanel = new JPanel();
        categoryInputPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 3));
        categoryInputPanel.setBackground(config.ADD_NEW_DATA_PANEL_COLOR);

        categoryHeading = new JLabel("Category:");
        categoryHeading.setFont(config.PRIMARY_FONT);
        categoryHeading.setForeground(config.PRIMARY_TEXT_COLOR);
        categoryHeading.setPreferredSize(new Dimension(100 + config.PANEL_X_GAP, INPUT_ROW_HEIGHT));
        categoryInputPanel.add(categoryHeading);

        categoryInput = new JTextField();
        categoryInput.setFont(config.PRIMARY_FONT);
        categoryInput.setPreferredSize(new Dimension(200, INPUT_ROW_HEIGHT));
        categoryInputPanel.add(categoryInput);

        // FILLING GAP IN REST OF ROW BEFORE SHORTCUT BUTTONS ARE ADDED
        JLabel categoryGapFiller = new JLabel();
        categoryGapFiller.setPreferredSize(new Dimension(config.DISPLAY_WIDTH - (categoryHeading.getPreferredSize().width + categoryInput.getPreferredSize().width), INPUT_ROW_HEIGHT));
        categoryGapFiller.setBackground(config.PANEL_BACKGROUND_COLOR);
        categoryGapFiller.setOpaque(true);
        categoryInputPanel.add(categoryGapFiller);

        // ADDING SHORTCUT BUTTONS
        if (isRecurring) {
            categoryShortcutsRecurring.forEach(b -> paintCategoryShortcutButton(b, categoryButtonColorSelect));  // painting the shortcut buttons.
        }
        else {
            categoryShortcutsOneOff.forEach(b -> paintCategoryShortcutButton(b, categoryButtonColorSelect));  // painting the shortcut buttons.
        }

        categoryInputPanel.setPreferredSize(new Dimension(config.DISPLAY_WIDTH, (3*INPUT_ROW_HEIGHT) + 6));
        this.add(categoryInputPanel, 25);

    }

    public void paintValueInput() {

        // Paints the value input row.

        valueHeading = new JLabel("Amount:        £");
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

        // Paints the cancel and confirm buttons, and the system response label.

        closeButton = new JButton("Close");
        closeButton.setFont(config.PRIMARY_FONT);
        closeButton.setForeground(config.PRIMARY_TEXT_COLOR);
        closeButton.setBackground(config.RESET_BUTTON_COLOR);
        closeButton.setFocusable(false);
        this.add(closeButton);

        confirmButton = new JButton("Confirm");
        confirmButton.setFont(config.PRIMARY_FONT);
        confirmButton.setForeground(config.PRIMARY_TEXT_COLOR);
        confirmButton.setBackground(config.APPLY_BUTTON_COLOR);
        confirmButton.setFocusable(false);
        this.add(confirmButton);

        systemResponseLabel = new JLabel();
        systemResponseLabel.setFont(config.PRIMARY_FONT);
        systemResponseLabel.setForeground(config.PRIMARY_TEXT_COLOR);
        systemResponseLabel.setPreferredSize(new Dimension(250, confirmButton.getPreferredSize().height));
        this.add(systemResponseLabel);

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

    public void paintCategoryShortcutButton(String category, int i) {

        // Paints a single category shortcut button containing the string argument. Also adds it the HashMap.
        
        // CREATE AND PAINT BUTTON
        JButton categoryButton = new JButton(category);
        categoryButton.setFont(config.PRIMARY_FONT);
        categoryButton.setForeground(config.PRIMARY_TEXT_COLOR);
        categoryButton.setBackground(CATEGORY_BUTTON_COLORS[i]);
        categoryButton.setFocusable(false);
        categoryButton.setActionCommand("category" + category);
        categoryInputPanel.add(categoryButton);

        // ADD BUTTON TO HASHMAP
        categoryShortcutButtonsMap.put(category, categoryButton);
        
        categoryButtonColorSelect = 1 - i;

    }
    
}
