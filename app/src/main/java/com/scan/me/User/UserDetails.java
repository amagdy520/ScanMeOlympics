package com.scan.me.User;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.scan.me.Data;
import com.scan.me.R;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UserDetails extends AppCompatActivity {
    public static final String USER_ID = "ID";

    @BindView(R.id.user_name)
    TextView nameTextView;
    @BindView(R.id.user_type)
    TextView typeTextView;
    @BindView(R.id.user_section)
    TextView user_section;
    @BindView(R.id.user_year)
    TextView user_year;
    @BindView(R.id.user_department)
    TextView user_department;
    @BindView(R.id.user_email)
    TextView user_email;
    @BindView(R.id.user_code)
    TextView codeTextView;

    String userId;
    private User user;
    String uid;
    private User myUser;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);
        ButterKnife.bind(this);
        userId = getIntent().getExtras().getString(USER_ID);
        uid=FirebaseAuth.getInstance ().getUid ();
        getUserData();
    }

    private void getUserData()
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.keepSynced(true);
        reference.child(Data.USERS).child(userId).addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                user = dataSnapshot.getValue(User.class);
                setUserData();
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {}
        });
    }

    private void setUserData() {
        nameTextView.setText(user.getName());
        typeTextView.setText(user.getType());
        user_section.setText (user.getSection ());
        user_year.setText (user.getYear ());
        user_department.setText (user.getDepartment ());
        user_email.setText (user.getEmail ());
        codeTextView.setText(user.getCode());
    }

    @OnClick(R.id.generate_code)
    void generateCode() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.keepSynced(true);
        reference.child(Data.USERS).child(userId).child("code").setValue("" + (new Random().nextInt(9000) + 1000));
    }

    @OnClick(R.id.edit_user)
    void edit_user()
    {
        Intent intent = new Intent (UserDetails.this,EditUser.class);
        intent.putExtra ("USER_ID",user.getId ());
        startActivity (intent);
    }

    @OnClick(R.id.del_user)
    void delete()
    {
        //delete from database
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
//        reference.child(Data.USERS).child(userId).removeValue ();

        //delete from Auth.
      getMyData();


    }

    private void getMyData()
    {
        DatabaseReference reference=FirebaseDatabase.getInstance ().getReference ();
        reference.keepSynced (true);
        reference.child (Data.USERS).orderByChild ("uid").equalTo (uid).addListenerForSingleValueEvent (new ValueEventListener ()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                for(DataSnapshot userSnapshot:dataSnapshot.getChildren ()){
                    myUser=userSnapshot.getValue (User.class);
                }
                deleteUser();
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        });
    }

    private void deleteUser()
    {
        FirebaseAuth.getInstance().signInWithEmailAndPassword (user.getEmail (), user.getPassword())
                .addOnSuccessListener (new OnSuccessListener<AuthResult> ()
                {
                    @Override
                    public void onSuccess(AuthResult authResult)
                    {
                        authResult.getUser ().delete ();
                        DatabaseReference reference=FirebaseDatabase.getInstance ().getReference ();
                        reference.keepSynced (true);
                        reference.child (Data.USERS).child (user.getId ()).removeValue ();
                        FirebaseAuth.getInstance().signInWithEmailAndPassword (myUser.getEmail (), myUser.getPassword()).addOnSuccessListener (new OnSuccessListener<AuthResult> ()
                        {
                            @Override
                            public void onSuccess(AuthResult authResult)
                            {
                                finish ();
                            }
                        });

                    }
                });
    }


}
