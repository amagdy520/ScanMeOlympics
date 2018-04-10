package com.scan.me.SignupScreen;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.scan.me.Data;
import com.scan.me.HomeScreen.UsersAdapter;
import com.scan.me.LoginScreen.LoginActivity;
import com.scan.me.R;
import com.scan.me.User.User;

import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Signup extends Activity implements UsersAdapter.OnUserClickListener {
    @BindView(R.id.signup_year)
    AutoCompleteTextView yearAutoCompleteTextView;
    @BindView(R.id.signup_department)
    AutoCompleteTextView departmentAutoCompleteTextView;
    @BindView(R.id.signup_section)
    EditText sectionEditText;
    @BindView(R.id.singup_radiogroup)
    RadioGroup radioGroup;
    @BindView(R.id.user_name)
    TextView nameTextView;
    @BindView(R.id.code)
    EditText codeEditText;
    @BindView(R.id.email)
    EditText emailEditText;
    @BindView(R.id.password)
    EditText passwordEditText;
    @BindView(R.id.conf_password)
    EditText confPasswordEditText;

    List<String> years = new ArrayList<>();
    List<String> departments = new ArrayList<>();
    private ArrayList<User> users;
    private Dialog dialog;
    User selectedUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);
        onRadioChange();
        getYearData();
    }

    private void getYearData() {
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
    }

    private void setOnClick(final AutoCompleteTextView autoCompleteTextView) {
        autoCompleteTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autoCompleteTextView.showDropDown();
            }
        });
    }

    @OnClick(R.id.choose_user)
    void choose_user() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.keepSynced(true);
        String hash = null;
        if (radioGroup.getCheckedRadioButtonId() == R.id.student) {
            String year = yearAutoCompleteTextView.getText().toString();
            String department = departmentAutoCompleteTextView.getText().toString();
            String section = sectionEditText.getText().toString();
            hash = year + "-" + department + "-" + section;
        }

        reference.child(Data.USERS).orderByChild("hash").equalTo(hash).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                users = new ArrayList<User>();
                for (DataSnapshot usersSnapshot : dataSnapshot.getChildren()) {
                    User user = usersSnapshot.getValue(User.class);
                    user.setId(usersSnapshot.getKey());
                    users.add(user);
                }
                Log.e("Size", users.size() + "");
                openUsersDialog();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @OnClick(R.id.sign_up)
    void signUp() {
        String email=emailEditText.getText().toString();
        String password=passwordEditText.getText().toString();
        String confPassword=confPasswordEditText.getText().toString();
        String code=codeEditText.getText().toString();
        if(password.equals(confPassword)){
            if(!code.equals(selectedUser.getCode())){
                Toast.makeText(this, "Wrong Code", Toast.LENGTH_SHORT).show();
                return;
            }
            FirebaseAuth auth=FirebaseAuth.getInstance();
            auth.createUserWithEmailAndPassword(email,password)
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            uploadUser(authResult);
                        }
                    });

        }
    }

    private void uploadUser(AuthResult authResult) {
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference();
        reference.keepSynced(true);
        selectedUser.setEmail(authResult.getUser().getEmail());
        selectedUser.setUid(authResult.getUser().getUid());
        selectedUser.setCode(null);
        selectedUser.setMac(getMacAddress());
        reference.child(Data.USERS).child(selectedUser.getId()).setValue(selectedUser);
        Bundle bundle=new Bundle();
        bundle.putString(LoginActivity.EMAIL,emailEditText.getText().toString());
        bundle.putString(LoginActivity.PASSWORD,passwordEditText.getText().toString());
        setResult(RESULT_OK,new Intent().putExtras(bundle));
        finish();
    }

    private void openUsersDialog() {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_users_dialog);
        dialog.show();
        RecyclerView userRecyclerView = (RecyclerView) dialog.findViewById(R.id.users_recycler);
        UsersAdapter adapter = new UsersAdapter(this, users, this);
        userRecyclerView.setAdapter(adapter);
        userRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

    }


    private void onRadioChange() {
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.student) {
                    yearAutoCompleteTextView.setVisibility(View.VISIBLE);
                    departmentAutoCompleteTextView.setVisibility(View.VISIBLE);
                    sectionEditText.setVisibility(View.VISIBLE);
                } else {
                    yearAutoCompleteTextView.setVisibility(View.GONE);
                    departmentAutoCompleteTextView.setVisibility(View.GONE);
                    sectionEditText.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void onUserClicked(int position) {
        selectedUser = users.get(position);
        dialog.dismiss();
        nameTextView.setText(selectedUser.getName());


    }
    private String getMacAddress() {
        try {
            // get all the interfaces
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            //find network interface wlan0
            for (NetworkInterface networkInterface : all) {
                if (!networkInterface.getName().equalsIgnoreCase("wlan0")) continue;
                //get the hardware address (MAC) of the interface
                byte[] macBytes = networkInterface.getHardwareAddress();
                if (macBytes == null) {
                    return null;
                }


                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    //gets the last byte of b
                    res1.append(Integer.toHexString(b & 0xFF) + ":");
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

}