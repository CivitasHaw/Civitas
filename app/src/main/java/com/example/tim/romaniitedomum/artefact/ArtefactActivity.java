package com.example.tim.romaniitedomum.artefact;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.example.tim.romaniitedomum.ApplicationClass;
import com.example.tim.romaniitedomum.MainActivity;
import com.example.tim.romaniitedomum.R;
import com.example.tim.romaniitedomum.map.MapActivity;

import java.io.ByteArrayOutputStream;

/**
 * Created by TimStaats 03.03.2019
 */

public class ArtefactActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "ArtefactActivity";

    private DrawerLayout drawer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artefact);

        initDrawerAndToolbar();

        Intent intent = getIntent();
        String content = intent.getStringExtra("artefacts");
        if (content.equals("list")){
            fragmentSwitcher2(new ArtefactListFragment(), true, "ArtefactListFragment");
        } else if (content.equals("markerClick")){
            ArtefactDetailFragment artefactDetailFragment = new ArtefactDetailFragment();
            Bundle b = new Bundle();
            b.putString("artefactName", intent.getStringExtra("artefactName"));
            b.putDouble("latitude", intent.getDoubleExtra("latitude", 0.0));
            b.putDouble("longitude", intent.getDoubleExtra("longitude", 0.0));
            artefactDetailFragment.setArguments(b);
            fragmentSwitcher2(artefactDetailFragment, false,"ArtefactDetailFragment");
        } else {
            NewArtefactFragment newArtefactFragment = new NewArtefactFragment();
            Bundle args = new Bundle();
            String content2 = intent.getStringExtra(getResources().getString(R.string.origin));
            if (content2.equals("btnAddArtefact")){ // creating Artefact at device location
                args.putString(getResources().getString(R.string.origin), "btnAddArtefact");
                args.putDouble("latitude", ApplicationClass.mDeviceLocation.getLatitude());
                args.putDouble("longitude", ApplicationClass.mDeviceLocation.getLongitude());
            } else if (content2.equals("onMapLongClick")){ // creating Artefact at marker location
                args.putString(getResources().getString(R.string.origin), "onMapLongClick");
                args.putDouble("latitude", ApplicationClass.mArtefactLatLng.latitude);
                args.putDouble("longitude", ApplicationClass.mArtefactLatLng.longitude);
            }
            newArtefactFragment.setArguments(args);
            fragmentSwitcher2(newArtefactFragment, false, "");
        }
    }

    // for navigation_drawer needed
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            startActivity(new Intent(ArtefactActivity.this, MapActivity.class));
        }

    }

    private void initDrawerAndToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout_artefact_activity);
        NavigationView navigationView = findViewById(R.id.nav_view);

        View header = navigationView.getHeaderView(0);
        TextView tvNavName = header.findViewById(R.id.text_nav_user_name);
        String name = ApplicationClass.user.getProperty("name").toString();
        tvNavName.setText(name);
        TextView tvNavEmail = header.findViewById(R.id.text_nav_user_email);
        String email = ApplicationClass.user.getEmail();
        tvNavEmail.setText(email);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_artefacts);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
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
        String origin = "camera";
        args.putString(getResources().getString(R.string.origin), origin);
        args.putByteArray("image", byteArray);
        NewArtefactFragment fragment = new NewArtefactFragment();
        fragment.setArguments(args);

        fragmentSwitcher2(fragment, false, "");
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_artefacts:
                Log.d(TAG, "onNavigationItemSelected: artefacts");
                Toast.makeText(this, "Artefacts clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_map:
                Log.d(TAG, "onNavigationItemSelected: map");
                Intent intent = new Intent(ArtefactActivity.this, MapActivity.class);
                intent.putExtra("artefacts", "list");
                startActivity(intent);
                break;
            case R.id.nav_info:
                Log.d(TAG, "onNavigationItemSelected: impressum");
                Toast.makeText(this, "Impressum clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_settings:
                Log.d(TAG, "onNavigationItemSelected: settings");
                Toast.makeText(this, "Settings clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_logout:
                Backendless.UserService.logout(new AsyncCallback<Void>() {
                    @Override
                    public void handleResponse(Void response) {
                        Log.d(TAG, "handleResponse: logout");
                        Toast.makeText(ArtefactActivity.this, getResources().getText(R.string.user_logout), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ArtefactActivity.this, MainActivity.class);
                        startActivity(intent);
                        ArtefactActivity.this.finish();
                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {
                        Toast.makeText(ArtefactActivity.this, "Error: " + fault.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

                break;
            case R.id.nav_send:
                Log.d(TAG, "onNavigationItemSelected: send");
                Toast.makeText(this, "Send clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_share:
                Log.d(TAG, "onNavigationItemSelected: share");
                Toast.makeText(this, "Share clicked", Toast.LENGTH_SHORT).show();
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
