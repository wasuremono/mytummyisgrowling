package comp4920.mytummyisgrowling;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class RegisterActivity extends AppCompatActivity {
    private EditText[] credentials;
    private int[] credentialsMinLengths;
    private ArrayList<String> existingUsernames;
    private boolean validUsername;
    private boolean validPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        credentials = new EditText[]{(EditText) findViewById(R.id.register_enter_username),
                (EditText) findViewById(R.id.register_enter_password),
                (EditText) findViewById(R.id.register_confirm_password)
        };
        credentialsMinLengths = new int[]{getResources().getInteger(R.integer.register_username_min_char_limit),
                getResources().getInteger(R.integer.register_password_min_char_limit),
                getResources().getInteger(R.integer.register_password_min_char_limit)
        };
        //TODO: Grab existing usernames from database
        //Use dummy values for now
        existingUsernames = new ArrayList<>();
        existingUsernames.add("Jimson".toLowerCase());
        existingUsernames.add("Johnson".toLowerCase());
        existingUsernames.add("Jackson".toLowerCase());
        validUsername = false;
        validPassword = false;

        //Watch for changes in username field
        credentials[0].addTextChangedListener(new TextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                //Check username is valid
                TextView usernameHint = (TextView) findViewById(R.id.register_username_hint);
                if(credentials[0].length() >= credentialsMinLengths[0]){
                    String givenUsername = credentials[0].getText().toString().toLowerCase();
                    if(existingUsernames.contains(givenUsername)){
                        validUsername = false;
                        usernameHint.setText(R.string.register_username_taken);
                        usernameHint.setTextColor(0xFFFF0000);
                    } else {
                        validUsername = true;
                        usernameHint.setText(R.string.register_username_available);
                        usernameHint.setTextColor(0xFF00FF00);
                    }
                } else {
                    validUsername = false;
                    usernameHint.setText(R.string.register_enter_username_hint);
                    usernameHint.setTextColor(0xFF000000);
                }

                //Activate button if username and password is valid
                Button nextButton = (Button) findViewById(R.id.register_credentials_next_button);
                if(validUsername && validPassword){
                    nextButton.setEnabled(true);
                    nextButton.setClickable(true);
                } else {
                    nextButton.setEnabled(false);
                    nextButton.setClickable(false);
                }
            }
        });

        //Watch for changes in password fields
        for(int i = 1; i < credentials.length; i++) {
            credentials[i].addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {}
                @Override
                public void afterTextChanged(Editable s) {
                    //Check if password is valid
                    TextView passwordHint = (TextView) findViewById(R.id.register_password_hint);
                    if((credentials[1].length() >= credentialsMinLengths[1]) && (credentials[2].length() >= credentialsMinLengths[2])) {
                        if (credentials[1].getText().toString().equals(credentials[2].getText().toString())) {
                            validPassword = true;
                            passwordHint.setText(R.string.register_enter_password_hint);
                            passwordHint.setTextColor(0xFF000000);
                        } else {
                            validPassword = false;
                            passwordHint.setText(R.string.register_password_mismatch);
                            passwordHint.setTextColor(0xFFFF0000);
                        }
                    } else {
                        validPassword = false;
                        passwordHint.setText(R.string.register_enter_password_hint);
                        passwordHint.setTextColor(0xFF000000);
                    }

                    //Activate button if both username and password are valid
                    Button nextButton = (Button) findViewById(R.id.register_credentials_next_button);
                    if(validUsername && validPassword){
                        nextButton.setEnabled(true);
                        nextButton.setClickable(true);
                    } else {
                        nextButton.setEnabled(false);
                        nextButton.setClickable(false);
                    }
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register, menu);
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

    public void acceptCredentials (View view){
        String username = ((EditText) findViewById(R.id.register_enter_username)).getText().toString();
        String password = ((EditText) findViewById(R.id.register_enter_password)).getText().toString();
        //TODO: Check and make sure username was not taken while user was registering
        //TODO: add user's credentials to database
        //TODO: Next activity (let user edit preferences, or just log them in)
    }
}
