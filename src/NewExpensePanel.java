import java.awt.Dimension;
import java.awt.FlowLayout;
import java.text.DateFormatSymbols;
import java.time.LocalDate;
import java.time.Year;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.swing.JComboBox;
import javax.swing.JPanel;

public class NewExpensePanel extends JPanel {

    Config config = new Config();

    NewExpensePanel() {

        this.setLayout(new FlowLayout());
        this.setBackground(config.EXPENSES_COLOR);

        // DATE INPUT
        LocalDate todayDate = LocalDate.now();

        JComboBox<Integer> daySelect = new JComboBox<>();
        IntStream.range(1, 32).boxed().collect(Collectors.toList()).forEach(i -> daySelect.addItem(i));
        daySelect.setSelectedItem(todayDate.getDayOfMonth());
        daySelect.setPreferredSize(new Dimension(100, 50));
        this.add(daySelect);

        JComboBox<Integer> monthSelect = new JComboBox<>();
        IntStream.range(1, 13).boxed().collect(Collectors.toList()).forEach(i -> monthSelect.addItem(i));
        monthSelect.setSelectedItem(todayDate.getMonthValue());
        monthSelect.setPreferredSize(new Dimension(100, 50));
        this.add(monthSelect);

        JComboBox<Integer> yearSelect = new JComboBox<>();
        Integer currentYear = Year.now().getValue();  // gets current year.
        IntStream.range(currentYear - 10, currentYear + 1).boxed().sorted(Collections.reverseOrder()).collect(Collectors.toList()).forEach(i -> yearSelect.addItem(i));  // populated yearSelect with last 10 years.
        yearSelect.setPreferredSize(new Dimension(100, 50));
        this.add(yearSelect);

        // CATEGORY INPUT
        // wall of buttons with a label to show which one has been selected.

        // VALUE INPUT
        // simple textfield input

        // cancel or confirm buttons. ActionListener in AppFrame.
        // will need input checks to make sure all inputs are present and valid

    }
    
}
