package com.scan.me;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.scan.me.HomeScreen.Home;
import com.scan.me.LoginScreen.LoginActivity;
import com.scan.me.SignupScreen.Signup;

public class SplashScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (FirebaseAuth.getInstance().getCurrentUser() == null) {
                    Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(i);
                } else {
                    Intent i = new Intent(getApplicationContext(), Home.class);
                    startActivity(i);
                }
                finish();
            }
        }, 1000);
    }
}
