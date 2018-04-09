package pr.util.measures;

/**
 * Created by Seba on 03/03/2018.
 */
public class EuclideanDistanceMeasurement extends DistanceMeasurement {

    public EuclideanDistanceMeasurement(int instanceId1, int instanceId2, double distance, int trainingLabel, int testTrueLabel) {
        super(instanceId1,instanceId2, distance, trainingLabel, testTrueLabel);
    }

    public EuclideanDistanceMeasurement(int instanceId1, int instanceId2, double distance) {
        this.instance1Id = instanceId1;
        this.instance2Id = instanceId2;
        this.distance = distance;
        this.instance1TestDataLabel = -1;
        this.instance2TrainingDataLabel = -1;
    }

    @Override
    public String toString() {
        return instance1Id + ";"
                + instance2Id + ";"
                + (instance1TestDataLabel != -1 ? instance1TestDataLabel : "") + ";"
                + (instance2TrainingDataLabel != -1 ? instance2TrainingDataLabel : "") + ";"
                + Math.round(distance * 100.0) / 100.0d;
    }
}
