package com.scan.me.User;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.scan.me.Data;
import com.scan.me.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditUser extends AppCompatActivity
{
    @BindView(R.id.edit_user_name)
    EditText edit_nameTextView;
    @BindView(R.id.edit_user_type)
    EditText edit_typeTextView;
    @BindView(R.id.edit_user_email)
    EditText edit_user_email;
    @BindView(R.id.edit_user_section)
    EditText edit_user_section;
    @BindView(R.id.edit_user_year)
    EditText edit_user_year;
    @BindView(R.id.edit_user_department)
    EditText edit_user_department;

    @BindView(R.id.section_text)
    TextView section_text;
    @BindView(R.id.department_text)
    TextView department_text;
    @BindView(R.id.year_text)
    TextView year_text;

    String userId;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_edit_user);

        ButterKnife.bind(this);

        Bundle data = getIntent().getExtras();
        userId = data.getString ("USER_ID");

        getUserData ();


    }

    private void getUserData()
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.keepSynced(true);
        reference.child(Data.USERS).child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                setUserData();
                setV ();
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {
            }
        });
    }

    private void setUserData()
    {
        edit_nameTextView.setText(user.getName());
        edit_typeTextView.setText(user.getType());
        edit_user_section.setText (user.getSection ());
        edit_user_year.setText (user.getYear ());
        edit_user_department.setText (user.getDepartment ());
        edit_user_email.setText (user.getEmail ());
    }

    private void setV()
    {
        if(!user.getType ().toString ().equals ("Student"))
        {
            edit_user_section.setVisibility (View.GONE);
            edit_user_year.setVisibility (View.GONE);
            edit_user_department.setVisibility (View.GONE);
            year_text.setVisibility (View.GONE);
            department_text.setVisibility (View.GONE);
            section_text.setVisibility (View.GONE);
        }
    }

    @OnClick(R.id.confirm_edit)
    void confirm_edit()
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        if(!edit_nameTextView.getText ().toString ().equals (user.getName()))
        {
            reference.child(Data.USERS).child(userId).child ("name").setValue (edit_nameTextView.getText ().toString ());
        }

        if(!edit_typeTextView.getText ().toString ().equals (user.getType ()))
        {
            reference.child(Data.USERS).child(userId).child ("type").setValue (edit_typeTextView.getText ().toString ());
        }

        if(!edit_user_section.getText ().toString ().equals (user.getSection ()))
        {
            reference.child(Data.USERS).child(userId).child ("section").setValue (edit_user_section.getText ().toString ());
        }

        if(!edit_user_year.getText ().toString ().equals (user.getYear ()))
        {
            reference.child(Data.USERS).child(userId).child ("year").setValue (edit_user_year.getText ().toString ());
        }

        if(!edit_user_department.getText ().toString ().equals (user.getDepartment ()))
        {
            reference.child(Data.USERS).child(userId).child ("department").setValue (edit_user_department.getText ().toString ());
        }

        if(!edit_user_email.getText ().toString ().equals (user.getEmail ()))
        {
            reference.child(Data.USERS).child(userId).child ("email").setValue (edit_user_email.getText ().toString ());
        }

        finish();
    }
}
