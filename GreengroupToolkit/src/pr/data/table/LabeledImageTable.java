package pr.data.table;

import pr.data.table.row.LabeledImage;

import java.io.File;
import java.util.HashSet;
import java.util.Scanner;

/**
 * Created by Seba on 10/03/2018.
 */
public class LabeledImageTable extends Table<LabeledImage> {

    public Table<LabeledImage> importCSV(File csvFile, boolean hasLabel) {
        String separator = ",";
        Table<LabeledImage> images = new Table<LabeledImage>();
        Scanner inputStream;
        try {
            inputStream = new Scanner(csvFile);
            while(inputStream.hasNext()){
                String line= inputStream.next();
                String[] values = line.split(separator);
                int label = -1;
                int startIndex = 0;
                if(hasLabel) {
                    startIndex = 1;
                    label = Integer.parseInt(values[0]);
                }
                double[] array = new double[values.length-startIndex];
                for(int i = startIndex; i < values.length - startIndex; i++) {
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
