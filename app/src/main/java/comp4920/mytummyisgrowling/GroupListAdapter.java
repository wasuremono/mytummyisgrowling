package comp4920.mytummyisgrowling;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Carl Oehme on 20/10/2015.
 */

class Group {
    private String id;
    private String name;
    private String pass;
    private String leaderName;
    private int leaderId;
    private ArrayList<Integer> memberIds;

    public ArrayList<Integer> getMemberIds() {
        return memberIds;
    }

    public void setMemberIds(ArrayList<Integer> memberIds) {
        this.memberIds = memberIds;
    }

    public Group(String name, String pass, String leaderName, int leaderId){
        this.name = name;
        this.pass= pass;
        this.leaderName = leaderName;
        this.leaderId = leaderId;
        this.memberIds = new ArrayList<>();
        addMemberId(leaderId);
        //TODO: Generate group Id
    }

    public void addMemberId(int id) {
        this.memberIds.add(id);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id =id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getLeaderName() {
        return leaderName;
    }

    public void setLeaderName(String leaderName) {
        this.leaderName = leaderName;
    }

    public int getLeaderId() {
        return leaderId;
    }

    public void setLeaderId(int leaderId) {
        this.leaderId = leaderId;
    }

}
public class GroupListAdapter extends ArrayAdapter<Group> {
    private Context context;
    private int resource;
    private ArrayList<Group> groupList;

    public GroupListAdapter(Context context, int resource, ArrayList<Group> groupList){
        super(context, resource, groupList);
        this.context = context;
        this.resource = resource;
        this.groupList = groupList;
    }
    @Override
    public int getCount() {
        return groupList.size();
    }

    @Override
    public Group getItem(int position) {
        return groupList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater=
                ((Activity) context).getLayoutInflater();
        View item=inflater.inflate(resource, parent, false);

        TextView groupName = (TextView) item.findViewById(R.id.grid_item_group_name);
        TextView groupLeader = (TextView) item.findViewById(R.id.grid_item_group_leader_name);

        Group g = groupList.get(position);
        groupName.setText(g.getName());
        groupLeader.setText(g.getLeaderName());
        return item;
    }
}
