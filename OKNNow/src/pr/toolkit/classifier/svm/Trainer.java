package  pr.toolkit.classifier.svm;
import java.io.*;
import libsvm.svm;
import libsvm.svm_model;

public class Trainer
{
    private File modelFile;
    private svm_train svmAdaptor;

    public Trainer(File modelFile)
    {
        this.modelFile = modelFile;
        this.svmAdaptor = new svm_train();
    }

    public void train(File trainingDataFile, String libSvmArgs) throws IOException
    {
        StringBuilder args = new StringBuilder();
        args.append(libSvmArgs);
        args.append(" ").append(trainingDataFile.getAbsolutePath());
        args.append(" ").append(modelFile.getAbsolutePath());

        svmAdaptor.run(args.toString().split("\\s+"));
    }

    public double predict(String input) throws IOException
    {
        svm_model model = svm.svm_load_model(modelFile.getAbsolutePath());
        return svm_predict.predictResult(input, model);
    }

}
