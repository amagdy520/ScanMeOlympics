package com.scan.me;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TimePicker;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ReserveActivity extends AppCompatActivity {


    @BindView(R.id.year)
    AutoCompleteTextView yearAutoCompleteTextView;
    @BindView(R.id.department)
    AutoCompleteTextView departmentAutoCompleteTextView;
    @BindView(R.id.section)
    EditText sectionEditText;
    @BindView(R.id.from)
    EditText fromEditText;
    @BindView(R.id.to)
    EditText toEditText;
    @BindView(R.id.lecture_name)
    EditText lectureNameEditText;
    private List<String> years = new ArrayList<>();
    private List<String> departments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve);
        ButterKnife.bind(this);
        getDataYearsData();


    }

    @OnClick(R.id.from)
    void getFromTime() {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                fromEditText.setText(selectedHour + ":" + selectedMinute);
            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }

    @OnClick(R.id.to)
    void geToTime() {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                toEditText.setText(selectedHour + ":" + selectedMinute);
            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }

    @OnClick(R.id.reserve_button)
    void reserve() {
        String year = yearAutoCompleteTextView.getText().toString();
        String department = departmentAutoCompleteTextView.getText().toString();
        String section = sectionEditText.getText().toString();
        String from = fromEditText.getText().toString();
        String to = toEditText.getText().toString();
        String name = lectureNameEditText.getText().toString();
        String hash = year + "-" + department + "-" + section;

        // TODO : add Validation
        Reservation reservation = new Reservation(name, null, null, from, to, hash, false);
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putParcelable(RoomDetails.ROOM_RESERVE, reservation);
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finish();
    }


    private void getDataYearsData() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child(Data.DATA).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot yearDataSnapshot : dataSnapshot.child(Data.YEARS).getChildren()) {
                    years.add(yearDataSnapshot.getKey());
                }
                for (DataSnapshot departmentSnapshot : dataSnapshot.child(Data.DEPARTMENTS).getChildren()) {
                    departments.add(departmentSnapshot.getKey());
                }

                setDataToAutoComplete();


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setDataToAutoComplete() {
        ArrayAdapter<String> yearsAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, years);
        yearAutoCompleteTextView.setAdapter(yearsAdapter);
        ArrayAdapter<String> departmentAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, departments);
        departmentAutoCompleteTextView.setAdapter(departmentAdapter);
        setOnClick(yearAutoCompleteTextView);
        setOnClick(departmentAutoCompleteTextView);
        Log.e("Size", years.size() + "");


    }

    void setOnClick(final AutoCompleteTextView autoCompleteTextView) {
        autoCompleteTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autoCompleteTextView.showDropDown();
            }
        });
    }
}
