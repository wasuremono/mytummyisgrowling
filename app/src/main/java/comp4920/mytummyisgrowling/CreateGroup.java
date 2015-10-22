package comp4920.mytummyisgrowling;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class CreateGroup extends AppCompatActivity {
    private EditText[] inputFields;
    private int[] inputMinLengths;
    private boolean validGroupName;
    private boolean validPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        TextView t;
        t = (EditText) findViewById(R.id.create_group_enter_name);
        Typeface customFont = Typeface.createFromAsset(getAssets(), "fonts/palacio.ttf");
        t.setTypeface(customFont);
        t = (TextView) findViewById(R.id.create_group_name_hint);
        t.setTypeface(customFont);
        t = (EditText) findViewById(R.id.create_group_enter_pass);
        t.setTypeface(customFont);
        t = (TextView) findViewById(R.id.create_group_pass_hint);
        t.setTypeface(customFont);
        t = (EditText) findViewById(R.id.create_group_confirm_pass);
        t.setTypeface(customFont);

        inputFields= new EditText[]{(EditText) findViewById(R.id.create_group_enter_name),
                (EditText) findViewById(R.id.create_group_enter_pass),
                (EditText) findViewById(R.id.create_group_confirm_pass)
        };
        inputMinLengths = new int[]{getResources().getInteger(R.integer.register_username_min_char_limit),
                getResources().getInteger(R.integer.register_password_min_char_limit),
                getResources().getInteger(R.integer.register_password_min_char_limit)
        };

        validGroupName = false;
        validPassword = false;

        //Watch for changes in group name field
        inputFields[0].addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                //Check username is valid
                TextView usernameHint = (TextView) findViewById(R.id.create_group_name_hint);
                if (inputFields[0].length() >= inputMinLengths[0])
                    validGroupName = true;
                else
                    validGroupName = false;

                //Activate button if username and password is valid
                Button nextButton = (Button) findViewById(R.id.create_group_button);
                if (validGroupName && validPassword) {
                    nextButton.setEnabled(true);
                    nextButton.setClickable(true);
                } else {
                    nextButton.setEnabled(false);
                    nextButton.setClickable(false);
                }
            }
        });

        //Watch for changes in password fields
        for(int i = 1; i<inputFields.length; i++)
        {
           inputFields[i].addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    //Check if password is valid
                    TextView passwordHint = (TextView) findViewById(R.id.create_group_pass_hint);
                    if ((inputFields[1].length() >= inputMinLengths[1]) && (inputFields[2].length() >= inputMinLengths[2])) {
                        if (inputFields[1].getText().toString().equals(inputFields[2].getText().toString())) {
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
                    Button nextButton = (Button) findViewById(R.id.create_group_button);
                    if (validGroupName && validPassword) {
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

    public void createGroup(View view){
        //TODO: get next groupId to be genereated in database
        //TODO: Generate idString (groupName + next groupId)
        //TODO: add group to database

        //TODO: Open a dialog telling user the group id and pass, move to next intent when dialog is closed

        Intent intent = new Intent(this, GroupSelect.class);
        startActivity(intent);
    }
}
