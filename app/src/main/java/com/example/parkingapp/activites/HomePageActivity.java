package com.example.parkingapp.activites;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.parkingapp.R;
import com.example.parkingapp.viewmodel.UserViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class HomePageActivity extends AppCompatActivity  {

    private UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        Intent intent = getIntent();
        String loggedInEmail = intent.getStringExtra("email");

        userViewModel = UserViewModel.getInstance();

        if(loggedInEmail != null) {
            userViewModel.searchUserByEmail(loggedInEmail);
        }

        userViewModel.getUserRepository().userId.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if(s != null) {
                    initializeBottomTabBar();
                }
            }
        });

    }

    public void initializeBottomTabBar() {

        BottomNavigationView navView = findViewById(R.id.nav_view);


        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }

    public void initiateLogout() {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("REMEMBER_ME", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("email");
        editor.commit();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

}