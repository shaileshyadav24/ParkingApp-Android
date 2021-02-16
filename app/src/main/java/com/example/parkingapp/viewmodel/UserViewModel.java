package com.example.parkingapp.viewmodel;

import com.example.parkingapp.model.User;
import com.example.parkingapp.repositories.UserRepository;

public class UserViewModel {

    private static final UserViewModel ourInstance = new UserViewModel();
    private final UserRepository userRepository = new UserRepository();

    public static UserViewModel getInstance() {
        return ourInstance;
    }

    private UserViewModel() {
    }

    public UserRepository getUserRepository() {
        return this.userRepository;
    }

    public void addUser(User user, String password) {
        this.userRepository.addUser(user, password);
    }

    public void validateUser(String email, String password) {
        userRepository.isValidUser(email, password);
    }

    public void searchUserByEmail(String email) {
        userRepository.getUser(email);
    }

    public void updateUserProfile(User user) {
        userRepository.updateUserInfo(user);
    }

    public void updatePasswordOfUser(String password, String newPassword, String email) {
        userRepository.updatePasswordOfUser(password, newPassword, email);
    }

    public void deleteUser(String email, String password) {
        userRepository.deleteUserAccount(email, password);
    }
}
