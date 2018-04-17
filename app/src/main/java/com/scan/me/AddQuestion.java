package com.scan.me;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Ahmed Magdy on 4/17/2018.
 */

public class AddQuestion {

    EditText mUserQuestion;
    Button mUploadQuestion;


    FirebaseAuth mAuth;
    DatabaseReference mDatabase;

    Activity mActivity;
    public void showDialog(final Activity activity) {
        final Dialog dialog = new Dialog(activity);
        mActivity = activity;
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Questions");
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.add_question);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(true);

        mUserQuestion = (EditText) dialog.findViewById(R.id.UserQuestion);
        mUploadQuestion = (Button) dialog.findViewById(R.id.UploadQuestion);


        mUploadQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mUserQuestion.getText().toString().equals("")){
                    Toast.makeText(mActivity,"Please enter your question.",Toast.LENGTH_SHORT).show();
                }else{
                    DatabaseReference databaseReference = mDatabase.push();
                    databaseReference.child("Question").setValue(mUserQuestion.getText().toString());
                    databaseReference.child("User").setValue(mAuth.getCurrentUser().getUid());
                }
            }
        });
        dialog.show();
    }
}
