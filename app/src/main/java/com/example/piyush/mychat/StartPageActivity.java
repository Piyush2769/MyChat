package com.example.piyush.mychat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StartPageActivity extends AppCompatActivity {

    private Button alreadyButton,newButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_page);

        alreadyButton=findViewById(R.id.alreadyButton);
        newButton=findViewById(R.id.newButton);

        alreadyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent=new Intent(StartPageActivity.this,LoginActivity.class);
                startActivity(registerIntent);
            }
        });

        newButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent=new Intent(StartPageActivity.this,RegisterActivity.class);
                startActivity(loginIntent);
            }
        });
    }
}
