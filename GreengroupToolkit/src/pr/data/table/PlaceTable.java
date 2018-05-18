package pr.data.table;

import pr.data.counting.Statistics;
import pr.data.table.row.GsmGeo;
import pr.data.table.row.Row;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.*;
import java.util.stream.Collectors;

public class PlaceTable {
    //a Place should be a group of cells, defined by a certain radius

    public Map<Integer, ArrayList<Integer>> groupCells(GsmTable gsmTable, GsmGeoTable gsmGeoTable) {
        Map<Integer, Integer> topCells = Statistics.countVisitsPerCell(gsmTable);
        Map<Integer, Double[]> cellCoords = new HashMap<Integer, Double[]>();
        //iterate thorugh the top cell down an get nearby cells by 500 meter radius.

        Iterator<Integer> iter = topCells.keySet().iterator();
        while (iter.hasNext()) {
            Integer cell = iter.next();
            if(cellCoords.containsKey(cell)) continue;
            Iterator<Row<GsmGeo>> ggIter =  gsmGeoTable.getTable().values().iterator();
            while (ggIter.hasNext()) {
                GsmGeo gg = (GsmGeo) ggIter.next();
                if(cell == gg.getCell())
                    cellCoords.put(cell, new Double[] {(double)gg.getLongitude(),(double)gg.getLatitude()});

            }
        }

        Iterator<Integer> topCellIter = topCells.keySet().iterator();
        HashMap<Integer, ArrayList<Integer>> places = new HashMap<Integer, ArrayList<Integer>>();

        HashSet<Integer> visited = new HashSet<Integer>();

        while(topCellIter.hasNext()) {

            Integer centerCell = topCellIter.next();
            if(visited.contains(centerCell) || topCells.get(centerCell) < 100)
                continue;

            if(cellCoords.containsKey(centerCell)) {
                Double[] centerLongLat = cellCoords.get(centerCell);

                //search through the gsmGeoTable and gather the closes ones
                Iterator<Long> gsmGeoTableIter = gsmGeoTable.getTable().keySet().iterator();
                while(gsmGeoTableIter.hasNext()) {
                    GsmGeo gg = (GsmGeo) gsmGeoTable.getTable().get(gsmGeoTableIter.next());
                    if(visited.contains(gg.getCell()))
                        continue;

                    if(belongsToPlace(centerLongLat[0], centerLongLat[1], gg.getLongitude(), gg.getLatitude(), 1500)) {
                        if(places.containsKey(centerCell)) {
                            places.get(centerCell).add(gg.getCell());
                        } else {
                            ArrayList<Integer> list = new ArrayList<Integer>();
                            list.add(gg.getCell());
                            places.put(centerCell, list);
                        }
                        visited.add(gg.getCell());
                    }
                }
            }
        }

        Statistics.countVisitsPerPlace(topCells, places);


        ArrayList<String> lines = new ArrayList<String>();
        Iterator<Integer> iterPlaces = places.keySet().iterator();

        while(iterPlaces.hasNext()) {
            Integer cellId = iterPlaces.next();
            ArrayList<Integer> place = places.get(cellId);
            for (Integer i: place
                 ) {
                Double[] coords = cellCoords.get(i);
                String line = i + ", " + coords[0] + ", " + coords[1];
                lines.add(line);
            }
        }
        writeCSV(lines, "placeGeoCoordinates");

        return places;
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

    private boolean belongsToPlace(double centerLon, double centerLat, double celLon, double cellLat, int meterFromCenter) {
        double d = (((Math.acos(Math.sin((centerLat*Math.PI/180)) * Math.sin((cellLat*Math.PI/180))+Math.cos((centerLat*Math.PI/180))
                *Math.cos((cellLat*Math.PI/180))*Math.cos(((centerLon-celLon)*Math.PI/180))))*180/Math.PI)*60*1.1515*1609.344);
        if( d <= meterFromCenter)
            return true;
        else
            return false;
    }

}
