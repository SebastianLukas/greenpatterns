package pr.data.table.row;

import java.text.DateFormat;
import java.time.format.DateTimeFormatter;
import java.time.*;
import java.util.concurrent.TimeUnit;

public class GsmTrack extends Row<GsmTrack> {
    private long timestamp;
    private String dateTime;
    private int gsmCell;
    private int label;
    private String format = "E MMM d HH:mm:ss uuuu";
    private int SECONDS = 1;
    private int MINUTES = 2;

    public GsmTrack() { }

    public GsmTrack(String rawLine, int timeGranularity) {
        String[] tokens = rawLine.split("\t");
        dateTime = tokens[0];
        DateTimeFormatter f = DateTimeFormatter.ofPattern(format);
        try {
            LocalDateTime dateTime = LocalDateTime.from(f.parse(tokens[0]));
            if(timeGranularity == MINUTES) {
                this.timestamp = TimeUnit.SECONDS.toMinutes(dateTime.atZone(ZoneId.systemDefault()).toInstant().getEpochSecond());
            } else {
                this.timestamp = dateTime.atZone(ZoneId.systemDefault()).toInstant().getEpochSecond();
            }
            this.gsmCell = Integer.parseInt(tokens[1]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setLabel(int label) {
        this.label = label;
    }

    public int getLabel() {
        return this.label;
    }

    public String toString() {
        return label + " " + timestamp + " " + gsmCell;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public int getGsmCell() {
        return gsmCell;
    }

    public String getDateTime() {
        return dateTime;
    }
}
