import java.awt.Color;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class HeaderTable extends JTable {
    
    // This is the header row which appears before each monthly JTable.

    HeaderTable() {

        Config config = new Config();

        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.addColumn("Date");
        tableModel.addColumn("Category");
        tableModel.addColumn("Value");
        tableModel.addRow(new Object[]{"Date", "Category", "Value"});
        this.setModel(tableModel);  // creates the JTable using the DefaultTableModel.
        
        // FORMATTING
        this.setFont(config.HEADER_TABLE_FONT);
        this.setShowGrid(false);
        this.setFocusable(false);
        this.setEnabled(false);
        this.setRowHeight(30);
        this.setBackground(config.EXPENSES_COLOR);
        this.setForeground(new Color(150, 150, 150));

    }

}
