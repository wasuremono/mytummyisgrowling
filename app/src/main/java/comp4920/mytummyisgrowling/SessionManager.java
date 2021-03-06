package comp4920.mytummyisgrowling;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ken on 10/13/2015.
 */
public class SessionManager {
    public SessionManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    SharedPreferences pref;
    Editor editor;
    Context _context;
    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "MTIGPref";
    private static final String isLogin = "isLoggedin";
    private static final String userId = "userID";
    private static final String userPrefs = "userPref";
    private static final String userGroups = "userGroup";
    private static final String avatarPath = "userAvatar";

    public void doLogin(int id, String prefs, String groups) {
        editor.putBoolean(isLogin, true);
        editor.putInt(userId, id);
        editor.putString(userGroups, groups);
        editor.putString(userPrefs, prefs);
        editor.commit();

    }

    public void doLogout(){
        editor.putBoolean(isLogin, false);
        editor.putString(avatarPath, "");
        editor.putString(userGroups, "");
        editor.putString(userPrefs, "");
        editor.commit();
        checkLogin();
    }

    public void checkLogin(){
        if(!this.isLoggedin()){
            Intent i = new Intent(_context, Home.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            _context.startActivity(i);
        } else {
            Intent i = new Intent(_context, Search.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            _context.startActivity(i);
        }
    }

    public boolean isLoggedin() {
        return pref.getBoolean(isLogin,false);
    }

    public int getId() {
        return pref.getInt(userId, 0);
    }

    public void setAvatarPath(String filePath) {
        editor.putString(avatarPath, filePath);
        editor.commit();
    }

    public String getUserPrefs() {
        return pref.getString(userPrefs, "");
    }

    public String getAvatarpath() {
        return pref.getString(avatarPath, "");
    }

    public String getGroups() {
        return pref.getString(userGroups, "");
    }

    public void updateGroup(String newGroup) {
        editor.putString(userGroups, newGroup);
        editor.commit();


    }
}
