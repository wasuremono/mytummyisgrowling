package comp4920.mytummyisgrowling;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import comp4920.mytummyisgrowling.yelp.YelpAPI;


import comp4920.mytummyisgrowling.searchResultObjects.Business;
import comp4920.mytummyisgrowling.searchResultObjects.Center;
import comp4920.mytummyisgrowling.searchResultObjects.Coordinate;
import comp4920.mytummyisgrowling.searchResultObjects.Location;
import comp4920.mytummyisgrowling.searchResultObjects.Region;
import comp4920.mytummyisgrowling.searchResultObjects.SearchResponse;
import comp4920.mytummyisgrowling.searchResultObjects.Span;

import com.google.gson.Gson;

public class DisplayMessageActivity extends AppCompatActivity {

    private String searchResult;
    private StringBuffer concatResult;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get the message from the intent
        Intent intent = getIntent();
        final String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        setTitle("Results for " + message);


        final TextView textView = new TextView(this);
        textView.setMovementMethod(new ScrollingMovementMethod());
        textView.setTextSize(20);



        new Thread(new Runnable() {
            public void run() {
                String resultBody;
                String searchCuisine = message;

                YelpAPI yelpApi = new YelpAPI();
                resultBody = yelpApi.searchForBusinessesByLocation(searchCuisine, "Sydney, Australia");

                System.out.println("\033[0;1m" + "Result Body");
                System.out.println(resultBody);

                searchResult = resultBody;
                Gson gson = new Gson();
                SearchResponse response = gson.fromJson(resultBody, SearchResponse.class);

                concatResult = new StringBuffer("GSON RESULTS\n");
                concatResult.append("JSON\n");
                concatResult.append("Region\n");
                concatResult.append("Span: \n" + response.getRegion().getSpan().getLatitude_delta() + "\n");
                concatResult.append("Span: \n" + response.getRegion().getSpan().getLongitude_delta() + "\n");
                concatResult.append("Center: \n" + response.getRegion().getCenter().getLatitude() + "\n");
                concatResult.append("Center: \n" + response.getRegion().getCenter().getLongitude() + "\n");

                concatResult.append("Total: \n" + response.getTotal() + "\n");

                //for(Business business : response.getBusinesses()) {
                for(int i = 0; i < response.getBusinesses().size(); i++) {
                    Business business = response.getBusinesses().get(i);
                    concatResult.append("==============================" + "\n");
                    concatResult.append(" =====Business "  + i + " ====" + "\n");
                    concatResult.append("is_claimed: \n" + business.is_claimed() + "\n");
                    concatResult.append("Rating: \n" + business.getRating() + "\n");
                    concatResult.append("Mobile URL: \n" + business.getMobile_url() + "\n");
                    concatResult.append("Rating IMG URL: \n" + business.getRating_img_url() + "\n");
                    concatResult.append("Review Count: \n" + business.getReview_count() + "\n");
                    concatResult.append("Name: \n" + business.getName() + "\n");
                    concatResult.append("Rating IMG URL Small: \n" + business.getRating_img_url_small() + "\n");
                    concatResult.append("URL: \n" + business.getUrl() + "\n");
                    concatResult.append("Snippet Text: \n" + business.getSnippet_text() + "\n");
                    concatResult.append("Phone: \n" + business.getPhone() + "\n");

                    concatResult.append("=====Location ====" + "\n");
                    concatResult.append("City: \n" + business.getLocation().getCity() + "\n");
                    concatResult.append("Display Address: " + "\n");
                    for (String dispAdd : business.getLocation().getDisplay_address()) {
                        concatResult.append(dispAdd + "\n");
                    }
                    concatResult.append("Geo Accuracy: \n" + business.getLocation().getGeo_accuracy() + "\n");
                    //concatResult.append("Neighbourhoods: ");
                    //for (String n : business.getLocation().getNeighbourhods()) {
                    //     concatResult.append(n);
                    //}
                    concatResult.append("Postal Code: \n" + business.getLocation().getPostal_code() + "\n");
                    concatResult.append("Country Code: \n" + business.getLocation().getCountry_code() + "\n");
                    concatResult.append("Address: " + "\n");
                    for (String address : business.getLocation().getAddress()) {
                        concatResult.append(address + "\n");
                    }

                    //concatResult.append("===== Coordinate ====" + "\n");
                    //concatResult.append("Latitude: " + business.getLocation().getCoordinate().getLatitude() + "\n");
                    //concatResult.append("Longitude: " + business.getLocation().getCoordinate().getLongitude() + "\n");

                    concatResult.append("===== Other ====" + "\n");
                    concatResult.append("Display Phone: \n" + business.getDisplay_phone() + "\n");
                    concatResult.append("Rating IMG URL Large: \n" + business.getRating_img_url_large() + "\n");
                    concatResult.append("is_closed: \n" + business.is_closed() + "\n");


                }




//                System.out.println("Region");
//                System.out.println("Span: " + response.getRegion().getSpan().getLatitude_delta());
//                System.out.println("Span: " + response.getRegion().getSpan().getLongitude_delta());
//                System.out.println("Center: " + response.getRegion().getCenter().getLatitude());
//                System.out.println("Center: " + response.getRegion().getCenter().getLongitude());
//
//                System.out.println("Total: " + response.getTotal());
//
//                for(Business business : response.getBusinesses()) {
//                    System.out.println("==============================");
//
//                    System.out.println(" =====Business Info ====");
//                    System.out.println("is_claimed: " + business.is_claimed());
//                    System.out.println("Rating: " + business.getRating());
//                    System.out.println("Mobile URL: " + business.getMobile_url());
//                    System.out.println("Rating IMG URL: " + business.getRating_img_url());
//                    System.out.println("Review Count: " + business.getReview_count());
//                    System.out.println("Name: " + business.getName());
//                    System.out.println("Rating IMG URL Small: " + business.getRating_img_url_small());
//                    System.out.println("URL: " + business.getUrl());
//                    System.out.println("Snippet Text: " + business.getSnippet_text());
//                    System.out.println("Phone: " + business.getPhone());
//
//                    System.out.println("=====Location ====");
//                    System.out.println("City: " + business.getLocation().getCity());
//                    System.out.println("Display Address: ");
//                    for (String dispAdd : business.getLocation().getDisplay_address()) {
//                        System.out.println(dispAdd);
//                    }
//                    System.out.println("Geo Accuracy: " + business.getLocation().getGeo_accuracy());
//                    //System.out.println("Neighbourhoods: ");
//                    //for (String n : business.getLocation().getNeighbourhods()) {
//                    //     System.out.println(n);
//                    //}
//                    System.out.println("Postal Code: " + business.getLocation().getPostal_code());
//                    System.out.println("Country Code: " + business.getLocation().getCountry_code());
//                    System.out.println("Address: ");
//                    for(String address : business.getLocation().getAddress()) {
//                        System.out.println(address);
//                    }
//
//                    //System.out.println("===== Coordinate ====");
//                    //System.out.println("Latitude: " + business.getLocation().getCoordinate().getLatitude());
//                    //System.out.println("Longitude: " + business.getLocation().getCoordinate().getLongitude());
//
//
//                    System.out.println("===== Other ====");
//                    System.out.println("Display Phone: " + business.getDisplay_phone());
//                    System.out.println("Rating IMG URL Large: " + business.getRating_img_url_large());
//                    System.out.println("is_closed: " + business.is_closed());
//                }



                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //textView.setText(searchResult);
                        textView.setText(concatResult);
                        setContentView(textView);
                    }
                });


            }
        }).start();

    }

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_display_message, menu);
        return true;
    }
    */

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
