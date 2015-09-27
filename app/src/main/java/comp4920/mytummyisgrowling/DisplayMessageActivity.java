package comp4920.mytummyisgrowling;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import comp4920.mytummyisgrowling.yelp.YelpAPI;

public class DisplayMessageActivity extends AppCompatActivity {

    private String searchResult;

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

                System.out.println(resultBody);

                searchResult = resultBody;

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textView.setText(searchResult);
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
