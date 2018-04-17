package com.scan.me;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class AboutApp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_app);
    }
    public void onClick(View view){
        switch(view.getId()){
            case R.id.facebook:
                Intent fb = openFacebook(AboutApp.this);
                startActivity(fb);
                break;
            case R.id.instagram:
                Uri uri = Uri.parse("http://instagram.com/_u/"+"ahmed.magdy.abd_alzaher");
                Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);
                likeIng.setPackage("com.instagram.android");
                try {
                    startActivity(likeIng);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://instagram.com/"+"ahmed.magdy.abd_alzaher")));
                }
                break;
            case R.id.twitter:
                Intent intent = null;
                try {
                    // get the Twitter app if possible
                    this.getPackageManager().getPackageInfo("com.twitter.android", 0);
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?user_id=USERID"));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                } catch (Exception e) {
                    // no Twitter app, revert to browser
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/PROFILENAME"));
                }
                this.startActivity(intent);
                break;
            case R.id.whatsapp:
                Uri whs = Uri.parse("smsto:" + "01091920361");
                Intent i = new Intent(Intent.ACTION_SENDTO, whs);
                i.setPackage("com.whatsapp");
                startActivity(Intent.createChooser(i, ""));
                break;
        }
    }
    public static Intent openFacebook(Context context) {
        try {
            context.getPackageManager().getPackageInfo("com.facebook.katana", 0);
            return new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/"+"ScanMe"));

        } catch (Exception e) {
            return new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/ScanMe"));
        }

    }
}
