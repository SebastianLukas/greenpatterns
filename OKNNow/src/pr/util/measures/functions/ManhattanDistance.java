package pr.util.measures.functions;

/**
 * Created by Seba on 04/03/2018.
 */
public class ManhattanDistance {
    public static double compute(double[] a, double[] b) {
        double total = 0;
        double diff;
        for(int j = 0; j < a.length; j++) {
            diff = Math.abs(a[j] - b[j]);
            //if(diff == 0) {
            //    continue;
            //}
            //else {
                total += diff;
            //}
        }
        return total;
    }
}
