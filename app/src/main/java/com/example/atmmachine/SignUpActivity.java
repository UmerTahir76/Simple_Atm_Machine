package com.example.atmmachine;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.atmmachine.databinding.ActivitySignupBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class SignUpActivity extends AppCompatActivity {
    ActivitySignupBinding mBinding;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        List<User> userList = new ArrayList<>();
        mBinding.loginText.setOnClickListener(v->{
            startActivity(new Intent(this, LoginActivity.class));
        });

        db.collection("users").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    User user = documentSnapshot.toObject(User.class);
                    userList.add(user);
                }
            }
        });

        mBinding.signupButton.setOnClickListener(v -> {
            String userName = mBinding.name.getText().toString().trim();
            String email = mBinding.email.getText().toString().trim();
            String password = mBinding.password.getText().toString().trim();

            if (userName.isEmpty()) {
                mBinding.name.setError("Error");
                mBinding.name.requestFocus();
            } else if (email.isEmpty()) {
                mBinding.email.setError("Error");
                mBinding.email.requestFocus();
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                mBinding.email.setError("Please enter a valid email address");
                mBinding.email.requestFocus();
            } else if (password.isEmpty()) {
                mBinding.password.setError("Error");
                mBinding.password.requestFocus();
            } else {

                Register(userName, email, password);

            }
        });
    }

    @SuppressLint("NotConstructor")

    private void Register(String name, String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser currentUser = mAuth.getCurrentUser();
                        addUserToFireStore(currentUser.getUid(), name, email, password);
                        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                        finish();
                        startActivity(intent);
                    } else {
                        Toast.makeText(SignUpActivity.this, "Error occurred: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void addUserToFireStore(String uid, String name, String email, String password ) {
        User user = new User(uid, name, email, password);
        db.collection("users").document(uid)
                .set(user)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(SignUpActivity.this, "User added to FireStore", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(SignUpActivity.this, "Failed to add user to FireStore: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
