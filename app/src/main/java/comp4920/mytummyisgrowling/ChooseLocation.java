package comp4920.mytummyisgrowling;

import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class ChooseLocation extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    public final static String SEARCH_STRINGS = "comp4920.mytummyisgrowling.SEARCH_STRINGS";

    private static final String LOG_TAG = "ExampleApp";
    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";

    //------------ make your specific key ------------
    private static final String API_KEY = "AIzaSyDV2fxpLkj0lBsManAMagOu1iShV0PIYRk";


    private GoogleApiClient mGoogleApiClient;
    public static final String TAG = MapsActivity.class.getSimpleName();
    private Location currentLocation;
    SessionManager session;

    private String myLat;
    private String myLong;

    final ArrayList<List<Preference>> memberPreferences = new ArrayList<>();
    private ArrayList<String> searchStrings;


    private HashMap<String, Integer> cuisineScores = new HashMap<>();
    private int maxCuisines = 3;
    private int maxRank = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        session = new SessionManager(getApplicationContext());

        AutoCompleteTextView autoCompView = (AutoCompleteTextView) findViewById(R.id.mainAutoComplete);
        autoCompView.setAdapter(new GooglePlacesAutocompleteAdapter(this, R.layout.list_item));
        autoCompView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String str = (String) parent.getItemAtPosition(position);
                //Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
                System.out.println("You have selected: " + str);

                // Get the lat and long by geocoding the String address.
                LatLng selectedLatLng = determineLatLngFromAddress(getApplicationContext(), str);
                System.out.println("Selected Lat: " + selectedLatLng.latitude);
                System.out.println("Selected Long: " + selectedLatLng.longitude);
                Toast.makeText(getApplicationContext(), selectedLatLng.latitude + "\n" + selectedLatLng.longitude, Toast.LENGTH_SHORT).show();
                // Set the lat and loc
                myLat = Double.toString(selectedLatLng.latitude);
                myLong = Double.toString(selectedLatLng.longitude);
            }
        });

        searchStrings = new ArrayList<>();
        Intent intent = getIntent();
        if(!intent.hasExtra(MainActivity.CHOOSE_OWN_SEARCH)){
            EditText editText = (EditText) findViewById(R.id.mainCuisine);
            TextView textView = (TextView) findViewById(R.id.mainCuisineTextView);
            RelativeLayout relLayout = (RelativeLayout)editText.getParent();
            relLayout.removeView(editText);
            relLayout.removeView(textView);
            if(intent.hasExtra(MainActivity.PREFERENCE_SEARCH)){
                //GET PREFERENCES FOR CURRENT USER
                Gson gson = new Gson();
                List<Preference> list = gson.fromJson(session.getUserPrefs(), new TypeToken<List<Preference>>() {
                }.getType());
                for(Preference p: list){
                    searchStrings.add(p.getCuisine());
                }
            } else {
                if(intent.hasExtra(MainActivity.GROUP_PREFERENCE_SEARCH)){
                    //GET PREFERENCES OF ALL GROUP MEMBERS
                    //RUN PREFERENCE ALGORITHM
                    if(intent.hasExtra(GroupDetails.GROUP_MEMBER_IDS)){
                        ArrayList<Integer> groupMemberIds = intent.getIntegerArrayListExtra(GroupDetails.GROUP_MEMBER_IDS);

                        for (final int memberId : groupMemberIds) {
                            RequestQueue queue = Volley.newRequestQueue(this);
                            StringRequest strReq = new StringRequest(Request.Method.POST,
                                    AppConfig.URL_CREATE_GROUP, new Response.Listener<String>() {

                                @Override
                                public void onResponse(String s) {
                                    Gson gson = new Gson();
                                    System.out.println("Response string: " + s);
                                   /* GroupMember response = gson.fromJson(s, GroupMember.class);*/
                                    List<Preference> response = gson.fromJson(s, new TypeToken<List<Preference>>() {
                                    }.getType());

                                    if (response != null) {
                                        System.out.println("Response preferences size: " + response.size());
                                        memberPreferences.add(response);
                                    }
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    System.out.println("That didn't work!");
                                }
                            }) {
                                @Override
                                protected Map<String, String> getParams() {
                                    Map<String, String> params = new HashMap<String, String>();
                                    params.put("userId", String.valueOf(memberId));
                                    params.put("activity", "getPrefs");

                                    return params;
                                }


                            };
                            queue.add(strReq);
                        }
                        System.out.println("MemberPreferences size: " + memberPreferences.size());

                    }
                }
            }

        }

        Button mainCurrLocButton = (Button) findViewById(R.id.mainCurrLocButton);
        mainCurrLocButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // On clicking curr location button, get the user's current location.
                // Send the user to the Results List Activity
                // And put the current lat and long on the intent.

                Intent prevIntent = getIntent();
                if(prevIntent.hasExtra(MainActivity.CHOOSE_OWN_SEARCH)){
                    EditText editText = (EditText) findViewById(R.id.mainCuisine);
                    String searchTerm = editText.getText().toString();

                    // Check if the user has entered a cuisine into the bar
                    if(TextUtils.isEmpty(searchTerm)){
                        editText.setError("Can't have an empty search.");
                        return;
                    } else {
                        searchStrings.add(searchTerm);
                    }
                } else if(prevIntent.hasExtra(MainActivity.GROUP_PREFERENCE_SEARCH)){
                   addTopPreferences();
                }
                Intent nextIntent = new Intent(getChooseLocation(), ResultListActivity.class);

                nextIntent.putExtra(SEARCH_STRINGS, searchStrings);

                String currLatitude = String.valueOf(currentLocation.getLatitude());
                String currLongitude = String.valueOf(currentLocation.getLongitude());
                String currLatLong = currLatitude + "," + currLongitude;
                nextIntent.putExtra("currLatLong", currLatLong);

                myLat = currLatitude;
                myLong = currLongitude;

                nextIntent.putExtra("mainMyLat", myLat);
                nextIntent.putExtra("mainMyLong", myLong);

                startActivity(nextIntent);

            }
        });


        Button mainOwnLocButton = (Button) findViewById(R.id.mainOwnLocButton);
        mainOwnLocButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // On clicking curr location button, get the user's current location.
                // Send the user to the Results List Activity
                // And put the current lat and long on the intent.
                Intent prevIntent = getIntent();
                if(prevIntent.hasExtra(MainActivity.CHOOSE_OWN_SEARCH)){
                    EditText editText = (EditText) findViewById(R.id.mainCuisine);
                    String searchTerm = editText.getText().toString().trim();

                    // Check if the user has entered a cuisine into the bar
                    if(TextUtils.isEmpty(searchTerm)){
                        editText.setError("Can't have an empty search.");
                        return;
                    } else {
                        searchStrings.add(searchTerm);
                    }
                } else if(prevIntent.hasExtra(MainActivity.GROUP_PREFERENCE_SEARCH)){
                    addTopPreferences();
                }

                Intent nextIntent = new Intent(getChooseLocation(), ResultListActivity.class);

                nextIntent.putExtra(SEARCH_STRINGS, searchStrings);

                String currLatitude = String.valueOf(currentLocation.getLatitude());
                String currLongitude = String.valueOf(currentLocation.getLongitude());
                String currLatLong = currLatitude + "," + currLongitude;

                nextIntent.putExtra("currLatLong", currLatLong);

                nextIntent.putExtra("mainMyLat", myLat);
                nextIntent.putExtra("mainMyLong", myLong);

                AutoCompleteTextView autoCompView = (AutoCompleteTextView) findViewById(R.id.mainAutoComplete);
                String autoCompMessage = autoCompView.getText().toString();
                // Check if the user has entered a location.
                if (TextUtils.isEmpty(autoCompMessage)) {
                    autoCompView.setError("Can't have an empty location.");
                    return;
                }

                // Check if the user has entered a valid address
                //if(!isValidAddress(getApplicationContext(), autoCompMessage)) {
                //    autoCompView.setError("Enter a valid address.");
                //    return;
                //}

                startActivity(nextIntent);
            }
        });

    }

    private void addTopPreferences(){
        for(List<Preference> pList : memberPreferences){
            for(Preference p : pList){
                String cuisine = p.getCuisine();
                if(!cuisineScores.containsKey(cuisine)){
                    cuisineScores.put(cuisine, 0);
                }
                int currentScore = cuisineScores.get(cuisine);
                int newScore = currentScore + maxRank - p.getRank() + 1;
                cuisineScores.put(cuisine, newScore);
            }
        }

        //Get top Results
        if(cuisineScores.size() > maxCuisines) {
            for(int i=0; i < maxCuisines; i++) {
                Map.Entry<String, Integer> maxEntry = getMaxPreferenceEntry();
                searchStrings.add(maxEntry.getKey());
                cuisineScores.remove(maxEntry.getKey());
            }
        } else {
            for(String cuisine : cuisineScores.keySet()){
                searchStrings.add(cuisine);
                Log.d ("MATT", "Added " + cuisine);
            }
        }
    }

    private Map.Entry<String, Integer> getMaxPreferenceEntry(){
        Map.Entry<String, Integer> maxEntry = null;
        for(Map.Entry<String,Integer> entry: cuisineScores.entrySet()){
            if(maxEntry == null || entry.getValue() > maxEntry.getValue()){
                maxEntry = entry;
            }
        }
        return maxEntry;
    }

    public LatLng determineLatLngFromAddress(Context appContext, String strAddress) {
        LatLng latLng = null;
        Geocoder geocoder = new Geocoder(appContext, Locale.getDefault());
        List<Address> geoResults = null;

        try {
            geoResults = geocoder.getFromLocationName(strAddress, 1);
            while (geoResults.size() == 0) {
                geoResults = geocoder.getFromLocationName(strAddress, 1);
            }
            if (geoResults.size() > 0) {
                Address addr = geoResults.get(0);
                latLng = new LatLng(addr.getLatitude(), addr.getLongitude());
            }
        } catch (Exception e) {
            System.out.print(e.getMessage());
        }
        return latLng; //LatLng value of address
    }

    public boolean isValidAddress(Context appContext, String strAddress) {
        boolean result = false;
        LatLng latLng = null;
        Geocoder geocoder = new Geocoder(appContext, Locale.getDefault());
        List<Address> geoResults = null;

        try {
            geoResults = geocoder.getFromLocationName(strAddress, 1);
            while (geoResults.size() == 0) {
                geoResults = geocoder.getFromLocationName(strAddress, 1);
            }
            if (geoResults.size() > 0) {
                //  Address addr = geoResults.get(0);
                // latLng = new LatLng(addr.getLatitude(),addr.getLongitude());
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            System.out.print(e.getMessage());
        }
        return result;
    }


    //public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
    //    String str = (String) adapterView.getItemAtPosition(position);
    //    Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    //}

    public static ArrayList<String> autocomplete(String input) {
        ArrayList<String> resultList = null;

        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
            sb.append("?key=" + API_KEY);
            sb.append("&components=country:au");
            sb.append("&input=" + URLEncoder.encode(input, "utf8"));

            URL url = new URL(sb.toString());

            System.out.println("URL: " + url);
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error processing Places API URL", e);
            return resultList;
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error connecting to Places API", e);
            return resultList;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        try {

            // Create a JSON object hierarchy from the results
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");
            // Extract the Place descriptions from the results
            resultList = new ArrayList<String>(predsJsonArray.length());
            for (int i = 0; i < predsJsonArray.length(); i++) {

                System.out.println(predsJsonArray.getJSONObject(i).getString("description"));
                System.out.println(predsJsonArray.getJSONObject(i).getString("place_id"));

                System.out.println("============================================================");

                resultList.add(predsJsonArray.getJSONObject(i).getString("description"));


            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Cannot process JSON results", e);
        }

        return resultList;
    }

    class GooglePlacesAutocompleteAdapter extends ArrayAdapter<String> implements Filterable {
        private ArrayList<String> resultList;

        public GooglePlacesAutocompleteAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        @Override
        public int getCount() {
            return resultList.size();
        }

        @Override
        public String getItem(int index) {
            return resultList.get(index);
        }

        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new FilterResults();
                    if (constraint != null) {
                        // Retrieve the autocomplete results.
                        resultList = autocomplete(constraint.toString());

                        // Assign the data to the FilterResults
                        filterResults.values = resultList;
                        filterResults.count = resultList.size();
                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    if (results != null && results.count > 0) {
                        notifyDataSetChanged();
                    } else {
                        notifyDataSetInvalidated();
                    }
                }
            };
            return filter;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id == R.id.action_home){
            Intent intent = new Intent(this, Search.class);
            startActivity(intent);
        } else if (id == R.id.action_settings) {
            Intent intent = new Intent(this, AccountSettingsActivity.class);
            startActivity(intent);
        } else if (id == R.id.action_logout) {
            session.doLogout();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Called when the user clicks the Send button
     */
    /*public void sendMessage(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, ResultListActivity.class);
        EditText editText = (EditText) findViewById(R.id.mainCuisine);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);

        String currLatitude = String.valueOf(currentLocation.getLatitude());
        String currLongitude = String.valueOf(currentLocation.getLongitude());
        String currLatLong = currLatitude + "," + currLongitude;

        intent.putExtra("currLatLong", currLatLong);

        //startActivity(intent);
    }*/

    @Override
    public void onConnected(Bundle bundle) {
        Log.i(TAG, "Location services connected.");
        currentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Location services suspended. Please reconnect.");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        // setUpMapIfNeeded();
        mGoogleApiClient.connect();
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    public ChooseLocation getChooseLocation() {
        return ChooseLocation.this;
    }

}