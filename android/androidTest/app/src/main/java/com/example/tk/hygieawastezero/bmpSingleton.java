package com.example.tk.hygieawastezero;

import android.graphics.Bitmap;

public class bmpSingleton {

    private static bmpSingleton INSTANCE = null;
    static private Bitmap bmp;
    private bmpSingleton(){};

    public static bmpSingleton getINSTANCE() {
        if(INSTANCE == null){
            INSTANCE = new bmpSingleton();
        }
        return(INSTANCE);
    }

    public Bitmap getBmp() {
        return bmp;
    }

    static public void setBmp(Bitmap b) {
        bmp = b;
    }
}
