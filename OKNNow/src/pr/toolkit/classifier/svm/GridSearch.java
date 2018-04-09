package pr.toolkit.classifier.svm;

import java.io.IOException;
import java.util.ArrayList;

/**
 *
 */
public class GridSearch {

    private ArrayList<Double> generateTestingValues(int power, int base, int n){
        ArrayList<Double> list = new ArrayList<Double>();
        for(int i = 0;i<n;i++){
            list.add(Math.pow(base, power++));
        }
        return list;
    }

    /**
     *
     * @param kernelCode 1 for linear kernel and 2 for rbf
     * @throws IOException
     */
    public void gridSearch(int kernelCode, int nFolds, String filePath) throws IOException {
        ArrayList<Double> cValues = generateTestingValues(2,2,5);
        ArrayList<Double> gValues = new ArrayList<Double>();
        gValues.add(0.0d);
        String [] args = {"-v",Integer.toString(nFolds),"-t",Integer.toString(kernelCode),"-q","-d","2","-g","0","-c","0",filePath};
        if(kernelCode != 0) {
            gValues = generateTestingValues(-10, 2, 10);
        }
        svm_train s = new svm_train();
        for(int i = 0;i<cValues.size();i++){
            args[10] = Double.toString( cValues.get(i));
            for(int j = 0;j<gValues.size();j++){
                args[8] = Double.toString( gValues.get(j));
                for(String e : args){
                    System.out.print(e+" ");
                }
                System.out.println("");
                s.main(args);
            }
        }
    }
}
