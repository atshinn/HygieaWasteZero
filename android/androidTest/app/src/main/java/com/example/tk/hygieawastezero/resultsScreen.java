package com.example.tk.hygieawastezero;

import android.content.Intent;
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

        String resultStr = (String)extras.get("apiResults");
        TextView scrollable1 = findViewById(R.id.scrollableText);
        TextView scrollable2 = findViewById(R.id.ScrollableText);
        TextView results = findViewById(R.id.resultText);

        //scrollable1.setText((String)extras.get("apiResults"));
        scrollable2.setText("I set the text in code");
        results.setText(resultStr);

        goHome.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code to get rid of something maybe, who knows
                //old: setContentView(R.layout.activity_opening_capture);
                //old 2: Intent startOpening = new Intent(resultsScreen.this, openingCapture.class);
                //old 2: startActivity(startOpening);
                setResult(RESULT_OK);
                finish();
            }
        });
    }
}
