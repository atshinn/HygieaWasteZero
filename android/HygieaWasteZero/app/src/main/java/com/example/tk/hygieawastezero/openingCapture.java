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
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;

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

    public Task<Location> task;

    String accessKey = "null";
    String secretKey = "null";

    static String buttonPressed = "";

    private Button captureButton, recycleButton, compostButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opening_capture);

        ////Stores credentials across activities
        Bundle extras = getIntent().getExtras();
        accessKey = extras.getString("accessKey");
        secretKey = extras.getString("secretKey");

        this.recycleButton = findViewById(R.id.recycle);
        this.compostButton = findViewById(R.id.compost);
        this.captureButton = findViewById(R.id.capture);

         //Checks if user is in the developer group
         //Can be used to trigger certain features

        if(extras.getBoolean("isDev")){
            this.recycleButton.setVisibility(View.VISIBLE);
            this.compostButton.setVisibility(View.VISIBLE);
            this.captureButton.setVisibility(View.GONE);

            Log.d("isDev", "True");
        } else {
            this.recycleButton.setVisibility(View.GONE);
            this.compostButton.setVisibility(View.GONE);
            this.captureButton.setVisibility(View.VISIBLE);

            Log.d("isDev", "False");
        }

        // ATTACH LISTENERS TO BUTTONS REGARDLESS OF THEIR VISIBILITY
        // COULD BE SIMPLIFIED TO ONE LISTENER
        this.captureButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openingCapture.buttonPressed = "unlabled/";
                captureImage();
            }
        });

        this.recycleButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openingCapture.buttonPressed = "recycle/";
                captureImage();
            }
        });

        this.compostButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openingCapture.buttonPressed = "compost/";
                captureImage();
            }
        });


        bmpSingleton.getINSTANCE();

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        getLastLocation();



        final SurfaceView camPreview = findViewById(R.id.cameraPreview);
        previewHolder = camPreview.getHolder();
        previewHolder.addCallback(this);
        previewHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        picCallback = new Camera.PictureCallback(){
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                Intent startPreview = new Intent(openingCapture.this, previewScreen.class);

                Bitmap decodeBitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                bmpSingleton.setBmp(decodeBitmap);

                // GET LOCATION DATA
                if (task.getResult() == null){
                    loc[0] = 0;
                    loc[1] = 0;
                } else {
                    loc[0] = task.getResult().getLatitude();
                    loc[1] = task.getResult().getLongitude();
                }

                // PUT EXTRAS
                startPreview.putExtra("path", saveToInternalStorage(decodeBitmap)); // NEED TO COME UP WITH DIFFERENT SOLUTION SO UI THREAD DOESNT HANG WHILE FILE I/0 IS HAPPENING
                startPreview.putExtra("location", loc);
                startPreview.putExtra("accessKey", accessKey);
                startPreview.putExtra("secretKey", secretKey);
                startPreview.putExtra("buttonPressed", openingCapture.buttonPressed+"/");

                // START NEXT VIEW (IN FUTURE MAYBE WE JUST HIDE THE CAMERA VIEW AND GET RID OF PREVIEW
                //THEN PUT LOADING WIDGET HERE)
                camera.release();
                startActivity(startPreview);
            }
        };
    }

    ////Will be removed if file i/o is cut out////
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
    ////////////////////////////////////////////////

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
                        public void onSuccess(final Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location == null) {
                                Log.d("LKL Err", "Can't get last known location, need to work out how to get our own");
                                /*
                                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                                    LocationManager mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                                    LocationListener mLocationListener = new LocationListener() {
                                        @Override
                                        public void onLocationChanged(Location l) {
                                            location.set(l);
                                        }

                                        @Override
                                        public void onStatusChanged(String provider, int status, Bundle extras) { }

                                        @Override
                                        public void onProviderEnabled(String provider) { }

                                        @Override
                                        public void onProviderDisabled(String provider) { }
                                    };
                                    mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, mLocationListener);
                                }
                                */
                            }
                        }
                    });
        }
    }
    
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {}
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {}
}
