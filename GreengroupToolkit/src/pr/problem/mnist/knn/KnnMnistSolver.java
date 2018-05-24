package pr.problem.mnist.knn;

import pr.data.Dataset;
import pr.data.table.LabelTable;
import pr.data.table.LabeledImageTable;

import pr.toolkit.classifier.knn.KNN;


import java.io.File;

public class KnnMnistSolver {

    public static void solve() {

        String baseDataPath = "./././data/";

        LabeledImageTable train = new LabeledImageTable();
        Dataset.setTable("train", train.importCSV(new File(baseDataPath + "train/mnist/train.csv"), true));
        LabeledImageTable test = new LabeledImageTable();
        Dataset.setTable("test", test.importCSV(new File(baseDataPath + "validation/mnist_test.csv"), false));
        LabelTable labels = new LabelTable();
        Dataset.setTable("labels", labels.importCSV(new File(baseDataPath + "train/mnist/train.csv"), ",", 0,  0));

        KNN knn = new KNN("train", "test", "labels" );

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
    }
}