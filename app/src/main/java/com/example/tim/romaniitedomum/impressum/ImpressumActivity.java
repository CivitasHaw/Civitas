package com.example.tim.romaniitedomum.impressum;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.tim.romaniitedomum.R;
import com.example.tim.romaniitedomum.Util.Util;
import com.example.tim.romaniitedomum.map.MapActivity;

/**
 * created by Tim Staats 26.04.2019
 */

public class ImpressumActivity extends AppCompatActivity {

    private static final String TAG = "ImpressumActivity";
    private Intent originIntent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_impressum);
        setTitle("Impressum");
        originIntent = new Intent(ImpressumActivity.this, MapActivity.class);
        originIntent.putExtra(getResources().getString(R.string.origin), Util.IMPRESSUM_ACTIVITY);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(originIntent);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
