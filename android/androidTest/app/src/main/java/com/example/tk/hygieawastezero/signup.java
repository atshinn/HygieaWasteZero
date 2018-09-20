package com.example.tk.hygieawastezero;

import android.content.Context;
import android.content.ContextWrapper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.JsonWriter;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;

public class signup extends AppCompatActivity {

    boolean nameValid = false;
    boolean passValid = false;
    boolean emailValid = false;

    TextView namehint;
    TextView passhint;
    TextView emailhint;

    TextView name;
    TextView pass;
    TextView email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        namehint = findViewById(R.id.registerNameHint);
        passhint = findViewById(R.id.registerPassHint);
        emailhint = findViewById(R.id.registerEmailHint);

        final Button submit = findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Add some code later to grab text from text fields and validate it before finishing
                //Probably some more code after that to store the text and send it off to the server
                if(nameValid && passValid && emailValid){
                    saveCredsToInternal();
                    setResult(RESULT_OK);
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
                setResult(RESULT_CANCELED);
                finish();
            }
        });

        //Live text validation for username and password
        name = findViewById(R.id.registername);
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

        pass = findViewById(R.id.registerpass);
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
                } else if (text.length() > 64){
                    passhint.setText("Password is too long.");
                    passValid = false;
                }
                else{
                    passValid = true;
                    passhint.setText("This is a valid password!");
                }
            }
        });

        email = findViewById(R.id.registerEmail);
        email.addTextChangedListener(new TextValidator(email) {
            @Override
            public void validate(TextView textView, String text) {
                if(text.length() == 0) {
                    emailhint.setText("Please enter an email.");
                    emailValid = false;
                } else if (Patterns.EMAIL_ADDRESS.matcher(text).matches()){
                    emailhint.setText("This is a valid email address!");
                    emailValid = true;
                } else {
                    emailhint.setText("This is not a valid email address.");
                    emailValid = false;
                }
            }
        });
    }
    private void saveCredsToInternal() {
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        File directory = cw.getDir("credDir", Context.MODE_PRIVATE);
        File mypath = new File(directory, "creds.json");

        //Prints path nicely if needed
        //Log.d("Path", mypath.toString());

        try {
            FileWriter fw = new FileWriter(mypath);
            JsonWriter jw = new JsonWriter(fw);
            jw.beginObject()
                    .name("username")
                    .value(name.getText().toString())
                    .name("password")
                    .value(pass.getText().toString())
                    .endObject();
            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
