/*
 * This is the CSV Reader for extracting the saved data.
 */

import java.io.*;
import java.nio.file.*;
import java.util.*;

import java.time.*;
import java.time.format.DateTimeFormatter;

import com.opencsv.*;


public class CSVHandler {

    static Config config = new Config();

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

    public static HashMap<Integer, ArrayList<DataEntry>> createMonthLists(ArrayList<DataEntry> dataList) {

        // Creates a HashMap with a list of relevant data entries for every month of each year.

        HashMap<Integer, ArrayList<DataEntry>> dataMonthLists = new HashMap<>();
        ArrayList<DataEntry> extractedList;

        for (DataEntry dataEntry : dataList) {  // loops through each DataEntry.
            LocalDate date = dataEntry.getDate();  // extract the date of the DataEntry.
            int monthListID = makeMonthListID(date);
            extractedList = dataMonthLists.get(monthListID);
            if (extractedList == null) {  // there is no list for this month yet.
                // Creating new list for this month.
                ArrayList<DataEntry> newMonthList = new ArrayList<>();
                newMonthList.add(dataEntry);
                dataMonthLists.put(monthListID, newMonthList);  // adds list to HashMap.
            }
            else {  // List for this month has been extracted from HashMap.
                extractedList = dataMonthLists.get(monthListID);
                extractedList.add(dataEntry);
                dataMonthLists.put(monthListID, extractedList);  // overwriting into HashMap.
            }
        }

        return dataMonthLists;

    }

    public static int makeMonthListID(LocalDate date) {

        // turns a LocalDate object into a MonthListID for identifying which list it belongs to.
        // Eg: June 2023 = 202306, December 2021 = 202112

        int month = date.getMonthValue();
        int year = date.getYear();
        int monthID = Integer.parseInt(Integer.toString(year) + (month < 10 ? "0" : "") + month); 
        return monthID;
        
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
