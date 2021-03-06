package comp4920.mytummyisgrowling;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;

import android.app.Activity;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class AccountSettingsActivity extends Activity {
    private ImageView avatarView;
    public static final String PREFS_NAME = "MyPrefs";
    private String picturePath;
    private SessionManager session;
    private Bitmap thumbnail;
    private ProgressDialog dialog;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        session = new SessionManager(getApplicationContext());
        setContentView(R.layout.activity_account_settings);
        avatarView = (ImageView) findViewById(R.id.avatar_view);
        avatarView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,
                        "Select Picture"), 1);

            }
        });
        saveButton = (Button) findViewById(R.id.save_button);
        saveButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                updateAvatar();

            }
        });
    }

    private void updateAvatar() {
        dialog.setMessage("Uploading Avatar");
        dialog.show();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        final byte[] byteArray = byteArrayOutputStream.toByteArray();
        final String image_str = Base64.encodeToString(byteArray, Base64.DEFAULT);
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_UPLOADAVATAR, new Response.Listener<String>() {

            @Override
            public void onResponse(String s) {
                //System.out.println("Sent " + s + " bytes.");
                dialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                System.out.println("That didn't work!");
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user", String.valueOf(session.getId()));
                params.put("data", image_str);

                return params;
            }


        };
// Add the request to the RequestQueue.
        queue.add(strReq);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            Intent intent = new Intent(this, AccountSettingsActivity.class);
            startActivity(intent);
        } else if (id == R.id.action_logout) {
            session.doLogout();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                Uri selectedImage = data.getData();
                ;
                picturePath = getPath(selectedImage);
                System.out.println(picturePath);
                thumbnail = (BitmapFactory.decodeFile(picturePath));
                resize();
                avatarView.setImageBitmap(thumbnail);
                session.setAvatarPath(picturePath);
                saveButton.setEnabled(true);
                saveButton.setClickable(true);
            }
        }
    }


    @Override
    protected void onResume() {
        picturePath = session.getAvatarpath();
        if (picturePath != "") {
            thumbnail = (BitmapFactory.decodeFile(picturePath));

            resize();
            avatarView.setImageBitmap(thumbnail);
        }
        super.onResume();
    }

    private void resize() {

        float wratio = (int) thumbnail.getWidth() / 640;
        float hratio = (int) thumbnail.getHeight() / 800;
        if (thumbnail.getWidth() > 640) {
            thumbnail = Bitmap.createScaledBitmap(thumbnail, 640, (int) (thumbnail.getHeight() / wratio), false);
        } else if (thumbnail.getHeight() > 800) {
            thumbnail = Bitmap.createScaledBitmap(thumbnail, (int) (thumbnail.getWidth() / hratio), 800, false);
        }
    }

    public String getPath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;

    }
}
