package pr.report;

import pr.toolkit.classifier.knn.SingleClassClassificationStatistics;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Seba on 03/03/2018.
 */
public class KNNReport implements Report {

    ArrayList<String> reports = new ArrayList<String>();

    public void addReport(int k, Map<Integer, SingleClassClassificationStatistics> results, int distanceMetric) {
        StringBuilder builder = new StringBuilder("Distance measures: '1' Euclidean; '2' Manhattan.\n");
        Iterator<Integer> iter = results.keySet().iterator();
        double accuracySum = 0;
        while(iter.hasNext()) {
            Integer label = iter.next();
            SingleClassClassificationStatistics result = results.get(label);
            double accuracy = Math.round(result.getLabelClassificationAccuracy()*100.0);
            accuracySum += accuracy;
            builder.append(k +"NN Classification accuracy on digit '" + label + "' : " + accuracy + "%" + " over #" + result.getTotal() + " test instances. Distance metric: '"+ distanceMetric +"'\n"  );
        }
        builder.append("Overall accuracy: " + Math.round(accuracySum/(double)results.keySet().size())+"%");
        reports.add(builder.toString() + "\n");
    }

    @Override
    public void print() {
        for (String s: reports
             ) {
            System.out.println(s);
        }
    }
}
