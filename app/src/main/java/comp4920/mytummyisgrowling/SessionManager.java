package comp4920.mytummyisgrowling;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
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

    public void doLogin(){
        editor.putBoolean(isLogin,true);
        editor.commit();
    }

    public void doLogout(){
        editor.putBoolean(isLogin,false);
        editor.commit();
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
        }
    }

    public boolean isLoggedin() {
        return pref.getBoolean(isLogin,false);
    }
}
