package com.example.parkingapp.activites;

import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.parkingapp.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent;
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("REMEMBER_ME", MODE_PRIVATE);
        String sharedPreferenceUsername = sharedPreferences.getString("email", null);

        if (sharedPreferenceUsername != null && !sharedPreferenceUsername.isEmpty()) {
            intent = new Intent(this, HomePageActivity.class);
            intent.putExtra("email", sharedPreferenceUsername);
        } else {
            intent = new Intent(this, LoginActivity.class);
        }
        startActivity(intent);
    }
}