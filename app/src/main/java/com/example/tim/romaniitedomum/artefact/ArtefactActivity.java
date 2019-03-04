package com.example.tim.romaniitedomum.artefact;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.example.tim.romaniitedomum.R;

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
            fragmentSwitcher2(new NewArtefactFragment(), false, "");
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
}
