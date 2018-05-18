package pr.util.measures.functions;

/**
 * Created by Seba on 11/03/2018.
 */
public class StandardDeviation {
    public static double compute(double[] values) {
        double sum = 0.0d;
        double pow2Mean = Math.pow(Mean.compute(values),2);
        for(int i = 0; i < values.length; i++) {
            sum += (Math.pow(values[i],2) - pow2Mean);
        }
        return Math.sqrt(sum / values.length);
    }

    public static double[] compute(double[][] values) {
        double[] result = new double[values.length];
        for(int i = 0; i < values.length; i++) {
           result[i] = compute(values[i]);
        }
        return result;
    }
}
