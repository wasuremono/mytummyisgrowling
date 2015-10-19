package comp4920.mytummyisgrowling;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.mobeta.android.dslv.DragSortController;
import com.mobeta.android.dslv.DragSortListView;

import org.json.simple.JSONObject;

public class EditPreferencesActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private TextView subHeaderText;
    private TextView errorHintText;

    private ArrayList<String> cuisineList;

    private ArrayList<Preference> preferenceList;
    private PreferenceListAdapter preferenceAdapter;
    private DragSortListView prefLV;
    private SessionManager session;
    private Button skipButton;
    private Button discardChangesButton;
    private Button saveChangesButton;

    private boolean changesMade;

    private DragSortListView.DropListener onDrop =
            new DragSortListView.DropListener() {
                @Override
                public void drop(int from, int to) {
                    if (from != to) {
                        Preference pref = preferenceAdapter.getItem(from);
                        preferenceAdapter.remove(pref);
                        preferenceAdapter.insert(pref, to);
                        pref.setRank(to + 1);
                    }
                }
            };

    private DragSortListView.RemoveListener onRemove = new DragSortListView.RemoveListener()
    {
        @Override
        public void remove(int which)
        {
            //Insert cuisine alphabetically into cuisine List
            Preference p = preferenceList.get(which);
            String cuisine = p.getCuisine();
            int insertAt = Collections.binarySearch(cuisineList, cuisine);
            if(insertAt < 0 )
                insertAt = (insertAt * -1) -1;
            if(insertAt == 0)
                insertAt = 1;
            cuisineList.add(insertAt, cuisine);

            //Remove cuisine from preference list
            preferenceAdapter.remove(preferenceAdapter.getItem(which));

            //Check if this is the first change from user
            if(!changesMade){
                updateButtons();
            }

            //Ensure error text is blank
            errorHintText.setText("");

            if(preferenceList.size() == 0) {
                //No preferences, so update header text
                updateSubHeaderText();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_preferences);
        session = new SessionManager(getApplicationContext());

        //Get TextViews from layout
        subHeaderText =  (TextView) findViewById(R.id.edit_preferences_sub_header_text);
        errorHintText = (TextView) findViewById(R.id.edit_preferences_error_hint);

        //Get Buttons from layout
        skipButton = (Button) findViewById(R.id.edit_preferences_skip_button);
        discardChangesButton = (Button) findViewById(R.id.edit_preferences_discard_changes_button);
        saveChangesButton = (Button) findViewById(R.id.edit_preferences_save_changes_button);
        changesMade = false;

        //TODO: get cuisinelist
        //TODO: get preferenceList

        //For now, use dummy data
        cuisineList = new ArrayList<>(Arrays.asList("Select a cuisine...", "Japanese", "Chinese", "Mexican", "Thai", "Italian", "Spanish", "Lebanese", "Vietnamese", "Korean", "German", "French", "Indian"));
    /*    preferenceList = new ArrayList<>(Arrays.asList(new Preference("Japanese", 1), new Preference("Thai", 2)));*/
        preferenceList = new ArrayList<>();

        updateSubHeaderText();

        //Set up draggable preference list
        prefLV = (DragSortListView) findViewById(R.id.edit_preferences_layout_list);
        preferenceAdapter = new PreferenceListAdapter(this, R.layout.row_preference, preferenceList);
        prefLV.setAdapter(preferenceAdapter);
        prefLV.setDropListener(onDrop);
        prefLV.setRemoveListener(onRemove);

        DragSortController controller = new DragSortController(prefLV);
        controller.setRemoveEnabled(true);
        controller.setSortEnabled(true);
        controller.setDragInitMode(1);

        prefLV.setFloatViewManager(controller);
        prefLV.setOnTouchListener(controller);
        prefLV.setDragEnabled(true);

        //Sort cuisines alphabetically
        Collections.sort(cuisineList.subList(1, cuisineList.size()-1));

        //Set up cuisine spinner
        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<> (this, android.R.layout.simple_spinner_dropdown_item, cuisineList);
        Spinner spinner = (Spinner) findViewById(R.id.edit_preferences_layout_spinner);
        spinner.setAdapter(stringArrayAdapter);
        spinner.setOnItemSelectedListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_preferences, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(position != 0) {
            //Set spinner selection to default selection
            Spinner cuisineSpinner = (Spinner) findViewById(R.id.edit_preferences_layout_spinner);
            cuisineSpinner.setSelection(0);

            //Check if list is full
            if(preferenceList.size() < 10) {
                String cuisine = cuisineList.get(position);

                //Add cuisine to preference list
                preferenceList.add(new Preference(cuisine));
                preferenceList.get(preferenceList.size() - 1).setRank(preferenceList.size());
                DragSortListView preferenceListView = (DragSortListView) findViewById(R.id.edit_preferences_layout_list);
                PreferenceListAdapter preferenceAdapter = (PreferenceListAdapter) preferenceListView.getInputAdapter();
                preferenceAdapter.notifyDataSetChanged();

                //Remove cuisine from cuisine list
                cuisineList.remove(position);

                //Check if this is the first change from user
                if(!changesMade){
                    updateButtons();
                }

                if(preferenceList.size() == 1) {
                    //First preference added, so update header text
                    updateSubHeaderText();
                }
            } else {
                //Preference list is full, notify user
                errorHintText.setText(R.string.edit_preferences_max_pref_error_text);
                errorHintText.setTextColor(0xFFFF0000);
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void updateSubHeaderText(){
        if(preferenceList.size() == 0) {
            subHeaderText.setText(R.string.edit_preferences_no_preferences_text);
        }else {
            subHeaderText.setText(R.string.edit_preferences_sub_title_text);
        }
    }

    //This method is only called after the users first change to the preference list
    private void updateButtons(){
        changesMade = true;
        skipButton.setClickable(false);
        skipButton.setEnabled(false);
        discardChangesButton.setClickable(true);
        discardChangesButton.setEnabled(true);
        saveChangesButton.setClickable(true);
        saveChangesButton.setEnabled(true);
    }

    public void submitWithChanges (View view){
        /**
         Iterator<Preference> pIterator = preferenceList.iterator();
         while(pIterator.hasNext()){
         Preference p = (Preference) pIterator.next();
         Gson gson = new Gson();
         gson.toJson(p);
         System.out.println(gson.toJson(p));
         }
         **/
        final Gson gson = new Gson();
        System.out.println(gson.toJson(preferenceList));
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_EDITPREF, new Response.Listener<String>() {

            @Override
            public void onResponse(String s) {

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
                params.put("id", String.valueOf(session.getId()));
                params.put("JSON", gson.toJson(preferenceList));

                return params;
            }


        };
// Add the request to the RequestQueue.
        queue.add(strReq);

        //TODO: update users preferences in database
        //TODO: go to next activity
        Intent intent = new Intent(this, EatingWithActivity.class);
        startActivity(intent);
    }

    public void submitWithoutChanges (View view){
        //TODO: go to next activity
        Intent intent = new Intent(this, EatingWithActivity.class);
        startActivity(intent);
    }
}

