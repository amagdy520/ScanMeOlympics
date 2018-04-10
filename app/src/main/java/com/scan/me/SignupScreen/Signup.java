package com.scan.me.SignupScreen;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.scan.me.Data;
import com.scan.me.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Signup extends Activity
{
    @BindView(R.id.signup_year)
    AutoCompleteTextView yearAutoCompleteTextView;
    @BindView(R.id.signup_department)
    AutoCompleteTextView departmentAutoCompleteTextView;
    @BindView(R.id.signup_section)
    EditText sectionEditText;

    @BindView (R.id.singup_radiogroup)
    RadioGroup radioGroup;

    List<String> years = new ArrayList<> ();
    List<String> departments = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_signup);
        ButterKnife.bind(this);
        onRadioChange();
        getYearData();
    }

    private void getYearData()
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child (Data.DATA).addListenerForSingleValueEvent (new ValueEventListener ()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                for ( DataSnapshot yearDataSnapshot: dataSnapshot.child (Data.YEARS).getChildren ())
                {
                    years.add (yearDataSnapshot.getKey());
                }
                for (DataSnapshot departmentSnapshot : dataSnapshot.child(Data.DEPARTMENTS).getChildren())
                {
                    departments.add(departmentSnapshot.getKey());
                }
                setDataToAutoComplete();
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        });
    }

    private void setDataToAutoComplete()
    {
        ArrayAdapter<String> yearsAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, years);
        yearAutoCompleteTextView.setAdapter(yearsAdapter);
        ArrayAdapter<String> departmentAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, departments);
        departmentAutoCompleteTextView.setAdapter(departmentAdapter);
        setOnClick(yearAutoCompleteTextView);
        setOnClick(departmentAutoCompleteTextView);
    }

    private void setOnClick(final AutoCompleteTextView autoCompleteTextView)
    {
        autoCompleteTextView.setOnClickListener (new View.OnClickListener ()
        {
            @Override
            public void onClick(View v)
            {
                autoCompleteTextView.showDropDown ();
            }
        });
    }

    @OnClick(R.id.choose_user)
    void choose_user()
    {

    }


    private void onRadioChange()
    {
        radioGroup.setOnCheckedChangeListener (new RadioGroup.OnCheckedChangeListener ()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                if(checkedId==R.id.singup_student)
                {
                    yearAutoCompleteTextView.setVisibility (View.VISIBLE);
                    departmentAutoCompleteTextView.setVisibility (View.VISIBLE);
                    sectionEditText.setVisibility (View.VISIBLE);
                }
                else
                {
                    yearAutoCompleteTextView.setVisibility (View.GONE);
                    departmentAutoCompleteTextView.setVisibility (View.GONE);
                    sectionEditText.setVisibility (View.GONE);
                }
            }
        });
    }
}
