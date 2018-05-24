package pr;

import pr.problem.mnist.kmeans.KMeansMnistSolver;
import pr.problem.mnist.knn.KnnMnistSolver;
import pr.problem.mnist.svm.SvmMnistSolver;
import pr.problem.molecules.MoleculeClassifier;

import pr.toolkit.dtw.WordCutter;

public class Main {
    public static void main(String[] args) {

        /*
        int timeGranularity = 2;
        GsmTable gsmTable = new GsmTable();
        File file = new File("./././data/gsm-trace-5961-2.txt");
        gsmTable.importCSV(file, timeGranularity);
        gsmTable.writeCSV();

        GeoTable geoTable = new GeoTable();
        file = new File("./././data/usr_5961_gps_records.txt");
        geoTable.importCSV(file, timeGranularity);
        //geoTable.addDateTime(file, timeGranularity);
        geoTable.writeCSV();

        GsmGeoTable ggt = new GsmGeoTable(gsmTable, geoTable);
        Dataset.setTable("gsmGeoTable", ggt);
        ggt.writeCSV();

        PlaceTable pt = new PlaceTable();
        Map<Integer, ArrayList<Integer>> places = pt.groupCells(gsmTable, ggt);
        */

        //TimeLocationsTable table = new TimeLocationsTable();
        //File file = new File("./././data/mc.txt");
        //table.importCSV(file);
        //ArrayList a = table.removeDuplicates();
        //table.writeFile("./././data/clean.txt", a);


        //WordCutter w = new WordCutter();
		//w.cutOutWords(
		//	"../../../data/locations/",
		//	"../../../data/images/",
		//	"../../../data/cropped_images/");

        //@todo recator to use a properties file to configure the model, input, parameters and whatsoever one can confgure.
        //KnnMnistSolver.solve();
        //KMeansMnistSolver.solve();
        //SvmMnistSolver.solve();
        MoleculeClassifier.solve();

    }

}
