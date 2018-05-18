package pr.problem.molecules;

import algorithms.GraphMatching;
import xml.XMLParser;

public class Classifier {

    public void run() {
        //we need to define config parameters in a properties file
        String propertiesPath = "relative path to root/data/config.properties";

        /*


        ## Quick Start

        We recommend the following three steps:

        #### STEP 1:
        Unzip the archive `Sources.zip`, integrate the whole framework in an IDE (e.g. Eclipse) and build the project.

        #### STEP 2:
        Define a properties file in order to define the parameters of your graph matching task. In the folder `properties` you find three examples of such properties (for more details on these parameters we refer to our paper:

        K. Riesen, S. Emmenegger and H. Bunke.
        A Novel Software Toolkit for Graph Edit Distance Computation.. In W.G. Kropatsch et al.,
        editors, Proc. 9th Int. Workshop on Graph Based Representations in Pattern Recognition, LNCS 7877, 142–151, 2013.*

        #### STEP 3:
        Run the graph matching as a java application.
        The main method is in `GraphMatching.java`, the sole program argument is an URL pointing to the properties file
        (e.g.: `./properties/properties_molecules.prop`)



         */


        //Steps
        //1. parse graphs
        //2. compute approx ged:
        //   cost matrix
        //   hungarian alg to find optimal assignment
        //   derive edit path costs from the result -> distance for classification
        //3. use KNN on computed distances to classify

        try {
            GraphMatching gm = new GraphMatching(propertiesPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}