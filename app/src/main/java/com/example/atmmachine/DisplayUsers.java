package com.example.atmmachine;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.atmmachine.databinding.ActivityDisplayUsersBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;


public class DisplayUsers extends AppCompatActivity {
    private FirebaseFirestore db;
    private UserAdapter adapter;
    private ActivityDisplayUsersBinding mBinding;
    private List<UserData> userList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityDisplayUsersBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        userList = new ArrayList<>();
        mBinding.userRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();

        db.collection("users").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        userList.clear();
                        for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()) {
                            UserData user = snapshot.toObject(UserData.class);
                            if (user != null) {
                                userList.add(user);
                            }
                        }
                adapter = new UserAdapter(userList);
                mBinding.userRecyclerView.setAdapter(adapter);
            }
        });
    }
}
