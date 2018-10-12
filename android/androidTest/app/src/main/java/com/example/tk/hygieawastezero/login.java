package com.example.tk.hygieawastezero;

import android.Manifest;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.JsonWriter;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.auth.AWSSessionCredentials;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.cognitoidentity.model.Credentials;
import com.amazonaws.services.s3.AmazonS3Client;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class login extends AppCompatActivity {
    Context appContext;
    CognitoUserPool userPool;
    String key_url = "https://cognito-idp.us-west-2.amazonaws.com/us-west-2_2KW8CF0tm/.well-known/jwks.json";
    // Initialize the Amazon Cognito credentials provider
    CognitoCachingCredentialsProvider credentialsProvider;
    AmazonS3Client s3Client;

    final int CAMERA_REQUEST_CODE = 1;
    final int LOCATION_REQUEST_CODE = 2;

    boolean nameValid = false;
    boolean passValid = false;

    TextView namehint;
    TextView passhint;

    TextView name;
    TextView pass;

    int REQUEST_LOGIN = 0;

    private WebView loginView;
    private String token = "";

    private ProgressBar loadWidget;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //For android 6 and above permissions
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[] {android.Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_REQUEST_CODE);
        }

        userPool  = new CognitoUserPool(getApplicationContext(), "us-west-2_2KW8CF0tm", "8mrvs89q1frh6hqooc4nt9b0", "53hqi241c7u51am44nckkjv6m2ugv0jima5nqglgu07ebtrsm7", Regions.US_WEST_2);
        credentialsProvider = new CognitoCachingCredentialsProvider(
                getApplicationContext(),
                "us-west-2:6bd013d4-d707-4d4b-9174-29170cd89ad1", // Identity pool ID
                Regions.US_WEST_2 // Region
        );
        s3Client = new AmazonS3Client(credentialsProvider);

        loadWidget = findViewById(R.id.progressBarLogin);
        loadWidget.setIndeterminate(true);
        loadWidget.setVisibility(View.INVISIBLE);

        namehint = findViewById(R.id.logNameHint);
        passhint = findViewById(R.id.logPassHint);

        //Live text validation for username and password
        name = findViewById(R.id.loginname);
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

        pass = findViewById(R.id.loginpass);
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

        final Button login = findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Currently unlogin-able due to the lack of a server to check against, add code later to do this
                if(nameValid && passValid){
                    //Then send httpRequest
                    if(false) { //if response is okay
                        saveCredsToInternal();
                        Intent swap = new Intent(login.this, openingCapture.class);
                        swap.putExtra("username", name.getText().toString());
                        swap.putExtra("password", pass.getText().toString());
                        startActivity(swap);
                        finish();
                    } else {
                        Toast.makeText(login.this, "Login information doesn't match any known account", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(login.this, "Login information doesn't match guidelines", Toast.LENGTH_LONG).show();
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
                //If activity comes back with an accepted credential, auto log them in.
                startActivityForResult(swap, REQUEST_LOGIN);
            }
        });

        name = findViewById(R.id.loginname);
        pass = findViewById(R.id.loginpass);

        loadCredsFromInternal();

        namehint.setVisibility(View.INVISIBLE);
        passhint.setVisibility(View.INVISIBLE);
        name.setVisibility(View.INVISIBLE);
        pass.setVisibility(View.INVISIBLE);
        login.setVisibility(View.INVISIBLE);
        guest.setVisibility(View.INVISIBLE);
        register.setVisibility(View.INVISIBLE);

        loginView = findViewById(R.id.loginView);

        loginView.getSettings().setJavaScriptEnabled(true);
        loginView.getSettings().setUseWideViewPort(true);
        loginView.getSettings().setLoadWithOverviewMode(true);
        loginView.getSettings().setSupportZoom(true);
        loginView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        loginView.setBackgroundColor(Color.WHITE);

        loginView.setWebChromeClient(new WebChromeClient());
        loginView.setWebViewClient(webBrowser.getINSTANCE());
        //Note to self: Always use the fully correct URL, this won't account for any minor differences
        //signupView.loadUrl("https://www.google.com/");
        loginView.loadUrl("https://hywz-auth.auth.us-west-2.amazoncognito.com/login?response_type=token&client_id=8mrvs89q1frh6hqooc4nt9b0&redirect_uri=hygieawastezero://");
        pollWebView();
        //Don't do these things ---v
        //while(token == ""){} //Main thread waits while user navigates signup/signin screens
        ////implement token handling here
        //Log.d("Token", token);

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
            case LOCATION_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    Toast.makeText(this, "Location Permissions are required for this app", Toast.LENGTH_LONG).show();
                    finish();
                }
                break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_LOGIN) {
            if (resultCode == RESULT_OK) {
                Intent swap = new Intent(login.this, openingCapture.class);
                startActivity(swap);
                finish();
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
            //It's okay if it's not found, expected even.
            //e.printStackTrace();
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

    public void pollWebView(){

        final Handler tokenHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                String token = (String) msg.obj;
                loginView.setVisibility(View.INVISIBLE);
                loadWidget.setVisibility(View.VISIBLE);
                setToken(token);
                handleToken();
            }
        };
        new Thread(new Runnable() {
            int runthrough = 0;
            @Override
            public void run() {
                while (runthrough < 1) {
                    try {
                        Thread.sleep(1000);
                        tokenHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (webBrowser.getINSTANCE().getTokenChanged()){
                                    String token = webBrowser.getINSTANCE().getToken();
                                    Message msg = new Message();
                                    msg.obj = token;
                                    tokenHandler.sendMessage(msg);
                                }
                            }
                        });
                        if (webBrowser.getINSTANCE().getTokenChanged()){
                            runthrough++;
                        }
                    } catch (Exception e) {
                    }
                }
            }
        }).start();

    }

    public void setToken(String t){
        token = t;
    }

    public void handleToken(){
        //Log.d("Token", token);
        tokenBearer bearer = new tokenBearer(token);
        //Log.d("Id", bearer.getIdToken());
        //Log.d("Access", bearer.getAccessToken());
        //Log.d("Expires in", Integer.toString(bearer.getExpiration()));
        //Log.d("Token type", bearer.getTokenType());
        Map<String, String> logins = new HashMap<String, String>();
        logins.put("cognito-idp.us-west-2.amazonaws.com/us-west-2_2KW8CF0tm", bearer.getIdToken());
        credentialsProvider.setLogins(logins);
        final Handler credsHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                AWSSessionCredentials credentials = (AWSSessionCredentials) msg.obj;
                handleCreds(credentials);
            }
        };
        new Thread(new Runnable() {
            @Override
            public void run() {
                AWSSessionCredentials creds = credentialsProvider.getCredentials();
                Message msg = new Message();
                msg.obj = creds;
                credsHandler.sendMessage(msg);
            }
        }).start();
    }

    public void handleCreds(AWSSessionCredentials c){
        Intent swap = new Intent(login.this, openingCapture.class);
        swap.putExtra("accessKey", c.getAWSAccessKeyId());
        swap.putExtra("secretKey", c.getAWSSecretKey());
        startActivity(swap);
        finish();
    }
}
