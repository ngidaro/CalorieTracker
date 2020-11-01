package com.example.calorietracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class HomeActivity extends AppCompatActivity {

    TextView tvUsername;
    ImageView ivBarcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        tvUsername = findViewById(R.id.ha_username);
        String username = getIntent().getStringExtra("username");

        tvUsername.setText(username);

        // Getting the Image Viewer, and making it clickable to go to BarcodeActivity
        ivBarcode = findViewById(R.id.barcode_image);
        ivBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToBarcodeActivity();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        //Inflate the menu
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Takes user to BarcodeActivity
    protected void goToBarcodeActivity()
    {
        Intent intent = new Intent(this,BarcodeActivity.class);
        startActivity(intent);
    }

    // Prevents user from pressing back into the login screen
    @Override
    public void onBackPressed()
    {
        moveTaskToBack(true);
    }
}



