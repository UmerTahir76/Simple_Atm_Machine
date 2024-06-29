package com.example.atmmachine;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.atmmachine.databinding.ActivityAdminLoginBinding;

public class AdminLogin extends AppCompatActivity {
    ActivityAdminLoginBinding mBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityAdminLoginBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        String email = "admin@gmail.com";
        String password = "admin123";

        mBinding.loginButton.setOnClickListener(v->{
            String enterEmail = mBinding.email.getText().toString();
            String enterPass = mBinding.password.getText().toString();

            if (enterEmail.isEmpty()){
                mBinding.email.setError("Error");
                mBinding.email.requestFocus();
            }else if (enterPass.isEmpty()){
                mBinding.password.setError("Error");
                mBinding.password.requestFocus();
            }else if (enterEmail.equals(email) && enterPass.equals(password)){
                startActivity(new Intent(this, DisplayUsers.class));
            }else {
                Toast.makeText(this, "Invalid fields", Toast.LENGTH_SHORT).show();
            }
        });
    }
}