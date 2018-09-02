package com.example.tk.hygieawastezero;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class previewScreen extends AppCompatActivity {

    int REQUEST_EXIT = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_screen);

        loadImageFromStorage("/data/user/0/com.example.tk.hygieawastezero/app_imageDir/");

        final Button retake = findViewById(R.id.retake);
        retake.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code to get rid of previous picture to prevent memory leak, maybe?
                //old: setContentView(R.layout.activity_opening_capture);
                //old 2: Intent startOpening = new Intent(previewScreen.this, openingCapture.class);
                //old 2: startActivity(startOpening);
                finish();
            }
        });

        final Button confirm = findViewById(R.id.confirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code to send something to server and get ID of item back by next screen
                //old: setContentView(R.layout.activity_results_screen);
                Intent startResults = new Intent(previewScreen.this, resultsScreen.class);
                startActivityForResult(startResults, REQUEST_EXIT);
            }
        });

    }
    private void loadImageFromStorage(String path)
    {
        try {
            File f=new File(path, "pic.jpg");
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            ImageView img = findViewById(R.id.picDisplay);
            img.setImageBitmap(b);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
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
}
