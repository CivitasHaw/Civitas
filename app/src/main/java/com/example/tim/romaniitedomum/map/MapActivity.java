package com.example.tim.romaniitedomum.map;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.example.tim.romaniitedomum.ApplicationClass;
import com.example.tim.romaniitedomum.MainActivity;
import com.example.tim.romaniitedomum.R;
import com.example.tim.romaniitedomum.Util.FilterHelper;
import com.example.tim.romaniitedomum.Util.Util;
import com.example.tim.romaniitedomum.artefact.Artefact;
import com.example.tim.romaniitedomum.artefact.ArtefactActivity;
import com.example.tim.romaniitedomum.impressum.ImpressumActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

/**
 * Created by TimStaats 21.02.2019
 */

public class MapActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        View.OnClickListener {

    private static final String TAG = "MapActivity";
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    public static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 8f;
    private static final float CLOSE_ZOOM = 15f;

    private DrawerLayout drawer;
    private NavigationView navigationView;
    private FloatingActionButton btnAddArtefact;

    private FilterHelper mFilterHelper;
    private boolean isMapFilterMenuExpanded = false;
    private ConstraintLayout mapFilterLayout;
    private RadioGroup radioGroupMapFilter;
    private RadioButton radioButtonMapFilter;
    private EditText etMapFilterAgeFrom, etMapFilterAgeTo;
    private Spinner spinnerMapFilterCategory;
    private Button btnMapFilterAnnoDominiFrom, btnMapFilterAnnoDominiTo, btnMapFilterApplyCategory,
            btnMapFilterApplyAge, btnMapFilterShowResultAtList, btnMapFilterReset;

    private boolean mLocationPermissionGranted = false;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        initDrawerAndToolbar();

        getLocationPermission();

