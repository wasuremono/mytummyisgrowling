package comp4920.mytummyisgrowling;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import comp4920.mytummyisgrowling.yelp.YelpAPI;


import comp4920.mytummyisgrowling.searchResultObjects.Business;
import comp4920.mytummyisgrowling.searchResultObjects.Center;
import comp4920.mytummyisgrowling.searchResultObjects.Coordinate;
import comp4920.mytummyisgrowling.searchResultObjects.Location;
import comp4920.mytummyisgrowling.searchResultObjects.Region;
import comp4920.mytummyisgrowling.searchResultObjects.SearchResponse;
import comp4920.mytummyisgrowling.searchResultObjects.Span;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.transform.Result;

public class ResultListActivity extends AppCompatActivity {

    private String searchResult;
    private StringBuffer concatResult;
    private ArrayList<String> nameValues;

    private ArrayList<Business> businessList;
    private ArrayList<Business> finalBusinessList;

    private ResultListAdapter resultListAdapter;
    private static LatLng NEWARK;
    private UiSettings mUiSettings;
    private CameraPosition POSITION;
    private int finalHeight;
    private int finalWidth;
    private String currLatLong;

    private ImageView detailsListStaticMapImageView;
    private ProgressDialog dialog;

    private ArrayList<String> searchStrings;
    // List to test that listActivity is working
    // with intended List of String cuisine inputs
    private ArrayList<String> testStringList = new ArrayList<String>();
    // List of ArrayList of Businesses
    private ArrayList<ArrayList<Business>> testListofBusinesses = new ArrayList<ArrayList<Business>>();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_list);
        dialog = ProgressDialog.show(this, "", "Searching...", true);

        // Add cuisines to the testStringList
        testStringList.add("Thai");
        testStringList.add("Korean");
        testStringList.add("kebab");
        testStringList.add("African");

        // Get the message from the intent
        Intent intent = getIntent();
       /* final String message = intent.getStringExtra(ChooseLocation.SEARCH_STRINGS);*/
        if(intent.hasExtra(ChooseLocation.SEARCH_STRINGS)){
            searchStrings = intent.getStringArrayListExtra(ChooseLocation.SEARCH_STRINGS);
        } else {
            searchStrings = testStringList;
        }

        final String latLong = intent.getStringExtra("currLatLong");

        final String myLat = intent.getStringExtra("mainMyLat");
        final String myLong = intent.getStringExtra("mainMyLong");
        final String myLatLong = myLat + "," + myLong;

        final GoogleMap mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                .getMap();

        currLatLong = latLong;
        String currLatLongList[] = currLatLong.split("\\s*,\\s*");
        String currLat = currLatLongList[0];
        String currLong = currLatLongList[1];
        NEWARK = new LatLng(Double.parseDouble(myLat), Double.parseDouble(myLong));
        POSITION =
                new CameraPosition.Builder().target(NEWARK)
                        .zoom(12)
                        .build();
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(POSITION));
        setTitle("Results");

        nameValues = new ArrayList<String>();
        businessList = new ArrayList<Business>();
        finalBusinessList = new ArrayList<Business>();

        resultListAdapter = new ResultListAdapter(finalBusinessList);

        new Thread(new Runnable() {

            public void run() {

                // Get size of searchStrings
                int listSize = searchStrings.size();
                System.out.println("Size of searchStrings is: " + listSize);

                if(listSize == 1) {
                    String resultBody;
                    String searchCuisine = searchStrings.get(0);
                    Gson gson = new Gson();

                    YelpAPI yelpApi = new YelpAPI();
                    //  resultBody = yelpApi.searchForBusinessesByLocation(searchCuisine, "Sydney, Australia");
                    System.out.println("Search cuisine: " + searchCuisine);
                    resultBody = yelpApi.searchForBusinessesByLatLong(searchCuisine, myLatLong);

                    searchResult = resultBody;
                    SearchResponse response = gson.fromJson(resultBody, SearchResponse.class);
                    for (Business business : response.getBusinesses()) {
                        finalBusinessList.add(business);
                    }

                } else {

                    // For each cuisine String in the list...
                    for (String cuisine : searchStrings) {
                        System.out.println("Cuisine: " + cuisine);
                        // Get busList for each cuisine by Yelp search.
                        ArrayList<Business> busList = new ArrayList<Business>();
                        // Perform Yelp search for the cuisine
                        String resultBody;
                        String searchCuisine = cuisine;
                        Gson gson = new Gson();
                        List<String> categories = Arrays.asList(searchCuisine.split(","));
                        YelpAPI yelpApi = new YelpAPI();
                        //  resultBody = yelpApi.searchForBusinessesByLocation(searchCuisine, "Sydney, Australia");
                        int limit = 30 / categories.size();
                        for (String category : categories) {
                            int thisCat = 0;
                            //resultBody = yelpApi.searchForBusinessesByLatLong(category, myLatLong);
                            resultBody = yelpApi.searchForBusinessesSetLimit(category, myLatLong, "3");
                            searchResult = resultBody;
                            SearchResponse response = gson.fromJson(resultBody, SearchResponse.class);
                            for (Business business : response.getBusinesses()) {
                                boolean hasCategory = false;
                                if (business.getCategories() != null) {
                                    for (List l : business.getCategories()) {
                                        if (l.contains(category)) hasCategory = true;
                                    }
                                    if (hasCategory && (thisCat++ <= limit)) {
                                        //  nameValues.add(business.getName());
                                        System.out.println("thisCat: " + thisCat);
                                        busList.add(business);
                                        //  businessList.add(business);
                                    }
                                }
                            }
                            // Add each cuisines businessList to the ArrayList<Business>
                            testListofBusinesses.add(busList);
                        }
                    }

//                for(int i=0; i < testStringList.size(); i++) {
//                    ArrayList<Business> currList = testListofBusinesses.get(i);
//                    System.out.println("Size of currList is: " + currList.size());
//                }

                    // Alternate through the lists and add the businesses to
                    // finalBusinessList for displaying on the screen.
                    for (int j = 0; j < 3; j++) {
                        for (int i = 0; i < searchStrings.size(); i++) {
                            ArrayList<Business> currList = testListofBusinesses.get(i);
                            if(currList.size() > 0) {
                                finalBusinessList.add(currList.get(j));
                            }
                        }
                    }
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //ArrayAdapter adapter = new ArrayAdapter(getResultListActivity(), android.R.layout.simple_list_item_1, nameValues);
                        // testing git commitsdfadsfasdfsdfs test
                        mMap.setMyLocationEnabled(false);
                        mUiSettings = mMap.getUiSettings();
                        mUiSettings.setZoomControlsEnabled(true);
                        mUiSettings.setCompassEnabled(true);
                        mUiSettings.setMyLocationButtonEnabled(true);
                        mUiSettings.setScrollGesturesEnabled(true);
                        mUiSettings.setZoomGesturesEnabled(true);
                        mUiSettings.setTiltGesturesEnabled(false);
                        mUiSettings.setRotateGesturesEnabled(true);

                        Marker currLocation = mMap.addMarker(new MarkerOptions().position(NEWARK)
                                        .title("Current Location!")
                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                                        .snippet("This is where you are.")
                        );
                        for (int i = 0; i < finalBusinessList.size(); i++) {
                            Business currBus = finalBusinessList.get(i);
                            String currBusName = currBus.getName();
                            LatLng currBusLatLng = new LatLng(currBus.getLocation().getCoordinate().getLatitude(),
                                    currBus.getLocation().getCoordinate().getLongitude());
                            String indexString = Integer.toString(i).trim();
                            System.out.println("indexString is: " + indexString);
                            Marker businessMarker = mMap.addMarker(new MarkerOptions().position(currBusLatLng).title(currBusName).snippet(indexString));
                        }

                        ListView listView = (ListView) findViewById(R.id.resultListView);
                        listView.setAdapter(resultListAdapter);
                        dialog.dismiss();
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                System.out.println("YOU CLICKED " + resultListAdapter.businessList.get(position).getName());
                                Business clickedBusiness = (resultListAdapter.businessList.get(position));
                                //    Business clickedBusiness = businessList.get(position);
                                Intent sendIntent = new Intent(getResultListActivity(), ResultDetailsActivity.class);
                                sendIntent.putExtra("sentIntent", clickedBusiness);
                                sendIntent.putExtra("sentCurrLatLong", currLatLong);
                                startActivity(sendIntent);


//                                Intent clickedIntent = new Intent(getResultListActivity(), ResultDetailsActivity.class);
//                                clickedIntent.putExtra("clickedIntent", clickedBusiness);
//                                startActivity(clickedIntent);

                            }
                        });

                        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                            @Override
                            public void onMapClick(LatLng latLng) {
                                String currLatLong = latLong;
                                String currLatLongList[] = currLatLong.split("\\s*,\\s*");
                                String currLat = currLatLongList[0];
                                String currLong = currLatLongList[1];

                                Intent sendBusinessList = new Intent(getResultListActivity(), MapsFromListActivity.class);
                                sendBusinessList.putExtra("sentLatBusinessList", currLat);
                                sendBusinessList.putExtra("sentLongBusinessList", currLong);
                                sendBusinessList.putExtra("sentBusinessList", finalBusinessList);
                                startActivity(sendBusinessList);
                            }
                        });

                    }
                });




            }

        }).start();






    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_result_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_Sort) {

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public ResultListActivity getResultListActivity() {
        return ResultListActivity.this;
    }




    public class ResultListAdapter extends BaseAdapter {

        List<Business> businessList;
        private LayoutInflater inflater = null;

        public ResultListAdapter(List<Business> businessList) {

            this.businessList = businessList;
        }

        @Override
        public int getCount() {
            return businessList.size();
        }

        @Override
        public Object getItem(int position) {
            return businessList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
           if(convertView == null) {
               inflater = (LayoutInflater) ResultListActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
               convertView = inflater.inflate(R.layout.row_restaurant, parent,false);
           }

            ImageView restaurantImage = (ImageView) convertView.findViewById(R.id.resultListImage);
            TextView restaurantName = (TextView) convertView.findViewById(R.id.resultListName);
            TextView restaurantAddress = (TextView) convertView.findViewById(R.id.resultListAddress);
            TextView restaurantRating = (TextView) convertView.findViewById(R.id.resultListRating);
            TextView restaurantCost = (TextView) convertView.findViewById(R.id.resultListCost);
            TextView restaurantOpen = (TextView) convertView.findViewById(R.id.resultListOpen);

            Business business = businessList.get(position);


            String imageURL = business.getImage_url();

            Picasso.with(getResultListActivity()).load(imageURL).into(restaurantImage);
            restaurantName.setText(business.getName());

            StringBuffer addressString = new StringBuffer("");
            for(String string : business.getLocation().getDisplay_address()) {
                addressString.append(string + "\n");
                System.out.println(string);
            }

            restaurantAddress.setText(addressString);
            restaurantRating.setText(String.valueOf(business.getRating()));
            restaurantCost.setText("COST");
            restaurantOpen.setText("OPEN");

            return convertView;
        }
    }



}
