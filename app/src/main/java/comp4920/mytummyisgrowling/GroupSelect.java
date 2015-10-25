package comp4920.mytummyisgrowling;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class GroupSelect extends AppCompatActivity {
    public final static String GROUP_ID = "comp4920.mytummyisgrowling.GROUP_ID";
    private ArrayList<Group> groupList;
    private GridView gridView;
    GroupListAdapter groupAdapter;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Gson gson = new Gson();
        setContentView(R.layout.activity_group_select);
        session = new SessionManager(getApplicationContext());
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_CREATE_GROUP, new Response.Listener<String>() {

            @Override
            public void onResponse(String s) {
                Gson gson = new Gson();
                List<Group> response = new LinkedList<Group>();
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
                params.put("activity", "read");

                return params;
            }


        };
        queue.add(strReq);
        groupList = new ArrayList<Group>();

        List<Group> groups = gson.fromJson(session.getGroups(), new TypeToken<List<Group>>() {
        }.getType());
        for (Group g : groups) {
            if (g != null) {
                groupList.add(g);
            }
        }
        gridView = (GridView) findViewById(R.id.group_select_grid_view);
        groupAdapter = new GroupListAdapter(this, R.layout.grid_item_group, groupList);
        gridView.setAdapter(groupAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            Button viewGroup = (Button) findViewById(R.id.group_select_view_group_details_button);

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                viewGroup.setEnabled(true);
                viewGroup.setClickable(true);
                for (Group g : groupList)
                    g.setSelected(false);
                groupList.get(position).setSelected(true);
                groupAdapter.notifyDataSetChanged();
            }
        });
    }

        //TODO: grab the groups that the current user is already in (most recent first), and get leaders names
        //Use dummy data for now
/**
        groupList = new ArrayList<>(Arrays.asList(new Group(1, "YummyTummies", "12345", "YummyTummies12345", "Frederick", 4),
                new Group(2, "YummyTummies", "12345","YummyTummies12345", "Frederick", 4), new Group(3, "YummyTummies", "12345","YummyTummies12345", "Frederick", 4), new Group(4,"YummyTummies", "12345","YummyTummies12345", "Frederick", 4),
                new Group(5, "YummyTummies", "12345","YummyTummies12345", "Frederick", 4), new Group(6, "YummyTummies", "12345","YummyTummies12345", "Frederick", 4), new Group(7, "YummyTummies", "12345","YummyTummies12345", "Frederick", 4),
                new Group(8, "YummyTummies", "12345","YummyTummies12345", "Frederick", 4), new Group(9, "YummyTummies", "12345","YummyTummies12345", "Frederick", 4), new Group(10, "YummyTummies", "12345","YummyTummies12345", "Frederick", 4),
                new Group(11, "YummyTummies", "12345","YummyTummies12345", "Frederick", 4), new Group(12, "YummyTummies", "12345","YummyTummies12345", "Frederick", 4), new Group(13, "YummyTummies", "12345","YummyTummies12345", "Frederick", 4),
                new Group(14, "YummyTummies", "12345","YummyTummies12345", "Frederick", 4), new Group(15, "YummyTummies", "12345","YummyTummies12345", "Frederick", 4), new Group(16, "YummyTummies", "12345","YummyTummies12345", "Frederick", 4)));
 **/



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

    public void createGroup (View view) {
        Intent intent = new Intent(this, CreateGroup.class);
        startActivity(intent);
    }

    public void joinGroup (View view) {
        Intent intent = new Intent(this, JoinGroup.class);
        startActivity(intent);
    }

    public void selectGroup (View view){
        Intent intent = new Intent(this, GroupDetails.class);
        Group selectedGroup = null;
        Gson gson = new Gson();
        String groupString = new String();
        for(Group g: groupList){
            if(g.isSelected()) {
                selectedGroup = g;
                groupString = gson.toJson(g, Group.class);
                System.out.println(groupString);
                break;
            }
        }

        intent.putExtra(GROUP_ID, groupString);
        startActivity(intent);
    }

    @Override
    protected void onResume() {

        super.onResume();
    }
}
