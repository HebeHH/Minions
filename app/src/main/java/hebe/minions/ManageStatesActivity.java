package hebe.minions;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ManageStatesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_states);



        setTitle("Manage States");
        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#8b2249")));




        Button btnYes = findViewById(R.id.msSet);
        btnYes.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                AlertDialog doneDialogue = new AlertDialog.Builder(ManageStatesActivity.this).create();
                doneDialogue.setTitle("Done!");
                doneDialogue.show();
                doneDialogue.getWindow().setLayout(600, 400);
                doneDialogue.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#20ee00")));

                try {
                    wait(15000);
                } catch (Exception e) {}

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
}
