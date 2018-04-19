package pr.toolkit.clusterer.validators;

import pr.data.Dataset;
import pr.data.table.Table;
import pr.data.table.row.LabeledImage;
import pr.toolkit.clusterer.ClusterCenter;
import pr.util.measures.CompactnessMeasurement;
import pr.util.measures.functions.EuclideanDistance;

import java.util.*;

/**
 * Created by Seba on 09/03/2018.
 */
public class Davis_Bouldin_Index implements ClusterValidationIndex {
    private HashMap<Integer, HashSet<Integer>> clustering;
    private HashMap<Integer, ClusterCenter> centers;
    private HashMap<Integer, Double> diameters;
    private HashMap<String, CompactnessMeasurement> compactness;

    private double index = -1;

    public Davis_Bouldin_Index(HashMap<Integer, HashSet<Integer>> clustering, HashMap<Integer, ClusterCenter> centers) {
        this.clustering = clustering;
        this.centers = centers;
    }

    @Override
    public String getName() {
        return "Davis Bouldin Index";
    }

    @Override
    public double getValue() {
        return this.index;
    }

    @Override
    public double Compute() {
        //compute diameters
        Table<LabeledImage> images = Dataset.getTable("train");
        Set<Integer> clusterKeys = clustering.keySet();
        this.diameters = new HashMap<Integer, Double>();
        for (Integer ckey: clusterKeys
             ) {
            Iterator<Integer> iter = clustering.get(ckey).iterator();
            //iterate over images in this cluster.
            double distancesToClusterCenter = 0.0d;
            while(iter.hasNext()) {
                Integer key = iter.next();
                LabeledImage li = (LabeledImage) images.getRow(key);
                distancesToClusterCenter += EuclideanDistance.compute(li.getImage(), centers.get(ckey).getCenter());
            }
            double clusterDiameter = distancesToClusterCenter / clustering.get(ckey).size();
            diameters.put(ckey, clusterDiameter);
        }
        //compute compactness
        Set<Integer> centerKeys = centers.keySet();
        Integer[] centerKeysArray = centerKeys.toArray(new Integer[0]);
        this.compactness = new HashMap<String,CompactnessMeasurement>();
        for(int i = 0; i < centerKeysArray.length-1; i++) {
            ClusterCenter ci = centers.get(centerKeysArray[i]);
            for(int j = i+1; j < centerKeysArray.length; j++) {
                ClusterCenter cj = centers.get(centerKeysArray[j]);
                double c = (diameters.get(ci.getId()) + diameters.get(cj.getId())) / EuclideanDistance.compute(ci.getCenter(), cj.getCenter());
                this.compactness.put(ci.getId()+","+cj.getId(), new CompactnessMeasurement(ci.getId(), cj.getId(),c));
            }
        }

        ArrayList<CompactnessMeasurement> ris = new ArrayList<CompactnessMeasurement>();
        Integer[] clusterKeysArray = clustering.keySet().toArray(new Integer[0]);
        for(int i = 0; i < clusterKeysArray.length-1; i++) {
            Integer clusterKeyI = clusterKeysArray[i];
            CompactnessMeasurement currentCompactness = new CompactnessMeasurement(1,1, -1);
            for (int j = i+1; j < centerKeysArray.length; j++) {
                Integer clusterKeyJ = clusterKeysArray[j];
                CompactnessMeasurement kij = this.compactness.get(clusterKeyI+","+clusterKeyJ);
                if(currentCompactness.getValue() < kij.getValue())
                    currentCompactness = kij;
            }
            ris.add(currentCompactness);
        }

        //compute DB Index

        index = 0.0d;
        for(int i = 0; i < ris.size(); i++) {
            index += ris.get(i).getValue();
        }
        index = index / (double)clusterKeysArray.length;
        System.out.println("Davis Bouldin Index " + this.index);
        return index;
    }

}
