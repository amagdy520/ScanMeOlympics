package com.scan.me;

import android.*;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddRoom extends AppCompatActivity {
    @BindView(R.id.room_number)
    EditText roomNumberEditText;
    @BindView(R.id.room_floor)
    EditText floorEditText;
    @BindView(R.id.type)
    AutoCompleteTextView typeEditText;
    @BindView(R.id.latitude)
    EditText latitudeEditText;
    @BindView(R.id.longitude)
    EditText longitudeEditText;
    String[] types = {Room.HALL, Room.LAB, Room.STAGE};
    private FusedLocationProviderClient mFusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_room);
        ButterKnife.bind(this);


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, types);
        typeEditText.setAdapter(adapter);
        typeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                typeEditText.showDropDown();
            }
        });
    }

    @OnClick(R.id.done)
    void done() {
        if (Validation.checkValidation(roomNumberEditText)
                && Validation.checkValidation(floorEditText)
                && Validation.checkValidation(typeEditText)
                && Validation.checkValidation(longitudeEditText)
                && Validation.checkValidation(latitudeEditText)) {
            String number = roomNumberEditText.getText().toString();
            String floor = floorEditText.getText().toString();
            String type = typeEditText.getText().toString();
            String longitude = longitudeEditText.getText().toString();
            String latitude = latitudeEditText.getText().toString();

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
            reference.child(Data.ROOMS).push().setValue(new Room(number,type, floor,
                     Double.parseDouble(latitude), Double.parseDouble(longitude)));
            finish();

        }


    }

    @OnClick(R.id.my_location)
    void getDeviceLocation() {
        if (ActivityCompat
                .checkSelfPermission(this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION
                )
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION,
                            android.Manifest.permission.ACCESS_FINE_LOCATION},
                    1
            );


            return;
        }
        try {
            mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
            Task locationResult = mFusedLocationProviderClient.getLastLocation();

            locationResult.addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()) {
                        // Set the map's camera position to the current location of the device.
                        Location mLastLocation = (Location) task.getResult();

                        latitudeEditText.setText(mLastLocation.getLatitude() + "");
                        longitudeEditText.setText(mLastLocation.getLongitude() + "");


                    }
                }
            });

        } catch (
                SecurityException e)

        {
            Log.e("Exception: %s", e.getMessage());
        }

    }

}
