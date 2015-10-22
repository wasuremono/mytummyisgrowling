package comp4920.mytummyisgrowling;

import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Carl Oehme on 21/10/2015.
 */


class GroupMember {
    private int id;
    private String name;
    private boolean checked;
    private boolean leader;

    public GroupMember(int id, String name) {
        this.id = id;
        this.name = name;
        this.checked = false;
        this.leader = false;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public boolean isLeader() {
        return leader;
    }

    public void setLeader(boolean leader) {
        this.leader = leader;
    }
}
public class GroupMemberListAdapter extends ArrayAdapter<GroupMember> {
    private Context context;
    private int resource;
    private ArrayList<GroupMember> groupMemberList;

    public GroupMemberListAdapter(Context context, int resource, ArrayList<GroupMember> groupMemberList) {
        super(context, resource, groupMemberList);
        this.context = context;
        this.resource = resource;
        this.groupMemberList = groupMemberList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater =
                ((Activity) context).getLayoutInflater();
        View row = inflater.inflate(resource, parent, false);

        TextView memberName = (TextView) row.findViewById(R.id.row_group_member_name);
        CheckBox cb = (CheckBox) row.findViewById(R.id.row_group_member_checkbox);

        GroupMember gM = groupMemberList.get(position);
        memberName.setText(gM.getName());
        if(gM.isChecked())
            cb.setChecked(true);
        else
            cb.setChecked(false);

        return row;
    }
}
