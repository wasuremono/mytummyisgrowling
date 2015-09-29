package comp4920.mytummyisgrowling.searchResultObjects;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Andy Lee on 27/09/2015.
 */
public class Business implements Serializable {

    private boolean is_claimed;
    private int rating;
    private String mobile_url;
    private String rating_img_url;
    private int review_count;
    private String name;
    private String rating_img_url_small;
    private String url;
    private List<List<String>> categories;
    private String phone;
    private String snippet_text;
    private String image_url;
    private String display_phone;
    private String rating_img_url_large;
    private String id;
    private boolean is_closed;
    private Location location;


    public boolean is_claimed() {
        return is_claimed;
    }

    public void setIs_claimed(boolean is_claimed) {
        this.is_claimed = is_claimed;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getMobile_url() {
        return mobile_url;
    }

    public void setMobile_url(String mobile_url) {
        this.mobile_url = mobile_url;
    }

    public String getRating_img_url() {
        return rating_img_url;
    }

    public void setRating_img_url(String rating_img_url) {
        this.rating_img_url = rating_img_url;
    }

    public int getReview_count() {
        return review_count;
    }

    public void setReview_count(int review_count) {
        this.review_count = review_count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRating_img_url_small() {
        return rating_img_url_small;
    }

    public void setRating_img_url_small(String rating_img_url_small) {
        this.rating_img_url_small = rating_img_url_small;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<List<String>> getCategories() {
        return categories;
    }

    public void setCategories(List<List<String>> categories) {
        this.categories = categories;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSnippet_text() {
        return snippet_text;
    }

    public void setSnippet_text(String snippet_text) {
        this.snippet_text = snippet_text;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getDisplay_phone() {
        return display_phone;
    }

    public void setDisplay_phone(String display_phone) {
        this.display_phone = display_phone;
    }

    public String getRating_img_url_large() {
        return rating_img_url_large;
    }

    public void setRating_img_url_large(String rating_img_url_large) {
        this.rating_img_url_large = rating_img_url_large;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean is_closed() {
        return is_closed;
    }

    public void setIs_closed(boolean is_closed) {
        this.is_closed = is_closed;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

}
