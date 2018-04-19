package pr.data.table;

import pr.data.table.row.Row;

import java.io.File;
import java.io.FileWriter;
import java.util.*;

/**
 * Created by Seba on 02/03/2018.
 */
public class Table<T> {
    private HashMap<Integer, Row<T>> table = null;
    private Integer autoincrementId = null;
    public Table(){
        this.autoincrementId = 0;
        table = new HashMap<Integer, Row<T>>();
    }

    public void setTable(HashMap<Integer, Row<T>> table, int lastId) {
        this.table = table;
        this.autoincrementId = lastId;
    }

    public HashMap<Integer, Row<T>> getTable() {
        return this.table;
    }

    public void addRow(Row<T> row) {
        incrementId();
        table.put(this.autoincrementId, row);
    }

    public Row<T> getRow(Integer id) {
        return table.get(id);
    }

    private void incrementId() {
        this.autoincrementId++;
    }


    public int getSize() {
        return this.table.size();
    }

    public Table<T> importCSV(File csvFile) { return null; }

    public void store(String name) {
        File file = new File("./././data/"+name+".csv");
        try {
            FileWriter writer = new FileWriter(file);
            for(int i = 1; i <= getSize(); i++) {
                Row<T> row = table.get(i);
                writer.write(row.toString()+"\n");
            }
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
