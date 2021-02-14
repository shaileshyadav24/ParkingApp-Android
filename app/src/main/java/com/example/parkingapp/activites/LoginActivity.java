package com.example.parkingapp.activites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.parkingapp.R;
import com.example.parkingapp.viewmodel.UserViewModel;
import com.google.firebase.FirebaseApp;

import static android.view.View.GONE;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = this.getClass().getCanonicalName();
    public static final String MyPREFERENCES = "EMAILADDRESS" ;
    SharedPreferences sharedpreferences;

    private EditText emailAddress;
    private EditText password;
    private CheckBox rememberMe;
    private TextView errorMessage;
    private Button loginBtn;
    private TextView createAccount;

    private UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        this.userViewModel = UserViewModel.getInstance();

        emailAddress = findViewById(R.id.username);
        password = findViewById(R.id.password);
        rememberMe = findViewById(R.id.rememberMe);
        errorMessage = findViewById(R.id.validationMessage);
        loginBtn = findViewById(R.id.loginUser);
        loginBtn.setOnClickListener(this);
        createAccount = findViewById(R.id.registration);
        createAccount.setOnClickListener(this);

        this.userViewModel.getUserRepository().signInStatus.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s.equals("SUCCESS")) {
                    goToDashboard();
                } else {
                    errorMessage.setVisibility(View.VISIBLE);
                    errorMessage.setText(s);
                }
            }
        });
    }

    public void goToDashboard() {
        if(rememberMe.isChecked()) {
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString("email", emailAddress.getText().toString());
        }
        Intent makeIntent = new Intent(this, DashboardActivity.class);
        makeIntent.putExtra("email", emailAddress.getText().toString());
        startActivity(makeIntent);
    }

    public void validateUser() {
        String email = emailAddress.getText().toString();
        String pwd = password.getText().toString();
        errorMessage.setVisibility(GONE);
        errorMessage.setText("");
        if (!email.isEmpty() && email != "" && !pwd.isEmpty() && pwd != "") {
            this.userViewModel.validateUser(email, pwd);
        } else if (email.isEmpty() || email == "") {
            errorMessage.setVisibility(View.VISIBLE);
            errorMessage.setText("Please enter email");
        } else if (pwd.isEmpty() || pwd == "") {
            errorMessage.setVisibility(View.VISIBLE);
            errorMessage.setText("Please enter password");
        }
    }

    @Override
    public void onClick(View view) {
        if (view != null) {
            switch (view.getId()) {
                case R.id.loginUser:
                    this.validateUser();
                    break;
                case R.id.registration:
                    Intent makeIntent = new Intent(this, RegistrationActivity.class);
                    startActivity(makeIntent);
                    break;
            }
        }
    }
}