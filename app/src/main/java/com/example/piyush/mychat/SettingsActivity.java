package com.example.piyush.mychat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity {

    private CircleImageView settingDisplayImage;
    private TextView settingsDisplayName;
    private TextView settingsDisplaystatus;
    private Button setingsChangeProfileImage;
    private Button settingsChangeStatus;
    private StorageReference storeProfileImage;

    private final static int Gallery_Pick=1;

    private DatabaseReference getUserDataReference;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        mAuth=FirebaseAuth.getInstance();
        String online_user_id= Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        getUserDataReference= FirebaseDatabase.getInstance().getReference().child("Users").child(online_user_id);
        storeProfileImage= FirebaseStorage.getInstance().getReference().child("Profile_Images");
        settingDisplayImage=findViewById(R.id.settings_profile_image);
        settingsDisplayName=findViewById(R.id.settings_username);
        settingsDisplaystatus=findViewById(R.id.settings_userstatus);
        setingsChangeProfileImage=findViewById(R.id.settings_change_image);
        settingsChangeStatus=findViewById(R.id.settings_change_status);

        getUserDataReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("ByteOrderMark")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {

                String name = dataSnapshot.child("user_name").getValue().toString();
                String status = dataSnapshot.child("user_status").getValue().toString();
                String image = dataSnapshot.child("user_image").getValue().toString();
                String thumb_image = dataSnapshot.child("user_thumb_image").getValue().toString();

                settingsDisplayName.setText(name);
                settingsDisplaystatus.setText(status);
                Picasso.get().load(image).into(settingDisplayImage);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        setingsChangeProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent =new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,Gallery_Pick);
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==Gallery_Pick && resultCode == RESULT_OK && data!=null)
        {
            Uri ImageUri=data.getData();
            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON).setAspectRatio(1,1)
                    .start(this);

        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK)
            {
                Uri resultUri = result.getUri();
                String user_id= Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
                StorageReference filePath=storeProfileImage.child(user_id + ".jpg");
                filePath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task)
                    {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(SettingsActivity.this, "Profile Picture uploading..", Toast.LENGTH_LONG).show();

                            String downloadUrl= Objects.requireNonNull(task.getResult().getDownloadUrl()).toString();
                            getUserDataReference.child("user_image").setValue(downloadUrl).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(SettingsActivity.this, "Uploaded Successfully", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        else
                        {
                            Toast.makeText(SettingsActivity.this, "Error Occured while uploading Image", Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }
            else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE)
            {
                Exception error = result.getError();
            }
        }
    }
}
