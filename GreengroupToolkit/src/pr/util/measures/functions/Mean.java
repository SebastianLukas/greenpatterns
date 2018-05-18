package pr.util.measures.functions;

import java.math.BigDecimal;

/**
 * Created by Seba on 11/03/2018.
 */
public class Mean {
    private double[] partialMeanSum;
    private double count = 0;

    public static double compute(double[] values) {
        double sum = 0.0d;
        for(int i=0; i < values.length; i++) {
            sum += values[i];
        }
        return sum/values.length;
    }

    public static double[] compute(double[][] values) {
        double[] result = new double[values.length];
        for(int i=0; i < values.length; i++) {
            result[i] = compute(values[i]);
        }
        return result;
    }

    public void add(double[] value) {
        if(count == 0) { partialMeanSum = new double[value.length]; }
        for(int i=0; i < value.length; i++) {
            partialMeanSum[i] += value[i];
        }
        count++;
    }

    public double[] getMean() {
        for(int i=0; i < partialMeanSum.length; i++) {
            partialMeanSum[i] = partialMeanSum[i] / count;
        }
        count = 0;
        return partialMeanSum;
    }
}
