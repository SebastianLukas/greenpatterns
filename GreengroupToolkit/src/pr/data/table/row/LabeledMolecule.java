package pr.data.table.row;

public class LabeledMolecule extends Row<LabeledMolecule> {
    private int molecule;
    private int label;

    public LabeledMolecule(int label, int molecule) {
        this.label = label;
        this.molecule = molecule;
    }

    public int getMolecule() {
        return this.molecule;
    }

    public int getLabel() {
        return this.label;
    }

    @Override
    public String toString() {
        return label+"";
    }
}
