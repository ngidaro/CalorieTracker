package com.example.calorietracker;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Toast;

import user.User;

public class LoginActivity extends AppCompatActivity {

    Button btnLogin;
    EditText etUsername;
    EditText etPassword;

    TextView tvCreateAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogin = findViewById(R.id.log_login);
        etUsername = findViewById(R.id.log_username);
        etPassword = findViewById(R.id.log_password);
        tvCreateAccount = findViewById(R.id.log_create_account);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ShowToast")
            @Override
            public void onClick(View v) {

                // Temporary for dev purposes
//                User.getUser("ngidaro",
//                        "123",
//                        LoginActivity.this);

                if(etUsername.getText().toString().equals("") ||
                   etPassword.getText().toString().equals(""))
                {
                    Toast.makeText(LoginActivity.this, "One or more fields are empty", Toast.LENGTH_LONG).show();
                }
                else {

                    User.getUser(etUsername.getText().toString(),
                            etPassword.getText().toString(),
                            LoginActivity.this);

                }
            }
        });

        tvCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, CreateAccountActivity.class);
                startActivity(intent);
            }
        });
    }
}