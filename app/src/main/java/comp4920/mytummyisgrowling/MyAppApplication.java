package comp4920.mytummyisgrowling;

import android.app.Application;

/**
 * Created by Andy Lee on 25/10/2015.
 */
public class MyAppApplication extends Application {

    private String resultListSortMode = "0";

    public String getResultListSortMode() {
        return resultListSortMode;
    }
    public void setResultListSortMode(String sm) {
        resultListSortMode = sm;
    }


}
