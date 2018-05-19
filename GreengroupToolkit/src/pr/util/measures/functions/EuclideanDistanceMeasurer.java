package pr.util.measures.functions;

import pr.data.table.row.LabeledImage;
import pr.data.table.Table;
import pr.util.measures.*;
import pr.util.measures.comparators.DistanceComparator;

import java.util.*;

/**
 * Created by Seba on 09/03/2018.
 */
public class EuclideanDistanceMeasurer extends DistanceMeasurer {

    public ArrayList<DistanceMeasurement> computeEuclideanDistances(int id, int[] instance, Table table, int size) {
        return computeEuclideanDistances(id, copyFromIntArray(instance), table, size);
    }

    public ArrayList<DistanceMeasurement> computeEuclideanDistances(int id, double[] instance, Table table, int size) {
        PriorityQueue<DistanceMeasurement> sortedDistances =  new PriorityQueue<DistanceMeasurement>(size, new DistanceComparator());
        for(int i = 1; i <= table.getSize(); i++) {
            LabeledImage trainingRecord = (LabeledImage)table.getRow(i);
            double[] trainingInstance = trainingRecord.getImage();
            DistanceMeasurement distanceMeasure = new DistanceMeasurement(id, i,
                    EuclideanDistance.compute(trainingInstance, instance));
            if(sortedDistances.size()>=size) {
                sortedDistances.poll();
                sortedDistances.add(distanceMeasure);
            } else {
                sortedDistances.add(distanceMeasure);
            }
        }
        ArrayList<DistanceMeasurement> reverted = new ArrayList<DistanceMeasurement>();
        while(!sortedDistances.isEmpty()) {
            reverted.add(0, sortedDistances.poll());
        }
        return reverted;
    }




    public static double[] copyFromIntArray(int[] source) {
        double[] dest = new double[source.length];
        for(int i=0; i<source.length; i++) {
            dest[i] = source[i];
        }
        return dest;
    }
}
