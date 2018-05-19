package pr.data.table;

import pr.data.table.row.Label;
import java.io.File;
import java.util.Scanner;

/**
 * Created by Seba on 10/03/2018.
 */
public class LabelTable extends Table<Label> {

    final int TYPESTRING = 1;
    final int TYPEINTEGER = 2;
    public Table<Label> importCSV(File csvFile, String separator, int index, int type) {
        //String separator = ",";
        Table<Label> labels = new Table<Label>();
        Scanner inputStream;
        try {
            inputStream = new Scanner(csvFile);
            while(inputStream.hasNextLine()){
                String line= inputStream.nextLine();
                String[] values = line.split(separator);
                switch(type) {
                    case TYPEINTEGER: labels.addRow(new Label(Integer.parseInt(values[index])));
                        break;
                    case TYPESTRING: {
                        //todo clean
                        if(values[index].equals("a"))
                            labels.addRow(new Label(1));
                        else
                            labels.addRow(new Label(2));
                        break;
                    }
                }

                //for(int i = 0; i < values.length; i++) {
                //    labels.addRow(new Label(Integer.parseInt(values[i])));
                //}
            }
            inputStream.close();
            this.setTable(labels.getTable(), labels.getSize());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }
}
