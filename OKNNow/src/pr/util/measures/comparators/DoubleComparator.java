package pr.util.measures.comparators;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Created by Seba on 11/03/2018.
 */
public class DoubleComparator implements Comparator<Double>, Serializable  {


    @Override
    public int compare(Double x, Double y) {
        if (x > y) {
            return 1;
        }
        if (x < y) {
            return -1;
        }
        return 0;
    }
}