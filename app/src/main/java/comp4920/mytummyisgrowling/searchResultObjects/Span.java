package comp4920.mytummyisgrowling.searchResultObjects;

import java.io.Serializable;

/**
 * Created by Andy Lee on 27/09/2015.
 */
public class Span implements Serializable {

    private double latitude_delta;
    private double longitude_delta;

    public double getLatitude_delta() {
        return latitude_delta;
    }

    public void setLatitude_delta(double latitude_delta) {
        this.latitude_delta = latitude_delta;
    }

    public double getLongitude_delta() {
        return longitude_delta;
    }

    public void setLongitude_delta(double longitude_delta) {
        this.longitude_delta = longitude_delta;
    }


}
