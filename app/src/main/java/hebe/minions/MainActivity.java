package hebe.minions;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Switch;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;

public class MainActivity extends AppCompatActivity {

    public static MainActivity instance;
    public static String     deviceName = "raspi1";

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AndroidNetworking.initialize(getApplicationContext());

        instance = this;

        setContentView(R.layout.activity_main);

        deviceName = this.getPreferences(Context.MODE_PRIVATE).getString("deviceName","raspi1");

        Button btnSetDevice = findViewById(R.id.btnSetDevice);
        btnSetDevice.setOnClickListener(btnSetDeviceClickListener);

        // fan slider
        SeekBar sldFan = findViewById(R.id.sldFan);
        sldFan.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                Log.d("MainActivity", "Fan set to "+i+" with boolean "+b);
                myDweet("Fan",i+"");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        // aircon button
        Switch swtFan = findViewById(R.id.swtAircon);
        swtFan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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
        Switch swtLights = findViewById(R.id.swtLights);
        swtLights.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Log.d("MainActivity","Toggled Lights...");
                myDweet("Lights",(b ? "On" : "Off"));
            }
        });


    }

    @SuppressLint("StaticFieldLeak")
    public void myDweet(String key, String value) {
        String dweetURL = "http://192.241.140.108:5000/set/"+ MainActivity.deviceName+"_"+key+"?value="+value;

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

    //    public void dweet(String key, String value) {
//        String dweetURL = "https://dweet.io/dweet/for/"+MainActivity.deviceName+"?"+key+"="+value;
//        new AsyncTask<String, Void, Void>(
//
//        ) {
//            @Override
//            protected Void doInBackground(String... urls) {
//                try {
//
//                    java.net.URL url = new java.net.URL(urls[0]);
//                    Log.d("MainActivity","Dweeting - "+urls[0]);
//                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
////                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
//                    urlConnection.disconnect();
//                    Log.d("MainActivity","Dweeted.");
//                } catch (Exception e) {
//                    Log.d("MainActivity", "Error sending dweet");
//                    Log.d("MainActivity",e.toString());
//                }
//
//                return null;
//            }
//        }.execute(dweetURL);
//    }
}
