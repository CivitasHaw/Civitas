package com.example.tim.romaniitedomum.artefact;


import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tim.romaniitedomum.ApplicationClass;
import com.example.tim.romaniitedomum.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by TimStaats 03.03.2019
 */

public class NewArtefactFragment extends Fragment {

    private static final String TAG = "NewArtefactFragment";

    private TextView textView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_artefact, container, false);

        initNewArtefact(view);

        List<Address> adr = new ArrayList<>();
        Geocoder geocoder = new Geocoder(getContext(), Locale.GERMAN);
        String name = (String) ApplicationClass.user.getProperty("name");
        String street = "";
        Location location = ApplicationClass.mLocation;
        try {
            adr = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            street = adr.get(0).getAddressLine(0);
        } catch (IOException e) {
            e.printStackTrace();
        }


        textView.setText(name + "\n" + "Address: " + street);
        return view;
    }

    private void initNewArtefact(View view) {
        textView = view.findViewById(R.id.textView);
    }
}
