package pr.toolkit.clusterer;

/**
 * Created by Seba on 11/03/2018.
 */
public class ClusterCenter {
    private int id;
    private double[] center;


    public ClusterCenter(int id, double[] center) {
        this.id = id;
        this.center = center;
    }

    public int getId() {
        return id;
    }

    public double[] getCenter() {
        return center;
    }

    public void setCenter(double[] center) {
        this.center = center;
    }


}
