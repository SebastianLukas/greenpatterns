package  pr.toolkit.classifier.svm;
import java.io.*;


import libsvm.svm;
import libsvm.svm_model;

/**
 * Wrapper for libsvm classes.
 * Trains the data using Gaussian kernel
 * @author Nirav
 *
 */
public class Trainer
{
    private File modelFile;
    private svm_train svmAdaptor;

    public Trainer(File modelFile)
    {
        this.modelFile = modelFile;
        this.svmAdaptor = new svm_train();
    }

    /**
     * Trains the SVM on input data using Gaussian (RBF) kernel
     * @throws IOException
     */
    public void train(File trainingDataFile, String libSvmArgs) throws IOException
    {
        StringBuilder args = new StringBuilder();
        args.append(libSvmArgs);
        args.append(" ").append(trainingDataFile.getAbsolutePath());
        args.append(" ").append(modelFile.getAbsolutePath());

        svmAdaptor.run(args.toString().split("\\s+"));
    }

    /**
     * Predicts the SVM classification for the given input data.
     * Input validation is not done at this time, so don't send invalid input.
     * @param input
     * @throws IOException
     */
    public double predict(String input) throws IOException
    {
        svm_model model = svm.svm_load_model(modelFile.getAbsolutePath());
        return svm_predict.predictResult(input, model);
    }

    /**
     * Validate the model by comparing it against the known labeled data set (either training or test set).
     * @param dataToValidateFile
     * @throws IOException
     */
  /*
  public void validateDataSet(String dataToValidateFile) throws IOException
  {
    File inputFile = new File(dataToValidateFile);
    String namePrefix = inputFile.getName().replaceFirst("\\.*?$", "");
    String outputFileName = inputFile.getParent() + File.separator + namePrefix + "_validated.csv";

    if(!inputFile.exists() || !inputFile.isFile() || !inputFile.canRead())
      throw new IllegalArgumentException(inputFile.getAbsolutePath() + " must be a valid readable file.");

    File outputFile = new File(outputFileName);
    BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
    writer.write("Actual_Output,Predicted_Output");
    writer.newLine();

    int totalSamples = 0;
    int numCorrect = 0;

    BufferedReader reader = new BufferedReader(new FileReader(inputFile));
    String line = null;

    String tokens[] = null;
    double actualOutputClass;
    double predictedOutputClass;
    StringBuilder inputData = null;

    while((line = reader.readLine()) != null)
    {
      totalSamples++;
      tokens = line.trim().split("\\s+");
      actualOutputClass = Double.parseDouble(tokens[0]);
      inputData = new StringBuilder();

      for(int i = 1; i < tokens.length; i++)
        inputData.append(tokens[i] + "\t");

      predictedOutputClass = predict(inputData.toString().trim());

      if(predictedOutputClass == actualOutputClass)
        numCorrect++;

      writer.write(actualOutputClass +"," + predictedOutputClass);
      writer.newLine();
    }
    reader.close();
    writer.close();

    System.out.format("Total Samples : %d\tCorrectly Classified : %d\tPercent Correctly Classified : %.2f\n", totalSamples, numCorrect, (1.0 * numCorrect / totalSamples * 100.0));
  }
  */
}
