package pr.toolkit.classifier.knn;

import pr.data.Dataset;
import pr.data.table.row.Label;
import pr.data.table.row.LabeledImage;
import pr.data.table.Table;
import pr.report.KNNReport;
import pr.util.measures.*;
import pr.util.measures.functions.EuclideanDistance;
import pr.util.measures.functions.ManhattanDistance;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.*;

/**
 * Created by Seba on 28/02/2018.
 */
public class KNN {

    private Table trainingData;
    private Table testData;
    private Table labels;

    private int loggingSteps = 500; //processed test instances

    private ArrayList<List<DistanceMeasurement>> euclideanDistances;
    private ArrayList<List<DistanceMeasurement>> manhattanDistances;

    private ArrayList<Classification> classification;

    private KNNReport report;

    public final static int EUCLIDEAN_DISTANCE = 1;
    public final static int MANHATTAN_DISTANCE = 2;

    public KNN(String trainingDatasetName, String testingDatasetName, String labelDatasetName) {
        this.trainingData = Dataset.getTable(trainingDatasetName);
        this.testData = Dataset.getTable(testingDatasetName);
        this.labels = Dataset.getTable(labelDatasetName);
    }


    public void run(int k, int distanceMetric) {
        switch (distanceMetric) {
            case EUCLIDEAN_DISTANCE :
                System.out.println("Run "+k+"NN on euclidean distances");
                if(this.euclideanDistances == null || this.euclideanDistances.isEmpty()) {
                    this.computeDistances(EUCLIDEAN_DISTANCE, false);
                }
                run(k, this.euclideanDistances, EUCLIDEAN_DISTANCE, true);
                break;
            case MANHATTAN_DISTANCE :
                System.out.println("Run "+k+"NN on manhattan distances");
                if(this.manhattanDistances == null || this.manhattanDistances.isEmpty()) {
                    this.computeDistances(MANHATTAN_DISTANCE, false);
                }
                run(k, this.manhattanDistances, MANHATTAN_DISTANCE,true );
                break;
        }
    }


