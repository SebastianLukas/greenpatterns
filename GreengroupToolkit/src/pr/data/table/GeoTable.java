package pr.data.table;

import pr.data.table.row.GeoTrack;
import java.io.*;
import java.util.*;

public class GeoTable extends Table<GeoTrack> {

    public void addDateTime(File csvFile, int timeGranularity) {
        Table<GeoTrack> rows = new Table<GeoTrack>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(csvFile));
            for(String line; (line = br.readLine()) != null; ) {
                GeoTrack r = new GeoTrack(line, timeGranularity);
                //rows.addRow(r, r.getTimestamp());
                rows.addRow(r);
            }
            // line is not visible here.
        } catch(Exception e) {
            e.printStackTrace();
        }
        List<Integer> keys = new ArrayList<Integer>(this.getTable().keySet());
        ArrayList<String> lines = new ArrayList<String>();
        Collections.sort(keys);
        Iterator<Integer> iter = keys.iterator();
        while(iter.hasNext()) {
            GeoTrack geoTrack = (GeoTrack) this.getRow(iter.next());
            lines.add(geoTrack.getDatetime() +", " + geoTrack.getLongitude() + ", "
                    + geoTrack.getLatitude() + ", "
                    + geoTrack.getTimestamp() + ", "
                    + geoTrack.getVelocity());
        }
        try {
            File file = new File("./././data/geoTrackWithDateTime.csv");
            FileWriter writer = new FileWriter(file);
            BufferedWriter bwriter = new BufferedWriter(writer);
            for (String line: lines
                    ) {
                bwriter.write(line);
                bwriter.newLine();
            }
            bwriter.flush();
            bwriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Table<GeoTrack> importCSV(File csvFile, int timeGranularity) {
        Table<GeoTrack> rows = new Table<GeoTrack>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(csvFile));
            for(String line; (line = br.readLine()) != null; ) {
                GeoTrack r = new GeoTrack(line, timeGranularity);
                rows.addRow(r, r.getTimestamp());
                //rows.addRow(r);
            }
            // line is not visible here.
        } catch(Exception e) {
            e.printStackTrace();
        }
        this.setTable(rows.getTable());
        return this;
    }

    public void writeCSV() {
        ArrayList<String> lines = new ArrayList<String>();
        List<Integer> keys = new ArrayList<Integer>(this.getTable().keySet());
        Collections.sort(keys);
        Iterator<Integer> iter = keys.iterator();
        while(iter.hasNext()) {
            GeoTrack geoTrack = (GeoTrack) this.getRow(iter.next());
            lines.add(geoTrack.getDatetime() +", " + geoTrack.getLongitude() + ", "
                    + geoTrack.getLatitude() + ", "
                    + geoTrack.getTimestamp() + ", "
                    + geoTrack.getVelocity());
        }
        try {
            File file = new File("./././data/geoTrack.csv");
            FileWriter writer = new FileWriter(file);
            BufferedWriter bwriter = new BufferedWriter(writer);
            for (String line: lines
                    ) {
                bwriter.write(line);
                bwriter.newLine();
            }
            bwriter.flush();
            bwriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
