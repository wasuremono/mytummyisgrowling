package comp4920.mytummyisgrowling.searchResultObjects;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Andy Lee on 27/09/2015.
 */
public class Location implements Serializable {

    private List<String> address;
    private String city;
    private Coordinate coordinate;
    private String country_code;
    private List<String> display_address;
    private int geo_accuracy;
    private List<String> neighbourhods;
    private String postal_code;
    private String state_code;

    public List<String> getAddress() {
        return address;
    }

    public void setAddress(List<String> address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    public String getCountry_code() {
        return country_code;
    }

    public void setCountry_code(String country_code) {
        this.country_code = country_code;
    }

    public List<String> getDisplay_address() {
        return display_address;
    }

    public void setDisplay_address(List<String> display_address) {
        this.display_address = display_address;
    }

    public int getGeo_accuracy() {
        return geo_accuracy;
    }

    public void setGeo_accuracy(int geo_accuracy) {
        this.geo_accuracy = geo_accuracy;
    }

    public List<String> getNeighbourhods() {
        return neighbourhods;
    }

    public void setNeighbourhods(List<String> neighbourhods) {
        this.neighbourhods = neighbourhods;
    }

    public String getPostal_code() {
        return postal_code;
    }

    public void setPostal_code(String postal_code) {
        this.postal_code = postal_code;
    }

    public String getState_code() {
        return state_code;
    }

    public void setState_code(String state_code) {
        this.state_code = state_code;
    }
}
