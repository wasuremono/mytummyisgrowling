package comp4920.mytummyisgrowling;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class Search extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
    }

    public void searchByPreference (View view) {
        Intent intent = new Intent(this, EditPreferencesActivity.class);
        startActivity(intent);
    }

    public void chooseMyOwn (View view) {
        Intent intent = new Intent(this, ChooseLocation.class);
        startActivity(intent);
    }

}
