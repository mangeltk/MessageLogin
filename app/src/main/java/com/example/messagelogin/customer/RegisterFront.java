package com.example.messagelogin.customer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.messagelogin.R;
import com.example.messagelogin.databinding.ActivityRegisterFrontBinding;
import com.example.messagelogin.owner.SignUpOwner;

public class RegisterFront extends AppCompatActivity {

    private ActivityRegisterFrontBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_front);

        Button signUpCustomer, signUpOwner;
        signUpCustomer = findViewById(R.id.button_signUpCustomer);
        signUpOwner = findViewById(R.id.button_signUpOwner);

        signUpCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), SignUp.class));
            }
        });

        signUpOwner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), SignUpOwner.class));
            }
        });
    }

}