package com.example.piyush.mychat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    private EditText registerName,registerEmail,registerPass;
    private FirebaseAuth mAuth;
    private DatabaseReference storeUserDefaultDatabase;

    private ProgressDialog loadingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerEmail=findViewById(R.id.register_email);
        registerName=findViewById(R.id.register_name);
        registerPass=findViewById(R.id.register_password);
        Button create = findViewById(R.id.create_account);
        mAuth=FirebaseAuth.getInstance();

        Toolbar mToolbar = findViewById(R.id.register_toolbar);
        setSupportActionBar(mToolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Sign Up");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        loadingBar=new ProgressDialog(this);

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name=registerName.getText().toString();
                String email=registerEmail.getText().toString();
                String password=registerPass.getText().toString();
                RegisterAccount(name,email,password);
            }
        });
    }

    private void RegisterAccount( final String name, String email,String password)
    {
        if(TextUtils.isEmpty(name))
        {
            Toast.makeText(RegisterActivity.this, "Please Enter your Name", Toast.LENGTH_LONG).show();
        }
        if(TextUtils.isEmpty(email))
        {
            Toast.makeText(RegisterActivity.this, "Please Enter your E-mail", Toast.LENGTH_LONG).show();
        }
        if(TextUtils.isEmpty(password))
        {
            Toast.makeText(RegisterActivity.this, "Please Enter your Password", Toast.LENGTH_LONG).show();
        }
        else
        {
            loadingBar.setTitle("Creating an Account");
            loadingBar.setMessage("Please wait while we are loading your account");
            loadingBar.show();

            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        String current_user= mAuth.getCurrentUser().getUid();
                        storeUserDefaultDatabase= FirebaseDatabase.getInstance().getReference().child("Users").child(current_user);

                        storeUserDefaultDatabase.child("user_name").setValue(name);
                        storeUserDefaultDatabase.child("user_status").setValue("Hello Friends! Chai pilo ");
                        storeUserDefaultDatabase.child("user_image").setValue("default_image");
                        storeUserDefaultDatabase.child("user_thumb_image").setValue("default_picture")
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if(task.isSuccessful())
                                {
                                    Intent mainIntent =new Intent(RegisterActivity.this,MainActivity.class);
                                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(mainIntent);
                                    finish();
                                }
                            }
                        });
                    }
                    else
                    {
                        Toast.makeText(RegisterActivity.this, "Error Occured , Try Again!", Toast.LENGTH_SHORT).show();
                    }

                    loadingBar.dismiss();
                }
            });
        }

    }


}
