package com.example.tk.hygieawastezero;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class previewScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_screen);

        final Button retake = findViewById(R.id.retake);
        retake.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code to get rid of previous picture to prevent memory leak, maybe?
                //old: setContentView(R.layout.activity_opening_capture);
                Intent startOpening = new Intent(previewScreen.this, openingCapture.class);
                startActivity(startOpening);
            }
        });

        final Button confirm = findViewById(R.id.confirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code to send something to server and get ID of item back by next screen
                //old: setContentView(R.layout.activity_results_screen);
                Intent startResults = new Intent(previewScreen.this, resultsScreen.class);
                startActivity(startResults);
            }
        });
    }
}
