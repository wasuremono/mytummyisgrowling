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

import java.util.ArrayList;

public class GroupDetails extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {
    public final static String GROUP_MEMBER_IDS = "comp4920.mytummyisgrowling.GROUP_MEMBER_IDS";
    private Group group;
    private ArrayList<GroupMember> groupMemberList;
    private ListView listView;
    GroupMemberListAdapter groupMemberAdapter;
    private Button useGroupButton;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_details);
        session = new SessionManager(getApplicationContext());

        Intent intent = getIntent();
        int groupId = intent.getIntExtra(GroupSelect.GROUP_ID, -1);

        //TODO: get group with groupId from database, and get all members names
        //Use dummy data for now

        group = new Group(1, "YummyTummies", "12345", "YummyTummies12345", "Frederick", 4);
        group.addMemberId(2);
        group.addMemberId(3);
        group.addMemberId(4);
        group.addMemberId(5);
        group.addMemberId(6);
        group.addMemberId(7);

        groupMemberList = new ArrayList<>();
        GroupMember leader = new GroupMember(group.getLeaderId(), group.getLeaderName());
        leader.setLeader(true);
        groupMemberList.add(leader);
        for(int memberId: group.getMemberIds()){
            groupMemberList.add(new GroupMember(memberId, "Johnny"));
        }

        TextView nameTV = (TextView) findViewById(R.id.group_details_name);
        TextView idStringTV = (TextView) findViewById(R.id.group_details_id_string);
        TextView passTV = (TextView) findViewById(R.id.group_details_pass);

        nameTV.setText(group.getName());
        idStringTV.setText(group.getIdString());
        passTV.setText(group.getPass());

        listView = (ListView) findViewById(R.id.group_details_members_listView);
        groupMemberAdapter = new GroupMemberListAdapter(this, R.layout.row_group_member, groupMemberList);
        listView.setAdapter(groupMemberAdapter);

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
        if (id == R.id.action_settings) {
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
