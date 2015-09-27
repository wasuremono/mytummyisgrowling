package comp4920.mytummyisgrowling.searchResultObjects;

/**
 * Created by Andy Lee on 27/09/2015.
 */

import java.util.List;


public class SearchResponse {

    private Region region;
    private List<Business> businesses;
    private int total;

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region newRegion) {
        this.region = newRegion;
    }

    public List<Business> getBusinesses() {
        return businesses;
    }

    public void setBusinesses(List<Business> newBusinesses){
        this.businesses = newBusinesses;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int newTotal) {
        this.total = newTotal;
    }

}
