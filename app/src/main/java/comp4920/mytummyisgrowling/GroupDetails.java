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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ListView;
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
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class GroupDetails extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {
    public final static String GROUP_MEMBER_IDS = "comp4920.mytummyisgrowling.GROUP_MEMBER_IDS";
    private Group group;
    private ArrayList<GroupMember> groupMemberList;
    private ListView listView;
    GroupMemberListAdapter groupMemberAdapter;
    private Button useGroupButton;
    private SessionManager session;
    private ArrayList <String> cuisines = new ArrayList <String> ();
    private ArrayList <Integer> totals = new ArrayList <Integer> ();
    private ArrayList <Integer> indexes = new ArrayList <Integer> ();
    private ArrayList <String> finalstrings = new ArrayList <String> ();
    private int index;
    private int maxResults = 3;
    private int maxCuisines = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_details);
        session = new SessionManager(getApplicationContext());

        Intent intent = getIntent();
        String groupInfo = intent.getStringExtra(GroupSelect.GROUP_ID);
        Gson gson = new Gson();
        group = gson.fromJson(groupInfo, Group.class);
        //TODO: get group with groupId from database, and get all members names
        //Use dummy data for now


        groupMemberList = new ArrayList<GroupMember>();
        System.out.println("membeIds size: " + group.getMemberIds().size());
        listView = (ListView) findViewById(R.id.group_details_members_listView);
        groupMemberAdapter = new GroupMemberListAdapter(this, R.layout.row_group_member, groupMemberList);
        listView.setAdapter(groupMemberAdapter);
        for (final int memberId : group.getMemberIds()) {
            RequestQueue queue = Volley.newRequestQueue(this);
            StringRequest strReq = new StringRequest(Request.Method.POST,
                    AppConfig.URL_CREATE_GROUP, new Response.Listener<String>() {

                @Override
                public void onResponse(String s) {
                    Gson gson = new Gson();
                    GroupMember response = gson.fromJson(s, GroupMember.class);
                    if (response != null) {
                        System.out.println("Response: " + response.getName());
                        groupMemberList.add(response);

                        if (response.getId() == group.getLeaderId()) {
                            group.setLeaderName(response.getName());
                        }
                        groupMemberAdapter.notifyDataSetChanged();

                        // If a member preference contains a cuisine not seen yet, add it to the list
                        // Add a 0 entry to the totals arraylist representing a new cuisine added
                        if (!cuisines.contains (Member.cuisines)) {
                            cuisines.add(Member.cuisine);
                            totals.add(0);
                        }

                        // Get the index of the current cuisine being examined
                        index = cuisines.indexOf(group.member.cuisine);
                        // Get the current cumulative point value for that cuisine
                        int total = totals.get(index);
                        // Get the rank of the cuisine in question for that member
                        // Do 10 minus rank plus 1 to get the total points to add.
                        // eg. If rank 1, we're adding 10 points, if rank 2, we're adding 9 points to total
                        total = total + maxCuisines - individual.getRank + 1;
                        // Set the new value for total
                        totals.set (index, total);
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
                    params.put("userId", String.valueOf(memberId));
                    params.put("activity", "getName");

                    return params;
                }


            };
            queue.add(strReq);
        }

        int max = 0;

        // Iterate through the list and find the maxresults (3) number of max ints
        // ie. 3 largest ints.
        // Find their indexes and put their cuisine names into the finalstrings list for passing
        for (int i = 0; i < maxResults; i++) {
            for (int j = 0; j < totals.size(); j++) {
                if (totals.get(j) > max) {
                    max = totals.get (j);
                }
            }

            index = totals.indexOf (max);
            indexes.add (index);
            totals.set(index, 0);
        }

        // Final strings is a list of maxResults (3) strings
        // representing the 3 cuisines with the highest totals
        for (int i = 0; i < indexes.size(); i++) {
            finalstrings.add (cuisines.get(indexes.get(i)));
        }

        TextView nameTV = (TextView) findViewById(R.id.group_details_name);
        TextView idStringTV = (TextView) findViewById(R.id.group_details_id_string);
        TextView passTV = (TextView) findViewById(R.id.group_details_pass);

        nameTV.setText(group.getName());
        idStringTV.setText(String.valueOf(group.getId()));
        passTV.setText(group.getPass());
        System.out.println("GroupMemberList size: " + groupMemberList.size());


        useGroupButton = (Button) findViewById(R.id.group_details_use_group_button);

        /*listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GroupMember gM = groupMemberList.get(position);
                gM.toggleChecked();
                groupMemberAdapter.notifyDataSetChanged();
            }
        });*/
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

    public void useGroup (View view) {
        Intent intent = new Intent(this, ChooseLocation.class);
        ArrayList<Integer> groupMemberIds = new ArrayList<>();
        for(GroupMember gm: groupMemberList){
            if(gm.isChecked())
                groupMemberIds.add(gm.getId());
        }

        intent.putExtra(MainActivity.GROUP_PREFERENCE_SEARCH, true);
        intent.putExtra(GROUP_MEMBER_IDS, groupMemberIds);
        startActivity(intent);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int position = listView.getPositionForView(buttonView);
        GroupMember gM = groupMemberList.get(position);
        gM.setChecked(isChecked);

        boolean validChoice = false;
        for(GroupMember g: groupMemberList){
            if(g.isChecked()){
                validChoice = true;
                break;
            }
        }

        if(validChoice) {
            useGroupButton.setEnabled(true);
            useGroupButton.setClickable(true);
        } else {
            useGroupButton.setEnabled(false);
            useGroupButton.setClickable(false);
        }
    }
}
