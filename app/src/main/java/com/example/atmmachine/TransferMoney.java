package com.example.atmmachine;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.atmmachine.databinding.ActivityTransferMoneyBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;

public class TransferMoney extends AppCompatActivity {
    ActivityTransferMoneyBinding mBinding;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityTransferMoneyBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());


        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        mBinding.sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String recipientEmail = mBinding.userEmail.getText().toString().trim();
                String amountStr = mBinding.amount.getText().toString().trim();

                if (recipientEmail.isEmpty()) {
                    mBinding.userEmail.setError("Enter recipient email!");
                    mBinding.userEmail.requestFocus();
                    return;
                }

                if (amountStr.isEmpty()) {
                    mBinding.amount.setError("Enter amount!");
                    mBinding.amount.requestFocus();
                    return;
                }

                int transferAmount = Integer.parseInt(amountStr);
                performTransfer(recipientEmail, transferAmount);
            }
        });
    }

    private void performTransfer(final String recipientEmail, final int transferAmount) {
        if (currentUser == null) {
            Toast.makeText(TransferMoney.this, "User not authenticated", Toast.LENGTH_SHORT).show();
            return;
        }

        final String senderUserId = currentUser.getUid();

        db.collection("users")
                .whereEqualTo("email", recipientEmail)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        DocumentSnapshot recipientSnapshot = task.getResult().getDocuments().get(0);
                        final String recipientUserId = recipientSnapshot.getId();

                        final DocumentReference senderDocRef = db.collection("users").document(senderUserId);
                        final DocumentReference recipientDocRef = db.collection("users").document(recipientUserId);

                        db.runTransaction(new Transaction.Function<Void>() {
                            @Override
                            public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                                DocumentSnapshot senderSnapshot = transaction.get(senderDocRef);
                                DocumentSnapshot recipientSnapshot = transaction.get(recipientDocRef);

                                int senderBalance = senderSnapshot.getLong("balance").intValue();
                                int recipientBalance = recipientSnapshot.getLong("balance").intValue();

                                if (senderBalance < transferAmount) {
                                    throw new FirebaseFirestoreException("Insufficient funds",
                                            FirebaseFirestoreException.Code.ABORTED);
                                }

                                int newSenderBalance = senderBalance - transferAmount;
                                int newRecipientBalance = recipientBalance + transferAmount;

                                transaction.update(senderDocRef, "balance", newSenderBalance);
                                transaction.update(recipientDocRef, "balance", newRecipientBalance);

                                return null;
                            }
                        }).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(TransferMoney.this, "Transfer successful", Toast.LENGTH_SHORT).show();
                                finish(); // close the TransferActivity
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(TransferMoney.this, "Transfer failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                    } else {
                        Toast.makeText(TransferMoney.this, "Recipient not found", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(TransferMoney.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}