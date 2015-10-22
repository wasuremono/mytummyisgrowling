package comp4920.mytummyisgrowling;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ken on 10/13/2015.
 */
public class LoginResponse implements Serializable{

        private boolean error;
        private int uid;
        private String email;
    private List<Preference> prefs = new ArrayList<Preference>();
        private String errorMessage;


    public List<Preference> getPrefs() {
        return (List<Preference>) prefs;
    }
        public boolean getError() {
            return error;
        }
        public int getId() {
            return uid;
        }
        public String getEmail() {
            return email;
        }

        public String getErrorMessage() {
            return errorMessage;
        }

}
