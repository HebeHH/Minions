package hebe.minions;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.widget.EditText;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;

public class ManageEventsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_events);


        setTitle("Manage Events");
        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#8b2249")));

//        new AsyncTaskParseJson().execute();

        Button btnEdit = findViewById(R.id.meEdit);
        btnEdit.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                EditText aeName = (EditText)findViewById(R.id.meName);
                Intent intent = new Intent(ManageEventsActivity.this, AddEventActivity.class);
                intent.putExtra("Name", aeName.getText().toString());
                startActivity(intent);
            }
        });


        Button btnDel = findViewById(R.id.meDel);
        btnDel.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                EditText aeName = (EditText)findViewById(R.id.meName);

                delEvent(aeName.getText()+"");

                AlertDialog doneDialogue = new AlertDialog.Builder(ManageEventsActivity.this).create();
                doneDialogue.setTitle("Done!");
                doneDialogue.show();
                doneDialogue.getWindow().setLayout(600, 400);
                doneDialogue.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#20ee00")));

                try {
                    wait(15000);
                } catch (Exception e) {
                }

                Intent intent = new Intent(ManageEventsActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        Button btnNo = findViewById(R.id.meCancel);
        btnNo.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent(ManageEventsActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
    @SuppressLint("StaticFieldLeak")
    public void delEvent(String title) {
        String dweetURL = "http://192.241.140.108:5000/delevent/"+ MainActivity.deviceName+"?title="+title;

        new AsyncTask<String, Void, Void>() {
            @Override
            protected Void doInBackground(String... urls) {
                try {

                    AndroidNetworking.get(urls[0]).build()
                            .getAsString(new StringRequestListener() {
                                @Override
                                public void onResponse(String response) {
                                    Log.d("MainActivity","myDweet successful.");
                                }

                                @Override
                                public void onError(ANError anError) {
                                    Log.d("MainActivity","myDweet errored.");
                                    Log.d("MainActivity",anError.getErrorDetail());
                                    Log.d("MainActivity",anError.getMessage());
                                }
                            });

                } catch (Exception e) {
                    Log.d("MainActivity", "Error sending dweet");
                    Log.d("MainActivity",e.toString());
                }

                return null;
            }
        }.execute(dweetURL);
    }
}



//
//    public class AsyncTaskParseJson extends AsyncTask<String, String, String> {
//
//        final String TAG = "AsyncTaskParseJson.java";
//
//        // set your json string url here
//        String yourJsonStringUrl = "http://192.241.140.108:5000/get/"+ MainActivity.deviceName;
//
//        // contacts JSONArray
//        JSONArray dataJsonArr = null;
//
//        @Override
//        protected void onPreExecute() {}
//
//        @Override
//        protected String doInBackground(String... arg0) {
//
//            try {
//
//                // instantiate our json parser
//                HttpHandler jParser = new HttpHandler();
//
//                // get json string from url
//                JSONObject json = jParser.getJSONFromUrl(yourJsonStringUrl);
//
//                Log.d(TAG,"URL "+yourJsonStringUrl+" returned "+json);
//
//                // get the array of users
//                dataJsonArr = json.getJSONArray("events");
//
//                // loop through all users
//                for (int i = 0; i < dataJsonArr.length(); i++) {
//
//                    JSONObject c = dataJsonArr.getJSONObject(i);
//
//                    // Storing each json item in variable
//                    String Fan = c.getString("Fan");
//                    String Lights = c.getString("Lights");
//                    String Aircon = c.getString("Aircon");
//
//                    // show the values in our logcat
//                    Log.e(TAG, "Fan: " + Fan
//                            + ", Lights: " + Lights
//                            + ", Aircon: " + Aircon);
//
//                }
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(String strFromDoInBg) {}
//    }
//
//}
