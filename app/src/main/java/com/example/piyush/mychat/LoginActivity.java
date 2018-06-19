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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private FirebaseAuth mAuth;
    private Button loginb;
    private EditText login_email,login_pass;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth=FirebaseAuth.getInstance();
        mToolbar=findViewById(R.id.login_app_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Sign In");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        loadingBar=new ProgressDialog(this);

        loginb=findViewById(R.id.login_button);
        login_email=findViewById(R.id.login_email);
        login_pass=findViewById(R.id.login_password);

        loginb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=login_email.getText().toString();
                String pass=login_pass.getText().toString();

                LoginUserAccount(email,pass);
            }
        });


    }

    private void LoginUserAccount(String email, String pass)
    {
        if(TextUtils.isEmpty(email))
        {
            Toast.makeText(LoginActivity.this, "Please Enter your E-mail", Toast.LENGTH_LONG).show();
        }
        if(TextUtils.isEmpty(pass))
        {
            Toast.makeText(LoginActivity.this, "Please Enter your Password", Toast.LENGTH_LONG).show();
        }
        else
        {
            loadingBar.setTitle("Logging you in!");
            loadingBar.setMessage("Please wait while we are loading");
            loadingBar.show();
                mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful())
                        {
                            Intent mainIntent =new Intent(LoginActivity.this,MainActivity.class);
                            mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(mainIntent);
                            finish();
                        }
                        else
                        {
                            Toast.makeText(LoginActivity.this, "Error Occured , Try Again!", Toast.LENGTH_SHORT).show();
                        }

                        loadingBar.dismiss();

                    }
                });

        }
    }
}
