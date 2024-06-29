
package com.example.atmmachine;

        import androidx.appcompat.app.AppCompatActivity;

        import android.content.Intent;
        import android.os.Bundle;
        import android.text.TextUtils;
        import android.widget.EditText;
        import android.widget.Toast;

        import com.example.atmmachine.databinding.ActivityUpdateProfileBinding;
        import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.firestore.FirebaseFirestore;

        import java.util.HashMap;
        import java.util.Map;

public class UpdateProfile extends AppCompatActivity {

    private ActivityUpdateProfileBinding mBinding;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityUpdateProfileBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String email = intent.getStringExtra("email");

        mBinding.name.setText(name);
        mBinding.email.setText(email);

        mBinding.update.setOnClickListener(v -> updateUserProfile());
    }

    private void updateUserProfile() {
        String name = mBinding.name.getText().toString().trim();
        String email = mBinding.email.getText().toString().trim();

        if (name.isEmpty() ){
            mBinding.name.setError("Name is required");
            return;
        }

        if (email.isEmpty()) {
            mBinding.email.setError("Email is required");
            return;
        }

        // Get the current user ID
        String userId = mAuth.getCurrentUser().getUid();

        // Create a map to store the updated values
        Map<String, Object> userUpdates = new HashMap<>();
        userUpdates.put("userName", name);
        userUpdates.put("email", email);

        // Update the user document in Firestore
        db.collection("users").document(userId).update(userUpdates)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(UpdateProfile.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(UpdateProfile.this, "Failed to update profile", Toast.LENGTH_SHORT).show();
                });
    }
}

