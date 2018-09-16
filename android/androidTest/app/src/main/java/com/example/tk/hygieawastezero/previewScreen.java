package com.example.tk.hygieawastezero;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
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


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class previewScreen extends AppCompatActivity {

    int REQUEST_EXIT = 0;
    private ProgressBar loadWidget;
    private String gVisionRequestJsonStr = "{\\\"requests\\\":[{\\\"image\\\":{\\\"content\\\":\\\"REPLACEME\\\"},\\\"features\\\":[{\\\"type\\\":\\\"FACE_DETECTION\\\",\\\"maxResults\\\":1},{\\\"type\\\":\\\"LANDMARK_DETECTION\\\",\\\"maxResults\\\":1},{\\\"type\\\":\\\"LOGO_DETECTION\\\",\\\"maxResults\\\":1},{\\\"type\\\":\\\"LABEL_DETECTION\\\",\\\"maxResults\\\":1},{\\\"type\\\":\\\"IMAGE_PROPERTIES\\\",\\\"maxResults\\\":1}]}]}\\\"features\\\":[{\\\"type\\\":\\\"FACE_DETECTION\\\",\\\"maxResults\\\":1},{\\\"type\\\":\\\"LANDMARK_DETECTION\\\",\\\"maxResults\\\":1},{\\\"type\\\":\\\"LOGO_DETECTION\\\",\\\"maxResults\\\":1},{\\\"type\\\":\\\"LABEL_DETECTION\\\",\\\"maxResults\\\":1},{\\\"type\\\":\\\"FACE_DETECTION\\\",\\\"maxResults\\\":1}]}]}";
    final private String url = "https://vision.googleapis.com/v1/images:annotate?key=AIzaSyDKdBvb9PpzsB4al3iMKQoX8zs6a9lNFL4";


    static String apiResults = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_screen);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        loadWidget = findViewById(R.id.progressBar);
        loadWidget.setIndeterminate(true);
        loadWidget.setVisibility(View.VISIBLE);
        JSONObject json = null;

        Bundle extras = getIntent().getExtras();
        //gVisionRequestJsonStr = gVisionRequestJsonStr.replace("REPLACEME",this.loadImageFromStorage(extras.getString("path");
        json = getRequestObj(loadImageFromStorage(extras.getString("path")));

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
                startResults.putExtra("apiResults", error.getLocalizedMessage());
                startActivityForResult(startResults, REQUEST_EXIT);
            }

        });

        requestQueue.add(jsonObjectRequest);




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
    private String loadImageFromStorage(String path)
    {
        String encoded = "";
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            File f=new File(path, "pic.jpg");
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            b.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream .toByteArray();
            encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }finally{
            return encoded;
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
            labelDetection.put("maxResults", 50);
            requests.put(image);
            requests.put(labelDetection);
            result.put("requests", requests);
        } catch (JSONException e) {
            e.printStackTrace();
            result = null;
        }

        return result;
    }
}
