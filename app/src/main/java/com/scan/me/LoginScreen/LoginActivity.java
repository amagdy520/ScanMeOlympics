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
import com.scan.me.HomeScreen.Home;
import com.scan.me.R;

import static android.content.ContentValues.TAG;


public class LoginActivity extends AppCompatActivity {

    private EditText mEmailInput, mPasswordInput;
    private TextInputLayout mEmailInputLayout, mPasswordInputLayout;
    private Button mLogin;
    private String mEmail, mPassword;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                //CHECKING USER PRESENCE
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if ((user != null)) {
                    Intent mAccount = new Intent(LoginActivity.this, Home.class);
                    startActivity(mAccount);
                }
            }
        };
        mAuth.addAuthStateListener(mAuthListener);
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
                }else {
                    Login(mEmailInput.getText().toString(), mPasswordInput.getText().toString());
                }
            }
        });
    }
    public void Login(String e, String p) {
        Log.d(TAG, "Login");

        final String email = e;
        final String password = p;
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        // TODO: Implement your own authentication logic here.

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
                            mAuth.signInWithEmailAndPassword(email, password)
                                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(LoginActivity.this, "Login user Successfully!", Toast.LENGTH_SHORT).show();
                                                progressDialog.dismiss();
                                                Intent intent = new Intent(LoginActivity.this,Home.class);
                                                startActivity(intent);
                                            }else{
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
                }, 500);
    }
}
