package com.example.tk.hygieawastezero;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.amazonaws.auth.AWSSessionCredentials;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.regions.Regions;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class login extends AppCompatActivity {

    public static CognitoCachingCredentialsProvider credentialsProvider;

    final int CAMERA_REQUEST_CODE = 1;
    final int LOCATION_REQUEST_CODE = 2;

    private WebView loginView;
    private String token = "";

    private ProgressBar loadWidget;

    boolean dev = false;

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

        credentialsProvider = new CognitoCachingCredentialsProvider(
                getApplicationContext(),
                "us-west-2:6bd013d4-d707-4d4b-9174-29170cd89ad1", // Identity pool ID
                Regions.US_WEST_2 // Region
        );

        loadWidget = findViewById(R.id.progressBarLogin);
        loadWidget.setIndeterminate(true);
        loadWidget.setVisibility(View.INVISIBLE);

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

        loginView.loadUrl(getString(R.string.signin_url));
        pollWebView();
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
        //Log.d("Id", bearer.getIdToken());
        //Log.d("Access", bearer.getAccessToken());
        //Log.d("Expires in", Integer.toString(bearer.getExpiration()));
        //Log.d("Token type", bearer.getTokenType());

        tokenBearer bearer = new tokenBearer(token);
        dev = isDev(bearer.getIdToken());
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
        swap.putExtra("isDev", dev);
        startActivity(swap);
        finish();
    }

    public boolean isDev(String s){
        String[] idParts = s.split("\\.");
        String enBody = idParts[1];
        //Log.d("EnBody", enBody);
        try{
            JSONObject deBody = new JSONObject(new String(Base64.decode(enBody, Base64.DEFAULT)));
            //Log.d("groups", deBody.getJSONArray("cognito:groups").getString(0));
            return "dev".matches(deBody.getJSONArray("cognito:groups").getString(0));
            //if(b){ Log.d("isDev", "True"); } else { Log.d("isDev", "False"); }
        } catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
}
