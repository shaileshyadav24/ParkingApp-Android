package com.example.parkingapp.activites.ui.Profile;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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

    private User loggedInUser;

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
                if (user.getName() != null) {
                    nameOfUser.setText("Name: " + user.getName());
                    emailOfUser.setText("Email: " + user.getEmail());
                    carPlateNo.setText("Car Plate Number: " + user.getCarPlateNumber());
                    contactNo.setText("Contact Number: " + user.getContactNumber());
                    loggedInUser = user;
                }
            }
        });


        return root;
    }

    public void initiateDeleteAccount() {
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.layout_delete_profile, null);


        EditText password = dialogView.findViewById(R.id.deleteConfirmPassword);
        TextView errorMessage = dialogView.findViewById(R.id.deleteProfileError);
        errorMessage.setVisibility(View.GONE);

        userViewModel.getUserRepository().statusOfDeleteAccount.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s != null) {
                    if (s.contentEquals("SUCCESS")) {
                        Toast.makeText(getContext(), "Account deleted successfully", Toast.LENGTH_SHORT).show();
                        ((HomePageActivity) getActivity()).initiateLogout();
                    } else {
                        errorMessage.setText(s);
                        errorMessage.setTextColor(Color.RED);
                        errorMessage.setVisibility(View.VISIBLE);
                    }
                }
            }
        });


        AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                .setTitle("Delete Profile")
                .setMessage("Are you sure want to delete your account? No account information will be available.")
                .setView(dialogView)
                .setPositiveButton("Delete", null)
                .setNegativeButton("Cancel", null)
                .create();

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {

                Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        errorMessage.setVisibility(View.GONE);

                        if (!password.getText().toString().isEmpty()) {
                            userViewModel.deleteUser(loggedInUser.getEmail(), password.getText().toString());
                        } else {
                            errorMessage.setText("Please fill password");
                            errorMessage.setTextColor(Color.RED);
                            errorMessage.setVisibility(View.VISIBLE);
                        }

                    }
                });
            }
        });

        alertDialog.show();
    }

    public void initiateUpdatePassword() {
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.layout_update_password, null);

        EditText password = dialogView.findViewById(R.id.passwordCurrent);
        EditText newPassword = dialogView.findViewById(R.id.passwordNew);
        TextView errorMessage = dialogView.findViewById(R.id.passwordError);
        errorMessage.setVisibility(View.GONE);

        AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                .setTitle("Update Password")
                .setView(dialogView)
                .setPositiveButton("Update", null)
                .setNegativeButton("Cancel", null)
                .create();


        userViewModel.getUserRepository().statusOfUpdatePassword.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s != null) {
                    if (s.contentEquals("SUCCESS")) {
                        errorMessage.setText("Password update successfull");
                        errorMessage.setTextColor(Color.GREEN);
                        errorMessage.setVisibility(View.VISIBLE);
                    } else {
                        errorMessage.setText(s);
                        errorMessage.setTextColor(Color.RED);
                        errorMessage.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {

                Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        errorMessage.setVisibility(View.GONE);
                        if (!password.getText().toString().isEmpty() &&
                                !newPassword.getText().toString().isEmpty()) {


                            if (password.getText().toString().length() >= 6 &&
                                    newPassword.getText().toString().length() >= 6) {


                                userViewModel.updatePasswordOfUser(password.getText().toString(),
                                        newPassword.getText().toString(),
                                        loggedInUser.getEmail());


                            } else {
                                errorMessage.setText("Password length should be more than or equal to 6");
                                errorMessage.setTextColor(Color.RED);
                                errorMessage.setVisibility(View.VISIBLE);

                            }

                        } else {
                            errorMessage.setText("Please fill all details");
                            errorMessage.setTextColor(Color.RED);
                            errorMessage.setVisibility(View.VISIBLE);

                        }

                    }
                });

            }
        });

        alertDialog.show();
    }

    public void initiateUpdateProfile() {
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.layout_update_profile, null);

        EditText name = dialogView.findViewById(R.id.profileName);
        EditText email = dialogView.findViewById(R.id.profileEmail);
        EditText plate = dialogView.findViewById(R.id.profilePlate);
        EditText phone = dialogView.findViewById(R.id.profilePhone);
        TextView errorMessage = dialogView.findViewById(R.id.profileError);

        name.setText(this.loggedInUser.getName());
        email.setText(this.loggedInUser.getEmail());
        plate.setText(this.loggedInUser.getCarPlateNumber());
        phone.setText(this.loggedInUser.getContactNumber());
        errorMessage.setVisibility(View.GONE);


        userViewModel.getUserRepository().statusOfUpdateProfile.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s != null) {
                    if (s.contentEquals("SUCCESS")) {
                        errorMessage.setText("Profile update successfull");
                        errorMessage.setTextColor(Color.GREEN);
                        errorMessage.setVisibility(View.VISIBLE);
                        userViewModel.searchUserByEmail(email.getText().toString());
                    } else {
                        errorMessage.setText(s);
                        errorMessage.setTextColor(Color.RED);
                        errorMessage.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                .setTitle("Update Profile")
                .setView(dialogView)
                .setPositiveButton("Update", null)
                .setNegativeButton(android.R.string.cancel, null)
                .create();

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {

                Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        errorMessage.setVisibility(View.GONE);

                        if (!name.getText().toString().isEmpty() &&
                                !email.getText().toString().isEmpty() &&
                                !plate.getText().toString().isEmpty() &&
                                !phone.getText().toString().isEmpty()) {

                            if (plate.getText().toString().length() < 2 || plate.getText().toString().length() > 8) {
                                errorMessage.setText("Car plate number should be between 2 to 8 characters.");
                                errorMessage.setTextColor(Color.RED);
                                errorMessage.setVisibility(View.VISIBLE);
                            } else {
                                User updatedUser = new User(
                                        name.getText().toString(),
                                        email.getText().toString(),
                                        phone.getText().toString(),
                                        plate.getText().toString()
                                );
                                userViewModel.updateUserProfile(updatedUser);
                            }
                        } else {
                            errorMessage.setText("Please fill all details");
                            errorMessage.setTextColor(Color.RED);
                            errorMessage.setVisibility(View.VISIBLE);
                        }
                    }
                });

            }
        });

        alertDialog.show();

    }

    @Override
    public void onClick(View view) {
        if (view != null) {
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
                    ((HomePageActivity) getActivity()).initiateLogout();
                    break;
            }
        }
    }
}