package pr.data.table;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import pr.data.table.row.LabeledMolecule;

import java.io.File;
import java.util.List;
import java.util.Scanner;

public class LabeledMoleculeTable extends Table<LabeledMolecule> {

    public Table <LabeledMolecule> importInstances(String dirPath) {
        Table<LabeledMolecule> labeledMolecules = new Table<LabeledMolecule>();

        File dir = new File(dirPath);
        String[] extensions = new String[] { "gxl" };

        List<File> files = (List<File>) FileUtils.listFiles(dir, extensions, false);
        for (File file : files) {
            String name = FilenameUtils.getBaseName(file.getName());
            labeledMolecules.addRow(new LabeledMolecule(-1, Integer.parseInt(name)));
        }

        this.setTable(labeledMolecules.getTable(), labeledMolecules.getSize());
        return this;
    }

    public Table<LabeledMolecule> importCSV(File csvFile) {
        String separator = " ";
        Table<LabeledMolecule> labeledMolecules = new Table<LabeledMolecule>();
        Scanner inputStream;
        try {
            inputStream = new Scanner(csvFile);
            while(inputStream.hasNextLine()){
                String line= inputStream.nextLine();
                String[] values = line.split(separator);
                int mol = Integer.parseInt(values[0]);
                int label = -1;
                if(values[1].equals("a"))
                    label = 1;
                else if(values[1].equals("i"))
                    label = 2;
                labeledMolecules.addRow(new LabeledMolecule(label, mol));
            }
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.setTable(labeledMolecules.getTable(), labeledMolecules.getSize());
        return this;
    }
}