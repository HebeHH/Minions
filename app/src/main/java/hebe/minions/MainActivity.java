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
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.ToggleButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    public static MainActivity instance;
    public static String     deviceName = "hebe";

    View.OnClickListener btnSetDeviceClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.instance);
            builder.setTitle("Please enter a device name");

            // Set up the input
            final EditText input = new EditText(MainActivity.instance);
            // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            builder.setView(input);

            // Set up the buttons
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Log.d("MainActivity","deviceName changed from "+ MainActivity.deviceName+" to "+input.getText().toString());
                    MainActivity.deviceName = input.getText().toString();
                    SharedPreferences.Editor prefEditor = MainActivity.instance.getPreferences(Context.MODE_PRIVATE).edit();
                    prefEditor.putString("deviceName", MainActivity.deviceName);
                    prefEditor.commit();
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            builder.show();
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.mnuCycleAircon:
                Intent intent0 = new Intent(this, CycleAircon.class);
                startActivity(intent0);
                return true;
            case R.id.mnuAddEvent:
                Intent intent1 = new Intent(this, AddEventActivity.class);
                startActivity(intent1);
                return true;
            case R.id.mnuMngeEvents:
                Intent intent2 = new Intent(this, ManageEventsActivity.class);
                startActivity(intent2);
                return true;
            case R.id.mnuAddState:
                Intent intent3 = new Intent(this, AddStateActivity.class);
                startActivity(intent3);
                return true;
            case R.id.mnuMngeStates:
                Intent intent4 = new Intent(this, ManageStatesActivity.class);
                startActivity(intent4);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AndroidNetworking.initialize(getApplicationContext());

        instance = this;

        setContentView(R.layout.activity_main);



        setTitle("Minions");
        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#8b2249")));

        deviceName = this.getPreferences(Context.MODE_PRIVATE).getString("deviceName","raspi1");

        Button btnSetDevice = findViewById(R.id.btnSetDevice);
        btnSetDevice.setOnClickListener(btnSetDeviceClickListener);

        // aircon button
//        Switch btnFan = findViewById(R.id.btnFan);
//        btnFan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                Log.d( "MainActivity", "Toggled Fan...");
//                myDweet("Fan", (b ? "On" : "Off"));
//            }
//        });

        ToggleButton btnFan = findViewById(R.id.btnFan);
        btnFan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Log.d( "MainActivity", "Toggled Fan...");
                myDweet("Fan", (b ? "On" : "Off"));
            }
        });

        // fan slider
        SeekBar sldFan = findViewById(R.id.sldFan);
        sldFan.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                Log.d("MainActivity", "Fan set to "+i+" with boolean "+b);
                myDweet("FanIntensity",i+"");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        // aircon button
        ToggleButton btnAircon = findViewById(R.id.btnAircon);
        btnAircon.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Log.d( "MainActivity", "Toggled Aircon...");
                myDweet("Aircon", (b ? "On" : "Off"));
            }
        });

        // aircon slider
        SeekBar sldSetAircon = (SeekBar) findViewById(R.id.sldSetAircon);
        sldSetAircon.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                Log.d("MainActivity", "Temp set to "+i+" with boolean "+b);
                myDweet("Temp",i+"");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        // lights button
        ToggleButton btnLights = findViewById(R.id.btnLights);
        btnLights.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Log.d("MainActivity","Toggled Lights...");
                myDweet("Lights",(b ? "On" : "Off"));
            }
        });

        final Button goState = findViewById(R.id.goState);
        goState.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                EditText stateName   = (EditText)findViewById(R.id.selState);

                goState(stateName.getText().toString());
                Log.d("MainActivity", "State Changed To: " + stateName.getText().toString());
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });


    }

    @SuppressLint("StaticFieldLeak")
    public void myDweet(String key, String value) {
        String dweetURL = "http://192.241.140.108:5000/set/"+ MainActivity.deviceName+"?gadget="+key+"&value="+value;

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

    @SuppressLint("StaticFieldLeak")
    public void goState(String title) {
        String dweetURL = "http://192.241.140.108:5000/setstate/"+ MainActivity.deviceName+"?title="+title;

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
