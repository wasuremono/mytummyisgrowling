package comp4920.mytummyisgrowling;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.Arrays;

public class GroupSelect extends AppCompatActivity {
    public final static String GROUP_ID = "comp4920.mytummyisgrowling.GROUP_ID";
    private ArrayList<Group> groupList;
    private GridView gridView;
    GroupListAdapter groupAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_select);

        //TODO: grab the groups that the current user is already in, and get leaders names
        //Use dummy data for now

        groupList = new ArrayList<>(Arrays.asList(new Group(1, "YummyTummies", "12345", "Frederick", 4),
                new Group(2, "YummyTummies", "12345", "Frederick", 4), new Group(3, "YummyTummies", "12345", "Frederick", 4), new Group(4,"YummyTummies", "12345", "Frederick", 4),
                new Group(5, "YummyTummies", "12345", "Frederick", 4), new Group(6, "YummyTummies", "12345", "Frederick", 4), new Group(7, "YummyTummies", "12345", "Frederick", 4),
                new Group(8, "YummyTummies", "12345", "Frederick", 4), new Group(9, "YummyTummies", "12345", "Frederick", 4), new Group(10, "YummyTummies", "12345", "Frederick", 4),
                new Group(11, "YummyTummies", "12345", "Frederick", 4), new Group(12, "YummyTummies", "12345", "Frederick", 4), new Group(13, "YummyTummies", "12345", "Frederick", 4),
                new Group(14, "YummyTummies", "12345", "Frederick", 4), new Group(15, "YummyTummies", "12345", "Frederick", 4), new Group(16, "YummyTummies", "12345", "Frederick", 4)));

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
