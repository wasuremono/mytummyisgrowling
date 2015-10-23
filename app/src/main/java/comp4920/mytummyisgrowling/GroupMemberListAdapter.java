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

        cb.setOnCheckedChangeListener((GroupDetails) context);
        GroupMember gM = groupMemberList.get(position);
        /*if(gM.isChecked())
            cb.setChecked(true);
        else
            cb.setChecked(false);*/
        memberName.setText(gM.getName());

        return row;
    }
}
