package com.example.tk.hygieawastezero;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.StringWriter;

import cz.msebera.android.httpclient.Header;

public class previewScreen extends AppCompatActivity {

    int REQUEST_EXIT = 0;
    private ProgressBar loadWidget;
    private String gVisionRequestJsonStr = "{\\\"requests\\\":[{\\\"image\\\":{\\\"content\\\":},\\\"features\\\":[{\\\"type\\\":\\\"FACE_DETECTION\\\",},{\\\"type\\\":\\\"LANDMARK_DETECTION\\\",},\\\":\\\"LOGO_DETECTION\\\",},{\\\"type\\\":\\\"LABEL_DETECTION\\\",},{\\\"type\\\":\\\"IMAGE_PROPERTIES\\\",}]}]}";
    final private String url = "http://hywz-dev.us-west-1.elasticbeanstalk.com/";


    static String apiResults = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_screen);
        loadWidget = findViewById(R.id.progressBar);
        loadWidget.setIndeterminate(true);
        loadWidget.setVisibility(View.VISIBLE);

        Bundle extras = getIntent().getExtras();
        RequestParams params = new RequestParams();
        File myFile = loadImageFromStorage(extras.getString("path"));
        try {
            params.put("file", myFile);
        } catch(FileNotFoundException e) {
            Log.e(this.getClass().getSimpleName(), Log.getStackTraceString(e));
        }
        HywzRestClient.post("image", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                findViewById(R.id.progressBar).setVisibility(View.GONE);
                Intent startResults = new Intent(previewScreen.this, resultsScreen.class);
                startResults.putExtra("apiResults", new String(responseBody));
                startActivityForResult(startResults, REQUEST_EXIT);
            }
            
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                findViewById(R.id.progressBar).setVisibility(View.GONE);
                Intent startResults = new Intent(previewScreen.this, resultsScreen.class);
                startResults.putExtra("apiResults", previewScreen.stackTraceToString(error));
                startActivityForResult(startResults, REQUEST_EXIT);
            }
        });



        //gVisionRequestJsonStr = gVisionRequestJsonStr.replace("REPLACEME",this.loadImageFromStorage(extras.getString("path");
        /*json = getRequestObj(loadImageFromStorage(extras.getString("path"))); //getRequestObj("img_content");

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                findViewById(R.id.progressBar).setVisibility(View.GONE);
                Intent startResults = new Intent(previewScreen.this, resultsScreen.class);
                startResults.putExtra("apiResults", response.toString());
                startActivityForResult(startResults, REQUEST_EXIT);
            }

        }, new Response.ErrorListener() {

            @Override

            public void onErrorResponse(VolleyError error) {
                findViewById(R.id.progressBar).setVisibility(View.GONE);
                Intent startResults = new Intent(previewScreen.this, resultsScreen.class);
                startResults.putExtra("apiResults", previewScreen.stackTraceToString(error));
                startActivityForResult(startResults, REQUEST_EXIT);
            }

        });

        requestQueue.add(jsonObjectRequest);*/




    ////USE TO GRAB PATH TO IMAGE FROM PREVIOUS ACTIVITY////

        /*Bundle extras = getIntent().getExtras();
        assert extras != null;

        loadImageFromStorage(extras.getString("path"));*/

        ////

        ////REMOVED BUTTONS////

        /*final Button retake = findViewById(R.id.retake);
        retake.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code to get rid of previous picture to prevent memory leak, maybe?
                //old: setContentView(R.layout.activity_opening_capture);
                //old 2: Intent startOpening = new Intent(previewScreen.this, openingCapture.class);
                //old 2: startActivity(startOpening);
                finish();
            }
        });*/

        /*final Button confirm = findViewById(R.id.confirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code to send something to server and get ID of item back by next screen
                //old: setContentView(R.layout.activity_results_screen);
                Intent startResults = new Intent(previewScreen.this, resultsScreen.class);
                startActivityForResult(startResults, REQUEST_EXIT);
            }
        });*/

        ////

        ////How to change text live
        //TextView txt = findViewById(R.id.loadingText);
        //txt.setText("Testing the change");


        //Intent startResults = new Intent(previewScreen.this, resultsScreen.class);
        //startResults.putExtra("apiResults", previewScreen.apiResults);
        //startActivityForResult(startResults, REQUEST_EXIT);
    }
    private File loadImageFromStorage(String path)
    {
        byte[] byteArray = null;
        String encoded = "ERROR ENCODING";
        File f = null;
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            f=new File(path, "pic.jpg");
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            b.compress(Bitmap.CompressFormat.JPEG, 75, byteArrayOutputStream);
            byteArray = byteArrayOutputStream .toByteArray();
            encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
        }
        catch (FileNotFoundException e)
        {
            Log.e(this.getClass().getSimpleName(), previewScreen.stackTraceToString(e));
        }catch(Exception e) {
            Log.e(this.getClass().getSimpleName(), previewScreen.stackTraceToString(e));
        }finally{
            return f;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_EXIT) {
            if (resultCode == RESULT_OK) {
                this.finish();
            }
        }
    }

    public JSONObject getRequestObj(String content){
        JSONObject result = new JSONObject();
        JSONArray requests = new JSONArray();
        JSONObject labelDetection = new JSONObject();
        JSONObject image = new JSONObject();
        try {
            image.put("content", content);
            labelDetection.put("type", "LABEL_DETECTION");
            requests.put(image);
            requests.put(labelDetection);
            result.put("requests", requests);
        } catch (JSONException e) {
            e.printStackTrace();
            result = null;
        }
        Log.d(this.getClass().getSimpleName(),result.toString());
        return result;
    }

    public static String stackTraceToString(VolleyError e){
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }
    public static String stackTraceToString(Throwable e){
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }
}
