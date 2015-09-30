package comp4920.mytummyisgrowling;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import comp4920.mytummyisgrowling.searchResultObjects.Business;

public class ResultDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_details);

        setTitle("Details");

        Intent receivedIntent = getIntent();


        Business receivedBusiness = (Business) receivedIntent.getSerializableExtra("sentIntent");
        System.out.println("got the intent business");

        ImageView detailsImage = (ImageView) this.findViewById(R.id.resultDetailsImage);
        TextView detailsName = (TextView) this.findViewById(R.id.resultDetailsName);
        TextView detailsAddress = (TextView) this.findViewById(R.id.resultDetailsAddress);
        TextView detailsOther = (TextView) this.findViewById(R.id.resultDetailsOther);
        RatingBar detailsRatingBar = (RatingBar) this.findViewById(R.id.resultsRatingBar);

        String imageURL = receivedBusiness.getImage_url();

        Picasso.with(this).load(imageURL).into(detailsImage);
        detailsName.setText(receivedBusiness.getName());

        StringBuffer addressString = new StringBuffer("");
        for(String string : receivedBusiness.getLocation().getDisplay_address()) {
            addressString.append(string + "\n");
        }

        detailsAddress.setText(addressString);

        detailsOther.setText(receivedBusiness.getSnippet_text());

        detailsRatingBar.setNumStars(receivedBusiness.getRating());

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
}
