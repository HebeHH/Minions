package hebe.minions;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;

public class ManageStatesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_states);



        setTitle("Manage States");
        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#8b2249")));

        Button btnEdit = findViewById(R.id.msEdit);
        btnEdit.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                EditText msName = (EditText)findViewById(R.id.msName);
                Intent intent = new Intent(ManageStatesActivity.this, AddStateActivity.class);
                intent.putExtra("Name", msName.getText().toString());
                startActivity(intent);
            }
        });


        Button btnDel = findViewById(R.id.msDel);
        btnDel.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                EditText msName = (EditText)findViewById(R.id.msName);

                delState(msName.getText()+"");

                AlertDialog doneDialogue = new AlertDialog.Builder(ManageStatesActivity.this).create();
                doneDialogue.setTitle("Done!");
                doneDialogue.show();
                doneDialogue.getWindow().setLayout(600, 400);
                doneDialogue.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#20ee00")));

                try {
                    wait(15000);
                } catch (Exception e) {
                }

                Intent intent = new Intent(ManageStatesActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        Button btnNo = findViewById(R.id.msCancel);
        btnNo.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent(ManageStatesActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
    @SuppressLint("StaticFieldLeak")
    public void delState(String title) {
        String dweetURL = "http://192.241.140.108:5000/delstate/"+ MainActivity.deviceName+"?title="+title;

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
