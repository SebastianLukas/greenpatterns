package pr.data.table.row;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

public class GeoTrack extends Row<GeoTrack> {
    private long timestamp;
    private String datetime;
    private float longitude;
    private float latitude;
    private float velocity;
    private String format = "E MMM d HH:mm:ss uuuu";
    private int SECONDS = 1;
    private int MINUTES = 2;

    public GeoTrack() { }

    public GeoTrack(String rawLine, int timeGranularity) {
        String[] tokens = rawLine.split(" ");
        try {
            this.longitude = Float.parseFloat(tokens[0]);
            this.latitude = Float.parseFloat(tokens[1]);
            if(timeGranularity == MINUTES) {
                this.timestamp = TimeUnit.SECONDS.toMinutes(Long.parseLong(tokens[2]));
            } else {
                this.timestamp = Long.parseLong(tokens[2]);
            }
            DateTimeFormatter f = DateTimeFormatter.ofPattern(format);
            try {
                Instant i =  Instant.ofEpochSecond(Long.parseLong(tokens[2]) + (long)7200);
                LocalDateTime dateTime = LocalDateTime.ofInstant(i, ZoneOffset.UTC);
                this.datetime = dateTime.format(f);
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                this.velocity = Float.parseFloat(tokens[3]);
            } catch (Exception e) {
                this.velocity= 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public long getTimestamp() {
        return timestamp;
    }

    public float getLongitude() {
        return longitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public float getVelocity() {
        return velocity;
    }

    public String getDatetime() {
        return datetime;
    }
}
