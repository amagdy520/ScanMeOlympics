package com.scan.me;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.scan.me.User.User;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddUser extends AppCompatActivity {
    public static final int ADMIN = 0;
    public static final int STUDENT = 1;
    public static final int TUTOR = 2;
    public static final int FROM_FOLDER=223;
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
    @BindView(R.id.admins)
    TextView adminsTextView;
    @BindView(R.id.students)
    TextView studentsTextView;
    @BindView(R.id.tutors)
    TextView tutorsTextView;

    List<String> years = new ArrayList<>();
    List<String> departments = new ArrayList<>();
    private int  selected=STUDENT;

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

    void setOnClick(final AutoCompleteTextView autoCompleteTextView) {
        autoCompleteTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autoCompleteTextView.showDropDown();
            }
        });
    }




    @OnClick(R.id.done)
    void createUser() {
       if(selected==STUDENT){
           if(Validation.checkValidation(yearAutoCompleteTextView)
                   &&Validation.checkValidation(departmentAutoCompleteTextView)
                   &&Validation.checkValidation(sectionEditText)
                   &&Validation.checkValidation(numberEditText)
                   &&Validation.checkValidation(nameEditText)){
               doUploadData();
           }
       }else {
           if(Validation.checkValidation(nameEditText)){
               doUploadData();
           }
       }



    }
    void doUploadData(){
        String year = yearAutoCompleteTextView.getText().toString();
        String department = departmentAutoCompleteTextView.getText().toString();
        String name = nameEditText.getText().toString();
        String number = numberEditText.getText().toString();
        String section = sectionEditText.getText().toString();
        String type;
        String hash;

        if(selected==STUDENT){
            type= User.STUDENT;
            hash = year + "-" + department + "-" + section;

        }else if(selected==ADMIN){
            type=User.ADMIN;
            hash=type;
        }else {
            type=User.TUTOR;
            hash=type;
        }
        User user =
                new User(null,null,
                        name, number,null, null, year, department, section,type , hash);
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference();
        reference.keepSynced(true);
        reference.child(Data.USERS).push().setValue(user);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==FROM_FOLDER && resultCode==RESULT_OK){
            String path =data.getData().toString();
            Log.e("Path",path);
        }
    }
    @OnClick(R.id.tutors)
    void selectTutors() {
        tutorsTextView.setTextColor(Color.parseColor("#FFD700"));
        tutorsTextView.setBackgroundColor(Color.parseColor("#081c24"));
        studentsTextView.setTextColor(Color.parseColor("#081c24"));
        studentsTextView.setBackgroundColor(Color.parseColor("#00010101"));
        adminsTextView.setTextColor(Color.parseColor("#081c24"));
        adminsTextView.setBackgroundColor(Color.parseColor("#00010101"));
        yearAutoCompleteTextView.setVisibility(View.GONE);
        departmentAutoCompleteTextView.setVisibility(View.GONE);
        sectionEditText.setVisibility(View.GONE);
        numberEditText.setVisibility(View.GONE);
        selected = TUTOR;
    }

    @OnClick(R.id.admins)
    void selectAdmins() {
        adminsTextView.setTextColor(Color.parseColor("#FFD700"));
        adminsTextView.setBackgroundResource(R.drawable.left_round);
        studentsTextView.setTextColor(Color.parseColor("#081c24"));
        studentsTextView.setBackgroundColor(Color.parseColor("#00010101"));
        tutorsTextView.setTextColor(Color.parseColor("#081c24"));
        tutorsTextView.setBackgroundColor(Color.parseColor("#00010101"));
        yearAutoCompleteTextView.setVisibility(View.GONE);
        departmentAutoCompleteTextView.setVisibility(View.GONE);
        sectionEditText.setVisibility(View.GONE);
        numberEditText.setVisibility(View.GONE);
        selected = ADMIN;

    }

    @OnClick(R.id.students)
    void selectStudents() {
        studentsTextView.setTextColor(Color.parseColor("#FFD700"));
        studentsTextView.setBackgroundResource(R.drawable.right_round);
        tutorsTextView.setTextColor(Color.parseColor("#081c24"));
        tutorsTextView.setBackgroundColor(Color.parseColor("#00010101"));
        adminsTextView.setTextColor(Color.parseColor("#081c24"));
        adminsTextView.setBackgroundColor(Color.parseColor("#00010101"));
        yearAutoCompleteTextView.setVisibility(View.VISIBLE);
        departmentAutoCompleteTextView.setVisibility(View.VISIBLE);
        sectionEditText.setVisibility(View.VISIBLE);
        numberEditText.setVisibility(View.VISIBLE);
        selected = STUDENT;

    }
}
