package pr.data.table;

import pr.data.table.row.LabeledMolecule;

import java.io.File;
import java.util.Scanner;

public class LabeledMoleculeTable extends Table<LabeledMolecule> {

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