package pr.problem.molecules;

import pr.data.Dataset;
import pr.data.table.LabelTable;
import pr.data.table.LabeledMoleculeTable;
import pr.data.table.Table;
import pr.data.table.row.GsmTrack;
import pr.data.table.row.LabeledImage;
import pr.data.table.row.LabeledMolecule;
import pr.toolkit.binding.gmtoolkit.CustomGraphMatching;
import pr.toolkit.binding.gmtoolkit.CustomXMLParser;
import pr.toolkit.classifier.knn.Classification;
import pr.toolkit.classifier.knn.KNN;
import pr.util.measures.DistanceMeasurement;
import pr.util.measures.comparators.DistanceComparator;
import pr.util.measures.comparators.DistanceComparatorAsc;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class MoleculeClassifier {

    public static void solve() {
        //we need to define config parameters in a properties file
        String propertiesPath = "./././data/properties/properties_molecules.prop";
        String baseDataPath = "./././data/";
        /*
        ## Quick Start

        We recommend the following three steps:

        #### STEP 1:
        Unzip the archive `Sources.zip`, integrate the whole framework in an IDE (e.g. Eclipse) and build the project.

        #### STEP 2:
        Define a properties file in order to define the parameters of your graph matching task.
         In the folder `properties` you find three examples of such properties (for more details on these parameters we refer to our paper:

        K. Riesen, S. Emmenegger and H. Bunke.
        A Novel Software Toolkit for Graph Edit Distance Computation.. In W.G. Kropatsch et al.,
        editors, Proc. 9th Int. Workshop on Graph Based Representations in Pattern Recognition, LNCS 7877, 142–151, 2013.*

        #### STEP 3:
        Run the graph matching as a java application.
        The main method is in `GraphMatching.java`, the sole program argument is an URL pointing to the properties file
        (e.g.: `./properties/properties_molecules.prop`)
         */

        //Steps
        //1. parse graphs
        //2. compute approx ged:
        //   cost matrix
        //   hungarian alg to find optimal assignment
        //   derive edit path costs from the result -> distance for classification
        //3. use KNN on computed distances to classify

        try {

            LabeledMoleculeTable train = new LabeledMoleculeTable();
            Dataset.setTable("train", train.importCSV(new File(baseDataPath + "molecules/train.txt")));

            LabeledMoleculeTable test = new LabeledMoleculeTable();
            //Dataset.setTable("test", test.importCSV(new File(baseDataPath + "validation/TestMolecules/valid.txt")));
            Dataset.setTable("test", test.importInstances(baseDataPath + "validation/TestMolecules/gxl/"));

            LabelTable labels = new LabelTable();
            //todo cleanup this later , only classes for reporting
            Dataset.setTable("labels", labels.importCSV(new File(baseDataPath + "molecules/train.txt"), " ", 1,  1));

            //generate valid.txt
            Table testData = Dataset.getTable("test");
            Iterator<Integer> iter = testData.getTable().keySet().iterator();
            File file = null;
            try {
                file = new File("./././data/validation/TestMolecules/valid.txt");
                FileWriter writer = new FileWriter(file);
                BufferedWriter bwriter = new BufferedWriter(writer);
                while (iter.hasNext()) {
                    LabeledMolecule lm = (LabeledMolecule) testData.getRow(iter.next());
                    bwriter.write(lm.getMolecule()+"");
                    bwriter.newLine();
                }
                bwriter.flush();
                bwriter.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            CustomGraphMatching cgm = new CustomGraphMatching(propertiesPath, new CustomXMLParser());
            double[][] d = cgm.run();


            KNN knn = new KNN("train", "test", "labels" );

            ArrayList<List<DistanceMeasurement>> distances = new ArrayList<>();

            //d source graphs (train)
            //d[0] target graphs (validation - test instances to classify)
            Table trainingData = Dataset.getTable("train");


            for(int col = 1; col <= d[0].length; col++) {
                List<DistanceMeasurement> testToTrainDistances = new ArrayList<>();
                LabeledMolecule testGraph = (LabeledMolecule)testData.getRow(col);
                int testLabel = testGraph.getLabel();
                for(int row = 1; row <= d.length; row++) {
                    LabeledMolecule trainGraph = (LabeledMolecule)trainingData.getRow(row);
                    int trainLabel = trainGraph.getLabel();
                    testToTrainDistances.add(new DistanceMeasurement(
                            col,
                            row,
                            (float)d[row-1][col-1],
                            testLabel,
                            trainLabel));
                }
                Collections.sort(testToTrainDistances, new DistanceComparatorAsc());
                testToTrainDistances = testToTrainDistances.subList(0, 15);
                distances.add(testToTrainDistances);
            }

            //

            knn.run(5, distances, -1, false);
            knn.showReport();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //for us todo:
        //1. get the distance matrix between the graphs out of GraphMatching
        //we compute them once! we should write a file to save the distances
        //2. as last step: load the prevous results and
        //use the pairwise distances between molecules (hope they are pairwise) as distance metric for KNN classifying
    }
}