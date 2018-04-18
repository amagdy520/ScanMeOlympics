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
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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
    @BindView(R.id.arc_progress)
    ArcProgress progress;
    private String uid;
    private Reservation reservation;
    int totalStudent, attendedStudents;
    List<UserAttend> userAttends;
    private List<String> years;
    private List<String> departments;
    private DatabaseReference reference;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecture_details);
        ButterKnife.bind(this);
        lectureId = getIntent().getExtras().getString(LECTURE_ID);
        userType = getIntent().getExtras().getString(USER_TYPE);
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        }
        if(!userType.equals(User.TUTOR)){
            progress.setVisibility(View.GONE);
            statButton.setVisibility(View.GONE);
        }
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
                        totalStudent = 0;
                        attendedStudents = 0;
                        for (DataSnapshot snapshot : dataSnapshot.child(Data.STUDENTS).getChildren()) {
                            totalStudent++;
                            if (userType.equals(User.TUTOR)) {
                                UserAttend userAttend = snapshot.getValue(UserAttend.class);
                                userAttend.setId(snapshot.getKey());
                                userAttends.add(userAttend);
                                if (userAttend.isAttend()) {
                                    attendedStudents++;
                                }
                            } else if (userType.equals(User.STUDENT)) {
                                UserAttend userAttend = snapshot.getValue(UserAttend.class);
                                if (userAttend.isAttend()) {
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
        }


    }

    @OnClick(R.id.start)
    void startLecture() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.keepSynced(true);
        reference.child(Data.LECTURES).child(lectureId)
                .child("attend").setValue(true);
        reference.child(Data.LECTURES).child(lectureId)
                .child("code").setValue("" + (new Random().nextInt(9000) + 1000));
        Intent intent = new Intent(this, QRCodeActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(QRCodeActivity.LECTURE_ID, lectureId);
        startActivity(intent.putExtras(bundle));

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



    private void add_students() {
        getYearData();

    }

    private void getYearData() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child(Data.DATA).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                years = new ArrayList<>();
                departments = new ArrayList<>();
                for (DataSnapshot yearDataSnapshot : dataSnapshot.child(Data.YEARS).getChildren()) {
                    years.add(yearDataSnapshot.getKey());
                }
                for (DataSnapshot departmentSnapshot : dataSnapshot.child(Data.DEPARTMENTS).getChildren()) {
                    departments.add(departmentSnapshot.getKey());
                }
                setUpDialog();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setUpDialog() {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.add_student_dialog);
        final AutoCompleteTextView year = (AutoCompleteTextView) dialog.findViewById(R.id.year);
        final AutoCompleteTextView department = (AutoCompleteTextView) dialog.findViewById(R.id.department);
        final EditText section = (EditText) dialog.findViewById(R.id.section);
        ArrayAdapter<String> yearAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, years);
        ArrayAdapter<String> departmentAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, departments);
        year.setAdapter(yearAdapter);
        year.setAdapter(yearAdapter);
        department.setAdapter(departmentAdapter);
        setOnClick(year);
        setOnClick(department);
        TextView addButton = (TextView) dialog.findViewById(R.id.add_students);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Validation.checkValidation(year)
                        && Validation.checkValidation(department)
                        && Validation.checkValidation(section)) {
                    String hash = year.getText().toString() + "-" + department.getText().toString() + "-" + section.getText().toString();
                    for (UserAttend userAttend : userAttends) {
                        if (hash.equals(userAttend.getHash())) {
                            Toast.makeText(LectureDetails.this, "Already Exists", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    getUsers(hash);

                }
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    private void getUsers(String hash) {
        reference = FirebaseDatabase.getInstance().getReference();
        reference.keepSynced(true);
        reference.child(Data.USERS).orderByChild("hash").equalTo(hash)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        ArrayList<UserAttend> users = new ArrayList<UserAttend>();
                        for (DataSnapshot usersSnapshot : dataSnapshot.getChildren()) {
                            User user = usersSnapshot.getValue(User.class);
                            users.add(new UserAttend(user.getId(), user.getUid(), user.getName(), user.getImage(), user.getHash(), false));
                        }
                        reserve(users, reservation);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private void reserve(ArrayList<UserAttend> users, Reservation reservation) {

        for (UserAttend userAttend : users) {
            reference.child(Data.LECTURES)
                    .child(lectureId)
                    .child(Data.STUDENTS)
                    .child(userAttend.getUid())
                    .setValue(userAttend);
            reference.child(Data.USERS)
                    .child(userAttend.getId())
                    .child(Data.LECTURES)
                    .child(lectureId)
                    .setValue(reservation);
        }
        Toast.makeText(this, "Added", Toast.LENGTH_SHORT).show();


    }

    private void setOnClick(final AutoCompleteTextView autoCompleteTextView) {
        autoCompleteTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autoCompleteTextView.showDropDown();
            }
        });
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
                if(userType.equals(User.TUTOR)){
                    attendStudent(result.getContents());
                }else {
                    attend(result.getContents());
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void attend(String code) {
        if(reservation.getCode().equals(code)){
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
            reference.keepSynced(true);
            reference.child(Data.LECTURES).child(lectureId).
                    child(Data.STUDENTS).child(uid).child("attend").setValue(true);
        }else {
            Toast.makeText(this, "Wrong Code", Toast.LENGTH_SHORT).show();
            scan();
        }
    }

    private void attendStudent(String uid) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.keepSynced(true);
        reference.child(Data.LECTURES).child(lectureId).
                child(Data.STUDENTS).child(uid).child("attend").setValue(true);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (userType.equals(User.TUTOR)) {
            getMenuInflater().inflate(R.menu.qr_code_menu, menu);
        }else {
            getMenuInflater().inflate(R.menu.qr_code_menu_student, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_scan) {
            scan();
            return true;
        } else if (item.getItemId() == R.id.action_add_students) {
            add_students();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

