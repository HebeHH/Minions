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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TimePicker;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;

import java.util.Calendar;

public class CycleAircon extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cycle_aircon);


        setTitle("Cycle Aircon");
        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#8b2249")));



        Button btnYes = findViewById(R.id.caSet);
        btnYes.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                Switch caBtnAircon1 = (Switch) findViewById(R.id.caBtnAircon1);
                SeekBar caSldTemp1 = (SeekBar) findViewById(R.id.caSldAircon1);
                EditText caMin1   = (EditText)findViewById(R.id.caMin1);
                Switch caBtnAircon2 = (Switch) findViewById(R.id.caBtnAircon2);
                SeekBar caSldTemp2 = (SeekBar) findViewById(R.id.caSldAircon2);
                EditText caMin2  = (EditText)findViewById(R.id.caMin2);
                EditText caRepeats   = (EditText)findViewById(R.id.caNumTimes);

                Calendar targetTime = Calendar.getInstance();
                long currTime = (Calendar.getInstance().getTimeInMillis())/1000;

                int time1 = Integer.parseInt(caMin1.getText().toString());
                int time2 = Integer.parseInt(caMin2.getText().toString());
                int repeats = Integer.parseInt(caRepeats.getText().toString());

                for (int i = 0; i < repeats+1; i++) {
                    long t1 = (time1 + time2)*i+currTime;
                    long t2 = t1 + time1;
                    Log.d("CycleAircon", "Event added for " + t1);
                    newAircon(i + "aircon", t1 + "", (caBtnAircon1.isChecked() ? "On" : "Off") + "", caSldTemp1.getProgress() + "");
                    Log.d("CycleAircon", "Event added for " + t2);
                    newAircon(i + "aircon", t2 + "", (caBtnAircon2.isChecked() ? "On" : "Off") + "", caSldTemp2.getProgress() + "");
                }


                AlertDialog doneDialogue = new AlertDialog.Builder(CycleAircon.this).create();
                doneDialogue.setTitle("Done!");
                doneDialogue.show();
                doneDialogue.getWindow().setLayout(600, 400);
                doneDialogue.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#20ee00")));

                try {
                    wait(15000);
                } catch (Exception e) {}

                Intent intent = new Intent(CycleAircon.this, MainActivity.class);
                startActivity(intent);
            }
        });

        Button btnNo = findViewById(R.id.caCancel);
        btnNo.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent(CycleAircon.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
    @SuppressLint("StaticFieldLeak")
    public void newAircon(String title, String time, String Aircon, String Temp) {
        String dweetURL = "http://192.241.140.108:5000/event/"+ MainActivity.deviceName+"?title="+title+"&time="+time+"&Aircon="+Aircon+"&Temp="+Temp;

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

