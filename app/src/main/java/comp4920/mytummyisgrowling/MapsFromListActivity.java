package comp4920.mytummyisgrowling;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import comp4920.mytummyisgrowling.searchResultObjects.Business;
import comp4920.mytummyisgrowling.yelp.YelpAPI;

public class MapsFromListActivity extends FragmentActivity {



    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    private static LatLng NEWARK;
    private UiSettings mUiSettings;
    private CameraPosition POSITION;

    private ArrayList<Business> myBusinessList;


    class MyInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
        private final View myContentsView;

        MyInfoWindowAdapter() {
            myContentsView = getLayoutInflater().inflate(R.layout.map_marker_window, null);
        }

        @Override
        public View getInfoContents(Marker marker) {

            int indexInt = 0;
            Business business = null;

            String snippet = marker.getSnippet();


            if(!snippet.equals("This is where you are.")) {
                indexInt = Integer.parseInt(marker.getSnippet());
                business = myBusinessList.get(indexInt);

                ImageView image = (ImageView) myContentsView.findViewById(R.id.mapWindowImage);
                String imageURL = business.getImage_url();
                Picasso.with(getMapsFromListActivity()).load(imageURL).into(image);


                TextView name = ((TextView)myContentsView.findViewById(R.id.mapWindowName));
                name.setText(business.getName());

                StringBuffer addressString = new StringBuffer("");
                for(String string : business.getLocation().getDisplay_address()) {
                    addressString.append(string + "\n");
                }
                TextView address = ((TextView)myContentsView.findViewById(R.id.mapWindowAddress));
                address.setText(addressString);

            } else {
                TextView name = ((TextView)myContentsView.findViewById(R.id.mapWindowName));
                name.setText("Your current location");

                TextView address = ((TextView)myContentsView.findViewById(R.id.mapWindowAddress));
                address.setText("You are currently here!");

            }




            return myContentsView;
        }

        @Override
        public View getInfoWindow(Marker marker) {
            return null;
        }




    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        Intent receivedIntent = getIntent();

        myBusinessList = (ArrayList<Business>) receivedIntent.getSerializableExtra("sentBusinessList");

        double latitude = Double.parseDouble(receivedIntent.getStringExtra("sentLatBusinessList"));
        double longitude = Double.parseDouble(receivedIntent.getStringExtra("sentLongBusinessList"));

        NEWARK = new LatLng(latitude, longitude);

        POSITION =
                new CameraPosition.Builder().target(NEWARK)
                        .zoom(12)
                        .build();

        System.out.println("MAP RECIEVED LAT: " + latitude);
        System.out.println("MAP RECIEVED LONG: " + longitude);

        setUpMapIfNeeded();





    }



    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.

        mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                .getMap();
        // Check if we were successful in obtaining the map.
        if (mMap != null) {
            setUpMap();
        }

    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {

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


        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(POSITION));


        Marker currLocation = mMap.addMarker(new MarkerOptions().position(NEWARK)
                        .title("Current Location!")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                        .snippet("This is where you are.")
        );


        mMap.setInfoWindowAdapter(new MyInfoWindowAdapter());

        for(int i = 0; i < myBusinessList.size(); i++) {
            Business currBus = myBusinessList.get(i);
            String currBusName = currBus.getName();
            LatLng currBusLatLng = new LatLng(currBus.getLocation().getCoordinate().getLatitude(),
                                              currBus.getLocation().getCoordinate().getLongitude());
            String indexString = Integer.toString(i).trim();
            System.out.println("indexString is: " + indexString);
            Marker businessMarker = mMap.addMarker(new MarkerOptions().position(currBusLatLng).title(currBusName).snippet(indexString));
        }


        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                if(!marker.getTitle().equals("Current Location!") ) {
                    int indexInt = 0;
                    Business business = null;
                    String snippet = marker.getSnippet();
                    indexInt = Integer.parseInt(marker.getSnippet());
                    business = myBusinessList.get(indexInt);

                    Intent sendIntent = new Intent(getMapsFromListActivity(), ResultDetailsActivity.class);
                    sendIntent.putExtra("sentIntent", business);
                    startActivity(sendIntent);

                } else {

                }
            }
        });



    }

    public MapsFromListActivity getMapsFromListActivity() {
        return MapsFromListActivity.this;
    }



}
