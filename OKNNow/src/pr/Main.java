package pr;

import pr.data.Dataset;
import pr.data.table.LabeledImageTable;
import pr.data.table.LibsvmConverter;
import pr.data.table.TimeLocationsTable;
import pr.toolkit.classifier.svm.*;

import java.io.File;
import java.util.ArrayList;


public class Main {
    public static void main(String[] args) {
        //TimeLocationsTable table = new TimeLocationsTable();
        //File file = new File("./././data/mc.txt");
        //table.importCSV(file);
        //ArrayList a = table.removeDuplicates();
        //table.writeFile("./././data/clean.txt", a);

        try {
            GridSearch gs = new GridSearch();
            //0 linear, 1 pol, 2 RBF, 3 sigmoid

            //gs.gridSearch(0,5,"./././data/mnist.svm.gridsearch_1k.txt");
            //gs.gridSearch(1,5,"./././data/mnist.svm.gridsearch_1k.txt");
            //gs.gridSearch(2,6,"./././data/mnist.svm.gridsearch_1k.txt");
            //gs.gridSearch(3,5,"./././data/mnist.svm.gridsearch_1k.txt");

            //gs.gridSearch(0,10,"./././data/mnist.svm.gridsearch_3k.txt");
            //gs.gridSearch(1,10,"./././data/mnist.svm.gridsearch_3k.txt");
            //gs.gridSearch(2,10,"./././data/mnist.svm.gridsearch_3k.txt");
            //gs.gridSearch(3,10,"./././data/mnist.svm.gridsearch_3k.txt");

            /*
            double gammas[] = new double[10];
            gammas[0] = 0.03100d + 0.00005d;
            for(int i = 1; i < gammas.length; i++) {
                gammas[i] = gammas[i-1] + 0.00005f;
            }
            gs.customGammaGridSearch(2,5, gammas, "./././data/mnist.svm.gridsearch_1k.txt");
            */

        } catch(Exception e) { e.printStackTrace(); }


        String modelFileName = "./././data/mnist.svm.model";
        File modelFile = new File(modelFileName);
        File trainDatasetFile = null;
        File testFile = null;

        if(!modelFile.exists()) {
            Dataset.load("./././data/train.csv", "train");
            trainDatasetFile = LibsvmConverter.convert((LabeledImageTable) Dataset.getTable("train"), "train");
            //trainFile = new File("./././data/full_mnist.svm.scale.train");
            try {
                //String libSvmArgs = "-t 2 -c 32 -g 0.03125 -d 2 -b 1";
                String libSvmArgs = "-t 1 -c 64 -g 0.25 -d 2";// -b 1";
                Trainer svm = new Trainer(modelFile);
                svm.train(trainDatasetFile, libSvmArgs);
            } catch (Exception e) { e.printStackTrace();}
        }
        Dataset.load("./././data/test.csv", "test");
        testFile = LibsvmConverter.convert((LabeledImageTable) Dataset.getTable("test"), "test");
        try {
            //0.03125 -c 8.0
            //String libSvmArgs = "-t 2 -c 8 -gamma 0.03125";-d 2 -g 0.5 -c 32.0

            Predictor predictor = new Predictor(testFile, modelFile);
            predictor.run();
        } catch (Exception e) { e.printStackTrace();}

        /*
        String[] fileNames = new String[] {"train.csv" };
        Main.setupData(fileNames);
        KMeans km = new KMeans(Dataset.getTable("train"), null);
        km.run(5, 100, KMeans.RANDOM_CENTERS, 5);
        km.run(7, 100, KMeans.RANDOM_CENTERS, 5);
        km.run(9, 100, KMeans.RANDOM_CENTERS, 5);
        km.run(10, 100, KMeans.RANDOM_CENTERS, 5);
        km.run(12, 100, KMeans.RANDOM_CENTERS, 5);
        km.run(15, 100, KMeans.RANDOM_CENTERS, 5);
        */

        //km.showReport();
        /*
        knn knn = new knn(database);
        knn.computeDistances(knn.EUCLIDEAN_DISTANCE, true).computeDistances(knn.MANHATTAN_DISTANCE, true);
        knn.run(1, knn.EUCLIDEAN_DISTANCE);
        knn.run(3, knn.EUCLIDEAN_DISTANCE);
        knn.run(5, knn.EUCLIDEAN_DISTANCE);
        knn.run(10, knn.EUCLIDEAN_DISTANCE);
        knn.run(15, knn.EUCLIDEAN_DISTANCE);

        knn.run(1, knn.MANHATTAN_DISTANCE);
        knn.run(3, knn.MANHATTAN_DISTANCE);
        knn.run(5, knn.MANHATTAN_DISTANCE);
        knn.run(10, knn.MANHATTAN_DISTANCE);
        knn.run(15, knn.MANHATTAN_DISTANCE);
        knn.showReport();
        */
    }

}
