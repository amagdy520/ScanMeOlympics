package com.scan.me;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddUser extends AppCompatActivity {

    @BindView(R.id.year)
    AutoCompleteTextView yearAutoCompleteTextView;
    @BindView(R.id.department)
    AutoCompleteTextView departmentAutoCompleteTextView;
    @BindView(R.id.section)
    EditText sectionEditText;
    @BindView(R.id.user_number)
    EditText numberEditText;
    @BindView(R.id.user_name)
    EditText nameEditText;

    List<String> years = new ArrayList<>();
    List<String> departments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);
        ButterKnife.bind(this);
        getDataYearsData();
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
        Log.e("Size",years.size()+"");


    }
    void setOnClick(final AutoCompleteTextView autoCompleteTextView){
        autoCompleteTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autoCompleteTextView.showDropDown();
            }
        });
    }


    @OnClick(R.id.done)
    void createUser() {
        String year=yearAutoCompleteTextView.getText().toString();
        String depratment=departmentAutoCompleteTextView.getText().toString();
        String name=nameEditText.getText().toString();
        String number=nameEditText.getText().toString();
        String section=nameEditText.getText().toString();


    }
}
