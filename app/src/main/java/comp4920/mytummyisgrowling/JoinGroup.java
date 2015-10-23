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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class JoinGroup extends AppCompatActivity {
    private EditText groupIdString;
    private EditText password;
    SessionManager session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_group);
        session = new SessionManager(getApplicationContext());
        groupIdString = (EditText) findViewById(R.id.join_group_enter_idString);
        password = (EditText) findViewById(R.id.join_group_enter_pass);
    }

    public void joinGroup(View view) {
        //TODO: grab group with groupIdString from database
        //TODO: check that password is correct
        //Use dummy data for now
        //String dummyIdString = "foodTeam412";
        //String dummyPass = "123456789";
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_CREATE_GROUP, new Response.Listener<String>() {

            @Override
            public void onResponse(String s) {
                Gson gson = new Gson();
                System.out.println(s);
                List<Group> response = new LinkedList<>();
                response = gson.fromJson(s, new TypeToken<List<Group>>() {
                }.getType());
                if (response != null && response.size() > 0) {
                    session.updateGroup(gson.toJson(response));

                } else {
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
                params.put("userId", String.valueOf(session.getId()));
                params.put("activity", "join");
                params.put("groupId", groupIdString.getText().toString());
                params.put("password", password.getText().toString());

                return params;
            }


        };
        queue.add(strReq);
        //if group id string exists in database
        /**
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
         **/

    }

}
