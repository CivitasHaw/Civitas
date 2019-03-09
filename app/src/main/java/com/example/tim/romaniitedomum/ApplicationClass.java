package com.example.tim.romaniitedomum;

import android.app.Application;
import android.location.Location;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;

/**
 * Created by TimStaats 21.02.2019
 */

public class ApplicationClass extends Application {

    public static final String APPLICATION_ID = "7E4DF0B7-34EF-5403-FF11-6C7F89ED2500";
    public static final String API_KEY = "117E75C0-D263-CA96-FF43-292245B54500";
    public static final String SERVER_URL = "https://api.backendless.com";

    public static BackendlessUser user;
    public static Location mLocation;
    public static Artefact mArtefact;

    @Override
    public void onCreate() {
        super.onCreate();

        Backendless.setUrl( SERVER_URL );
        Backendless.initApp( getApplicationContext(),
                APPLICATION_ID,
                API_KEY );

    }
}
