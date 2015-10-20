package comp4920.mytummyisgrowling;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.Arrays;

public class GroupSelect extends AppCompatActivity {
    private ArrayList<Group> groupList;
    private GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_select);

        //TODO: grab the groups that the current user is already in
        //Use dummy data for now
        groupList = new ArrayList<>(Arrays.asList(new Group("YummyTummies", "12345", "Frederick", 4),
                new Group("YummyTummies", "12345", "Frederick", 4), new Group("YummyTummies", "12345", "Frederick", 4), new Group("YummyTummies", "12345", "Frederick", 4),
                new Group("YummyTummies", "12345", "Frederick", 4), new Group("YummyTummies", "12345", "Frederick", 4), new Group("YummyTummies", "12345", "Frederick", 4),
                new Group("YummyTummies", "12345", "Frederick", 4), new Group("YummyTummies", "12345", "Frederick", 4), new Group("YummyTummies", "12345", "Frederick", 4),
                new Group("YummyTummies", "12345", "Frederick", 4), new Group("YummyTummies", "12345", "Frederick", 4), new Group("YummyTummies", "12345", "Frederick", 4),
                new Group("YummyTummies", "12345", "Frederick", 4), new Group("YummyTummies", "12345", "Frederick", 4), new Group("YummyTummies", "12345", "Frederick", 4)));

        gridView = (GridView) findViewById(R.id.group_select_grid_view);
        GroupListAdapter groupAdapter = new GroupListAdapter(this, R.layout.grid_item_group, groupList);
        gridView.setAdapter(groupAdapter);
    }

    public void createGroup (View view) {
        Intent intent = new Intent(this, CreateGroup.class);
        startActivity(intent);
    }

    public void joinGroup (View view) {
        Intent intent = new Intent(this, JoinGroup.class);
        startActivity(intent);
    }
}
