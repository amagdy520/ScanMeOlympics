package com.scan.me.User;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.scan.me.Data;
import com.scan.me.GlideApp;
import com.scan.me.R;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UserDetails extends AppCompatActivity {
    public static final String USER_ID = "ID";

    @BindView(R.id.user_name)
    TextView nameTextView;
    @BindView(R.id.user_type)
    TextView typeTextView;
    @BindView(R.id.user_section)
    TextView user_section;
    @BindView(R.id.user_year)
    TextView user_year;
    @BindView(R.id.user_department)
    TextView user_department;
    @BindView(R.id.user_email)
    TextView user_email;
    @BindView(R.id.user_code)
    TextView codeTextView;
    @BindView(R.id.generate_code)
    ImageView generate_code;
    @BindView(R.id.text_section)
    TextView text_section;
    @BindView(R.id.text_department)
    TextView text_department;
    @BindView(R.id.text_year)
    TextView text_year;
    @BindView(R.id.text_re)
    TextView text_re;

    String userId;
    private User user;
    private DatabaseReference mDatabase;
    private StorageReference mStorage;
    private FirebaseAuth mAuth;
    private final int GALLERY_REQUEST = 1;
    private Uri mImageUri;
    @BindView(R.id.image)
    ImageView image;
    String uid;
    private User myUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);
        ButterKnife.bind(this);
        userId = getIntent().getExtras().getString(USER_ID);
        uid = FirebaseAuth.getInstance().getUid();

        getUserData();
    }

    private void getUserData() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.keepSynced(true);
        reference.child(Data.USERS).child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                setUserData();

                if(user.getEmail ()!= null)
                {
                    codeTextView.setVisibility (View.GONE);
                    generate_code.setVisibility (View.GONE);
                    text_re.setVisibility (View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setUserData() {
        nameTextView.setText(user.getName());
        typeTextView.setText(user.getType());

        if(!user.getType ().equals ("Student"))
        {
            text_section.setVisibility (View.GONE);
            text_department.setVisibility (View.GONE);
            text_year.setVisibility (View.GONE);
            user_section.setVisibility (View.GONE);
            user_year.setVisibility (View.GONE);
            user_department.setVisibility (View.GONE);
        }
        else
        {
            user_section.setText (user.getSection ());
            user_year.setText (user.getYear ());
            user_department.setText (user.getDepartment ());
        }

        user_email.setText (user.getEmail ());
        codeTextView.setText(user.getCode());

        if (user.getImage() != null) {
            GlideApp.with(this)
                    .load(user.getImage())
                    .placeholder(R.drawable.user_pic)
                    .error(R.drawable.user_pic)
                    .apply(RequestOptions.circleCropTransform())
                    .into(image);

        }
    }

    @OnClick(R.id.generate_code)
    void generateCode() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.keepSynced(true);
        reference.child(Data.USERS).child(userId).child("code").setValue("" + (new Random().nextInt(9000) + 1000));
    }

    @OnClick(R.id.image)
    void openGallery() {
        if (!uid.equals(user.getUid())) {
            return;
        }
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GALLERY_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //******************************************************************************************
        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK) {
            mImageUri = data.getData();
            CropImage.activity(mImageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .start(this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                mImageUri = result.getUri();
                Log.e("Image", mImageUri.toString());
                Log.e("Last", mImageUri.getLastPathSegment());
                mStorage = FirebaseStorage.getInstance().getReference();
                mDatabase = FirebaseDatabase.getInstance().getReference();
                StorageReference filepath1 = mStorage.child("Profile").child(uid)
                        .child(mImageUri.getLastPathSegment());
                filepath1.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        mDatabase.child(Data.USERS).child(userId).child("image").setValue(downloadUrl.toString());
                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e("Fail leh", e.getMessage());
                            }
                        });


            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @OnClick(R.id.edit_user)
    void edit_user()
    {
        Intent intent = new Intent (UserDetails.this,EditUser.class);
        intent.putExtra ("USER_ID",user.getId ());
        startActivity (intent);
    }
}
