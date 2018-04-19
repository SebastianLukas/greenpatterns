package pr.util.measures.comparators;

import pr.util.measures.DistanceMeasurement;

import java.util.Comparator;

/**
 * Created by Seba on 11/03/2018.
 */
public class DistanceComparator implements Comparator<DistanceMeasurement> {
    @Override
    public int compare(DistanceMeasurement x, DistanceMeasurement y) {
        if (x.getValue() > y.getValue()) {
            return -1;
        }
        if (x.getValue() < y.getValue()) {
            return 1;
        }
        return 0;
    }
}


