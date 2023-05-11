package com.example.messagelogin.owner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.example.messagelogin.Constants;
import com.example.messagelogin.Customer_Homepage_BottomNav;
import com.example.messagelogin.Front;
import com.example.messagelogin.customer.PreferenceManager;
import com.example.messagelogin.customer.RegisterFront;
import com.example.messagelogin.databinding.ActivitySignInOwnerBinding;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignInOwner extends AppCompatActivity {

    private ActivitySignInOwnerBinding binding;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferenceManager = new PreferenceManager(getApplicationContext());
        /*if(preferenceManager.getBoolean(Constants.KEY_IS_SIGNED_IN))
        {
            Intent intent = new Intent(getApplicationContext(), Customer_Homepage_BottomNav.class);
            startActivity(intent);
            finish();
        }*/
        binding = ActivitySignInOwnerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setListeners();
    }

    private void setListeners()
    {
        //Making new account
        binding.textCreateNewAccount.setOnClickListener(v ->
                startActivity(new Intent(getApplicationContext(), RegisterFront.class)));

        binding.switchacc.setOnClickListener(v ->
                startActivity(new Intent(getApplicationContext(), Front.class)));
        //Logging in
        binding.buttonSignInOwner.setOnClickListener(v -> {
            if(isValidSignInDetails()) //method
            {
                signIn(); //method
            }
        });
    }

    private void signIn() {
        loading(true); //loading is a method
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_COLLECTION_OWNER)
                    .whereEqualTo(Constants.KEY_OWNER_EMAIL, binding.ownerEmail.getText().toString())
                .whereEqualTo(Constants.KEY_OWNER_PASSWORD, binding.ownerPassword.getText().toString())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null
                            && task.getResult().getDocuments().size() > 0) {
                        DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);

                        preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN, true);
                        preferenceManager.putString(Constants.KEY_USER_ID, documentSnapshot.getId());
                        preferenceManager.putString(Constants.KEY_OWNER_FIRSTNAME, documentSnapshot.getString(Constants.KEY_OWNER_FIRSTNAME));
                        preferenceManager.putString(Constants.KEY_OWNER_LAST_NAME, documentSnapshot.getString(Constants.KEY_OWNER_LAST_NAME));
                        preferenceManager.putString(Constants.KEY_OWNER_IMAGE, documentSnapshot.getString(Constants.KEY_OWNER_IMAGE));
                        startActivity(new Intent(SignInOwner.this, Customer_Homepage_BottomNav.class));

                    } else {
                        loading(false);
                        showToast("Unable to sign in");

                    }
                });
    }

    private void loading(Boolean isLoading)
    {
        if(isLoading)
        {
            binding.buttonSignInOwner.setVisibility(View.INVISIBLE);
            binding.progressBar.setVisibility(View.VISIBLE);
        }
        else
        {
            binding.progressBar.setVisibility(View.INVISIBLE);
            binding.buttonSignInOwner.setVisibility(View.VISIBLE);
        }
    }

    private void showToast(String message)
    {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    //this method is called in setListeners()
    private Boolean isValidSignInDetails()
    {
        if(binding.ownerEmail.getText().toString().trim().isEmpty())
        {
            showToast("Enter email");
            return false;
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(binding.ownerEmail.getText().toString()).matches())
        {
            showToast("Enter valid email");
            return false;
        }
        else if(binding.ownerPassword.getText().toString().trim().isEmpty())
        {
            showToast("Enter password");
            return false;
        }
        else
        {
            return true;
        }
    }



}