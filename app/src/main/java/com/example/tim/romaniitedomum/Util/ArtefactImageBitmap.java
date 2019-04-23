package com.example.tim.romaniitedomum.Util;

import android.util.Log;

public class ArtefactImageBitmap {

    private static final String TAG = "ArtefactImageBitmap";

    public static ArtefactImageBitmap instance;
    private byte[] byteArray;


    private ArtefactImageBitmap(){

    }

    public static ArtefactImageBitmap getInstance(){
        if (instance == null) {
            Log.d(TAG, "getInstance: instance created");
            instance = new ArtefactImageBitmap();
        }
        Log.d(TAG, "getInstance: instance exists");
        return instance;
    }

    public byte[] getByteArray() {
        return byteArray;
    }

    public void setByteArray(byte[] byteArray) {
        this.byteArray = byteArray;
    }
}
