package com.example.atmmachine;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.atmmachine.databinding.ActivityUserprofileBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class Userprofile extends AppCompatActivity {

    ActivityUserprofileBinding mBinding;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    String userName;
    String Email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityUserprofileBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());


        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();


        mBinding.logout.setOnClickListener(v->{
            mAuth.signOut();
            startActivity(new Intent(this, LoginActivity.class));
        });
        mBinding.edit.setOnClickListener(v->{
            Intent intent = new Intent(this, UpdateProfile.class);
            intent.putExtra("name",userName);
            intent.putExtra("email",Email);
            startActivity(intent);
        });




    }

    @Override
    protected void onResume() {
        super.onResume();
        fetch();
    }

    void  fetch(){
        db.collection("users").document(mAuth.getCurrentUser().getUid()).get()
                .addOnSuccessListener(documentSnapshot -> {

                    if (documentSnapshot.exists()) {

                        userName = documentSnapshot.getString("userName");
                        Email = documentSnapshot.getString("email");
                        Long balanceLong = documentSnapshot.getLong("balance");
                        int balance = (balanceLong != null) ? balanceLong.intValue() : 0;
                        mBinding.userName.setText(userName);
                        mBinding.userGmail.setText(Email);
                        mBinding.balance.setText(String.valueOf(balance));


                    } else {

                        Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {

                    Toast.makeText(this, "Failed to retrieve user data", Toast.LENGTH_SHORT).show();
                });
    }
    }
