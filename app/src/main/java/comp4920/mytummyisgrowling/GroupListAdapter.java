package comp4920.mytummyisgrowling;

import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Carl Oehme on 20/10/2015.
 */

class Group {
    private int id;
    private String name;
    private String idString;
    private String pass;
    private int leaderId;
    private String leaderName;
    private ArrayList<Integer> memberIds;
    private boolean selected;

    public Group(int id, String name, String pass, String idString, String leaderName, int leaderId){
        this.id = id;
        this.name = name;
        this.idString = idString;
        this.pass= pass;
        this.leaderId = leaderId;
        this.leaderName = leaderName;
        this.memberIds = new ArrayList<>();
        selected = false;
    }

    public void addMemberId(int id) {
        this.memberIds.add(id);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id =id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdString() {
        return idString;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public int getLeaderId() {
        return leaderId;
    }

    public void setLeaderId(int leaderId) {
        this.leaderId = leaderId;
    }

    public void setIdString(String idString) {
        this.idString = idString;
    }

    public String getLeaderName() {
        return leaderName;
    }

    public void setLeaderName(String leaderName) {
        this.leaderName = leaderName;
    }

    public ArrayList<Integer> getMemberIds() {
        return memberIds;
    }

    public void setMemberIds(ArrayList<Integer> memberIds) {
        this.memberIds = memberIds;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}


public class GroupListAdapter extends ArrayAdapter<Group> {
    private Context context;
    private int resource;
    private ArrayList<Group> groupList;

  /*  private static class ViewHolder {
        LinearLayout itemContainer;
        TextView groupName;
        TextView groupLeaderName;
    }*/

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

        LayoutInflater inflater =
                ((Activity) context).getLayoutInflater();
        View item = inflater.inflate(resource, parent, false);

        LinearLayout itemContainer = (LinearLayout) item.findViewById(R.id.grid_item_container);
        TextView groupName = (TextView) item.findViewById(R.id.grid_item_group_name);
        TextView groupLeaderName = (TextView) item.findViewById(R.id.grid_item_group_leader_name);

        Group g = groupList.get(position);
        groupName.setText(g.getName());
        groupLeaderName.setText(g.getLeaderName());
        if(g.isSelected())
            itemContainer.setBackgroundColor(ContextCompat.getColor(context, R.color.wallet_highlighted_text_holo_light));

        return item;
    }
}



/*
View item;
ViewHolder viewHolder;
if(convertView == null) {
        viewHolder = new ViewHolder();
        LayoutInflater inflater =
        ((Activity) context).getLayoutInflater();
        item = inflater.inflate(resource, parent, false);

        viewHolder.groupName = (TextView) item.findViewById(R.id.grid_item_group_name);
        viewHolder.groupLeaderName = (TextView) item.findViewById(R.id.grid_item_group_leader_name);
        item.setTag(viewHolder);
        } else {
        item = convertView;
        viewHolder = (ViewHolder) convertView.getTag();
        }
        LinearLayout itemContainer = (LinearLayout) item.findViewById(R.id.grid_item_container);
        */
/*LinearLayout itemContainer = (LinearLayout) item.findViewById(R.id.grid_item_container);
        TextView groupName = (TextView) item.findViewById(R.id.grid_item_group_name);
        TextView groupLeader = (TextView) item.findViewById(R.id.grid_item_group_leader_name);
*//*

        Group g = groupList.get(position);
        viewHolder.groupName.setText(g.getName());
        viewHolder.groupLeaderName.setText(g.getLeaderName());
        if(g.isSelected())
        itemContainer.setBackgroundColor(ContextCompat.getColor(context, R.color.wallet_highlighted_text_holo_light));

        return item;*/
