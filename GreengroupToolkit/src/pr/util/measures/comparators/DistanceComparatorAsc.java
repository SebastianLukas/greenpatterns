package pr.util.measures.comparators;

import pr.util.measures.DistanceMeasurement;

import java.util.Comparator;

public class DistanceComparatorAsc implements Comparator<DistanceMeasurement> {
    @Override
    public int compare(DistanceMeasurement x, DistanceMeasurement y) {
        if (x.getValue() < y.getValue()) {
            return -1;
        }
        if (x.getValue() > y.getValue()) {
            return 1;
        }
        return 0;
    }
}