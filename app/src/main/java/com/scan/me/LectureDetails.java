package com.scan.me;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.lzyzsd.circleprogress.ArcProgress;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.scan.me.User.User;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LectureDetails extends AppCompatActivity implements AttendAdapter.OnUserClickListener {

    public static final String LECTURE_ID = "ID";
    public static final String USER_TYPE = "Type";

    String lectureId, userType;

    @BindView(R.id.student_recycler)
    RecyclerView studentRecyclerView;
    @BindView(R.id.lecture_name)
    TextView lectureTextView;
    @BindView(R.id.start)
    Button statButton;
    @BindView(R.id.code)
    TextView codeTextView;
    @BindView(R.id.arc_progress)
    ArcProgress progress;
    private String uid;
    private Reservation reservation;
    int totalStudent,attendedStudents;
    List<UserAttend> userAttends;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecture_details);
        ButterKnife.bind(this);
        lectureId = getIntent().getExtras().getString(LECTURE_ID);
        userType = getIntent().getExtras().getString(USER_TYPE);
        Log.e("Type", userType);
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        }
        Calendar mcurrentTime = Calendar.getInstance();
        int year = mcurrentTime.get(Calendar.YEAR);
        int month = mcurrentTime.get(Calendar.MONTH);
        int day = mcurrentTime.get(Calendar.DAY_OF_MONTH);
//        today = year + "-" + month + "-" + day;
        getLectureData();
    }

    private void getLectureData() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.keepSynced(true);


        reference.child(Data.LECTURES).child(lectureId)
                .addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        reservation = dataSnapshot.getValue(Reservation.class);
                        userAttends = new ArrayList<>();
                        totalStudent=0;
                        attendedStudents=0;
                        for (DataSnapshot snapshot : dataSnapshot.child(Data.STUDENTS).getChildren()) {
                            totalStudent++;
                            if (userType.equals(User.TUTOR)) {
                                UserAttend userAttend = snapshot.getValue(UserAttend.class);
                                userAttend.setId(snapshot.getKey());
                                userAttends.add(userAttend);
                                if(userAttend.isAttend()){
                                    attendedStudents++;
                                }
                            } else if (userType.equals(User.STUDENT)) {
                                UserAttend userAttend = snapshot.getValue(UserAttend.class);
                                if(userAttend.isAttend()){
                                    attendedStudents++;
                                }
                                if (snapshot.getKey().equals(uid)) {
                                    userAttends.add(userAttend);
                                }
                            }
                        }
                        setUserRecycler();
                        setLectureData();

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private void setLectureData() {
        if (reservation == null) {
            return;
        }
        progress.setMax(totalStudent);
        progress.setProgress(attendedStudents);
        lectureTextView.setText(reservation.getName());
        if (userType.equals(User.TUTOR)) {
            statButton.setVisibility(View.VISIBLE);
            codeTextView.setVisibility(View.VISIBLE);
        }

        if (reservation.isAttend()) {
            statButton.setText("Stop");
        } else {
            statButton.setText("Start");

        }
        if (reservation.getCode() != null) {
            codeTextView.setText(reservation.getCode());
        }


    }

    @OnClick(R.id.start)
    void startLecture() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.keepSynced(true);
        boolean attend;
        attend = !reservation.attend;
        reference.child(Data.LECTURES).child(lectureId)
                .child("attend").setValue(attend);
        reference.child(Data.LECTURES).child(lectureId)
                .child("code").setValue("" + (new Random().nextInt(9000) + 1000));

    }


    private void setUserRecycler() {
        AttendAdapter adapter = new AttendAdapter(this, userAttends, this, userType, reservation.attend);
        studentRecyclerView.setAdapter(adapter);
        studentRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    @Override
    public void onSwitchClicked(final int position) {
        final boolean isChecked = !userAttends.get(position).isAttend();
        setUserRecycler();
        if (userType.equals(User.TUTOR)) {
            UserAttend userAttend = userAttends.get(position);
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
            reference.keepSynced(true);
            reference.child(Data.LECTURES).child(lectureId).
                    child(Data.STUDENTS).child(userAttend.getUid()).child("attend").setValue(isChecked);

        } else if (userType.equals(User.STUDENT)) {
            final Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.reservation_dialog);
            final EditText codeEditText = (EditText) dialog.findViewById(R.id.code);
            Button attendButton = (Button) dialog.findViewById(R.id.sendCode);
            attendButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (reservation.getCode().equals(codeEditText.getText().toString())) {
                        UserAttend userAttend = userAttends.get(position);
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                        reference.keepSynced(true);
                        reference.child(Data.LECTURES).child(lectureId).
                                child(Data.STUDENTS)
                                .child(userAttend.getUid())
                                .child("attend")
                                .setValue(isChecked);

                        reference.child(Data.USERS)
                                .child(userAttend.getUid())
                                .child(Data.LECTURES)
                                .child(lectureId)
                                .child("attend")
                                .setValue(isChecked);


                    } else {
                        Toast.makeText(LectureDetails.this, "Wrong Code", Toast.LENGTH_SHORT).show();


                    }
                    dialog.dismiss();
                }
            });
            dialog.show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (userType.equals(User.TUTOR)) {
            getMenuInflater().inflate(R.menu.qr_code_menu, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_scan) {
            scan();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void scan() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setPrompt("Scan a barcode"); // what will write in the bottom of the screen
        integrator.setDesiredBarcodeFormats(integrator.ALL_CODE_TYPES);
        integrator.setCameraId(0);
        integrator.setOrientationLocked(false);
        integrator.setCaptureActivity(ScanActivity.class);
        integrator.setBeepEnabled(true);
        integrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "You Cancelled", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, result.getContents() + "-", Toast.LENGTH_SHORT).show(); //tho show what in the qr code
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}

