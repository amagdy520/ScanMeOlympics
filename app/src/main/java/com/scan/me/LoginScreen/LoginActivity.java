package com.scan.me.LoginScreen;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.scan.me.Data;
import com.scan.me.HomeScreen.Home;
import com.scan.me.R;
import com.scan.me.SignupScreen.Signup;
import com.scan.me.User.User;

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

import butterknife.OnClick;

import static android.content.ContentValues.TAG;


public class LoginActivity extends AppCompatActivity {

    private EditText mEmailInput, mPasswordInput;
    private TextInputLayout mEmailInputLayout, mPasswordInputLayout;
    private Button mLogin;
    private String mEmail, mPassword;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    public static final int SIGN_UP = 1;
    public static final String EMAIL = "Email";
    public static final String PASSWORD = "Password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        mEmailInput = (EditText) findViewById(R.id.input_email);
        mEmailInputLayout = (TextInputLayout) findViewById(R.id.input_email_layout);
        mPasswordInput = (EditText) findViewById(R.id.input_password);
        mPasswordInputLayout = (TextInputLayout) findViewById(R.id.input_password_layout);
        mLogin = (Button) findViewById(R.id.login);
        mEmail = mEmailInput.getText().toString().trim().toLowerCase();
        mPassword = mPasswordInput.getText().toString().trim().toLowerCase();
        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mEmailInput.getText().toString().isEmpty() || mPasswordInput.getText().toString().isEmpty()) {
                    if (mEmailInput.getText().toString().isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(mEmail).matches()) {
                        mEmailInput.setError("Please, Enter Email!");
                    } else {
                        mEmailInput.setError(null);
                    }
                    if (mPasswordInput.getText().toString().isEmpty() || mPasswordInput.getText().toString().length() < 4 || mPasswordInput.getText().toString().length() > 10) {
                        mPasswordInput.setError("Please, Enter Password!");
                    } else {
                        mPasswordInput.setError(null);
                    }
                } else {
                    Login(mEmailInput.getText().toString(), mPasswordInput.getText().toString());
                }
            }
        });
    }

    public void Login(String e, String p) {


        final String email = e;
        final String password = p;
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();


        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
            mAuth = FirebaseAuth.getInstance();
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                successfulLogin(progressDialog);
                            } else {
                                Toast.makeText(LoginActivity.this, "Unable to login user", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        }
                    });
        } else {
            Toast.makeText(LoginActivity.this, "Please Check email and password", Toast.LENGTH_LONG).show();
            progressDialog.dismiss();
        }
    }


    private void successfulLogin(final ProgressDialog progressDialog) {
        final FirebaseAuth auth = FirebaseAuth.getInstance();
        String uid = auth.getCurrentUser().getUid();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.keepSynced(true);
        reference.child(Data.USERS).orderByChild("uid").equalTo(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e("Data", dataSnapshot.toString());
                User user = null;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    user = snapshot.getValue(User.class);
                }
                Log.e("mac1", user.getMac());
                Log.e("mac1", getMacAddress());

                if (user.getMac().equals(getMacAddress())) {
                    Toast.makeText(LoginActivity.this, "Login user Successfully!", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    Intent intent = new Intent(LoginActivity.this, Home.class);
                    startActivity(intent);
                    finish();
                } else {
                    auth.signOut();
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, "Wrong Mac Address", Toast.LENGTH_SHORT).show();


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    public void signUp(View view) {
        Intent intent = new Intent(this, Signup.class);
        startActivityForResult(intent, SIGN_UP);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SIGN_UP && resultCode == RESULT_OK) {
            String email = data.getExtras().getString(EMAIL);
            String password = data.getExtras().getString(PASSWORD);
            Login(email, password);
        }
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
