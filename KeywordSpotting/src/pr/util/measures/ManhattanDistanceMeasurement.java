package pr.util.measures;

/**
 * Created by Seba on 04/03/2018.
 */
public class ManhattanDistanceMeasurement extends DistanceMeasurement {
    public ManhattanDistanceMeasurement(int instance1Id, int instance2Id, double distance, int trainingLabel, int testTrueLabel) {
        this.instance1Id = instance1Id;
        this.instance2Id = instance2Id;
        this.distance = distance;
        this.instance1TestDataLabel = testTrueLabel;
        this.instance2TrainingDataLabel = trainingLabel;
    }

    @Override
    public String toString() {
        return instance1Id + ";" + instance2Id + ";" +instance1TestDataLabel + ";" + instance2TrainingDataLabel + ";" + distance;
    }
}
