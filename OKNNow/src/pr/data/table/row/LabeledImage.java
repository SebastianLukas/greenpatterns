package pr.data.table.row;

/**
 * Created by Seba on 03/03/2018.
 */
public class LabeledImage extends Row<LabeledImage> {
    private double[] image = null;
    private int label;

    public LabeledImage(int label, double[] image) {
        this.label = label;
        this.image = image;
    }

    public double[] getImage() {
        return this.image;
    }

    public int getLabel() {
        return this.label;
    }

    @Override
    public String toString() {
        return label+"";
    }
}
