package pr.util.measures;

import pr.data.Model;

/**
 * Created by Seba on 03/03/2018.
 */
public abstract class DistanceMeasurement implements Model<Double> {
    protected int instance1Id;
    protected int instance2Id;
    protected double distance;
    protected int instance1TestDataLabel;
    protected int instance2TrainingDataLabel;

    public DistanceMeasurement() {}

    public DistanceMeasurement(int instance1Id, int instance2Id, double distance, int instance2TrainingDataLabel, int instance1TestDataLabel) {
        this.instance1Id = instance1Id;
        this.instance2Id = instance2Id;
        this.distance = distance;
        this.instance1TestDataLabel = instance2TrainingDataLabel;
        this.instance2TrainingDataLabel = instance2TrainingDataLabel;
    }

    public DistanceMeasurement(int instance1Id, int instance2Id, double distance) {
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

    public Double getValue() {
        return distance;
    }
}
