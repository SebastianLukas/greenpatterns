package pr.data.table;

import pr.data.table.row.GeoTrack;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

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
        List<Long> keys = new ArrayList<Long>(this.getTable().keySet());
        ArrayList<String> lines = new ArrayList<String>();
        Collections.sort(keys);
        Iterator<Long> iter = keys.iterator();
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
        List<Long> keys = new ArrayList<Long>(this.getTable().keySet());
        Collections.sort(keys);
        Iterator<Long> iter = keys.iterator();
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
