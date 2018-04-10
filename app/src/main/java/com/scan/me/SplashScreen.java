package com.scan.me;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.scan.me.SignupScreen.Signup;

public class SplashScreen extends Activity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_splash_screen);

        new Handler ().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                Intent i = new Intent(getApplicationContext () , Signup.class);
                startActivity(i);
            }
        }, 1000);
    }
}
