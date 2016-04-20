import java.io.Serializable;

/**
 * Created by vikramgupta on 4/19/16.
 */
public class LocationClass implements Serializable{
    String lat;
    String lng;

    public LocationClass() {
        lat = "";
        lng = "";
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }
}
