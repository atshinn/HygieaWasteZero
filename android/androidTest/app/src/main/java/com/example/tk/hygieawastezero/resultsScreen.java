package com.example.tk.hygieawastezero;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class resultsScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results_screen);

        final Button goHome = findViewById(R.id.goHome);
        goHome.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code to get rid of something maybe, who knows
                //old: setContentView(R.layout.activity_opening_capture);
                Intent startOpening = new Intent(resultsScreen.this, openingCapture.class);
                startActivity(startOpening);
            }
        });
    }
}
