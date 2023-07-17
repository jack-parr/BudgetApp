/*
 * This is the CSV Reader for extracting the saved data.
 */

import java.awt.Color;
import java.awt.Font;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;

import java.time.*;
import java.time.format.DateTimeFormatter;

import com.opencsv.*;


public class CSVHandler {

    static Config config = new Config();
    final static Font MONTH_TABLE_FONT = new Font("Arial Rounded MT Bold", Font.PLAIN, 14);

    CSVHandler() {

    }

    public static ArrayList<DataEntry> readDataFromCSV(String fileName) {

        // this function takes the String fileName and returns a list of DataEntry, using a BufferedReader.

        ArrayList<DataEntry> dataList = new ArrayList<>();
        Path pathToFile = Paths.get(fileName);

        try (BufferedReader br = Files.newBufferedReader(pathToFile)) {
            String line = br.readLine();  // read first line.

            // loop until all lines are read.
            while (line != null) {
                String[] attributes = line.split(",");
                DataEntry dataEntry = createDataEntry(attributes);
                dataList.add(dataEntry);

                // read next line before looping. If end of file is reached, this will be null.
                line = br.readLine();
            }

        }
        catch (IOException ioe) {
            ioe.printStackTrace();
        }

        return dataList;

        }

    public static DataEntry createDataEntry(String[] metadata) {

        // takes a String array of attributes and turns them into the correct datatypes for the DataEntry class.

        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
        LocalDate date = LocalDate.parse(metadata[0], formatter);
        String category = metadata[1];
        float value = Float.parseFloat(metadata[2]);

        return new DataEntry(date, category, value);

    }

    public static HashMap<Integer, JTable> createMonthTables(ArrayList<DataEntry> dataList) {

        HashMap<Integer, JTable> dataMonthTables = new HashMap<>();
        HashMap<Integer, List<Integer>> monthTableSortLists = new HashMap<>();
        JTable extractedTable;

        for (DataEntry dataEntry : dataList) {  // loops through each DataEntry.
            LocalDate date = dataEntry.getDate();  // extract the date of the DataEntry.
            int monthTableID = makeMonthTableID(date);
            extractedTable = dataMonthTables.get(monthTableID);
            if (extractedTable == null) {  // there is no JTable for this month yet.
                // Creating new JTable for this month.
                DefaultTableModel tableModel = new DefaultTableModel();
                tableModel.addColumn("Date");
                tableModel.addColumn("Category");
                tableModel.addColumn("Value");
                tableModel.addRow(new Object[]{dateToTableString(dataEntry.date), dataEntry.category, dataEntry.value});
                JTable newTable = new JTable(tableModel);  // creates the JTable using the DefaultTableModel.
                JTable formattedTable = formatTable(newTable);  // formats the JTable.
                dataMonthTables.put(monthTableID, formattedTable);  // adds JTable to HashMap.

                // Creating new array of sort codes for this table and adding it to the HashMap.
                List<Integer> newSortList = new ArrayList<Integer>();
                newSortList.add(dataEntry.sortCode);
                monthTableSortLists.put(monthTableID, newSortList);
            }
            else {  // JTable for this month has been extracted from HashMap.
                List<Integer> tableSortList = monthTableSortLists.get(monthTableID);  // get sort code list for existing table.
                tableSortList.add(dataEntry.getSortCode());  // add new sort code to list.
                Collections.sort(tableSortList);  // sort list into ascending order.
                int sortCodeIndex = tableSortList.indexOf(dataEntry.sortCode);  // find index of sort code in list.

                DefaultTableModel tableModel = (DefaultTableModel) extractedTable.getModel();  // extracting the DefaultTableModel.
                tableModel.insertRow(sortCodeIndex, new Object[]{dateToTableString(dataEntry.date), dataEntry.category, dataEntry.value});  // adding to the DefaultTableModel.
                JTable updatedTable = new JTable(tableModel);  // create new Table using the updated DefaultTableModel.
                JTable formattedTable = formatTable(updatedTable);
                dataMonthTables.put(monthTableID, formattedTable);  // overwriting into HashMap.
            }
        }

        return dataMonthTables;

    }

