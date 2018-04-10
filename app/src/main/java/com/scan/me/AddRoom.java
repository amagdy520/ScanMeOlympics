package com.scan.me;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddRoom extends AppCompatActivity {

    @BindView(R.id.room_number)
    EditText roomNumberEditText;
    @BindView(R.id.type)
    EditText typeEditText;
    @BindView(R.id.latitude)
    EditText latitudeEditText;
    @BindView(R.id.longitude)
    EditText longitudeEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_room);
        ButterKnife.bind(this);

    }

    @OnClick(R.id.done)
    void done() {
        String number = roomNumberEditText.getText().toString();
        String type = typeEditText.getText().toString();
        String longitude = longitudeEditText.getText().toString();
        String latitude = latitudeEditText.getText().toString();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child(Data.ROOMS).push().setValue(new Room(number,
                type, Double.parseDouble(latitude), Double.parseDouble(longitude)));

    }


}
