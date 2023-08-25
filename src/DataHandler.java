/*
 * This is the CSV Reader for extracting the saved data. Note that ID column values do not bear any meaning as the data is read in, as they get reassigned.
 */

import java.io.*;
import java.nio.file.*;
import java.util.*;

import java.time.*;
import java.time.format.DateTimeFormatter;

import com.opencsv.*;


public class DataHandler {

    static Config config = new Config();

    DataHandler() {

    }

    protected static ArrayList<DataEntry> readDataFromCSV(String fileName) {

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

    protected static DataEntry createDataEntry(String[] metadata) {

        // takes a String array of attributes and turns them into the correct datatypes for the DataEntry class.

        String id = metadata[0];
        boolean isExpense = Boolean.parseBoolean(metadata[1]);
        boolean isRecurring = Boolean.parseBoolean(metadata[2]);
        String frequency = metadata[3];
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
        LocalDate startDate = LocalDate.parse(metadata[4], formatter);
        LocalDate endDate = LocalDate.parse(metadata[5], formatter);
        LocalDate nextDueDate = LocalDate.parse(metadata[6], formatter);
        String category = metadata[7];
        float value = Float.parseFloat(metadata[8]);

        return new DataEntry(id, isExpense, isRecurring, frequency, startDate, endDate, nextDueDate, category, value);

    }

    protected static HashMap<Integer, ArrayList<DataEntry>> createMonthLists(ArrayList<DataEntry> dataList) {

        // Creates a HashMap with a list of relevant data entries for every month of each year.

        HashMap<Integer, ArrayList<DataEntry>> dataMonthLists = new HashMap<>();
        ArrayList<DataEntry> extractedList;

        for (DataEntry dataEntry : dataList) {  // loops through each DataEntry.

            if (dataEntry.getId().startsWith("ONE")) {  // skips the recurring entry generators.

                LocalDate date = dataEntry.getStartDate();  // extract the date of the DataEntry.
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
            
        }

        return dataMonthLists;

    }

    private static int makeMonthListID(LocalDate date) {

        // turns a LocalDate object into a MonthListID for identifying which list it belongs to.
        // Eg: June 2023 = 202306, December 2021 = 202112

        int month = date.getMonthValue();
        int year = date.getYear();
        int monthID = Integer.parseInt(Integer.toString(year) + (month < 10 ? "0" : "") + month); 
        return monthID;
        
    }

    protected static void saveDataToCSV(ArrayList<DataEntry> dataList, String filePath) {

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

    private static String[] dataEntryToStringArray(DataEntry dataEntry) {

        // Returns a String array based on a DataEntry. Used when writing the dataList to CSV.

        String[] stringArray = new String[9];
        stringArray[0] = dataEntry.getId();
        stringArray[1] = Boolean.toString(dataEntry.getIsExpense());
        stringArray[2] = Boolean.toString(dataEntry.getIsRecurring());
        stringArray[3] = dataEntry.getFrequency();
        stringArray[4] = dataEntry.getStartDate().toString();
        stringArray[5] = dataEntry.getEndDate().toString();
        stringArray[6] = dataEntry.getNextDueDate().toString();
        stringArray[7] = dataEntry.getCategory();
        stringArray[8] = String.valueOf(dataEntry.getValue());

        return stringArray;

    }

    protected static void assignIds(ArrayList<DataEntry> dataList) {

        // Labels each DataEntry with a unique integer ID.
        // Format: ONE_1 for one-offs, REC_1 for recurring entry generators.

        int oneOffI = 1;
        int recI = 1;
        for (DataEntry dataEntry : dataList) {
            if (dataEntry.getIsRecurring()) {
                dataEntry.setId("REC_" + recI);
                recI += 1;
            }
            else {
                dataEntry.setId("ONE_" + oneOffI);
                oneOffI += 1;
            }
                
        }

    }
    
}

class DataEntry {

    String id;  // unique to each DataEntry.
    boolean isRecurring;  // either true (recurring entry generator) or false (one-off).
    boolean isExpense;  // either true (expense) or false (income).
    String frequency;  // code to say how often it repeats.
    LocalDate startDate;  // first occurance.
    LocalDate endDate;  // if isRecurring == false, then this is the same as startDate.
    LocalDate nextDueDate;  // if isRecurring == true, this is the next date this is due to recur.
    String category;  // category of the DataEntry.
    float value;  // value of the DataEntry.
    int sortCode;  // used for sorting into correct order when displaying.

    public DataEntry(String id, boolean isExpense, boolean isRecurring, String frequency, LocalDate startDate, LocalDate endDate, LocalDate nextDueDate, String category, float value) {

        this.id = id;
        this.isExpense = isExpense;
        this.isRecurring = isRecurring;
        this.frequency = frequency;
        this.startDate = startDate;
        this.endDate = endDate;
        this.nextDueDate = nextDueDate;
        this.category = category;
        this.value = value;
        this.sortCode = makeSortCode(startDate, category);

    }

    private int makeSortCode(LocalDate date, String category) {

        // Makes a Sort Code from the date and category of the DataEntry. 
        // When codes are sorted from lowest to highest, sorts descending by day, then ascending by first letter of 'category'.

        int day = date.getDayOfMonth();  // get numeric day of the month.
        int firstLetter = Character.getNumericValue(category.charAt(0));  // get numerical value of first letter. Note the count starts at 10.
        int sortCode = Integer.parseInt(Integer.toString(32 - day) + Integer.toString(firstLetter));  // concat two numbers together.
        return sortCode;

    }

    protected String getId() {
        return id;
    }

    protected void setId(String id) {
        this.id = id;
    }

    protected boolean getIsExpense() {
        return isExpense;
    }

    protected void setIsExpense(boolean isExpense) {
        this.isExpense = isExpense;
    }

    protected boolean getIsRecurring() {
        return isRecurring;
    }

    protected void setIsRecurring(boolean isRecurring) {
        this.isRecurring = isRecurring;
    }

    protected String getFrequency() {
        return frequency;
    }

    protected void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    protected LocalDate getStartDate() {
        return startDate;
    }

    protected void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    protected LocalDate getEndDate() {
        return endDate;
    }

    protected void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    protected LocalDate getNextDueDate() {
        return nextDueDate;
    }

    protected void setNextDueDate(LocalDate nextDueDate) {
        this.nextDueDate = nextDueDate;
    }

    protected String getCategory() {
        return category;
    }

    protected void setCategory(String category) {
        this.category = category;
    }

    protected float getValue() {
        return value;
    }

    protected void setValue(float value) {
        this.value = value;
    }

    protected int getSortCode() {
        return sortCode;
    }

    protected void setSortCode(int sortCode) {
        this.sortCode = sortCode;
    }

    @Override
    public String toString() {
        return "DataEntry [id=" + id + ", isExpense=" + isExpense + ", isRecurring=" + isRecurring + ", frequency=" + frequency + ", startDate=" + startDate + ", endDate=" + endDate + ", nextDueDate=" + nextDueDate + ", category=" + category + ", value=" + value + ", sortCode=" + sortCode + "]";
    }

}
