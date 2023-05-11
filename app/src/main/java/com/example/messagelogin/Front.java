package com.example.messagelogin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.messagelogin.customer.SignIn;
import com.example.messagelogin.databinding.ActivityFrontBinding;
import com.example.messagelogin.owner.SignInOwner;

public class Front extends AppCompatActivity {

    private ActivityFrontBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_front);


        Button signInCustomer, signInOwner;
        signInCustomer = findViewById(R.id.button_signInCustomer);
        signInOwner = findViewById(R.id.button_signInOwner);

        signInCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), SignIn.class));
            }
        });

        signInOwner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), SignIn.class));
            }
        });

    }

}