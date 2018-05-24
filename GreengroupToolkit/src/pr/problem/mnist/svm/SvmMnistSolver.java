package pr.problem.mnist.svm;

import pr.data.Dataset;
import pr.data.table.LabelTable;
import pr.data.table.LabeledImageTable;
import pr.data.table.LibsvmConverter;
import pr.toolkit.classifier.svm.GridSearch;
import pr.toolkit.classifier.svm.Predictor;
import pr.toolkit.classifier.svm.Trainer;

import java.io.File;

public class SvmMnistSolver {

        public static void solve() {
            String baseDataPath = "./././data/";
            LabeledImageTable train = new LabeledImageTable();
            Dataset.setTable("train", train.importCSV(new File(baseDataPath + "train/mnist/train.csv"), true));
            LabeledImageTable test = new LabeledImageTable();
            Dataset.setTable("test", test.importCSV(new File(baseDataPath + "validation/mnist_test.csv"), false));

            /*
            try {
                GridSearch gs = new GridSearch();
                //0 linear, 1 pol, 2 RBF, 3 sigmoid
                gs.gridSearch(0,5,"./././data/mnist.svm.gridsearch_1k.txt");
                gs.gridSearch(1,5,"./././data/mnist.svm.gridsearch_1k.txt");
                gs.gridSearch(2,6,"./././data/mnist.svm.gridsearch_1k.txt");
                gs.gridSearch(3,5,"./././data/mnist.svm.gridsearch_1k.txt");
                gs.gridSearch(0,10,"./././data/mnist.svm.gridsearch_3k.txt");
                gs.gridSearch(1,10,"./././data/mnist.svm.gridsearch_3k.txt");
                gs.gridSearch(2,10,"./././data/mnist.svm.gridsearch_3k.txt");
                gs.gridSearch(3,10,"./././data/mnist.svm.gridsearch_3k.txt");
                double gammas[] = new double[10];
                gammas[0] = 0.03100d + 0.00005d;
                for(int i = 1; i < gammas.length; i++) {
                    gammas[i] = gammas[i-1] + 0.00005f;
                }
                gs.customGammaGridSearch(2,5, gammas, "./././data/mnist.svm.gridsearch_1k.txt");
            } catch(Exception e) { e.printStackTrace(); }
            */

            String modelFileName = baseDataPath + "svm/mnist.svm.model";
            File modelFile = new File(modelFileName);
            File trainDatasetFile = null;
            File testFile = null;

            if(!modelFile.exists()) {
                trainDatasetFile = LibsvmConverter.convert((LabeledImageTable) Dataset.getTable("train"), "train");
                //trainFile = new File("./././data/full_mnist.svm.scale.train");
                try {
                    String libSvmArgs = "-t 2 -c 8 -g 0.03125 -b 1";
                    //String libSvmArgs = "-t 1 -c 64 -g 0.25 -d 2";// -b 1";
                    Trainer svm = new Trainer(modelFile);
                    svm.train(trainDatasetFile, libSvmArgs);
                } catch (Exception e) { e.printStackTrace();}
            }

            testFile = LibsvmConverter.convert((LabeledImageTable) Dataset.getTable("test"), "test");
            try {
                Predictor predictor = new Predictor(testFile, modelFile);
                predictor.run();
            } catch (Exception e) { e.printStackTrace();}
        }
}
