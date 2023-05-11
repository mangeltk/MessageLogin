package com.example.messagelogin.owner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.messagelogin.Constants;
import com.example.messagelogin.R;
import com.example.messagelogin.customer.Chat;
import com.example.messagelogin.customer.PreferenceManager;
import com.example.messagelogin.customer.UserListener;
import com.example.messagelogin.customer.UserMdl;
import com.example.messagelogin.customer.UsersAdapter;
import com.example.messagelogin.databinding.ActivityOwnerUserBinding;
import com.example.messagelogin.databinding.ActivityUsersBinding;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.List;

public class OwnerUserActivity extends AppCompatActivity implements UserListener {

    private ActivityOwnerUserBinding binding;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOwnerUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());
        setListeners();
        getUsers();
    }

    private void setListeners()
    {
        binding.imageBack.setOnClickListener(v -> onBackPressed());
    }

    private void getUsers()
    {
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        loading(true);

        database.collection(Constants.KEY_COLLECTION_USERS).get().addOnCompleteListener(task -> {
            loading(false);
            if (task.isSuccessful() && task.getResult() != null) {
                List<UserMdl> users = new ArrayList<>();
                for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                    UserMdl userMdl = new UserMdl();
                    userMdl.customersFirstname = queryDocumentSnapshot.getString(Constants.KEY_FIRST_NAME);
                    //userMdl.cospaceCityAddress = queryDocumentSnapshot.getString(Constants.KEY_BRANCH_CITY_ADDRESS);
                    userMdl.image = queryDocumentSnapshot.getString(Constants.KEY_IMAGE);
                    userMdl.token = queryDocumentSnapshot.getString(Constants.KEY_FCM_TOKEN);

                        if (task.isSuccessful() && task.getResult()!=null){
                            for (QueryDocumentSnapshot queryDocumentSnapshot1 : task.getResult()){
                                userMdl.ownerIDNum = queryDocumentSnapshot1.getString(Constants.KEY_OWNER_USER_ID);
                            }
                            userMdl.customersIDNum = queryDocumentSnapshot.getId();
                            users.add(userMdl);
                        }
                }
                if(users.size() > 0)
                {
                    OwnerAdapter ownerAdapter = new OwnerAdapter(users, this);
                    binding.usersRecyclerView.setAdapter(ownerAdapter);
                    binding.usersRecyclerView.setVisibility(View.VISIBLE);
                }
                else
                {
                    showErrorMessage();
                }
            }
            else
            {
                showErrorMessage();
            }
        });

    }


    private void showErrorMessage()
    {
        binding.textErrorMessage.setText(String.format("%s", "No user available"));
        binding.textErrorMessage.setVisibility(View.VISIBLE);
    }

    private void loading(Boolean isLoading)
    {
        if(isLoading)
        {
            binding.progressBar.setVisibility(View.VISIBLE);
        }
        else
        {
            binding.progressBar.setVisibility(View.INVISIBLE);
        }
    }

    public void onUserClicked(UserMdl user) {
        Intent intent = new Intent(getApplicationContext(), Chat.class);
        intent.putExtra(Constants.KEY_USER, user);
        startActivity(intent);
        finish();
    }
}
