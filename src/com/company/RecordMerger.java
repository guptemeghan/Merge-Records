package com.company;
import com.google.common.collect.Table;
import com.google.common.collect.TreeBasedTable;
import org.apache.commons.io.FilenameUtils;

public class RecordMerger {

    public static final String FILENAME_COMBINED = "combined.csv";

    //ASSUMPTION - All the fields are of type string
    private static Table<String, String, String> treeBasedTable = TreeBasedTable.create();

    /**
     * Entry point of this test.
     *
     * @param args command line arguments: first.html and second.csv.
     * @throws Exception bad things had happened.
     */
    public static void main(final String[] args) throws Exception {

        if (args.length == 0) {
            System.err.println("Usage: java RecordMerger file1 [ file2 [...] ]");
            System.exit(1);
        }

        // your code starts here.

        // first.html contains a table with 4 columns - ID, Name, Address, PhoneNum
        //second.csv has a table with 4 columns - Occupation, Name, Gender, ID
        //Since ID column is common for both the tables, and since we need to use that column
        //to combine the 2 tables, the data needs to be stored as a Map<Map<>>
        //Looking at the provided libs, guava has a interface Table<R,C,V>
        //In this case,
        //      R - ID - "1111"
        //      C - Column Name - "Name"
        //      V - Value corresponding to that ID row and column name - "John Smith"

        buildTableFromFileNames(args);
        CSVUtils.exportToCsv(treeBasedTable, FILENAME_COMBINED);
    }

    private static void buildTableFromFileNames(String[] fileNames) throws Exception {
        for (String fileName : fileNames){
            switch(FilenameUtils.getExtension(fileName)) {
                case "html":
                    treeBasedTable = HtmlUtils.parseHtmlTable(fileName, treeBasedTable);
                    break;
                case "csv":
                    treeBasedTable = CSVUtils.parseCsv(fileName, treeBasedTable);
                    break;
//                case "xml":
//                    treeBasedTable = XmlUtils.parseXml(fileName, treeBasedTable);
                default:
                    break;
            }
        }
    }
}
