package pr.problem.nextcellprediction;

import pr.data.Dataset;
import pr.data.table.Table;
import pr.data.table.row.GsmTrack;
import pr.toolkit.classifier.svm.Trainer;

import java.io.File;
import java.util.Iterator;

public class Predictor {

    public Predictor() {
        prepareDataset();

    }
    public static final String NNPTrain = "nnpTrain";
    private void prepareDataset() {
        Dataset.load("GsmTable", "", NNPTrain);
        Table table = Dataset.getTable(NNPTrain);
        Iterator<Integer> keys = table.getTable().keySet().iterator();
        GsmTrack previous = (GsmTrack)table.getRow((Integer)keys.next());
        int previousLocationId = previous.getGsmCell();
        while(keys.hasNext()) {
            Integer i = keys.next();
            int currentLocationId = ((GsmTrack) table.getRow(i)).getGsmCell();
            if(currentLocationId != previousLocationId) {
                previous.setLabel(currentLocationId);
                previous = (GsmTrack) table.getRow(i);
                previousLocationId = currentLocationId;
            }
        }
    }

    public void run(String[] args) {

        String modelFileName = "./././data/npp.svm.model";
        File modelFile = new File(modelFileName);
        File trainDatasetFile = null;
        File testFile = null;

        if(!modelFile.exists()) {

            //trainFile = new File("./././data/full_mnist.svm.scale.train");
            try {
                String libSvmArgs = "-t 2 -c 32 -g 0.03125 -d 2 -b 1";
                Trainer svmTrainer = new Trainer(modelFile);
                svmTrainer.train(trainDatasetFile, libSvmArgs);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
