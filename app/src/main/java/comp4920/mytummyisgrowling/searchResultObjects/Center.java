package comp4920.mytummyisgrowling.searchResultObjects;

import java.io.Serializable;

/**
 * Created by Andy Lee on 27/09/2015.
 */
public class Center implements Serializable {

    private double latitude;
    private double longitude;

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }


}
