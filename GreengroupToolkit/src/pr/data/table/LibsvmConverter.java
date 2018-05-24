package pr.data.table;

import pr.data.table.row.LabeledImage;

import java.io.*;
import java.util.ArrayList;

/**
 * Converts CSV feature files to a format expected by Libsvm
 * @author Nirav
 *
 */
public class LibsvmConverter
{
    public static File convert(LabeledImageTable table, String name)
    {
        File file = null;
        ArrayList<String> lines = new ArrayList<String>();
        for(int i = 1; i <= table.getSize(); i++) {
            LabeledImage image = (LabeledImage) table.getRow(i);

            StringBuilder line = new StringBuilder();
            line.append(image.getLabel());

            for(int idx = 0; idx < image.getImage().length; idx++)
            {
                float feature = (float)(image.getImage()[idx]/255.0d);
                if(feature != 0)
                    line.append(" ").append(idx).append(":").append(feature);
            }
            lines.add(line.toString());
        }
        try {
            file = new File("./././data/svm/libsvm.mnist." + name + ".txt");
            FileWriter writer = new FileWriter(file);
            BufferedWriter bwriter = new BufferedWriter(writer);
            for (String line: lines
                 ) {
                bwriter.write(line);
                bwriter.newLine();
            }
            bwriter.flush();
            bwriter.close();
        } catch (Exception e) {
           e.printStackTrace();
        }
        return file;
    }
}  
