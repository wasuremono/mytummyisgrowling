package comp4920.mytummyisgrowling;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class GroupDetails extends AppCompatActivity {
    private Group group;
    private ArrayList<GroupMember> groupMemberList;
    private ListView listView;
    GroupMemberListAdapter groupMemberAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_details);

        Intent intent = getIntent();
        int groupId = intent.getIntExtra(GroupSelect.GROUP_ID, -1);

        //TODO: get group with groupId from database, and get all members names
        //Use dummy data for now

        group = new Group(1, "YummyTummies", "12345", "Frederick", 4);
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
    }

}
