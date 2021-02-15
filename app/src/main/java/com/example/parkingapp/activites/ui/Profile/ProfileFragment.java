package com.example.parkingapp.activites.ui.Profile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.parkingapp.R;
import com.example.parkingapp.activites.HomePageActivity;
import com.example.parkingapp.activites.LoginActivity;
import com.example.parkingapp.model.User;
import com.example.parkingapp.viewmodel.UserViewModel;

public class ProfileFragment extends Fragment implements View.OnClickListener {

    private UserViewModel userViewModel;
    private TextView nameOfUser;
    private TextView emailOfUser;
    private TextView carPlateNo;
    private TextView contactNo;
    private Button updateProfile;
    private Button updatePassword;
    private Button deleteAccount;
    private Button logout;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        userViewModel = UserViewModel.getInstance();

        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        nameOfUser = root.findViewById(R.id.nameOfUser);
        emailOfUser = root.findViewById(R.id.emailOfUser);
        carPlateNo = root.findViewById(R.id.carPlateNoOfUser);
        contactNo = root.findViewById(R.id.phoneNumberOfUser);

        updateProfile = root.findViewById(R.id.updateProfile);
        updateProfile.setOnClickListener(this);

        updatePassword = root.findViewById(R.id.updatePassword);
        updatePassword.setOnClickListener(this);

        deleteAccount = root.findViewById(R.id.deleteAccount);
        deleteAccount.setOnClickListener(this);

        logout = root.findViewById(R.id.signOut);
        logout.setOnClickListener(this);

        userViewModel.getUserRepository().profileInfo.observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if(user.getName() != null) {
                    nameOfUser.setText("Name: "+ user.getName());
                    emailOfUser.setText("Email: "+ user.getEmail());
                    carPlateNo.setText("Car Plate Number: "+ user.getCarPlateNumber());
                    contactNo.setText("Contact Number: "+ user.getContactNumber());
                }
            }
        });


        return root;
    }

    public void initiateDeleteAccount() {

    }

    public void initiateUpdatePassword() {

    }

    public void initiateUpdateProfile() {

    }

    @Override
    public void onClick(View view) {
        if(view != null) {
            switch (view.getId()) {
                case R.id.updateProfile:
                    initiateUpdateProfile();
                    break;

                case R.id.updatePassword:
                    initiateUpdatePassword();
                    break;

                case R.id.deleteAccount:
                    initiateDeleteAccount();
                    break;

                case R.id.signOut:
//                    ((HomePageActivity) context)initiateLogout();
                    break;
            }
        }
    }
}