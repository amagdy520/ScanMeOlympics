package com.scan.me;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;

public class QRCodeActivity extends AppCompatActivity {


    @BindView(R.id.end_lecture)
    TextView endButton;
    @BindView(R.id.qr_code)
    ImageView qrCodeImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);
    }
}
