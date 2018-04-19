package pr.data.table;

import pr.data.table.row.LabeledImage;

import java.io.File;
import java.util.HashSet;
import java.util.Scanner;

/**
 * Created by Seba on 10/03/2018.
 */
public class LabeledImageTable extends Table<LabeledImage> {

    public Table<LabeledImage> importCSV(File csvFile) {
        String separator = ",";
        Table<LabeledImage> images = new Table<LabeledImage>();
        Scanner inputStream;
        try {
            inputStream = new Scanner(csvFile);
            while(inputStream.hasNext()){
                String line= inputStream.next();
                String[] values = line.split(separator);
                double[] array = new double[values.length-1];
                int label = Integer.parseInt(values[0]);
                for(int i = 1; i < values.length-1; i++) {
                    array[i] = Integer.parseInt(values[i]);
                }
                images.addRow(new LabeledImage(label, array));
            }
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.setTable(images.getTable(), images.getSize());
        return this;
    }
}
