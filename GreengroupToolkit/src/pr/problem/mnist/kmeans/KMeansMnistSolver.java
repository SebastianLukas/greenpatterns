package pr.problem.mnist.kmeans;

import pr.data.Dataset;
import pr.data.table.LabelTable;
import pr.data.table.LabeledImageTable;
import pr.toolkit.clusterer.KMeans;

import java.io.File;

public class KMeansMnistSolver {

    public static void solve() {

        String baseDataPath = "./././data/validation/";

        LabeledImageTable train = new LabeledImageTable();
        Dataset.setTable("train", train.importCSV(new File(baseDataPath + "train/mnist/train.csv"), true));
        LabeledImageTable test = new LabeledImageTable();
        Dataset.setTable("test", test.importCSV(new File(baseDataPath + "validation/mnist_test.csv"), false));
        LabelTable labels = new LabelTable();
        Dataset.setTable("labels", labels.importCSV(new File(baseDataPath + "train/mnist/train.csv"), ",", 0,  0));

        KMeans km = new KMeans(Dataset.getTable("train"), null);
        km.run(5, 100, KMeans.RANDOM_CENTERS, 5);
        km.run(7, 100, KMeans.RANDOM_CENTERS, 5);
        km.run(9, 100, KMeans.RANDOM_CENTERS, 5);
        km.run(10, 100, KMeans.RANDOM_CENTERS, 5);
        km.run(12, 100, KMeans.RANDOM_CENTERS, 5);
        km.run(15, 100, KMeans.RANDOM_CENTERS, 5);

    }

}
