package com.example.calorietracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import api.API;
import user.User;

public class MainActivity extends AppCompatActivity {

    Button btnLogin;
    EditText etUsername;
    EditText etPassword;

    TextView tvCreateAccount;
    TextView tvForgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLogin = findViewById(R.id.log_login);
        etUsername = findViewById(R.id.log_username);
        etPassword = findViewById(R.id.log_password);
        tvCreateAccount = findViewById(R.id.log_create_account);
        tvForgotPassword = findViewById(R.id.log_forgot_pass);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Temporary for dev purposes
                User.getUser("ngidaro",
                        "123",
                        MainActivity.this);

//                if(etUsername.getText().toString().equals("") ||
//                   etPassword.getText().toString().equals(""))
//                {
//                    // One or more fields are empty
//                }
//                else {
//
//                    User.getUser(etUsername.getText().toString(),
//                            etPassword.getText().toString(),
//                            MainActivity.this);
//
//                }


            }
        });

        tvCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CreateAccountActivity.class);
                startActivity(intent);
            }
        });
    }
}