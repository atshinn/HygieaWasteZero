package com.example.tk.hygieawastezero;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.JsonWriter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;

public class login extends AppCompatActivity {

    final int CAMERA_REQUEST_CODE = 1;

    TextView name;
    TextView pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //For android 6 and above permissions
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[] {android.Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
        }

        final Button login = findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Currently unlogin-able due to the lack of a server to check against, add code later to do this
                if(false){
                    saveCredsToInternal();
                    Intent swap = new Intent(login.this, openingCapture.class);
                    startActivity(swap);
                    finish();
                } else {
                    Toast.makeText(login.this, "Login information doesn't match any known account", Toast.LENGTH_LONG).show();
                }

            }
        });

        final Button guest = findViewById(R.id.guest);
        guest.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent swap = new Intent(login.this, openingCapture.class);
                startActivity(swap);
                finish();
            }
        });

        final Button register = findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent swap = new Intent(login.this, signup.class);
                startActivity(swap);
            }
        });

        name = findViewById(R.id.loginname);
        pass = findViewById(R.id.loginpass);

        loadCredsFromInternal();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case CAMERA_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    Toast.makeText(this, "Camera Permissions are required for this app", Toast.LENGTH_LONG).show();
                    finish();
                }
                break;
            }
        }
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
    private void loadCredsFromInternal() {
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        File directory = cw.getDir("credDir", Context.MODE_PRIVATE);
        File mypath = new File(directory, "creds.json");

        try {
            String jsonStr = getStringFromFile(mypath.getAbsolutePath());

            if (jsonStr != null) {
                JSONObject json = new JSONObject(jsonStr);
                name.setText(json.getString("username"));
                pass.setText(json.getString("password"));
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    public static String convertStreamToString(InputStream is) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        reader.close();
        return sb.toString();
    }

    public static String getStringFromFile (String filePath) throws Exception {
        File fl = new File(filePath);
        FileInputStream fin = new FileInputStream(fl);
        String ret = convertStreamToString(fin);
        fin.close();
        return ret;
    }
}
