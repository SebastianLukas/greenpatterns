package pr.util.measures;

import pr.data.Model;

/**
 * Created by Seba on 03/03/2018.
 */
public  class DistanceMeasurement implements Model<Float> {
    private int instance1Id;
    private int instance2Id;
    private float distance;
    private int instance1TestDataLabel;
    private int instance2TrainingDataLabel;

    public DistanceMeasurement() {}

    public DistanceMeasurement(int instance1Id, int instance2Id, float distance, int  instance2TrainingDataLabel, int instance1TestDataLabel) {
        this.instance1Id = instance1Id;
        this.instance2Id = instance2Id;
        this.distance = distance;
        this.instance1TestDataLabel = instance1TestDataLabel;
        this.instance2TrainingDataLabel = instance2TrainingDataLabel;
    }

    public DistanceMeasurement(int instance1Id, int instance2Id, float distance) {
        this.instance1Id = instance1Id;
        this.instance2Id = instance2Id;
        this.distance = distance;
    }

    public int getInstance1Id() {
        return instance1Id;
    }

    public int getInstance2Id() {
        return instance2Id;
    }

    public int getInstance1TestDataLabel() {
        return instance1TestDataLabel;
    }

    public int getInstance2TrainingDataLabel() {
        return instance2TrainingDataLabel;
    }

    public Float getValue() {
        return distance;
    }
}
