package pr.toolkit.classifier.knn;

import pr.data.Dataset;
import pr.data.table.row.Label;
import pr.data.table.row.LabeledImage;
import pr.data.table.Table;
import pr.report.KNNReport;
import pr.util.measures.*;
import pr.util.measures.functions.EuclideanDistance;
import pr.util.measures.functions.ManhattanDistance;

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

    private ArrayList<ArrayList<DistanceMeasurement>> euclideanDistances;
    private ArrayList<ArrayList<DistanceMeasurement>> manhattanDistances;

    private ArrayList<Classification> classification;

    private KNNReport report;

    public final static int EUCLIDEAN_DISTANCE = 1;
    public final static int MANHATTAN_DISTANCE = 2;

    public KNN(Dataset data) {
        this.trainingData = data.getTable("training");
        this.testData = data.getTable("test");
        this.labels = data.getTable("labels");
    }

    public void run(int k, int distanceMetric) {
        switch (distanceMetric) {
            case EUCLIDEAN_DISTANCE :
                System.out.println("Run "+k+"NN on euclidean distances");
                if(this.euclideanDistances == null || this.euclideanDistances.isEmpty()) {
                    this.computeDistances(EUCLIDEAN_DISTANCE, false);
                }
                run(k, this.euclideanDistances, EUCLIDEAN_DISTANCE);
                break;
            case MANHATTAN_DISTANCE :
                System.out.println("Run "+k+"NN on manhattan distances");
                if(this.manhattanDistances == null || this.manhattanDistances.isEmpty()) {
                    this.computeDistances(MANHATTAN_DISTANCE, false);
                }
                run(k, this.manhattanDistances, MANHATTAN_DISTANCE);
                break;
        }
    }

    private void run(int k, ArrayList<ArrayList<DistanceMeasurement>> distances, int distanceMetric) {
        this.classification = new ArrayList<Classification>();
        Iterator<ArrayList<DistanceMeasurement>> iter = distances.iterator();
        while(iter.hasNext()) {
            ArrayList<DistanceMeasurement> neighbours = iter.next();
            int majorityLabel = KNNClassify(k, neighbours);
            int id = neighbours.get(0).getInstance1Id();
            int trueLabel = neighbours.get(0).getInstance1TestDataLabel();
            this.classification.add(new Classification(id, trueLabel, majorityLabel));
        }
        if(report == null) report = new KNNReport();
        report.addReport(k, evaluateClassificationAccuracy(this.classification), distanceMetric);
    }


    private Map<Integer, SingleClassClassificationStatistics> evaluateClassificationAccuracy(ArrayList<Classification> classification) {
        Map<Integer, SingleClassClassificationStatistics> evaluation = new HashMap<Integer, SingleClassClassificationStatistics>();
        for(int i = 0; i < labels.getSize(); i++) {
            Integer label = ((Label)labels.getRow(new Integer(i+1))).getLabel();
            SingleClassClassificationStatistics cs = new SingleClassClassificationStatistics(label);
            evaluation.put(label, cs);
        }

        for(int i = 0; i < classification.size(); i++) {
            Classification c = classification.get(i);
            if(evaluation.containsKey(c.getTrueLabel())) {
                boolean correct = false;
                if(c.correct()) {
                    correct = true;
                }
                SingleClassClassificationStatistics cs = evaluation.get(c.getTrueLabel());
                cs.classificationResult(correct);
            }
        }
        return evaluation;
    }

    private int KNNClassify(int k, ArrayList<DistanceMeasurement> neighbours) {
        int majorityLabel = -1;
        Map<Integer, Integer> labelCount = new HashMap<Integer, Integer>();
        for(int i = 0; i < k; i++) {
            DistanceMeasurement d = neighbours.get(i);
            if(labelCount.containsKey(d.getInstance2TrainingDataLabel())) {
                int tot = labelCount.get(d.getInstance2TrainingDataLabel())+ 1;
                if(tot >= i/2) {
                    majorityLabel = d.getInstance2TrainingDataLabel();
                    break;
                }
                else { labelCount.put(d.getInstance2TrainingDataLabel(), tot); }
            } else {
                majorityLabel = d.getInstance2TrainingDataLabel();
                labelCount.put(d.getInstance2TrainingDataLabel(), 1);
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
                this.euclideanDistances = new ArrayList<ArrayList<DistanceMeasurement>>();
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
                this.manhattanDistances = new ArrayList<ArrayList<DistanceMeasurement>>();
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

    private void storeDistances(int metric, ArrayList<ArrayList<DistanceMeasurement>> distances) {
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
                ArrayList<DistanceMeasurement> singleDistanceSet = distances.get(i);
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
            DistanceMeasurement distance = new ManhattanDistanceMeasurement(id, i,
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
            EuclideanDistanceMeasurement distance = new EuclideanDistanceMeasurement(id, i,
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