package com.example.tim.romaniitedomum;

import android.app.Application;
import android.location.Location;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.example.tim.romaniitedomum.artefact.Artefact;
import com.google.android.gms.maps.model.LatLng;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by TimStaats 21.02.2019
 */

public class ApplicationClass extends Application {

    public static final String APPLICATION_ID = "4152780E-4BCA-A0EA-FFA2-46A67C876B00";
    public static final String API_KEY = "B2B550F6-08D6-FDCA-FFC4-77C056327000";
    public static final String SERVER_URL = "https://api.backendless.com";

    public static BackendlessUser user;
    public static Location mDeviceLocation;
    public static LatLng mTempArtefactLatLng;
    public static LatLng mArtefactLatLng;
    public static LatLng mScreenPosition;
    public static Artefact mArtefact;
    public static List<Artefact> mArtefactList;
    public static int position;


    @Override
    public void onCreate() {
        super.onCreate();

        Backendless.setUrl( SERVER_URL );
        Backendless.initApp( getApplicationContext(),
                APPLICATION_ID,
                API_KEY );

    }
}
