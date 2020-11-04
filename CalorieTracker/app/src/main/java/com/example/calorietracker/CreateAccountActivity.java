package com.example.calorietracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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


        final Context contextAccount = this;

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String password = etEmail.getText().toString();

                boolean badPassword = true;

                boolean checked = false;

                if (password.length() >= 11)
                    // Checks for an @. in the email address outlook and hotmail
                    for (int i = 0; i < password.length() - 10; i++) {
                        if (Character.toString(password.charAt(i)).equals("@") && Character.toString(password.charAt(i + 8)).equals(".")) {
                            badPassword = false;
                            checked = true;
                        }
                    }

                if (password.length() >= 9 && !checked)
                    // Checks for an @. in email address yahoo, gmail
                    for (int i = 0; i < password.length() - 8; i++) {
                        if(Character.toString(password.charAt(i)).equals("@") && Character.toString(password.charAt(i + 6)).equals(".")) {
                            badPassword = false;
                            checked = true;
                        }
                    }

                if (etUsername.getText().toString().equals("") ||
                    etPassword.getText().toString().equals("") ||
                    etEmail.getText().toString().equals(""))
                {
                    // One or more fields are empty
                    Toast.makeText(contextAccount,"Cannot leave a text field empty",Toast.LENGTH_LONG).show();
                }

                else if (etPassword.getText().toString().length() < 6)
                {
                    Toast.makeText(contextAccount,"Password must be at least 6 characters",Toast.LENGTH_LONG).show();
                }

                else if (badPassword)
                {
                    Toast.makeText(contextAccount,"Invalid email address",Toast.LENGTH_LONG).show();
                }

                else
                {
                    User.createUser(
                            etUsername.getText().toString(),
                            etPassword.getText().toString(),
                            etEmail.getText().toString(),
                            CreateAccountActivity.this);
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