        btnAddArtefact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToNewArtefactFragment("btnAddArtefact");
            }
        });
    }

    public void navigateToNewArtefactFragment(String origin) {
        ApplicationClass.mScreenPosition = getScreenPosition();
        Intent intent = new Intent(MapActivity.this, ArtefactActivity.class);
        intent.putExtra(getResources().getString(R.string.navigate_to_artefact_activity), "newArtefact");
        intent.putExtra(getResources().getString(R.string.origin), origin);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        setTitle("Civitas");
    }


    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    private void initDrawerAndToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        View header = navigationView.getHeaderView(0);
        TextView tvNavName = header.findViewById(R.id.text_nav_user_name);
        String name = ApplicationClass.user.getProperty(getResources().getString(R.string.backendless_property_name)).toString();
        tvNavName.setText(name);
        TextView tvNavEmail = header.findViewById(R.id.text_nav_user_email);
        String email = ApplicationClass.user.getEmail();
        tvNavEmail.setText(email);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_map);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        Intent intent;
        switch (menuItem.getItemId()) {
            case R.id.nav_artefacts:
                Log.d(TAG, "onNavigationItemSelected: artefacts");
                ApplicationClass.mScreenPosition = getScreenPosition();
                Log.d(TAG, "onMapReady: latLng: " + ApplicationClass.mScreenPosition);
                intent = new Intent(MapActivity.this, ArtefactActivity.class);
                intent.putExtra(getResources().getString(R.string.navigate_to_artefact_activity), "list");
                startActivity(intent);
                break;
            case R.id.nav_map:
                Log.d(TAG, "onNavigationItemSelected: map");
                //Toast.makeText(this, "Map clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_impressum:
                Log.d(TAG, "onNavigationItemSelected: impressum");
                //Toast.makeText(this, "Impressum clicked", Toast.LENGTH_SHORT).show();
                ApplicationClass.mScreenPosition = getScreenPosition();
                navigationView.setCheckedItem(R.id.nav_impressum);
                intent = new Intent(MapActivity.this, ImpressumActivity.class);
                intent.putExtra(Util.ORIGIN, Util.MAP_ACTIVITY);
                intent.putExtra("impressum", "impressum");
                startActivity(intent);
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
                        Toast.makeText(MapActivity.this, getResources().getText(R.string.user_logout), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MapActivity.this, MainActivity.class);
                        startActivity(intent);
                        MapActivity.this.finish();
                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {
                        Toast.makeText(MapActivity.this, "Error: " + fault.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

                break;
/*
            case R.id.nav_send:
                Log.d(TAG, "onNavigationItemSelected: send");
                Toast.makeText(this, "Send clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_share:
                Log.d(TAG, "onNavigationItemSelected: share");
                Toast.makeText(this, "Share clicked", Toast.LENGTH_SHORT).show();
                break;
*/
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // source
    // https://stackoverflow.com/questions/16056366/android-google-maps-how-to-get-the-area-which-is-currently-shown-in-screen-devi/16064924
    private LatLng getScreenPosition() {
        return mMap.getProjection().getVisibleRegion().latLngBounds.getCenter();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu_map, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.button_my_location:
                //Log.d(TAG, "onOptionsItemSelected: myLocation: clicked");
                moveCamera(new LatLng(ApplicationClass.mDeviceLocation.getLatitude(), ApplicationClass.mDeviceLocation.getLongitude()), CLOSE_ZOOM);
                break;
            case R.id.map_filter:
                if (!isMapFilterMenuExpanded) {
                    item.setIcon(R.drawable.ic_cancel_filter);
                    mapFilterLayout.setVisibility(View.VISIBLE);
                } else {
                    item.setIcon(R.drawable.ic_filter);
                    mapFilterLayout.setVisibility(View.GONE);
                }
                isMapFilterMenuExpanded = !isMapFilterMenuExpanded;
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    // for navigation_drawer needed
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.button_artefact_map_apply_age_filter:
                Log.d(TAG, "onClick: apply age filter clicked");
                break;
            case R.id.button_map_apply_category_filter:
                Log.d(TAG, "onClick: apply category filter clicked");
                break;
            case R.id.button_map_anno_domini_from:
                Log.d(TAG, "onClick: anno domini from clicked");
                break;
            case R.id.button_map_anno_domini_to:
                Log.d(TAG, "onClick: anno domini to clicked");
                break;
            case R.id.button_show_filter_as_a_list:
                Log.d(TAG, "onClick: show filter as a list clicked");
                break;
            case R.id.button_map_reset_filter:
                Log.d(TAG, "onClick: reset filter clicked");
                mFilterHelper.resetFilterHelperSettings();
                break;
        }
    }
    private void initMap() {
        btnAddArtefact = findViewById(R.id.floatingActionButton);
        mFilterHelper = FilterHelper.getInstance();
        mapFilterLayout = findViewById(R.id.layout_filter_map);
        radioGroupMapFilter = mapFilterLayout.findViewById(R.id.radioGroup_map);
        etMapFilterAgeFrom = mapFilterLayout.findViewById(R.id.edit_map_artefact_age_from);
        etMapFilterAgeTo = mapFilterLayout.findViewById(R.id.edit_map_artefact_age);
        spinnerMapFilterCategory = mapFilterLayout.findViewById(R.id.spinner_map_filter_category);
        btnMapFilterApplyCategory = mapFilterLayout.findViewById(R.id.button_map_apply_category_filter);
        btnMapFilterApplyAge = mapFilterLayout.findViewById(R.id.button_artefact_map_apply_age_filter);
        btnMapFilterAnnoDominiFrom = mapFilterLayout.findViewById(R.id.button_map_anno_domini_from);
        btnMapFilterAnnoDominiTo = mapFilterLayout.findViewById(R.id.button_map_anno_domini_to);
        btnMapFilterShowResultAtList = mapFilterLayout.findViewById(R.id.button_show_filter_as_a_list);
        btnMapFilterReset = mapFilterLayout.findViewById(R.id.button_map_reset_filter);

        btnMapFilterApplyAge.setOnClickListener(this);
        btnMapFilterApplyCategory.setOnClickListener(this);
        btnMapFilterAnnoDominiFrom.setOnClickListener(this);
        btnMapFilterAnnoDominiTo.setOnClickListener(this);
        btnMapFilterShowResultAtList.setOnClickListener(this);
        btnMapFilterReset.setOnClickListener(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                Log.d(TAG, "onMapReady: map is ready");
                //Toast.makeText(MapActivity.this, getResources().getText(R.string.toast_map_ready), Toast.LENGTH_SHORT).show();
                mMap = googleMap;

                // source
                // https://stackoverflow.com/questions/17549372/how-to-get-click-event-of-the-marker-text
                mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick(Marker marker) {
                        Log.d(TAG, "onInfoWindowClick: ");
                        ApplicationClass.mScreenPosition = getScreenPosition();
                        Artefact tempArtefact = (Artefact) marker.getTag();
                        Intent intent = new Intent(MapActivity.this, ArtefactActivity.class);
                        intent.putExtra(getResources().getString(R.string.navigate_to_artefact_activity), "markerClick");
                        intent.putExtra(Util.ARTEFACT_OBJECT_ID, tempArtefact.getObjectId());
                        startActivity(intent);
                    }
                });

                if (getIntent().getStringExtra(Util.ORIGIN).equals(Util.FILTER) && !ApplicationClass.mFilteredArtefactList.isEmpty()) {
                    for (int i = 0; i < ApplicationClass.mFilteredArtefactList.size(); i++) {
                        createMarker(ApplicationClass.mFilteredArtefactList.get(i));
                    }
                } else {
                    if (ApplicationClass.mArtefactList.size() > 0) {
                        for (int i = 0; i < ApplicationClass.mArtefactList.size(); i++) {
                            createMarker(ApplicationClass.mArtefactList.get(i));
                        }
                    }
                }

                mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                    @Override
                    public void onMapLongClick(LatLng latLng) {
                        ApplicationClass.mArtefactLatLng = latLng;
                        ApplicationClass.mScreenPosition = getScreenPosition();
                        Log.d(TAG, "onMapLongClick: lat: " + ApplicationClass.mArtefactLatLng.latitude + " lng: " + ApplicationClass.mArtefactLatLng.longitude);
                        navigateToNewArtefactFragment("onMapLongClick");

                    }
                });

                if (mLocationPermissionGranted) {
                    getDeviceLocation();

                    if (ActivityCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MapActivity.this,
                            Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    mMap.setMyLocationEnabled(true);
                    //mMap.getUiSettings().setMyLocationButtonEnabled(false);
                }
            }
        });
    }


    protected Marker createMarker(Artefact artefact) {

        int markerArtefactIcon = getMarkerArtefactIcon(artefact);
        Marker marker = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(artefact.getLatitude(), artefact.getLongitude()))
                .title(artefact.getArtefactName())
                .snippet(artefact.getArtefactDescription())
                .icon(bitmapDescriptorFromVector(this, markerArtefactIcon)));
        marker.setTag(artefact);

        return marker;
    }

    // if editing icons, also edit -> NewArtefactFragment -> populateCategoryList()
    private int getMarkerArtefactIcon(Artefact artefact) {
        //Icons from flaticon.com. They have to be mentioned in the app.

        int markerArtefactIcon;
        switch (artefact.getCategoryName()) {
            case Util.CATEGORY_BASILIKA:
                markerArtefactIcon = R.drawable.ic_map_basilica;
                break;
            case Util.CATEGORY_BOGEN:
                markerArtefactIcon = R.drawable.ic_map_bogen;
                break;
            case Util.CATEGORY_CHRISTENTUM:
                markerArtefactIcon = R.drawable.ic_map_christentum;
                break;
            case Util.CATEGORY_GRABSTAETTE:
                markerArtefactIcon = R.drawable.ic_map_grabstaette;
                break;
            case Util.CATEGORY_GRUENDUNGSMYTHOS:
                markerArtefactIcon = R.drawable.ic_map_grundungsmythos;
                break;
            case Util.CATEGORY_INFRASTRUKTUR:
                markerArtefactIcon = R.drawable.ic_map_infrastruktur;
                break;
            case Util.CATEGORY_KULTSTAETTE:
                markerArtefactIcon = R.drawable.ic_map_kultstaette;
                break;
            case Util.CATEGORY_PLATZANLAGE:
                markerArtefactIcon = R.drawable.ic_map_platzanlage;
                break;
            case Util.CATEGORY_SPIELSTAETTE:
                markerArtefactIcon = R.drawable.ic_map_spielstaette;
                break;
            case Util.CATEGORY_THERME:
                markerArtefactIcon = R.drawable.ic_map_therme;
                break;
            case Util.CATEGORY_WOHNKOMPLEX:
                markerArtefactIcon = R.drawable.ic_map_wohnkomplex;
                break;
            case Util.CATEGORY_POLITISCHE_INSTITUTION:
                markerArtefactIcon = R.drawable.ic_map_politische_institution;
                break;
            case "SaltAndPepper":
                markerArtefactIcon = R.drawable.ic_salt_and_pepper;
                break;
            default:
                markerArtefactIcon = R.drawable.ic_map_default_marker;
                break;
        }
        return markerArtefactIcon;
    }

    /**
     * bitmapDescriptorFromVector()
     * https://stackoverflow.com/questions/42365658/custom-marker-in-google-maps-in-android-with-vector-asset-icon
     */

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    private void getDeviceLocation() {
        Log.d(TAG, "getDeviceLocation: getting the devices current location");
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        try {
            if (mLocationPermissionGranted) {
                final Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "onComplete: found location");
                            Location currentLocation = (Location) task.getResult();
                            ApplicationClass.mDeviceLocation = currentLocation;

                            String origin = getIntent().getStringExtra(getResources().getString(R.string.origin));
                            Log.d(TAG, "onComplete: origin: " + origin);
                            switch (origin) {
                                case Util.ARTEFACT_DETAIL_FRAGMENT:
                                    Log.d(TAG, "onComplete: ArtefactDetailFragment");
                                    moveCamera(new LatLng(ApplicationClass.mArtefact.getLatitude(),
                                                    ApplicationClass.mArtefact.getLongitude()),
                                            CLOSE_ZOOM);
                                    break;
                                case Util.NEW_ARTEFACT_FRAGMENT:
                                    Log.d(TAG, "onComplete: NewArtefactFragment");
                                    moveCamera(new LatLng(ApplicationClass.mArtefact.getLatitude(),
                                                    ApplicationClass.mArtefact.getLongitude()),
                                            CLOSE_ZOOM);
                                    break;
                                case Util.LOGIN_FRAGMENT:
                                    Log.d(TAG, "onComplete: LoginFragment");
                                    moveCamera(new LatLng(ApplicationClass.mDeviceLocation.getLatitude(),
                                                    ApplicationClass.mDeviceLocation.getLongitude()),
                                            DEFAULT_ZOOM);
                                    break;
                                case Util.ORIGIN_DELETE_ARTEFACT:
                                    Log.d(TAG, "onComplete: deleteArtefact");
                                    moveCamera(ApplicationClass.mTempArtefactLatLng, DEFAULT_ZOOM);
                                    break;
                                case Util.ARTEFACT_LIST_FRAGMENT:
                                    Log.d(TAG, "onComplete: artefactList");
                                    moveCamera(ApplicationClass.mScreenPosition, DEFAULT_ZOOM);
                                    break;
                                case Util.IMPRESSUM_ACTIVITY:
                                    Log.d(TAG, "onComplete: impressumActivity");
                                    navigationView.setCheckedItem(R.id.nav_map);
                                    moveCamera(ApplicationClass.mScreenPosition, DEFAULT_ZOOM);
                                    break;
                                case Util.ARTEFACT_ACTIVITY:
                                    Log.d(TAG, "onComplete: impressumActivity from artefacts");
                                    navigationView.setCheckedItem(R.id.nav_map);
                                    moveCamera(ApplicationClass.mScreenPosition, DEFAULT_ZOOM);
                                    break;
                                case Util.EDIT_ARTEFACT_FRAGMENT:
                                    Log.d(TAG, "onComplete: editArtefact");
                                    navigationView.setCheckedItem(R.id.nav_map);
                                    moveCamera(ApplicationClass.mArtefactLatLng, CLOSE_ZOOM);
                                    break;
                                case Util.FILTER:
                                    navigationView.setCheckedItem(R.id.nav_map);
                                    moveCamera(new LatLng(ApplicationClass.mDeviceLocation.getLatitude(),
                                                    ApplicationClass.mDeviceLocation.getLongitude()),
                                            DEFAULT_ZOOM);
                                    break;
                                default:
                                    Log.d(TAG, "onComplete: default");
                                    // HAW coordinates
                                    moveCamera(new LatLng(53.556617, 10.022582), DEFAULT_ZOOM);
                                    break;
                            }

                        } else {
                            Log.d(TAG, "onComplete: current location is null");
                            Toast.makeText(MapActivity.this, getResources().getText(R.string.toast_location_null), Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }

        } catch (SecurityException e) {
            Log.e(TAG, "getDeviceLocation: " + e.getMessage());
        }
    }


    private void moveCamera(LatLng latLng, float zoom) {
         /**
         *     credits
         * https://stackoverflow.com/questions/32161757/how-to-animate-the-camera-to-a-specified-location-in-google-maps-v2-for-android
         */
        //Log.d(TAG, "moveCamera: moving camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude);
        CameraUpdate location = CameraUpdateFactory.newLatLngZoom(latLng, zoom);
        mMap.animateCamera(location);
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }

    private void getLocationPermission() {
        String[] permissions = {FINE_LOCATION, COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(), COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionGranted = true;
                initMap();
            } else {
                ActivityCompat.requestPermissions(this,
                        permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {
            ActivityCompat.requestPermissions(this,
                    permissions,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;

        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            mLocationPermissionGranted = false;
                            return;
                        }
                    }
                    mLocationPermissionGranted = true;
                    initMap();
                }
            }
        }
    }

}
