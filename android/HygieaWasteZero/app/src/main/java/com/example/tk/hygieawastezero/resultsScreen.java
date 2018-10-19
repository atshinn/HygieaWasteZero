package com.example.tk.hygieawastezero;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class resultsScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results_screen);

        Bundle extras = getIntent().getExtras();
        final Button goHome = findViewById(R.id.goHome);

        double[] location = extras.getDoubleArray("location");
        String resultStr = (String)extras.get("apiResults");
        String locationStr = ("Latitude: " + location[0] + "\nLongitude: " + location[1]);

        TextView scrollable1 = findViewById(R.id.scrollableText);
        TextView scrollable2 = findViewById(R.id.ScrollableText);
        TextView results = findViewById(R.id.resultText);

        scrollable1.setText(locationStr);
        scrollable2.setText(resultStr);

        goHome.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
    }
}
