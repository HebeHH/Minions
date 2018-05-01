package hebe.minions;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.DatePicker;

import android.widget.EditText;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;

import java.sql.Time;
import java.util.Calendar;

public class AddEventActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);


        setTitle("Add Event");
        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#8b2249")));


        Button btnYes = findViewById(R.id.aeSet);
        btnYes.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                EditText aeName   = (EditText)findViewById(R.id.aeName);
                TimePicker aeTime = (TimePicker) findViewById(R.id.aeTime);
                DatePicker aeDate = (DatePicker) findViewById(R.id.aeDate);
                Switch aeLights = (Switch) findViewById(R.id.aeLights);
                Switch aeBtnFan = (Switch) findViewById(R.id.aeBtnFan);
                Switch aeBtnAircon = (Switch) findViewById(R.id.aeBtnAircon);
                SeekBar aeSldTemp = (SeekBar) findViewById(R.id.aeSldAircon);
                SeekBar aeSldWind = (SeekBar) findViewById(R.id.aeSwtFan);

                Calendar targetTime = Calendar.getInstance();
                targetTime.set(aeDate.getYear(), aeDate.getMonth(), aeDate.getDayOfMonth(), aeTime.getHour(), aeTime.getMinute());
                long timeDiff = (targetTime.getTimeInMillis()-Calendar.getInstance().getTimeInMillis())/1000;


                Log.d("AddEventActivity", "Event added for "+targetTime.getTime());
                newEvent(aeName.getText()+"",timeDiff+"", (aeLights.isChecked() ? "On" : "Off")+"", ""+(aeBtnFan.isChecked() ? "On" : "Off"),
                        (aeBtnAircon.isChecked() ? "On" : "Off")+"", aeSldTemp.getProgress()+"", aeSldWind.getProgress()+"");

                AlertDialog doneDialogue = new AlertDialog.Builder(AddEventActivity.this).create();
                doneDialogue.setTitle("Done!");
                doneDialogue.show();
                doneDialogue.getWindow().setLayout(600, 400);
                doneDialogue.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#20ee00")));

                try {
                    wait(15000);
                } catch (Exception e) {}

                Intent intent = new Intent(AddEventActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        Button btnNo = findViewById(R.id.aeCancel);
        btnNo.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent(AddEventActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    public void newEvent(String title, String time, String Lights, String Fan, String FanIntensity, String Aircon, String Temp) {
        String dweetURL = "http://192.241.140.108:5000/event/"+ MainActivity.deviceName+"?title="+title+"&time="+time+"&Lights="+Lights
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