    public void run(int k, ArrayList<List<DistanceMeasurement>> distances, int distanceMetric, boolean labeledTestData) {
        this.classification = new ArrayList<Classification>();
        Iterator<List<DistanceMeasurement>> iter = distances.iterator();
        while(iter.hasNext()) {
            List<DistanceMeasurement> neighbours = iter.next();
            int majorityLabel = KNNClassify(k, neighbours);
            int id = neighbours.get(0).getTestId();
            int trueLabel = neighbours.get(0).getTestLabel();
            this.classification.add(new Classification(id, trueLabel, majorityLabel));
        }

        if(labeledTestData) {
            if(report == null) report = new KNNReport();
            report.addReport(k, evaluateClassificationAccuracy(this.classification), distanceMetric);
        } else {
            File file = null;
            try {
                file = new File("./././data/molecules/formatted_results.txt");
                FileWriter writer = new FileWriter(file);
                BufferedWriter bwriter = new BufferedWriter(writer);
                for (Classification c : this.classification
                        ) {
                    bwriter.write(c.getId() + ", " + c.getClassifiedLabel());
                    bwriter.newLine();
                }
                bwriter.flush();
                bwriter.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private Map<Integer, Result> evaluateClassificationAccuracy(ArrayList<Classification> classification) {
        Map<Integer, Result> evaluation = new HashMap<Integer, Result>();
        for(int i = 1; i <= labels.getSize(); i++) {
            Integer label = ((Label)labels.getRow(i)).getLabel();
            Result cs = new Result(label);
            if(!evaluation.containsKey(label))
                evaluation.put(label, cs);
        }

        for(int i = 0; i < classification.size(); i++) {
            Classification c = classification.get(i);
            if(evaluation.containsKey(c.getTrueLabel())) {
                boolean correct = false;
                if(c.correct()) {
                    correct = true;
                }
                Result cs = evaluation.get(c.getTrueLabel());
                cs.countResult(correct);
            }
        }
        return evaluation;
    }

    private int KNNClassify(int k, List<DistanceMeasurement> neighbours) {
        int majorityLabel = -1;
        Map<Integer, Integer> labelCount = new HashMap<Integer, Integer>();
        for(int i = 0; i < k; i++) {
            DistanceMeasurement d = neighbours.get(i);
            if(labelCount.containsKey(d.getTrainingLabel())) {
                int tot = labelCount.get(d.getTrainingLabel())+ 1;
                /*
                //if(tot >= i/2) {
                if(tot >= k/2) {
                    majorityLabel = d.getTrainingLabel();
                    break;
                }*/
                //else {
                    labelCount.put(d.getTrainingLabel(), tot);
                //}
            } else {
                majorityLabel = d.getTrainingLabel();
                labelCount.put(d.getTrainingLabel(), 1);
            }
        }
        return majorityLabel;
    }

    public void showReport() {
        if(report != null)
            report.print();
    }

    public KNN computeDistances(int distanceMetric, boolean save) {
        switch (distanceMetric) {
            case EUCLIDEAN_DISTANCE : {
                this.euclideanDistances = new ArrayList<List<DistanceMeasurement>>();
                for (int i = 1; i <= testData.getSize(); i++) {
                    LabeledImage toClassify = (LabeledImage) testData.getRow(i);
                    int trueLabel = toClassify.getLabel();
                    this.euclideanDistances.add(computeEuclideanDistances(toClassify.getImage(), trueLabel, i));
                    if(i%loggingSteps == 0) {
                        System.out.println("Computed distances for #"+i+" instances");
                    }
                }
                if(save) {
                    storeDistances(EUCLIDEAN_DISTANCE, euclideanDistances);
                }
                break;
            }
            case MANHATTAN_DISTANCE : {
                this.manhattanDistances = new ArrayList<List<DistanceMeasurement>>();
                for (int i = 1; i <= testData.getSize(); i++) {
                    LabeledImage toClassify = (LabeledImage) testData.getRow(i);
                    int trueLabel = toClassify.getLabel();
                    this.manhattanDistances.add(computeManhattanDistances(toClassify.getImage(), trueLabel, i));
                    if(i%loggingSteps == 0) {
                        System.out.println("Computed distances for #"+i+" instances");
                    }
                }
                if(save) {
                    storeDistances(MANHATTAN_DISTANCE, manhattanDistances);
                }
                break;
            }
        }
        return this;
    }

    private void storeDistances(int metric, ArrayList<List<DistanceMeasurement>> distances) {
        File file = null;
        switch (metric) {
            case EUCLIDEAN_DISTANCE :
                file = new File("./././data/euclidean.csv");
                break;
            case MANHATTAN_DISTANCE :
                file = new File("./././data/manhattan.csv");
                break;
            default:
                file = new File("./././data/unkownmetric.csv");
        }
        try {
            FileWriter writer = new FileWriter(file);
            for(int i = 0; i < distances.size(); i++) {
                List<DistanceMeasurement> singleDistanceSet = distances.get(i);
                for(int j = 0; j < singleDistanceSet.size(); j++ ) {
                    writer.write(singleDistanceSet.get(j).toString()+"\n");
                }
            }
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /*
    public void init() {
        this.distances = new ArrayList<ArrayList<DistanceMeasurement>>();
        for (int i = 1; i <= testData.getSize(); i++) {
            LabeledImage toClassify = (LabeledImage) testData.getRow(i);
            int trueLabel = toClassify.getClassLabel();
            //this.distances.add(computeEuclideanDistances(toClassify.getValue(), trueLabel, i));
            this.distances.add(computeManhattanDistances(toClassify.getValue(), trueLabel, i));
            if(i%loggingSteps == 0) {
                System.out.println("Computed distances for #"+i+" instances");
            }
        }
    }
    */

    private ArrayList<DistanceMeasurement> computeManhattanDistances(double[] toClassify, int testTrueLabel, int id) {
        Comparator<DistanceMeasurement> comparator = new DistanceComparator();
        Queue<DistanceMeasurement> sortedDistances =  new PriorityQueue<DistanceMeasurement>(15, comparator);
        for(int i = 1; i <= trainingData.getSize(); i++) {
            LabeledImage trainingRecord = (LabeledImage)trainingData.getRow(i);
            double[] trainingInstance = trainingRecord.getImage();
            int trainingLabel = trainingRecord.getLabel();
            DistanceMeasurement distance = new DistanceMeasurement(id, i,
                    ManhattanDistance.compute(trainingInstance, toClassify) /*Math.sqrt(total)*/,
                    trainingLabel,
                    testTrueLabel);
            if(sortedDistances.size()>=15) {
                sortedDistances.poll();
                sortedDistances.add(distance);
            } else {
                sortedDistances.add(distance);
            }
        }
        ArrayList<DistanceMeasurement> reverted = new ArrayList<DistanceMeasurement>();
        while(!sortedDistances.isEmpty()) {
            reverted.add(0,sortedDistances.poll());
        }
        return reverted;
    }

    private ArrayList<DistanceMeasurement> computeEuclideanDistances(double[] toClassify, int testTrueLabel, int id) {
        //key is testing instance and float is
        Comparator<DistanceMeasurement> comparator = new DistanceComparator();
        Queue<DistanceMeasurement> sortedDistances =  new PriorityQueue<DistanceMeasurement>(15, comparator);
        for(int i = 1; i <= trainingData.getSize(); i++) {
            LabeledImage trainingRecord = (LabeledImage)trainingData.getRow(i);
            double[] trainingInstance = trainingRecord.getImage();
            //float total = 0, diff;
            int trainingLabel = trainingRecord.getLabel();
            //for(int j = 0; j < trainingInstance.length; j++) {
            //    diff = Math.abs(trainingInstance[j] - toClassify[j]);
            //    total += diff * diff;
            //}
            DistanceMeasurement distance = new DistanceMeasurement(id, i,
                    EuclideanDistance.compute(trainingInstance, toClassify), //compute2,
                    trainingLabel, 
                    testTrueLabel);
            if(sortedDistances.size()>=15) {
                sortedDistances.poll();
                sortedDistances.add(distance);
            } else {
                sortedDistances.add(distance);
            }
        }
        ArrayList<DistanceMeasurement> reverted = new ArrayList<DistanceMeasurement>();
        while(!sortedDistances.isEmpty()) {
            reverted.add(0,sortedDistances.poll());
        }
        return reverted;
    }


    class DistanceComparator implements Comparator<DistanceMeasurement> {
        @Override
        public int compare(DistanceMeasurement x, DistanceMeasurement y) {
            if (x.getValue() > y.getValue()) {
                return -1;
            }
            if (x.getValue() < y.getValue()) {
                return 1;
            }
            return 0;
        }
    }
}