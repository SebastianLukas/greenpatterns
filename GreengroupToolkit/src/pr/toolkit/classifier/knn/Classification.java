package pr.toolkit.classifier.knn;

/**
 * Created by Seba on 03/03/2018.
 */
public class Classification {
    private int id;
    private int trueLabel;
    private int classifiedLabel;

    public int getId() {
        return id;
    }

    public int getTrueLabel() {
        return trueLabel;
    }

    public int getClassifiedLabel() {
        return classifiedLabel;
    }

    public Classification(int id, int trueLabel, int classifiedLabel) {
        this.id = id;
        this.trueLabel = trueLabel;
        this.classifiedLabel = classifiedLabel;
    }

    public boolean correct() {
        return  trueLabel == classifiedLabel;
    }
}
