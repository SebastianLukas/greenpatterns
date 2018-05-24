package pr.util.measures;

import pr.data.Model;

/**
 * Created by Seba on 03/03/2018.
 */
public  class DistanceMeasurement implements Model<Float> {
    private int testId;
    private int trainId;
    private float distance;
    private int testLabel;
    private int trainingLabel;

    public DistanceMeasurement(int testInstanceId, int trainInstanceId, float distance, int testLabel, int trainLabel) {
        this.testId = testInstanceId;
        this.trainId = trainInstanceId;
        this.distance = distance;
        this.testLabel = testLabel;
        this.trainingLabel = trainLabel;
    }

    public DistanceMeasurement(int testId, int trainId, float distance) {
        this.testId = testId;
        this.trainId = trainId;
        this.distance = distance;
    }

    public int getTestId() {
        return testId;
    }

    public int getTrainId() {
        return trainId;
    }

    public int getTestLabel() {
        return testLabel;
    }

    public int getTrainingLabel() {
        return trainingLabel;
    }

    public Float getValue() {
        return distance;
    }
}
