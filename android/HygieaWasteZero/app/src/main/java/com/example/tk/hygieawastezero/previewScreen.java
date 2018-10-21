package com.example.tk.hygieawastezero;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.services.s3.AmazonS3Client;
import com.loopj.android.http.RequestParams;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class previewScreen extends AppCompatActivity {

    private ProgressBar loadWidget;
    public static String fn = "";
    static String buttonPressedStr = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_screen);

        AWSMobileClient.getInstance().initialize(this).execute();

        loadWidget = findViewById(R.id.progressBar);
        loadWidget.setIndeterminate(true);
        loadWidget.setVisibility(View.VISIBLE);

        Bundle extras = getIntent().getExtras();

        buttonPressedStr = (String)extras.get("buttonPressed");

        final double [] location = extras.getDoubleArray("location");
        //Log.d("Location", "Latitude: " + location[0] + "  Longitude: " + location[1]);
        RequestParams params = new RequestParams();
        File myFile = loadImageFromStorage(extras.getString("path"));
        try {
            uploadImage(myFile);
        }catch(Exception e){
            Intent goBacktoLogin = new Intent(previewScreen.this, login.class);

            Log.e(this.getClass().getSimpleName(), stackTraceToString(e));
            startActivity(goBacktoLogin);
        }
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

    public static String stackTraceToString(Throwable e){
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }

    private void uploadImage(File img) throws Exception {

        if (login.credentialsProvider == null) {
            Log.e(this.getClass().getSimpleName(), "creds object is null");
            throw new Exception("CREDENTIALS ARE NULL");
        }

        Log.e(getClass().getSimpleName(), AWSMobileClient.getInstance().getConfiguration().toString());

        String defaultBucket = "hywz.wastezero";

        TransferUtility transferUtil = TransferUtility.builder()
            .context(getApplicationContext())
            .awsConfiguration(AWSMobileClient.getInstance().getConfiguration())
            .defaultBucket("hywz.wastezero")
            .s3Client(new AmazonS3Client(login.credentialsProvider))
            .build();

        previewScreen.fn = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date());
        previewScreen.fn +=".jpg";
        TransferObserver transferObv = transferUtil.upload(buttonPressedStr + fn, img);

        transferObv.setTransferListener(new TransferListener() {
            @Override
            public void onStateChanged(int id, TransferState state) {
                if (TransferState.COMPLETED == state) {
                    Intent startResults = new Intent(previewScreen.this, resultsScreen.class);
                    startResults.putExtra("apiResults", previewScreen.buttonPressedStr + previewScreen.fn);

                    startActivity(startResults);
                    finish();
                }
            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {

            }

            @Override
            public void onError(int id, Exception ex) {
                Log.e(getClass().getSimpleName(), "INSIDE ON ERROR");
                String exStr = previewScreen.stackTraceToString(ex);
                Intent startResults = new Intent(previewScreen.this, resultsScreen.class);
                startResults.putExtra("apiResults", exStr);
                startActivity(startResults);
                finish();
            }
        });


    }
}
