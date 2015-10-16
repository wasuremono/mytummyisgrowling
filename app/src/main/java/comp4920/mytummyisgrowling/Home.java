package comp4920.mytummyisgrowling;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;

public class Home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    public void login (View view) {
        Intent intent = new Intent(this, Search.class);
        startActivity(intent);
    }

    public void register (View view) {
        Intent intent = new Intent(this, Register.class);
        startActivity(intent);
    }
}
