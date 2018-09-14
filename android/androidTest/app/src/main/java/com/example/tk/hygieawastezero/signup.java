package com.example.tk.hygieawastezero;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class signup extends AppCompatActivity {

    boolean nameValid = false;
    boolean passValid = false;

    TextView namehint;
    TextView passhint;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        namehint = findViewById(R.id.namehint);
        passhint = findViewById(R.id.passhint);

        final Button submit = findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Add some code later to grab text from text fields and validate it before finishing
                //Probably some more code after that to store the text and send it off to the server
                if(nameValid && passValid){
                    finish();
                }
                else{
                    Toast.makeText(signup.this, "Username or Password doesn't adhere to guidelines", Toast.LENGTH_SHORT).show();
                }
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
                int count = 0;
                if(text.length() == 0)
                {
                    namehint.setText("Please enter a username.");
                    nameValid = false;
                }
                else{
                    for(int i = 0; i<text.length(); i++){
                        if(!Character.isLetterOrDigit(text.charAt(i))){
                            if(text.charAt(i) == '-' || text.charAt(i) == '_' || text.charAt(i) == '\''){
                                nameValid = true;
                                count = 0;
                            }
                            else if(text.charAt(i) == '.' && count == 0){
                                count++;
                                nameValid = true;
                            }
                            else{
                                nameValid = false;
                                i = text.length();
                            }
                        }
                        else{
                            count = 0;
                            nameValid = true;
                        }
                    }
                    if(!nameValid){
                        namehint.setText("A character in the username is invalid, or too many .'s in a row.");
                    }
                    else if(text.charAt(0) == '.' || text.charAt(text.length()-1) == '.'){
                        nameValid = false;
                        namehint.setText("Usernames cannot start or end with a period.");
                    }
                    else if (text.length() > 64) {
                        nameValid = false;
                        namehint.setText("Username is too long.");
                    }
                    else{
                        nameValid = true;
                        namehint.setText("This is a valid username!");
                    }
                }
            }
        });

        final EditText pass = findViewById(R.id.registerpass);
        pass.addTextChangedListener(new TextValidator(pass) {
            @Override
            public void validate(TextView textView, String text) {
                if(text.length() == 0){
                    passhint.setText("Please enter a password.");
                    passValid = false;
                } else if (text.length() < 8){
                    passhint.setText("Password is too short.");
                    passValid = false;
                } else if (text.charAt(0) == ' ' || text.charAt(text.length()-1) == ' '){
                    passhint.setText("Passwords cannot start or end with white space.");
                    passValid = false;
                }
                else{
                    passValid = true;
                    passhint.setText("This is a valid password!");
                }
            }
        });
    }
}
