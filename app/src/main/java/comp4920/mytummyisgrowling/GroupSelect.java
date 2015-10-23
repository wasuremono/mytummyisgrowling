package comp4920.mytummyisgrowling;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GroupSelect extends AppCompatActivity {
    public final static String GROUP_ID = "comp4920.mytummyisgrowling.GROUP_ID";
    private ArrayList<Group> groupList;
    private GridView gridView;
    GroupListAdapter groupAdapter;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_select);
        session = new SessionManager(getApplicationContext());

        //TODO: grab the groups that the current user is already in (most recent first), and get leaders names
        //Use dummy data for now
        Gson gson = new Gson();
        groupList = new ArrayList<Group>();
        List<Group> groups = gson.fromJson(session.getGroups(), new TypeToken<List<Group>>() {
        }.getType());
        for (Group g : groups) {
            if (g != null) {
                groupList.add(g);
            }
        }
/**
        groupList = new ArrayList<>(Arrays.asList(new Group(1, "YummyTummies", "12345", "YummyTummies12345", "Frederick", 4),
                new Group(2, "YummyTummies", "12345","YummyTummies12345", "Frederick", 4), new Group(3, "YummyTummies", "12345","YummyTummies12345", "Frederick", 4), new Group(4,"YummyTummies", "12345","YummyTummies12345", "Frederick", 4),
                new Group(5, "YummyTummies", "12345","YummyTummies12345", "Frederick", 4), new Group(6, "YummyTummies", "12345","YummyTummies12345", "Frederick", 4), new Group(7, "YummyTummies", "12345","YummyTummies12345", "Frederick", 4),
                new Group(8, "YummyTummies", "12345","YummyTummies12345", "Frederick", 4), new Group(9, "YummyTummies", "12345","YummyTummies12345", "Frederick", 4), new Group(10, "YummyTummies", "12345","YummyTummies12345", "Frederick", 4),
                new Group(11, "YummyTummies", "12345","YummyTummies12345", "Frederick", 4), new Group(12, "YummyTummies", "12345","YummyTummies12345", "Frederick", 4), new Group(13, "YummyTummies", "12345","YummyTummies12345", "Frederick", 4),
                new Group(14, "YummyTummies", "12345","YummyTummies12345", "Frederick", 4), new Group(15, "YummyTummies", "12345","YummyTummies12345", "Frederick", 4), new Group(16, "YummyTummies", "12345","YummyTummies12345", "Frederick", 4)));
 **/
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
        for(Group g: groupList){
            if(g.isSelected()) {
                selectedGroup = g;
                break;
            }
        }

        intent.putExtra(GROUP_ID, selectedGroup.getId());
        startActivity(intent);
    }
}
