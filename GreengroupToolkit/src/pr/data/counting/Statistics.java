package pr.data.counting;

import pr.data.Dataset;
import pr.data.table.GsmGeoTable;
import pr.data.table.GsmTable;
import pr.data.table.row.GsmTrack;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.*;
import java.util.stream.Collectors;

public class Statistics {

    public static Map<Integer, Integer> countVisitsPerCell(GsmTable gsmTable) {
        HashMap<Integer, Integer> count = new HashMap<Integer, Integer>();
        Iterator<Long> iter = gsmTable.getTable().keySet().iterator();
        while (iter.hasNext()) {

            GsmTrack gt = (GsmTrack) gsmTable.getRow(iter.next());
            int cell = gt.getGsmCell();
            if(count.containsKey(cell)) {
                count.put(cell, count.get(cell) + 1);
            } else count.put(cell, 1);
        }

        Map<Integer, Integer> sortedMap =
                count.entrySet().stream()
                        .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                                (e1, e2) -> e1, LinkedHashMap::new));

        writeCSV(sortedMap, "topVisitedCells");
        return sortedMap;
    }

    public static Map<Integer, Integer> countVisitsPerPlace(Map<Integer, Integer> cellVisits, Map<Integer, ArrayList<Integer>> places) {
        HashMap<Integer, Integer> count = new HashMap<Integer, Integer>();
        Iterator<Integer> iter = places.keySet().iterator();
        while (iter.hasNext()) {
            Integer placeId = iter.next();
            ArrayList<Integer> place = places.get(placeId);
            int placeVisitCount = 0;
            for (Integer cell: place
                 ) {
                placeVisitCount += cellVisits.get(cell);
            }
            count.put(placeId, placeVisitCount);
        }

        Map<Integer, Integer> sortedMap =
                count.entrySet().stream()
                        .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                                (e1, e2) -> e1, LinkedHashMap::new));

        ArrayList<String> lines = new ArrayList<String>();
        Iterator<Integer> iterPlaces = sortedMap.keySet().iterator();

        String[] labelAlphabet= "ABCDEFGHIJKLMNOPQRSTUVWXYZ".split("");

        int i = 0;
        while(iterPlaces.hasNext()) {
            Integer label = iterPlaces.next();
            Integer value = sortedMap.get(label);
            lines.add(labelAlphabet[i] + ", " + value);
            i++;
        }
        writeCSV(lines, "topVisitedPlaces");

        lines = new ArrayList<String>();
        iterPlaces = places.keySet().iterator();
        i = 0;
        while(iterPlaces.hasNext()) {
            Integer label = iterPlaces.next();
            ArrayList<Integer> values = places.get(label);
            String cells = values.stream().map(Object::toString)
                    .collect(Collectors.joining(", "));
            String line = labelAlphabet[i] + ", " + cells;
            i++;
            lines.add(line);
        }
        writeCSV(lines, "places");

        return sortedMap;
    }

    public static void writeCSV(ArrayList<String> lines, String name) {

        try {
            File file = new File("./././data/"+name+".csv");
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

    public static void writeCSV(Map<Integer, Integer> sortedMap, String name) {
        ArrayList<String> lines = new ArrayList<String>();
        Iterator<Integer> iter = sortedMap.keySet().iterator();
        while(iter.hasNext()) {
            Integer label = iter.next();
            Integer value = sortedMap.get(label);
            lines.add(label + ", " + value);
        }
        try {
            File file = new File("./././data/"+name+".csv");
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
