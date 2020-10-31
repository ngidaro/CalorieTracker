package com.example.calorietracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.toolbox.Volley;

import user.User;

public class CreateAccountActivity extends AppCompatActivity {

    EditText etUsername;
    EditText etPassword;
    EditText etEmail;

    Button btnSignup;

    TextView tvHaveAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        etUsername = findViewById(R.id.ca_username);
        etPassword = findViewById(R.id.ca_password);
        etEmail = findViewById(R.id.ca_email);
        btnSignup = findViewById(R.id.ca_signup);
        tvHaveAccount = findViewById(R.id.ca_have_account);

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (etUsername.getText().toString().equals("") ||
                    etPassword.getText().toString().equals("") ||
                    etEmail.getText().toString().equals(""))
                {
                    // One or more fields are empty
                }
                else
                {
                    User.createUser(
                            etUsername.getText().toString(),
                            etPassword.getText().toString(),
                            etEmail.getText().toString(),
                            CreateAccountActivity.this);
                    // Go to Home page with token
                    Intent intent = new Intent(CreateAccountActivity.this, HomeActivity.class);
                    startActivity(intent);
                }

            }
        });

        tvHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Go back to Login Page
                Intent intent = new Intent(CreateAccountActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}