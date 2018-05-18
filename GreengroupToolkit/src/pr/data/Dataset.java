package pr.data;

import pr.data.table.LabeledImageTable;
import pr.data.table.row.Label;
import pr.data.table.row.LabeledImage;
import pr.data.table.row.Row;
import pr.data.table.Table;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;

/**
 * Created by Seba on 02/03/2018.
 */
public class Dataset {
    private static HashMap<String, Table> tables = new HashMap<String, Table>();

    public static void load(String path, String name) {
        System.out.println(b);
        tables.put(name, new LabeledImageTable().importCSV(new File(path)));
    }

    /**
     * adds or subsitues table if already present
     * @param key
     * @param table
     */
    public static void setTable(String key, Table table) {
        tables.put(key, table);
    }

    public static Table getTable(String key) {
        return tables.get(key);
    }

    public static void importCSV(String name, File csvFile, String separator) {
        Table<LabeledImage> images = new Table<LabeledImage>();
        Table<Label> labels = new Table<Label>();
        HashSet<Integer> labelSet = new HashSet<Integer>();
        Scanner inputStream;
        try {
            inputStream = new Scanner(csvFile);
            while(inputStream.hasNext()){
                String line= inputStream.next();
                String[] values = line.split(separator);
                double[] array = new double[values.length-1];
                int label = Integer.parseInt(values[0]);
                labelSet.add(label);
                for(int i = 1; i < values.length-1; i++) {
                    array[i] = (double)Integer.parseInt(values[i]);
                }
                images.addRow(new LabeledImage(label, array));
            }
                inputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
        }
        Iterator<Integer> i = labelSet.iterator();
        while(i.hasNext()) {
            Row<Label> row = new Row<Label>(new Label(i.next()));
            labels.addRow(row);
        }
        setTable("labels", labels);
        setTable(name, images);
    }
}
