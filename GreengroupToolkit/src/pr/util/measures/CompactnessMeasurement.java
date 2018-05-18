package pr.util.measures;

/**
 * Created by Seba on 18/03/2018.
 */
public class CompactnessMeasurement {
    private int i;
    private int j;
    private double value;

    public CompactnessMeasurement(int i, int j, double value) {
        this.i = i;
        this.j = j;
        this.value = value;
    }

    public int getI() {
        return i;
    }

    public int getJ() {
        return j;
    }

    public double getValue() {
        return value;
    }


}
