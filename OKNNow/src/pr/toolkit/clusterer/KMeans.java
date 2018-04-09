package pr.toolkit.clusterer;

import pr.data.table.row.Distances;
import pr.data.table.row.LabeledImage;
import pr.data.table.Table;
import pr.report.KMeansReport;
import pr.toolkit.clusterer.validators.C_Index;
import pr.toolkit.clusterer.validators.Davis_Bouldin_Index;
import pr.util.measures.functions.Mean;
import pr.util.measures.functions.EuclideanDistance;
import pr.util.measures.functions.EuclideanDistanceMeasurer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by Seba on 09/03/2018.
 */
public class KMeans {
    private Table trainingData;
    private Table euclideanDistances;
    private int loggingSteps = 50; //processed test instances
    private KMeansReport report;

    //need another data structure and not arraylist
    //to handle c index memebership function
    //better a map with
    private HashMap<Integer, HashSet<Integer>> clustering;
    private HashMap<Integer, ClusterCenter> centers;

    public final static int RANDOM_CENTERS = 1;
    public final static int COMPUTED_CENTERS = 2;

    public KMeans(Table trainingData, Table euclideanDistances) {
        this.euclideanDistances = euclideanDistances;
        this.trainingData = trainingData;
    }

    //do not need this if i will choose first centers randomly
    public void computeDistances(boolean save) {
        if(this.euclideanDistances == null || this.euclideanDistances.getSize() == 0) {
            this.euclideanDistances = new Table<Distances>();
            EuclideanDistanceMeasurer edr = new EuclideanDistanceMeasurer();
            for (int i = 1; i <= trainingData.getSize(); i++) {
                LabeledImage instance = (LabeledImage) trainingData.getRow(i);
                Distances row = new Distances(edr.computeEuclideanDistances(i, instance.getImage(), trainingData, 1));
                this.euclideanDistances.addRow(row);
                if (i % loggingSteps == 0) {
                    System.out.println("Computed distances for #" + i + " instances");
                }
            }
            if (save) {
                euclideanDistances.store("distances");
            }
        }
    }

    public void run(int k, int iterations, int initCentersType, int runs) {
        System.out.println("Starting "+runs+" runs of k="+k+"-Means @100 Clustering Iterations");
        for(int i = 0; i < runs; i++) {
            System.out.println("Run " + (i+1));
            switch (initCentersType) {
                case RANDOM_CENTERS: {
                    randomCenters(k);
                    break;
                }
                case COMPUTED_CENTERS: {
                    computedCenters(k);
                    break;
                }
            }
            compute(k, iterations, 0);
            //try {
            //    saveImages();
            //} catch (Exception e) {}

            C_Index ci = new C_Index(clustering);
            ci.Compute();

            Davis_Bouldin_Index dbi = new Davis_Bouldin_Index(clustering, centers);
            dbi.Compute();
        }
    }

    private void saveImages() throws IOException {
        for (Integer centerId : centers.keySet()
                ) {
            BufferedImage image = getImageFromArray(centers.get(centerId).getCenter(), 28,28);
            File outputfile = new File("./././data/"+centerId+".jpeg");
            ImageIO.write(image, "jpeg", outputfile);
        }
    }

    public static BufferedImage getImageFromArray(double[] pixels, int width, int height) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        WritableRaster raster = (WritableRaster) image.getData();
        raster.setPixels(0,0,width,height,pixels);
        return image;
    }

    private void compute(int k, int iterations, int count) {
        count++;
        if (count % loggingSteps == 0) {
            System.out.println("Computed iteration #" + count + " on " + iterations);
        }
        if(iterations >= count) {
            cluster(k);
            compute(k, iterations, count);
        }
    }

    private void randomCenters(int k) {
        this.centers = new HashMap<Integer, ClusterCenter>();
        Random r = new Random();
        for(int i = 0; i < k; i++) {
            int imageId = r.nextInt(trainingData.getSize()-1)+1; //no 0
            ClusterCenter center = new ClusterCenter(imageId, ((LabeledImage)trainingData.getRow(imageId)).getImage());
            centers.put(imageId, center);
        }
    }

    private void initClustering() {
        this.clustering = new HashMap<Integer, HashSet<Integer>>();
        for (Integer centerId : centers.keySet()
                ) {
            HashSet<Integer> set = new HashSet<Integer>();
            set.add(centerId);
            this.clustering.put(centerId, set);
        }
    }

    private void computedCenters(int k) {
        this.centers = new HashMap<Integer, ClusterCenter>();
        this.clustering = new HashMap<Integer, HashSet<Integer>>();
        //one
        //based on varianceful features subsets
        //compute mean and deviation for each feature across images
        //select "cluster" k groups optaining max deviation among features
        //select randoms in that cluster

        //two pic random, compute distances onte to one distances with all
        // get k samples
    }

    //new clustering at each iteration?
    private void cluster(int k) {
        //iterate each center, take an image and compute the k distances, assign the nearest the repeat
        initClustering();

        //check validity DONE
        //PriorityQueue<DistanceMeasurement> centerDistances = new PriorityQueue<DistanceMeasurement>(2, new DistanceComparator());

        for (int idRow = 1; idRow <= trainingData.getSize(); idRow++) {
            int nearestCenterId = -1;
            int nearestInstanceId = -1;
            double currentDistance = Double.MAX_VALUE;
            for (Integer centerId : centers.keySet()
                    ) {
                ClusterCenter center = centers.get(centerId);
                LabeledImage trainingRecord = (LabeledImage)trainingData.getRow(idRow);
                //centerDistances.add(new EuclideanDistanceMeasurement(center.getId(), i,
                //        EuclideanDistance.compute(center.getCenter(), trainingRecord.getImage())));
                double newDistance = EuclideanDistance.compute(center.getCenter(), trainingRecord.getImage());
                if(newDistance < currentDistance) {
                    currentDistance = newDistance;
                    nearestCenterId = centerId;
                    nearestInstanceId = idRow;
                }
                //if(centerDistances.size()>1) {
                //    DistanceMeasurement discarded = centerDistances.poll();
                //    discarded.getInstance1Id();
                //}

            }
            //DistanceMeasurement nearest = centerDistances.poll();
            //centerDistances.clear();
            //clustering.get(nearest.getInstance1Id()).add(nearest.getInstance2Id());
            clustering.get(nearestCenterId).add(nearestInstanceId);
        }
        //recompute centers
        for (Integer key: clustering.keySet()
             ) {
            HashSet<Integer> imageIds = clustering.get(key);
            Mean mean = new Mean();
            Iterator<Integer> iter = imageIds.iterator();
            while(iter.hasNext()) {
                LabeledImage image = (LabeledImage)trainingData.getRow(iter.next());
                mean.add(image.getImage());
            }
            double[] center = mean.getMean();
            centers.get(key).setCenter(center);
        }
    }

    public void showReport() {

    }
}
