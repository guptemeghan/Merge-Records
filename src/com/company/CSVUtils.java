package com.company;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import com.google.common.collect.Table;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by meghan on 7/21/2019.
 */
public class CSVUtils {
    public static void exportToCsv(Table<String, String, String> treeBasedTable, String combinedFileName) throws IOException {
        CSVWriter writer = new CSVWriter(new FileWriter(combinedFileName),',','"');
        //fetch the column header names
        Set<String> columns = treeBasedTable.columnKeySet();

        //write the column headers first
        writer.writeNext(columns.toArray(new String[columns.size()]));

        for(String id : treeBasedTable.rowKeySet()){
            List<String> rowEntries = new ArrayList<>();
            for(String col : treeBasedTable.columnKeySet()){
                if(treeBasedTable.get(id, col) == null) {
                    rowEntries.add("");
                } else {
                    rowEntries.add(treeBasedTable.get(id, col));
                }
            }
            //write every row before reading the next row
            writer.writeNext(rowEntries.toArray(new String[rowEntries.size()]));
        }
        writer.close();
    }

    public static Table<String, String, String> parseCsv(String fileName, Table<String, String, String> treeBasedTable) throws Exception {
        //Steps for parsing
        //1. Use CSV reader to read the csv file into memory
        //2. Read the first line(header line) and store the header names in a list
        //3. Iterate over the remaining lines one by one and then add it to the table<ID, Column Name, Value>
        //4. Return the Table

        CSVReader csvReader = new CSVReader(new FileReader("data/" + fileName),',' , '"' , 0);
        String[] nextLine;
        int indexOfId = -1;
        List<String> listOfColHeadersInOrder = new ArrayList<>();

        if ((nextLine = csvReader.readNext()) != null){
            for (int i = 0; i < nextLine.length; i++){
                if (nextLine[i].equals("ID")) {
                    indexOfId = i;
                }
                listOfColHeadersInOrder.add(nextLine[i]);
            }
        }
        if (indexOfId==-1) {
            System.out.println("ID column not present");
            throw new Exception();
        }

        while ((nextLine = csvReader.readNext()) != null) {
            for (int i = 0; i < nextLine.length; i++) {
                if (treeBasedTable.get(nextLine[indexOfId], listOfColHeadersInOrder.get(i)) == null) {
                    treeBasedTable.put(nextLine[indexOfId], listOfColHeadersInOrder.get(i),nextLine[i]);
                }
            }
        }
        return treeBasedTable;
    }
}
