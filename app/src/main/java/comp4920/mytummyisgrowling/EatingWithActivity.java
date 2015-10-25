package comp4920.mytummyisgrowling;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class EatingWithActivity extends AppCompatActivity {
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eating_with);
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

    public void eatAlone (View view) {
        Intent intent = new Intent(this, ChooseLocation.class);
        intent.putExtra(MainActivity.PREFERENCE_SEARCH, true);
        startActivity(intent);
    }

    public void eatInGroup (View view) {
        Intent intent = new Intent(this, GroupSelect.class);
        startActivity(intent);
    }


}
