package com.scan.me.User;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

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
    @BindView(R.id.user_code)
    TextView codeTextView;
    String userId;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);
        ButterKnife.bind(this);
        userId = getIntent().getExtras().getString(USER_ID);
        getUserData();
    }

    private void getUserData() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.keepSynced(true);
        reference.child(Data.USERS).child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                setUserData();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setUserData() {
        nameTextView.setText(user.getName());
        typeTextView.setText(user.getType());
        codeTextView.setText(user.getCode());

    }

    @OnClick(R.id.generate_code)
    void generateCode() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.keepSynced(true);
        reference.child(Data.USERS).child(userId).child("code").setValue("" + (new Random().nextInt(9000) + 1000));
    }

}
