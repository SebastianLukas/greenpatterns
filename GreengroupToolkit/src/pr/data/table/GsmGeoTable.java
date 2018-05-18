package pr.data.table;

import pr.data.table.row.GeoTrack;
import pr.data.table.row.GsmGeo;
import pr.data.table.row.GsmTrack;
import pr.data.table.row.Row;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class GsmGeoTable extends Table<GsmGeo> {

    public GsmGeoTable(GsmTable gsm, GeoTable geo)  {
        Table<GsmGeo> rows = new Table<GsmGeo>();
        Iterator<Long> iter = gsm.getTable().keySet().iterator();
        while(iter.hasNext()) {
            Long id = iter.next();
            if(geo.getTable().containsKey(id)) {
                GsmTrack gsmTrack = (GsmTrack) gsm.getTable().get(id);
                GeoTrack geoTrack = (GeoTrack) geo.getTable().get(id);
                GsmGeo gsmGeo = new GsmGeo(geoTrack.getDatetime(), id,
                        geoTrack.getLongitude(),
                        geoTrack.getLatitude(),
                        gsmTrack.getGsmCell(),
                        geoTrack.getVelocity());
                rows.addRow(gsmGeo, id);
            }
        }
        this.setTable(rows.getTable());
    }

    public Table<GsmGeo> importCSV(File csvFile) {
        Table<GsmGeo> rows = new Table<GsmGeo>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(csvFile));
            for(String line; (line = br.readLine()) != null; ) {
                Row<GsmGeo> r = new GsmGeo(line);
                rows.addRow(r, r.getRow().getTimestamp());
            }
            // line is not visible here.
        } catch(Exception e) {
            e.printStackTrace();
        }
        this.setTable(rows.getTable(), rows.getSize());
        return this;
    }

    public void writeCSV() {
        ArrayList<String> lines = new ArrayList<String>();
        List<Long> keys = new ArrayList<Long>(this.getTable().keySet());
        Collections.sort(keys);
        Iterator<Long> iter = keys.iterator();
        while(iter.hasNext()) {
            GsmGeo gsmGeo = (GsmGeo) this.getRow(iter.next());
            lines.add(gsmGeo.getDateTime() + ", " + gsmGeo.getLongitude() + ", "
                    + gsmGeo.getLatitude() + ", "
                    + gsmGeo.getCell() + ", "
                    + gsmGeo.getTimestamp() + ", "
                    + gsmGeo.getVelocity());
        }
        try {
            File file = new File("./././data/gsmGeo.csv");
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