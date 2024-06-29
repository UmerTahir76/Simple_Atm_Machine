package com.example.atmmachine;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.example.atmmachine.databinding.ActivityOptionsBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;

public class OptionActivity extends AppCompatActivity {

    private ActivityOptionsBinding mBind;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBind = ActivityOptionsBinding.inflate(getLayoutInflater());
        setContentView(mBind.getRoot());

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();


        mBind.depositBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String amtStr = mBind.amount.getText().toString();

                if (amtStr.trim().isEmpty()) {
                    mBind.amount.setError("Enter Number!");
                    mBind.amount.requestFocus();
                } else {
                    int depositAmount = Integer.parseInt(amtStr);
                    updateBalance(depositAmount,true);
                }
            }
        });
        mBind.withdrawBtn.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                String amtStr = mBind.amount.getText().toString();
                if (amtStr.trim().isEmpty()) {
                    mBind.amount.setError("Enter Number!");
                    mBind.amount.requestFocus();
                } else {
                    int withdrawAmount = Integer.parseInt(amtStr);
                    updateBalance(withdrawAmount, false); // false for withdrawal
                }
            }
        });
        mBind.transferBtn.setOnClickListener(v->{
            startActivity(new Intent(this, TransferMoney.class));
        });
        mBind.userProfile.setOnClickListener(v->{
            startActivity(new Intent(this, Userprofile.class));
        });

    }

    private void updateBalance(final int amount, final boolean isDeposit) {
        if (currentUser == null) {
            Toast.makeText(OptionActivity.this, "User not authenticated", Toast.LENGTH_SHORT).show();
            return;
        }

        final String userId = currentUser.getUid();
        final DocumentReference userDocRef = db.collection("users").document(userId);

        db.runTransaction(new Transaction.Function<Void>() {
            @Override
            public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                DocumentSnapshot snapshot = transaction.get(userDocRef);

                int currentBalance = 0;
                if (snapshot.exists() && snapshot.contains("balance")) {
                    currentBalance = snapshot.getLong("balance").intValue();
                }

                int newBalance;
                if (isDeposit) {
                    newBalance = currentBalance + amount;
                } else {
                    if (currentBalance < amount) {
                        throw new FirebaseFirestoreException("Insufficient funds",
                                FirebaseFirestoreException.Code.ABORTED);
                    }
                    newBalance = currentBalance - amount;
                }

                transaction.update(userDocRef, "balance", newBalance);

                return null;
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                String message = isDeposit ? "Deposit successful" : "Withdrawal successful";
                Toast.makeText(OptionActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message = isDeposit ? "Deposit failed: " : "Withdrawal failed: ";
                Toast.makeText(OptionActivity.this, message + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }}
