package comp4920.mytummyisgrowling.searchResultObjects;

import java.io.Serializable;

/**
 * Created by Andy Lee on 27/09/2015.
 */
public class Region implements Serializable {

    private Span span;
    private Center center;

    public Span getSpan() {
        return span;
    }

    public void setSpan(Span span) {
        this.span = span;
    }

    public Center getCenter() {
        return center;
    }

    public void setCenter(Center center) {
        this.center = center;
    }

}
