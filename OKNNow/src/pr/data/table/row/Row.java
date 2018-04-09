package pr.data.table.row;

/**
 * Created by Seba on 09/03/2018.
 */
public class Row<T> {
    private T row;

    public Row() {}

    public Row(T row) {
        this.row = row;
    }

    public T getRow() {
        return row;
    }

    public String toString() {
        return row.toString();
    }
}
