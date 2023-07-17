import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class HeaderTable extends JTable {
    
    // This is the header row which appears before each monthly JTable.

    HeaderTable() {

        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.addColumn("Date");
        tableModel.addColumn("Category");
        tableModel.addColumn("Value");
        tableModel.addRow(new Object[]{"Date", "Category", "Value"});
        this.setModel(tableModel);  // creates the JTable using the DefaultTableModel.
        // FORMAT HEADER TABLE

    }

}
