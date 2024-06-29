package com.example.atmmachine;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

public class ModeActivity extends AppCompatActivity {

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mode);

        findViewById(R.id.user_mode).setOnClickListener(v->{
            startActivity(new Intent(this, SignUpActivity.class));
        });
        findViewById(R.id.admin_mode).setOnClickListener(v->{
            startActivity(new Intent(this, AdminLogin.class));
        });
    }
}