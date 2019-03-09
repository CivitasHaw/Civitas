package com.example.tim.romaniitedomum.artefact;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.backendless.geo.GeoPoint;
import com.example.tim.romaniitedomum.ApplicationClass;
import com.example.tim.romaniitedomum.Artefact;
import com.example.tim.romaniitedomum.R;
import com.example.tim.romaniitedomum.map.MapActivity;

import java.io.ByteArrayOutputStream;

/**
 * Created by TimStaats 03.03.2019
 */

public class ArtefactActivity extends AppCompatActivity {

    private static final String TAG = "ArtefactActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artefact);

        Intent intent = getIntent();
        String content = intent.getExtras().getString("artefacts");
        if (content.equals("list")){
            fragmentSwitcher2(new ArtefactListFragment(), true, "ArtefactListFragment");
        } else {
            NewArtefactFragment newArtefactFragment = new NewArtefactFragment();
            Bundle args = new Bundle();
            args.putDouble("latitude", ApplicationClass.mLocation.getLatitude());
            args.putDouble("longitude", ApplicationClass.mLocation.getLongitude());
            fragmentSwitcher2(newArtefactFragment, false, "");
        }
    }

    public void fragmentSwitcher2(Fragment fragment, boolean toBackstack, String name){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container_artefact_fragment, fragment);
        if(toBackstack){
            transaction.addToBackStack(name);
        } else {

        }
        transaction.commit();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bitmap = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        Bundle args = new Bundle();
        args.putByteArray("image", byteArray);
        NewArtefactFragment fragment = new NewArtefactFragment();
        fragment.setArguments(args);

        fragmentSwitcher2(fragment, false, "");
    }
}
