package pr.toolkit.classifier.svm;

import java.io.File;

public class Predictor {

    private File modelFile = null;
    private File testInstancesFile = null;

    public Predictor(File testInstancesFile, File modelFile) {
        this.modelFile = modelFile;
        this.testInstancesFile = testInstancesFile;
    }

    public void run() {
        try {
            svm_predict.main(new String[] {testInstancesFile.getPath(), modelFile.getPath(), "./././data/result.txt"});
        } catch (Exception e) {}
    }
}
