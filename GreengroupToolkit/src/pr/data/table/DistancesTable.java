package pr.data.table;

import pr.data.table.row.Distances;
import pr.util.measures.DistanceMeasurement;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

/**
 * Created by Seba on 10/03/2018.
 */
public class DistancesTable extends Table<ArrayList<DistanceMeasurement>> {

    //1;1;-1;-1;0.0,1;2;-1;-1;1.0
    @Override
    public Table<ArrayList<DistanceMeasurement>> importCSV(File csvFile, boolean hasLabel) {
        String listItemSeparator = ",";
        String itemSeparator = ";";
        Table<ArrayList<DistanceMeasurement>> table = new Table<ArrayList<DistanceMeasurement>>();

        try {
            BufferedReader br = new BufferedReader(new FileReader(csvFile));
            try {
                String line = br.readLine();
                ArrayList<DistanceMeasurement> dms = new ArrayList<DistanceMeasurement>();
                while (line != null) {
                    line = br.readLine();
                    if(line == null || line.length()==0) continue;
                    line = line.substring(1, line.length()-1);
                    String[] values = line.split(listItemSeparator);
                    for(int i = 0; i < values.length; i++) {
                        String item = values[i].trim();
                        String[] items = item.split(itemSeparator);
                        DistanceMeasurement edm = new DistanceMeasurement(
                                Integer.parseInt(items[0]), Integer.parseInt(items[1]),
                                Float.parseFloat(items[4]),
                                items[2] == "" ? -1 : Integer.parseInt(items[2]), items[3] == "" ? -1 : Integer.parseInt(items[3]));
                        dms.add(edm);
                    }
                    table.addRow(new Distances(dms));
                }
            } finally {
                br.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.setTable(table.getTable(), table.getSize());
        return this;
    }
}
