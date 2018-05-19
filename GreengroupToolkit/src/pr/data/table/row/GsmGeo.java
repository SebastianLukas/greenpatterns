package pr.data.table.row;

public class GsmGeo extends Row<GsmGeo> {
    private String dateTime;
    private int timestamp;
    private float longitude;
    private float latitude;
    private int cell;
    private float velocity;

    public GsmGeo() { }

    public GsmGeo(String dateTime, int timestamp, float longitude, float latitude, int cell, float velocity) {
        this.dateTime = dateTime;
        this.timestamp = timestamp;
        this.longitude = longitude;
        this.latitude = latitude;
        this.cell = cell;
        this.velocity = velocity;
    }

    public GsmGeo(String rawLine) {
        String[] tokens = rawLine.split("\t");
        try {
            this.longitude = Float.parseFloat(tokens[0]);
            this.latitude = Float.parseFloat(tokens[1]);
            this.timestamp = Integer.parseInt(tokens[2]);
            this.velocity = Float.parseFloat(tokens[3]);
            this.cell = Integer.parseInt(tokens[4]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getTimestamp() {
        return timestamp;
    }

    public float getLongitude() {
        return longitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public int getCell() {
        return cell;
    }

    public float getVelocity() {
        return velocity;
    }

    public String getDateTime() {
        return dateTime;
    }
}
