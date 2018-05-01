package hebe.minions;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TimePicker;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;

import java.util.Calendar;

public class AddStateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_state);

        setTitle("Add State");
        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#8b2249")));


        Button btnYes = findViewById(R.id.asSet);
        btnYes.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                EditText asName   = (EditText)findViewById(R.id.asName);
                Switch asLights = (Switch) findViewById(R.id.asLights);
                Switch asBtnFan = (Switch) findViewById(R.id.asBtnFan);
                Switch asBtnAircon = (Switch) findViewById(R.id.asBtnAircon);
                SeekBar asSldTemp = (SeekBar) findViewById(R.id.asSldAircon);
                SeekBar asSldWind = (SeekBar) findViewById(R.id.asSwtFan);

                Log.d("AddEventActivity", "New State "+asName.getText()+" added.");
                newState(asName.getText()+"", (asLights.isChecked() ? "On" : "Off")+"", ""+(asBtnFan.isChecked() ? "On" : "Off"),
                        (asBtnAircon.isChecked() ? "On" : "Off")+"", asSldTemp.getProgress()+"", asSldWind.getProgress()+"");


                AlertDialog doneDialogue = new AlertDialog.Builder(AddStateActivity.this).create();
                doneDialogue.setTitle("Done!");
                doneDialogue.show();
                doneDialogue.getWindow().setLayout(600, 400);
                doneDialogue.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#20ee00")));

                try {
                    wait(15000);
                } catch (Exception e) {}

                Intent intent = new Intent(AddStateActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        Button btnNo = findViewById(R.id.asCancel);
        btnNo.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent(AddStateActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    public void newState(String title, String Lights, String Fan, String FanIntensity, String Aircon, String Temp) {
        String dweetURL = "http://192.241.140.108:5000/state/"+ MainActivity.deviceName+"?title="+title+"&Lights="+Lights
                +"&Fan="+Fan+"&FanIntensity="+FanIntensity+"&Aircon="+Aircon+"&Temp="+Temp;

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
