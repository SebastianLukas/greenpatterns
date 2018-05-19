package pr.data.table;

import pr.data.table.row.GsmTrack;
import java.io.*;
import java.util.*;

public class GsmTable extends Table<GsmTrack> {
    public Table<GsmTrack> importCSV(File csvFile, int timeGranularity) {
        Table<GsmTrack> rows = new Table<GsmTrack>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(csvFile));
                for(String line; (line = br.readLine()) != null; ) {
                    GsmTrack r = new GsmTrack(line, timeGranularity);
                    rows.addRow(r, r.getTimestamp());
                    //rows.addRow(r);
                }
        } catch(Exception e) {
            e.printStackTrace();
        }
        this.setTable(rows.getTable());
        return this;
    }

    public void prepareFeatureSet() {
        //redefine cell numbers -> adjacent are good. hoppe are bad distances
        //week of day
        //time of stay in same cell in 1 min granularity
        //hour of day
        //normalize stuff between 0 and 1
        Iterator<Integer> keys = this.getTable().keySet().iterator();
        GsmTrack previousGsmTrack = (GsmTrack)this.getRow(keys.next());
        long longBeginningOfStay = previousGsmTrack.getTimestamp();
        float timeOfStay = 0f;
        ArrayList<Float> featureVector = null;
        ArrayList<ArrayList<Float>> features = new ArrayList<ArrayList<Float>>();
        long longestStay = 0;
        HashSet<Integer> locationSet = new HashSet<Integer>();
        //HashMap<Integer, HashSet<Integer>> neigbours = new HashMap<Integer, HashSet<Integer>>();
        while(keys.hasNext()) {
            Integer i = keys.next();
            featureVector = new ArrayList<Float>();
            GsmTrack currentGsmTrack = (GsmTrack)this.getRow(i);
            long timestamp = currentGsmTrack.getTimestamp();
            Date date = new Date(timestamp);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            float dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
            float hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
            timeOfStay += (timestamp - longBeginningOfStay);
            if(longestStay < timeOfStay) {
                longestStay = (long)timeOfStay;
            }
            if(!locationSet.contains(currentGsmTrack.getGsmCell())) {
                locationSet.add(currentGsmTrack.getGsmCell());
            }
            /*
            if(neigbours.containsKey(currentGsmTrack.getGsmCell())) {
                HashSet<Integer> neighbourhood = neigbours.get(currentGsmTrack.getGsmCell());
            }*/
            featureVector.add((float) currentGsmTrack.getLabel());
            featureVector.add(dayOfWeek);
            featureVector.add(hourOfDay);
            featureVector.add(timeOfStay);
            featureVector.add((float) currentGsmTrack.getGsmCell());
            features.add(featureVector);
            if(previousGsmTrack.getGsmCell() != currentGsmTrack.getGsmCell()) {
                timeOfStay = 0;
                longBeginningOfStay = timestamp;
                previousGsmTrack = currentGsmTrack;
            }
        }

        //encode label or normalize it between 0 - 1
        //1. numerate cells from 1 to x
        //encode them in n binary features.
        HashMap<Integer, Integer> dict = new HashMap<Integer, Integer>();
        Iterator<Integer> iter = locationSet.iterator();
        int i = 0;
        while(iter.hasNext()) {
            i++;
            Integer cell = iter.next();
            dict.put(cell, i);
        }

        //normalize values as floats from 0 to 1
        for (ArrayList<Float> vector: features
             ) {
            Float dow = vector.get(1);
            dow = dow / 7;
            Float hod = vector.get(2);
            hod = hod / 24;
            Float ls = vector.get(3);
            ls = ls/longestStay; //@todo have to compute the longes stay on the entire dataset
            String[] binary = Integer.toString(vector.get(4).intValue(),2).split("");
            Float[] binaryLoactionCode = new Float[binary.length];
            i = 0;
            for (String s: binary
                 ) {
                binaryLoactionCode[i] = Float.parseFloat(s);
                i++;
            }
            vector.remove(4);
            vector.addAll(Arrays.asList(binaryLoactionCode));
        }

        //LibsvmConverter.convert(this, "npp_train");
    }

    public ArrayList removeDuplicates() {
        ArrayList list = new ArrayList();
        for (Integer i: this.getTable().keySet()
             ) {
            GsmTrack s = (GsmTrack)this.getTable().get(i).getRow();
            if(!list.contains(s))
                list.add(s);
        }
        return list;
    }

    public void writeCSV() {
        ArrayList<String> lines = new ArrayList<String>();
        List<Integer> keys = new ArrayList<Integer>(this.getTable().keySet());
        Collections.sort(keys);
        Iterator<Integer> iter = keys.iterator();
        while(iter.hasNext()) {
            GsmTrack gsmTrack = (GsmTrack) this.getRow(iter.next());
            lines.add(gsmTrack.getDateTime() + ", " + gsmTrack.getGsmCell() + ", "
                    + gsmTrack.getTimestamp());
        }
        try {
            File file = new File("./././data/gsmTrack.csv");
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
