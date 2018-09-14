package com.example.tk.hygieawastezero;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class signup extends AppCompatActivity {

    boolean nameValid = false;
    boolean passValid = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        final Button submit = findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Add some code later to grab text from text fields and validate it before finishing
                //Probably some more code after that to store the text and send it off to the server
                finish();
            }
        });

        final Button cancel = findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        //Live text validation for username and password
        final EditText name = findViewById(R.id.registername);
        name.addTextChangedListener(new TextValidator(name) {
            @Override
            public void validate(TextView textView, String text) {
                
            }
        });

        final EditText pass = findViewById(R.id.registerpass);
        pass.addTextChangedListener(new TextValidator(pass) {
            @Override
            public void validate(TextView textView, String text) {

            }
        });
    }
}
