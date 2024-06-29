package com.example.atmmachine;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.atmmachine.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding mBinding;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        List<User> userList = new ArrayList<>();


        db.collection("users").get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                User user = documentSnapshot.toObject(User.class);
                userList.add(user);
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(LoginActivity.this, "Failed to fetch user data", Toast.LENGTH_SHORT).show();
        });



        mBinding.loginButton.setOnClickListener(v->{

            String email = mBinding.email.getText().toString();
            String password = mBinding.password.getText().toString();

            if (email.isEmpty()){
                mBinding.email.setError("Error");
                mBinding.email.requestFocus();
            } else if (password.isEmpty()) {
                mBinding.password.setError("Error");
                mBinding.password.requestFocus();
            }else {
                Login(email,password);
            }
        });
    }
    @SuppressLint("NotConstructor")
    private void Login(String email, String password){
        mAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(LoginActivity.this, OptionActivity.class);
                            finish();
                            startActivity(intent);
                        } else {
                            Toast.makeText(LoginActivity.this, "User does not exist", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}

