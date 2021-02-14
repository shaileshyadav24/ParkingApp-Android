package com.example.parkingapp.activites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.parkingapp.R;
import com.example.parkingapp.model.User;
import com.example.parkingapp.viewmodel.UserViewModel;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText registerName;
    private EditText registerEmail;
    private EditText registerPassword;
    private EditText registerConfirmPassword;
    private EditText registerPhoneNo;
    private EditText registerCarPlateNo;
    private TextView errorMessage;
    private Button saveRegister;
    private ImageView returnLogin;
    private UserViewModel userViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        userViewModel = UserViewModel.getInstance();

        registerName = findViewById(R.id.registerName);
        registerEmail = findViewById(R.id.registerEmail);
        registerPassword = findViewById(R.id.registerPassword);
        registerConfirmPassword = findViewById(R.id.registerCPassword);
        registerPhoneNo = findViewById(R.id.registerPhone);
        registerCarPlateNo = findViewById(R.id.registerPlateNo);
        errorMessage = findViewById(R.id.errorMessage);
        saveRegister = findViewById(R.id.registerUser);
        saveRegister.setOnClickListener(this);
        returnLogin = findViewById(R.id.returnToLogin);
        returnLogin.setOnClickListener(this);

        userViewModel.getUserRepository().statusOfRegistration.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if(s != null) {
                    if(s.contentEquals("SUCCESS")) {
                        errorMessage.setVisibility(View.VISIBLE);
                        errorMessage.setTextColor(Color.GREEN);
                        errorMessage.setText("Registration complete. Please check login.");
                    } else {
                        errorMessageRenderer(s);
                    }
                }
            }
        });
    }

    public boolean validateRegistrationDetails() {
        errorMessage.setVisibility(View.GONE);
        if (!registerName.getText().toString().isEmpty() &&
                !registerEmail.getText().toString().isEmpty() &&
                !registerPassword.getText().toString().isEmpty() &&
                !registerConfirmPassword.getText().toString().isEmpty() &&
                !registerPhoneNo.getText().toString().isEmpty() &&
                !registerCarPlateNo.getText().toString().isEmpty()) {


            if (registerPassword.getText().toString().length() >= 6) {

                if (registerPassword.getText().toString().contentEquals(registerConfirmPassword.getText().toString())) {
                    return false;
                } else {
                    errorMessageRenderer("Password does not match");
                    return true;
                }


            } else {
                errorMessageRenderer("Password length should be more than or equal to 6");
                return true;
            }

        } else {
            errorMessageRenderer("Please fill all details");
            return true;
        }
    }

    public void errorMessageRenderer(String message) {
        errorMessage.setVisibility(View.VISIBLE);
        errorMessage.setTextColor(Color.RED);
        errorMessage.setText(message);
    }

//    String name, String email, String contactNumber, String carPlateNumber

    public void initiateSaveNewUser() {
        if (!validateRegistrationDetails()) {
            User user = new User(registerName.getText().toString(),
                    registerEmail.getText().toString(),
                    registerPhoneNo.getText().toString(),
                    registerCarPlateNo.getText().toString());
            userViewModel.addUser(user, registerPassword.getText().toString());
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.registerUser) {
            this.initiateSaveNewUser();
        } else if(view.getId() == R.id.returnToLogin) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
    }
}