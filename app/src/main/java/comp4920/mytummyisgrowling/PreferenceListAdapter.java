package comp4920.mytummyisgrowling;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Carl Oehme on 11/10/2015.
 */
class Preference {
    private String cuisine;
    private int rank;

    public Preference(String cuisine){
        this.cuisine = cuisine;
        this.rank = 0;
    }

    public String getCuisine(){
        return cuisine;
    }

    public int getRank(){
        return rank;
    }

    public void setCuisine(String cuisine){
        this.cuisine = cuisine;
    }

    public void setRank(int rank){
        this.rank = rank;
    }

}

public class PreferenceListAdapter extends ArrayAdapter<Preference> {
    private Context context;
    private int resource;
    private ArrayList<Preference> preferenceList;

    public PreferenceListAdapter(Context context, int resource, ArrayList<Preference> preferenceList) {
        super(context, resource, preferenceList);
        this.context = context;
        this.resource = resource;
        this.preferenceList = preferenceList;
    }

    private static class PreferenceHolder {
        public TextView preferenceRank;
        public TextView preferenceCuisine;
        public Button upButton;
        public Button downButton;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;
        PreferenceHolder holder = new PreferenceHolder();

        /*if(convertView == null) {*/
            LayoutInflater inflater=
                    ((Activity) context).getLayoutInflater();
            View row=inflater.inflate(resource,parent,false);

            TextView preferenceRank = (TextView) row.findViewById(R.id.row_preference_rank);
            TextView preferenceCuisine = (TextView) row.findViewById(R.id.row_preference_cuisine);
            /*holder.upButton = (Button) row.findViewById(R.id.row_preference_up_button);
            holder.downButton = (Button) row.findViewById(R.id.row_preference_down_button);*/

           /* holder.upButton.setOnClickListener((EditPreferencesActivity) context);
            holder.downButton.setOnClickListener((EditPreferencesActivity) context);*/
       /* } else {
            holder = (PreferenceHolder) v.getTag();
        }*/

        Preference p = preferenceList.get(position);
        preferenceRank.setText(Integer.toString(position+1));
        preferenceCuisine.setText(p.getCuisine());
      /*  holder.preferenceCuisine.setText(p.getCuisine());
        holder.upButton.setTag(p);
        holder.downButton.setTag(p);*/

        return row;
    }

}

