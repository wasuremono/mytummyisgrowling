package comp4920.mytummyisgrowling;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import comp4920.mytummyisgrowling.searchResultObjects.Business;

public class ResultDetailsActivity extends AppCompatActivity {


    private ImageButton detailsStaticMap;

    private ImageView detailsStaticMapImageView;

    private int mapHeight;
    private int mapWidth;

    private int finalHeight;
    private int finalWidth;

    final private double staticLatitude = 0;
    final private double staticLongitude = 0;

    private String placeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_details);

        setTitle("Details");

        Intent receivedIntent = getIntent();


        Business receivedBusiness = (Business) receivedIntent.getSerializableExtra("sentIntent");
        System.out.println("got the intent business");

        placeName = receivedBusiness.getName();

        ImageView detailsImage = (ImageView) this.findViewById(R.id.resultDetailsImage);
        TextView detailsName = (TextView) this.findViewById(R.id.resultDetailsName);
        TextView detailsLocation = (TextView) this.findViewById(R.id.resultDetailsLocation);
        TextView detailsRating = (TextView) this.findViewById(R.id.resultDetailsRating);
        TextView detailsPhone = (TextView) this.findViewById(R.id.resultDetailsPhoneNum);

        TextView detailsAddress = (TextView) this.findViewById(R.id.resultDetailsAddress);
        TextView detailsOther = (TextView) this.findViewById(R.id.resultDetailsOther);

        String imageURL = receivedBusiness.getImage_url();
        Picasso.with(this).load(imageURL).into(detailsImage);

        detailsName.setText(receivedBusiness.getName());
        detailsLocation.setText(receivedBusiness.getLocation().getCity() + ", " +
                                receivedBusiness.getLocation().getState_code());
        detailsRating.setText("Rating:  " + receivedBusiness.getRating());
        detailsPhone.setText("Phone:   " + receivedBusiness.getDisplay_phone());

        StringBuffer addressString = new StringBuffer("");
        for(String string : receivedBusiness.getLocation().getDisplay_address()) {
            addressString.append(string + "\n");
        }
        detailsAddress.setText(addressString);

        detailsOther.setText(receivedBusiness.getSnippet_text());

        System.out.println("The business ID is: " + receivedBusiness.getId());

        final double staticLatitude = receivedBusiness.getLocation().getCoordinate().getLatitude();
        final double staticLongitude = receivedBusiness.getLocation().getCoordinate().getLongitude();


        final ImageView iv=(ImageView)findViewById(R.id.resultsStaticMapButton);
//
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("ResultDetaislACtivity: ACTION DOWN ON MAP");

                String sendName = placeName;
                String sendLat = Double.toString(staticLatitude);
                String sendLong = Double.toString(staticLongitude);

                Intent latLongToMap = new Intent(getResultDetailsActivity(), MapsActivity.class);
                latLongToMap.putExtra("sentLat", sendLat);
                latLongToMap.putExtra("sentLong", sendLong);
                latLongToMap.putExtra("sentPlaceName", sendName);
                startActivity(latLongToMap);
            }
        });

        final ViewTreeObserver vto = iv.getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                finalHeight = iv.getMeasuredHeight();
                finalWidth = iv.getMeasuredWidth();
                // Log.e("hilength", "Height: " + finalHeight + " Width: " + finalWidth);


                //  detailsStaticMap = (ImageButton) this.findViewById(R.id.resultsStaticMapButton);
                detailsStaticMapImageView = (ImageView) getResultDetailsActivity().findViewById(R.id.resultsStaticMapButton);
                StringBuffer mapURLBuffer = new StringBuffer("https://maps.googleapis.com/maps/api/staticmap?center=");
                mapURLBuffer.append(staticLatitude + "," + staticLongitude);
                mapURLBuffer.append("&zoom=18");
                mapURLBuffer.append("&size=");

                mapURLBuffer.append(finalWidth + "x" + finalHeight);

                mapURLBuffer.append("&markers=color:blue%7C");
                mapURLBuffer.append(staticLatitude + "," + staticLongitude);

                //  Picasso.with(this).load(mapURL).into(detailsStaticMap);
                //  Picasso.with(this).load(mapURL).into(detailsStaticMapImageView);
                Picasso.with(getResultDetailsActivity()).load(mapURLBuffer.toString()).into(detailsStaticMapImageView);


               // System.out.println("StringBuffer URL IS: " + mapURLBuffer);

                return true;
            }
        });




    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus){
      //  mapWidth = detailsStaticMap.getWidth();
      //  mapHeight = detailsStaticMap.getHeight();
        mapWidth = detailsStaticMapImageView.getWidth();
        mapHeight = detailsStaticMapImageView.getHeight();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_result_details, menu);
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


    public ResultDetailsActivity getResultDetailsActivity() {
        return ResultDetailsActivity.this;
    }

}
