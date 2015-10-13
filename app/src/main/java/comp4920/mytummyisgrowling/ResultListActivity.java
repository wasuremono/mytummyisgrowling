package comp4920.mytummyisgrowling;

import android.content.Context;
import android.content.Intent;
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

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.Result;

public class ResultListActivity extends AppCompatActivity {

    private String searchResult;
    private StringBuffer concatResult;
    private ArrayList<String> nameValues;

    private ArrayList<Business> businessList;

    private ResultListAdapter resultListAdapter;

    private int finalHeight;
    private int finalWidth;

    private ImageView detailsListStaticMapImageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_list);



        // Get the message from the intent
        Intent intent = getIntent();
        final String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        final String latLong = intent.getStringExtra("currLatLong");

        setTitle("ListView for " + message);

        nameValues = new ArrayList<String>();
        businessList = new ArrayList<Business>();

        resultListAdapter = new ResultListAdapter(businessList);

        new Thread(new Runnable() {
            public void run() {
                String resultBody;
                String searchCuisine = message;

                YelpAPI yelpApi = new YelpAPI();
               //  resultBody = yelpApi.searchForBusinessesByLocation(searchCuisine, "Sydney, Australia");
                resultBody = yelpApi.searchForBusinessesByLatLong(searchCuisine, latLong);
                System.out.println("Result Body");
                System.out.println(resultBody);

                searchResult = resultBody;
                Gson gson = new Gson();
                SearchResponse response = gson.fromJson(resultBody, SearchResponse.class);



                for(Business business : response.getBusinesses()) {
                    nameValues.add(business.getName());
                    businessList.add(business);
                }

                for(String business : nameValues) {
                    System.out.println(business);
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //ArrayAdapter adapter = new ArrayAdapter(getResultListActivity(), android.R.layout.simple_list_item_1, nameValues);
                        ListView listView = (ListView) findViewById(R.id.resultListView);
                        listView.setAdapter(resultListAdapter);

                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                System.out.println("YOU CLICKED " + resultListAdapter.businessList.get(position).getName());
                                Business clickedBusiness = (resultListAdapter.businessList.get(position));
                            //    Business clickedBusiness = businessList.get(position);
                                Intent sendIntent = new Intent(getResultListActivity(), ResultDetailsActivity.class);
                                sendIntent.putExtra("sentIntent", clickedBusiness);
                                startActivity(sendIntent);


//                                Intent clickedIntent = new Intent(getResultListActivity(), ResultDetailsActivity.class);
//                                clickedIntent.putExtra("clickedIntent", clickedBusiness);
//                                startActivity(clickedIntent);

                            }
                        });

                        final ImageView iv = (ImageView)findViewById(R.id.resultListStaticMap);

                        iv.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                String currLatLong = latLong;
                                String currLatLongList[] = currLatLong.split("\\s*,\\s*");
                                String currLat = currLatLongList[0];
                                String currLong = currLatLongList[1];

                                Intent sendBusinessList = new Intent(getResultListActivity(), MapsFromListActivity.class);
                                sendBusinessList.putExtra("sentLatBusinessList", currLat);
                                sendBusinessList.putExtra("sentLongBusinessList", currLong);
                                sendBusinessList.putExtra("sentBusinessList", businessList);
                                startActivity(sendBusinessList);
                            }
                        });


                        final ViewTreeObserver vto = iv.getViewTreeObserver();
                        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                            public boolean onPreDraw() {
                                finalHeight = iv.getMeasuredHeight();
                                finalWidth = iv.getMeasuredWidth();

                             //   System.out.println("current final Height in listView is: " + finalHeight);
                             //   System.out.println("current final Wdith in listView is: " + finalWidth);

                               // String link = "http://lovemeow.com/wp-content/uploads/2013/05/gLcOq-Imgur.jpg";

                                detailsListStaticMapImageView = (ImageView) getResultListActivity().findViewById(R.id.resultListStaticMap);

                                StringBuffer mapURLBuffer = new StringBuffer("https://maps.googleapis.com/maps/api/staticmap?center=");
                                // Add user's last known location coordinates
                                // as centre of map.
                                mapURLBuffer.append(latLong);
                                mapURLBuffer.append("&zoom=13");
                                mapURLBuffer.append("&size=");
                                mapURLBuffer.append(finalWidth + "x" + finalHeight);

                                // %7C  =  |     (pipe)
                                // Start adding markers of restaurants.
                                mapURLBuffer.append("&markers=color:green%7C");
                                mapURLBuffer.append("label:A%7C");
                                mapURLBuffer.append(latLong);


                             //   String listMarkers = "";
                              //  mapURLBuffer.append("&markers=color:red");
                                for(int i = 0; i < businessList.size(); i++) {
                                    String markerString = "&markers=color:red%7Clabel:" + i + "%7C";
                                    mapURLBuffer.append(markerString);
                                    Business currBus = businessList.get(i);

                                    double latitude = currBus.getLocation().getCoordinate().getLatitude();
                                    double longitude = currBus.getLocation().getCoordinate().getLongitude();
                                    String busLatLong = latitude + "," + longitude;
                                    mapURLBuffer.append(busLatLong);
                                    // markerString = markerString + busLatLong;
                              //      System.out.println("markerString: " + markerString);
                                    //listMarkers = markerString;

                                }

                            //    mapURLBuffer.append(listMarkers);
                           //     System.out.println("mapURLBuffer: " + mapURLBuffer);

                                Picasso.with(getResultListActivity()).load(mapURLBuffer.toString()).into(detailsListStaticMapImageView);


                                return true;
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
        if (id == R.id.action_settings) {
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
