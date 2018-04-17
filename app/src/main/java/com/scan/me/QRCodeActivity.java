package com.scan.me;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.WriterException;
import com.scan.me.HomeScreen.Home;
import com.scan.me.LoginScreen.LoginActivity;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.scan.me.QRCode.encodeAsBitmap;

public class QRCodeActivity extends AppCompatActivity {

    public static final String LECTURE_ID = "ID";

    @BindView(R.id.qr_code)
    ImageView qrCodeImageView;
    String lectureId;
    private ValueEventListener valueEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);
        ButterKnife.bind(this);
        lectureId = getIntent().getExtras().getString(LECTURE_ID);
        setCodeChangeListener();
    }

    private void setCodeChanger() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                reference.keepSynced(true);
                reference.child(Data.LECTURES).child(lectureId)
                        .child("code").setValue("" + (new Random().nextInt(9000) + 1000));
            }
        }, 10000);


    }

    private void setCodeChangeListener() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.keepSynced(true);
         valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String code = dataSnapshot.getValue(String.class);
                if (code != null) {

                    try {
                        Bitmap qrBitmap = QRCode.encodeAsBitmap(QRCodeActivity.this, code);
                        qrCodeImageView.setImageBitmap(qrBitmap);
                        setCodeChanger();
                    } catch (WriterException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        reference.child(Data.LECTURES).child(lectureId).child("code")
                .addValueEventListener(valueEventListener);
    }

    @OnClick(R.id.end_lecture)
    void endLecture() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.keepSynced(true);
        reference.child(Data.LECTURES).child(lectureId)
                .child("attend").setValue(false);
        reference.child(Data.LECTURES).child(lectureId).child("code").removeEventListener(valueEventListener);

        finish();
    }
}
