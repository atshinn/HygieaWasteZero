package com.example.tk.hygieawastezero;

import android.Manifest;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class openingCapture extends AppCompatActivity implements SurfaceHolder.Callback{

    Camera camera;

    Camera.PictureCallback picCallback;

    SurfaceHolder previewHolder;

    private FusedLocationProviderClient mFusedLocationClient;

    private double[] loc = new double[2];

    Task<Location> task;
    //OLD
    //final int CAMERA_REQUEST_CODE = 1;

    String username = "null";
    String password = "null";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opening_capture);

        /*////Stores credentials across activities
        Bundle extras = getIntent().getExtras();
        if(extras.getString("username") != null && extras.getString("password") != null){
            username = extras.getString("username");
            password = extras.getString("password");
        }
        else{
            username = "";
            password = "";
        }
        ////*/

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        getLastLocation();

        final Button capture = findViewById(R.id.capture);
        capture.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Add code later to do camera stuff before setting content view
                //setContentView(R.layout.activity_preview_screen);
                captureImage();
            }
        });

        final SurfaceView camPreview = findViewById(R.id.cameraPreview);
        previewHolder = camPreview.getHolder();
        //For android 6 and above permissions//OLD
        //if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
        //    ActivityCompat.requestPermissions(this, new String[] {android.Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
        //}
        //else {
            previewHolder.addCallback(this);
            previewHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        //}

        picCallback = new Camera.PictureCallback(){
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                Intent startPreview = new Intent(openingCapture.this, previewScreen.class);
                Bitmap decodeBitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                startPreview.putExtra("path", saveToInternalStorage(decodeBitmap));
                //double[] location = getLastLocation();
                //Log.d("Location", "Latitude: " + location[0] + "  Longitude: " + location[1]);
                loc[0] = task.getResult().getLatitude();
                loc[1] = task.getResult().getLongitude();
                startPreview.putExtra("location", loc);
                camera.release();
                startActivity(startPreview);
            }
        };
    }

    private String saveToInternalStorage(Bitmap bitmapImage){
        ContextWrapper cw = new ContextWrapper(getApplicationContext());

        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        File mypath = new File(directory,"pic.jpg");

        //Prints path nicely if needed
        //Log.d("Path", mypath.toString());

        //To fix weird skew issue
        Bitmap rotatedPic = rotate(bitmapImage);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            rotatedPic.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }

    private Bitmap rotate(Bitmap bitmapImage) {
        int width = bitmapImage.getWidth();
        int height = bitmapImage.getHeight();

        Matrix matrix = new Matrix();
        matrix.setRotate(90);

        return Bitmap.createBitmap(bitmapImage, 0, 0, width, height, matrix, true);
    }

    private void captureImage() {
        camera.takePicture(null, null, picCallback);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        camera = Camera.open();

        Camera.Parameters parameters;
        parameters = camera.getParameters();

        camera.setDisplayOrientation(90);
        parameters.setPreviewFrameRate(30);
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);

        //to fix skew after rotating
        Camera.Size supportedSize = null;
        List<Camera.Size> sizeList = camera.getParameters().getSupportedPreviewSizes();
        supportedSize = sizeList.get(0);
        for (int i = 1; i < sizeList.size(); i++){
            if((sizeList.get(i).width * sizeList.get(i).height)>(supportedSize.width * supportedSize.height)){
                supportedSize = sizeList.get(i);
            }
        }
        parameters.setPreviewSize(supportedSize.width, supportedSize.height);

        camera.setParameters(parameters);

        try {
            camera.setPreviewDisplay(holder);
        } catch (IOException e) {
            e.printStackTrace();
        }
        camera.startPreview();
    }

    public void getLastLocation(){
        //loc[0] = 0;
        //loc[1] = 0;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){

            task = mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location == null) {
                                location.setLatitude(0);
                                location.setLongitude(0);
                                //loc[0] = location.getLatitude();
                                //loc[1] = location.getLongitude();
                                //Log.d("Location", "Success!");
                                //Log.d("Location", "Latitude: " + loc[0] + "  Longitude: " + loc[1]);
                            }
                            //Log.d("Location", "Success, but null");
                        }
                    });
            //while(!task.isComplete()){}
            //loc[0] = task.getResult().getLatitude();
            //loc[1] = task.getResult().getLongitude();
        }
        //Log.d("Location", "Latitude: " + loc[0] + "  Longitude: " + loc[1]);
        //return loc;
    }
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {}
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {}

    /*OLD
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case CAMERA_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    previewHolder.addCallback(this);
                    previewHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
                } else {
                    Toast.makeText(this, "Camera Permissions are required for this app", Toast.LENGTH_LONG).show();
                }
                break;
            }
        }
    }*/
}