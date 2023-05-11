package com.example.messagelogin.owner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.widget.Toast;

import com.example.messagelogin.Constants;
import com.example.messagelogin.customer.PreferenceManager;
import com.example.messagelogin.customer.SignIn;
import com.example.messagelogin.customer.UsersActivity;
import com.example.messagelogin.databinding.ActivityMessagesBinding;
import com.example.messagelogin.databinding.ActivityOwnerMessagesBinding;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;

public class Owner_Messages extends AppCompatActivity {

    private ActivityOwnerMessagesBinding binding;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOwnerMessagesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());
        loadUserDetails();
        getToken();
        setListeners();
    }
    private void setListeners() {
        binding.ownerImageSignOut.setOnClickListener(v -> signOut());
        binding.ownerfabNewChat.setOnClickListener(v ->
                startActivity(new Intent(getApplicationContext(), OwnerUserActivity.class)));
    }

    private void loadUserDetails() {
        binding.ownertextFirstName.setText(preferenceManager.getString(Constants.KEY_OWNER_FIRSTNAME));
        binding.ownertextLastName.setText(preferenceManager.getString(Constants.KEY_OWNER_LAST_NAME));
        String imageString = preferenceManager.getString(Constants.KEY_IMAGE);
        if (imageString != null) {
            byte[] bytes = Base64.decode(imageString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            binding.ownerimageProfile.setImageBitmap(bitmap);
        }

    }

    private void showToast(String message)
    {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void getToken()
    {
        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(this::updateToken);

    }

    private void updateToken(String token) {
        preferenceManager.putString(Constants.KEY_FCM_TOKEN, token);
        FirebaseFirestore database = FirebaseFirestore.getInstance();

        String ownerUserId = preferenceManager.getString(Constants.KEY_OWNER_USER_ID);
        if (ownerUserId == null || ownerUserId.isEmpty()) {
            showToast("Owner User ID is null or empty");
            return;
        }

        DocumentReference ownerDocumentReference =
                database.collection(Constants.KEY_COLLECTION_OWNER)
                        .document(ownerUserId);
        ownerDocumentReference.update(Constants.KEY_FCM_TOKEN, token)
                .addOnFailureListener(e -> showToast("Unable to update token"));
    }



    private void signOut() {
        showToast("Signing Out...");
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference documentReference = database.collection(Constants.KEY_COLLECTION_OWNER)
                .document(preferenceManager.getString(Constants.KEY_OWNER_USER_ID));

        HashMap<String, Object> updates = new HashMap<>();
        updates.put(Constants.KEY_FCM_TOKEN, FieldValue.delete());
        documentReference.update(updates)
                .addOnSuccessListener(unused -> {
                    preferenceManager.clear();
                    startActivity(new Intent(getApplicationContext(), SignInOwner.class));
                    finish();
                })
                .addOnFailureListener(e -> showToast("Unable to sign out"));
    }


}