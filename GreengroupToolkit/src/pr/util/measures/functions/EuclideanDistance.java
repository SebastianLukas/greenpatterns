package pr.util.measures.functions;

/**
 * Created by Seba on 04/03/2018.
 */
public class EuclideanDistance  {
    public static float compute(double[] a, double[] b) {
        double total = 0;
        double diff;
        for(int j = 0; j < a.length; j++) {
            if(a[j] == b[j]) {
                continue;
            }
            else {
                diff = Math.abs(a[j] - b[j]);
                //if(diff == 0) {
                //    continue;
                //}
                //else {
                total += diff * diff;
                //}
            }
        }
        return (float)Math.sqrt(total);
    }

    //draft
    public static float compute2(int[] a, int[] b) {
        double total = 0, diff;
        for(int j = 0; j < a.length; j++) {
            if(a[j] == b[j]) continue;
            else {
                if(j>0 && (((j+1)%28)-1 < 27)) {
                    //diff = Math.abs(a[j-2] - b[j]) + Math.abs(a[j-1] - b[j]) + Math.abs(a[j+1] - b[j]) + Math.abs(a[j+2]-b[j]);
                    //if(diff < 201) continue;
                    diff = Math.abs(a[j-1] - b[j]) + Math.abs(a[j+1] - b[j]);
                    if(diff < 101) continue;
                }
            }

            diff = Math.abs(a[j] - b[j]);
            total += diff * diff;
        }
        return (float)Math.sqrt(total);
    }
}