    public static JTable formatTable(JTable baseTable) {

        // Applies all the necessary formatting to the JTable. This is called whenever a new JTable is generated for monthly data.

        baseTable.setFont(MONTH_TABLE_FONT);
        baseTable.setShowVerticalLines(false);
        baseTable.setFocusable(false);
        baseTable.setEnabled(false);
        baseTable.setRowHeight(30);
        baseTable.setGridColor(new Color(204, 0, 0));
        baseTable.setBackground(config.EXPENSES_COLOR);
        baseTable.setForeground(config.TABLE_TEXT_COLOR);

        return baseTable;
    }

    public static int makeMonthTableID(LocalDate date) {

        // turns a LocalDate object into a MonthTableID for identifying which JTable it belongs to.
        // Eg: June 2023 = 202306, December 2021 = 202112

        int month = date.getMonthValue();
        int year = date.getYear();
        int monthID = Integer.parseInt(Integer.toString(year) + (month < 10 ? "0" : "") + month); 
        return monthID;
        
    }

    public static Object[][] getTableArray(JTable table) {

        // Returns a double array containing the data from 'table'.

        int numRows = table.getModel().getRowCount();
        Object[][] tableData = new Object[numRows][];  // initialise with length.

        for (int i = 0; i < numRows; i++) {
            Object[] thisRow = getRowArray(table, i);  // get row from table.
            tableData[i] = thisRow;  // put row into array.
        }

        return tableData;

    }

    public static Object[] getRowArray(JTable table, int row) {

        // Returns an array containing the row data from 'table', at index 'row'.

        int numColumns = table.getModel().getColumnCount();
        Object[] rowData = new Object[numColumns];  // initialise with length.

        for (int i = 0; i < numColumns; i++) {
            Object thisCell = table.getModel().getValueAt(row, i);  // get cell from table row.
            rowData[i] = thisCell;  // put cell into array.
        }

        return rowData;
        
    }

    public static String dateToTableString(LocalDate date) {

        // Returns a string of day/month based on the LocalDate 'date'.

        String dateString = date.getDayOfMonth() + "/" + date.getMonthValue();
        return dateString;

    }

    public static void saveDataToCSV(ArrayList<DataEntry> dataList, String filePath) {

        // Saves the data from 'monthTables' into 'data.csv'.

        // First create file object for file placed at location specified by filepath.
        File file = new File(filePath);
    
        try {
            // Create FileWriter object with file as parameter
            FileWriter outputfile = new FileWriter(file);
    
            // Create CSVWriter with ',' as separator
            CSVWriter writer = new CSVWriter(outputfile, ',', CSVWriter.NO_QUOTE_CHARACTER, CSVWriter.DEFAULT_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END);
    
            // Create a List which contains a String array for each DataEntry.
            List<String[]> dataOut = new ArrayList<String[]>();
            for (DataEntry dataEntry : dataList) {
                dataOut.add(dataEntryToStringArray(dataEntry));
            }
            
            // Write List to the file.
            writer.writeAll(dataOut);
    
            // Closing writer connection
            writer.close();
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
        }

    }

    public static String[] dataEntryToStringArray(DataEntry dataEntry) {

        // Returns a String array based on a DataEntry.

        String[] stringArray = new String[3];
        stringArray[0] = dataEntry.getDate().toString();
        stringArray[1] = dataEntry.getCategory();
        stringArray[2] = String.valueOf(dataEntry.getValue());

        return stringArray;
    }
    
}

class DataEntry {

    LocalDate date;
    String category;
    float value;
    int sortCode;

    public DataEntry(LocalDate date, String category, float value) {

        this.date = date;
        this.category = category;
        this.value = value;
        this.sortCode = makeSortCode(date, category);

    }

    public int makeSortCode(LocalDate date, String category) {

        // Makes a Sort Code from the date and category of the DataEntry. 
        // When codes are sorted from lowest to highest, sorts descending by day, then ascending by first letter of 'category'.

        int day = date.getDayOfMonth();  // get numeric day of the month.
        int firstLetter = Character.getNumericValue(category.charAt(0));  // get numerical value of first letter.
        int sortCode = Integer.parseInt(Integer.toString(31 - day) + Integer.toString(firstLetter));  // concat two numbers together.
        return sortCode;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public int getSortCode() {
        return sortCode;
    }

    public void setSortCode(int sortCode) {
        this.sortCode = sortCode;
    }

    @Override
    public String toString() {
        return "DataEntry [date=" + date + ", category=" + category + ", value=" + value + ", sortCode=" + sortCode + "]";
    }

}
