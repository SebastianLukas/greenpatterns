package pr.toolkit.binding.gmtoolkit;

import util.Graph;
import util.GraphSet;
import xml.XMLParser;
import java.io.BufferedReader;
import java.io.FileReader;

public class CustomXMLParser extends XMLParser {

    private String targetGraphPath;
    public void setTargetGraphPath(String targetGraphPath) {
        this.targetGraphPath = targetGraphPath;
    }

    private String sourceGraphPath;
    public void setSourceGraphPath(String sourceGraphPath) {
        this.sourceGraphPath = sourceGraphPath;
    }

    public CustomXMLParser(){ }

    public GraphSet parseSourceCXL(String path) throws Exception {
        return parseCXL(path, this.sourceGraphPath);
    }

    public GraphSet parseTargetCXL(String path) throws Exception {
        return parseCXL(path, this.targetGraphPath);
    }


    private GraphSet parseCXL(String path, String graphPath) throws Exception {
        GraphSet graphSet = new GraphSet();

        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            for(String line; (line = br.readLine()) != null; ) {
                String[] parts = line.split(" ");
                String filename = parts[0] + ".gxl";
                Graph g = this.parseGXL(graphPath + filename);
                g.setFileName(filename);
                try {
                    g.setClassName(parts[1] == null ? "NO_CLASS" : parts[1]);
                } catch (Exception e) {
                    g.setClassName("NO_CLASS");
                }
                graphSet.add(g);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return graphSet;
    }
}
