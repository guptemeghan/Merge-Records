package com.company;

import com.google.common.collect.Table;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by meghan on 7/21/2019.
 */
public class HtmlUtils {
    public static Table<String, String, String> parseHtmlTable(String fileName, Table<String, String, String> treeBasedTable) throws Exception {
        //Steps for parsing
        //1. Use Jsoup to read the html file into memory
        //2. Get the header row(th) to retrieve the column names and add it to the list in order
        //3. Get the remaining rows(tr)
        //4. Iterate over the every row and column and add it to the table <ID, Column Name, Value>
        //5. Return the table

        Document doc = Jsoup.parse(new File("data/" + fileName), null);
        Elements columns = doc.select("table tr th");

        int indexOfId = -1;
        List<String> listOfColHeadersInOrder = new ArrayList<>();

        for (int i = 0; i < columns.size(); i++){
            if(columns.get(i).text().equals("ID")) {
                indexOfId = i;
            }
            listOfColHeadersInOrder.add(columns.get(i).text());
        }

        if (indexOfId==-1) {
            System.out.println("ID column not present");
            throw new Exception();
        }

        //get all the rows
        Elements rows = doc.select("tr");

        for (int i = 1; i < rows.size(); i++) { //first row is the col names so skip it.
            Element row = rows.get(i);
            Elements column = row.select("td");
            String id = column.get(indexOfId).text();
            for (int j = 0; j<column.size(); j++) {
                if(treeBasedTable.get(id, listOfColHeadersInOrder.get(j)) == null) {
                    treeBasedTable.put(id, listOfColHeadersInOrder.get(j), column.get(j).text());
                }
            }
        }
        return treeBasedTable;
    }
}
