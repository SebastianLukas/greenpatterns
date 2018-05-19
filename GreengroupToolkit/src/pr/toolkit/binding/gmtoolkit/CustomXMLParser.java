package pr.toolkit.binding.gmtoolkit;

import nanoxml.XMLElement;
import pr.data.table.row.GsmGeo;
import pr.data.table.row.Row;
import util.Graph;
import util.GraphSet;
import xml.XMLParser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Enumeration;
import java.util.Vector;

public class CustomXMLParser extends XMLParser {

    @Override
    public void setGraphPath(String graphPath) {
        this.graphPath = graphPath;
    }

    private String graphPath;

    public CustomXMLParser(){

    }

    @Override
    public GraphSet parseCXL(String path) throws Exception {
        GraphSet graphSet = new GraphSet();

        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            for(String line; (line = br.readLine()) != null; ) {
                String[] parts = line.split(" ");
                String filename = parts[0] + ".gxl";
                Graph g = this.parseGXL(this.graphPath + filename);
                g.setFileName(filename);
                g.setClassName(parts[1] == null ? "NO_CLASS" : parts[1]);
                graphSet.add(g);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return graphSet;
    }
}
