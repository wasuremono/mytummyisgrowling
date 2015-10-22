package comp4920.mytummyisgrowling;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class Search extends AppCompatActivity {
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        session = new SessionManager(getApplicationContext());
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
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, AccountSettingsActivity.class);
            startActivity(intent);
        } else if (id == R.id.action_logout) {
            session.doLogout();
        }
        return super.onOptionsItemSelected(item);
    }

    /*@Override
    public void onBackPressed() {
        finish();
        System.exit(0);
    }*/

    public void searchByPreference (View view) {
        Intent intent = new Intent(this, EditPreferencesActivity.class);
        startActivity(intent);
    }

    public void chooseMyOwn (View view) {
        Intent intent = new Intent(this, ChooseLocation.class);
        intent.putExtra(MainActivity.CHOOSE_OWN_SEARCH, true);
        startActivity(intent);
    }

}
