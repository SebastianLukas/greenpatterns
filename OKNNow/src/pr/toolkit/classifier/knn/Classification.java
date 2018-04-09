package pr.toolkit.classifier.knn;

/**
 * Created by Seba on 03/03/2018.
 */
public class Classification {
    private int id;
    private int realLabel;
    private int classifiedLabel;

    public int getId() {
        return id;
    }

    public int getTrueLabel() {
        return realLabel;
    }

    public int getClassifiedLabel() {
        return classifiedLabel;
    }

    public Classification(int id, int realLabel, int classifiedLabel) {
        this.id = id;
        this.realLabel = realLabel;
        this.classifiedLabel = classifiedLabel;
    }

    public boolean correct() {
        return realLabel == classifiedLabel;
    }
}
