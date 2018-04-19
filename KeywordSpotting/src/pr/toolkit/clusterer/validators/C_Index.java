package pr.toolkit.clusterer.validators;

import pr.data.Dataset;
import pr.data.table.Table;
import pr.data.table.row.LabeledImage;
import pr.util.measures.functions.EuclideanDistance;

import java.io.*;
import java.util.*;

/**
 * Created by Seba on 09/03/2018.
 */
public class C_Index implements ClusterValidationIndex {
    private HashMap<Integer, HashSet<Integer>> clustering;
    private double alpha = 0;
    private double sigma = 0;
    private double min;
    private double max;
    private double index = -1;
    private double k;

    private int loggingSteps = 4000; //processed test instances

    public C_Index(HashMap<Integer, HashSet<Integer>> clustering) {
        this.clustering = clustering;
        this.k = clustering.size();
        computeAlpha();
    }

    private void computeAlpha() {
        Set<Integer> clusterKeys = clustering.keySet();
        Iterator<Integer> iter = clusterKeys.iterator();
        double all = 0.0d;
        while(iter.hasNext()) {
            double size = clustering.get(iter.next()).size();
            all += (size * (size - 1)) / 2;
        }
        this.alpha = all;
    }

    @Override
    public String getName() {
        return "C-Index";
    }

    @Override
    public double getValue() {
        return 0;
    }

    @Override
    public double Compute() {
        sigma = 0;
        Table<LabeledImage> table = Dataset.getTable("train");
        boolean loaded = false;
        //double[][] a = null;
        float[] a = null;
        int tableSize = table.getSize();
        try {
            FileInputStream fis = new FileInputStream("./././data/distances.ser");
            ObjectInputStream in = new ObjectInputStream(fis);
            //a = (double[][]) in.readObject();
            a = (float[]) in.readObject();
            loaded = true;
        } catch (Exception e) {
            //a = new double[table.getSize()][table.getSize()];
            a = new float[(int)((tableSize * (tableSize - 1))/2)];
        }

        Integer[] clusterKeys = clustering.keySet().toArray(new Integer[0]);
        double distance = 0.0d;
        int n = 0;
        for (int i = 1; i < tableSize; i++) {
            if(i%loggingSteps == 0) {
               System.out.println("C-Index - Completion "+(int)(((double)i/(double)tableSize)*100.0d)+"%");
            }
            for (int j = i+1; j <= tableSize; j++) {
                /*
                if(loaded) {
                    distance = a[i - 1][j - 1];
                } else {
                    distance = EuclideanDistance.compute(((LabeledImage) table.getRow(i)).getImage(),
                            ((LabeledImage) table.getRow(j)).getImage());
                    a[i - 1][j - 1] = distance;
                }
                */

                if(loaded) {
                    distance = a[n];
                } else {
                    distance = EuclideanDistance.compute(((LabeledImage) table.getRow(i)).getImage(),
                            ((LabeledImage) table.getRow(j)).getImage());
                    a[n] = (float)distance;
                }
                n++;

                for (Integer clusterKey : clusterKeys
                        ) {
                    HashSet<Integer> cluster = clustering.get(clusterKey);
                    if(cluster.contains(i) && cluster.contains(j)) {
                       sigma += distance;
                       break; //obviously this pair will not occur in another cluster.
                   }
                }
            }
        }

        //if distances where loaded then there is no need to sort and serialize
        if(!loaded) {

            try {
                FileOutputStream fout = null;
                ObjectOutputStream oos = null;

                fout = new FileOutputStream("./././data/distances.ser");
                oos = new ObjectOutputStream(fout);
                oos.writeObject(a);
                oos.flush();
                oos.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        System.out.println("C-Index - Sorting...");
        Arrays.sort(a);

        //a[] is sorted
        for (int i = 0; i < alpha; i++) {
            min += a[i];
        }

        for (int i = a.length-(int)alpha; i <  a.length; i++) {
            max += a[i];
        }

        /*
        Collections.sort(distances, new DoubleComparator());
        List<Double> shortestList = distances.subList(0, (int)alpha-1);
        for (Double d: shortestList
             ) {
            min += d;
        }
        shortestList = null;
        */
        /*
        Collections.sort(distances, new DoubleComparatorDesc());
        List<Double> greatestList = distances.subList(0, (int)alpha-1);
        for (Double d: greatestList
                ) {
            max += d;
        }
        greatestList = null;
        distances = null;
        */
        this.index = (sigma - min) / (max - min);
        System.out.println("C-Index " + this.index);
        return index;
    }
}