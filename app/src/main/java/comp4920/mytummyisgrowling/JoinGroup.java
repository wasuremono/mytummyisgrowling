package comp4920.mytummyisgrowling;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class JoinGroup extends AppCompatActivity {
    private EditText groupIdString;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_group);

        groupIdString = (EditText) findViewById(R.id.join_group_enter_idString);
        password = (EditText) findViewById(R.id.join_group_enter_pass);
    }

    public void joinGroup(View view) {
        //TODO: grab group with groupIdString from database
        //TODO: check that password is correct
        //Use dummy data for now
        String dummyIdString = "foodTeam412";
        String dummyPass = "123456789";

        //if group id string exists in database
        TextView idStringHint = (TextView) findViewById(R.id.join_group_idString_hint);
        if(groupIdString.getText().toString().equals(dummyIdString)){
            idStringHint.setText("");
            //If password matches that in database
            TextView passHint = (TextView) findViewById(R.id.join_group_pass_hint);
            if (password.getText().toString().equals(dummyPass)){
                passHint.setText("");
                //TODO: add user to group in database
                Intent intent = new Intent(this, GroupSelect.class);
                startActivity(intent);
            } else {
                passHint.setText(R.string.join_group_pass_incorrect);
                passHint.setTextColor(0xFFFF0000);
            }
        } else {
            idStringHint.setText(R.string.join_group_idString_not_exist);
            idStringHint.setTextColor(0xFFFF0000);
        }

    }

}
