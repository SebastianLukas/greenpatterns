package pr.report;

import pr.toolkit.clusterer.validators.ClusterValidationIndex;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Seba on 09/03/2018.
 */
public class KMeansReport implements Report {

    ArrayList<String> reports = new ArrayList<String>();

    public void addReport(int k, Map<Integer, ClusterValidationIndex> results) {
        StringBuilder builder = new StringBuilder("Distance measures: '1' Euclidean\n");
        Iterator<Integer> iter = results.keySet().iterator();
        while(iter.hasNext()) {
            Integer label = iter.next();
            ClusterValidationIndex result = results.get(label);
            String type = result.getName();
            double index = Math.round(result.getValue());
            builder.append(k +"Means Cluster Index type '" + type + "' : " + index);
        }
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
