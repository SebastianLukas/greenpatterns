package pr.data.table;

import pr.data.table.row.LabeledImage;
import pr.data.table.row.Row;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class TimeLocationsTable extends Table<String> {
    public Table<String> importCSV(File csvFile) {

        Table<String> rows = new Table<String>();
        FileReader inputStream;

        try {
            BufferedReader br = new BufferedReader(new FileReader(csvFile));
                for(String line; (line = br.readLine()) != null; ) {

                    Row<String> r = new Row<String>(line);
                    rows.addRow(r);
                }
                // line is not visible here.

        } catch(Exception e) {}


        this.setTable(rows.getTable(), rows.getSize());
        return this;
    }

    public ArrayList removeDuplicates() {
        ArrayList list = new ArrayList();
        for (Integer i: this.getTable().keySet()
             ) {
            String s = (String)this.getTable().get(i).getRow();
            if(!list.contains(s))
                list.add(s);
        }
        return list;
    }


    public void writeFile(String fileName, ArrayList<String> list) {
        try {

            BufferedWriter bw = new BufferedWriter(new FileWriter(new File(fileName), true));
            for (String s: list
                    ) {
                bw.write(s);
                bw.newLine();
            }
            bw.close();} catch (Exception e) {}
    }
}
