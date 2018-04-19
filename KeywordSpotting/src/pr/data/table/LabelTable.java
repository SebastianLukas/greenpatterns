package pr.data.table;

import pr.data.table.row.Label;
import java.io.File;
import java.util.Scanner;

/**
 * Created by Seba on 10/03/2018.
 */
public class LabelTable extends Table<Label> {
    public Table<Label> importCSV(File csvFile) {
        String separator = ",";
        Table<Label> labels = new Table<Label>();
        Scanner inputStream;
        try {
            inputStream = new Scanner(csvFile);
            while(inputStream.hasNext()){
                String line= inputStream.next();
                String[] values = line.split(separator);
                for(int i = 0; i < values.length; i++) {
                    labels.addRow(new Label(Integer.parseInt(values[i])));
                }
            }
            inputStream.close();
            this.setTable(labels.getTable(), labels.getSize());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }
}
