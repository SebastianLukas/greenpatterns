package pr.data.table.row;

/**
 * Created by Seba on 04/03/2018.
 */
public class Label extends Row<Label> {
    private int label;
    public int getLabel() {
        return label;
    }
    public Label(Integer label) { this.label = label; }

    @Override
    public String toString() {
        return label+"";
    }
}
