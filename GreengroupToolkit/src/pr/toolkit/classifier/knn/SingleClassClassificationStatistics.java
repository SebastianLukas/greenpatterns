package pr.toolkit.classifier.knn;

public class SingleClassClassificationStatistics {
    private Integer classLabel;
    private int correctClassificationsCount;
    private int total;

    public SingleClassClassificationStatistics(Integer classLabel) {
        this.classLabel = classLabel;
        this.correctClassificationsCount = 0;
        this.total = 0;
    }

    public Integer getClassLabel() {
        return classLabel;
    }

    public int getCorrectClassificationsCount() {
        return correctClassificationsCount;
    }

    public int getTotal() {
        return total;
    }

    public void countResult(boolean correct) {
        this.total+=1;
        if(correct)
            this.correctClassificationsCount += 1;
    }

    public double getLabelClassificationAccuracy() {
        return (double)this.correctClassificationsCount/(double)this.total;
    }
}


