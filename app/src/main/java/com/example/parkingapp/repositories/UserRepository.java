package com.example.parkingapp.repositories;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.parkingapp.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserRepository {
    private final String TAG = this.getClass().getCanonicalName();
    private final String COLLECTION_NAME = "users";
    private final FirebaseFirestore db;
    public MutableLiveData<String> signInStatus = new MutableLiveData<String>();
    public MutableLiveData<String> userId = new MutableLiveData<String>();
    public MutableLiveData<String> statusOfRegistration = new MutableLiveData<>();

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public UserRepository() {
        this.db = FirebaseFirestore.getInstance();
    }

    public void addUser(final User user, String password) {
        try {
            mAuth.createUserWithEmailAndPassword(user.getEmail(), password)
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            Log.d(TAG, "Added into Authentication");
                            addUserToFirebase(user);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "Failure adding into Authentication");
                            statusOfRegistration.postValue(e.getLocalizedMessage());
                        }
                    });

        } catch (Exception ex) {
            Log.e(TAG, ex.toString());
            Log.e(TAG, ex.getLocalizedMessage());
            statusOfRegistration.postValue(ex.getLocalizedMessage());
        }
    }

    private void addUserToFirebase(User user) {
        try {
            db.collection(COLLECTION_NAME)
                    .add(user)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d(TAG, "Document added with ID : " + documentReference.getId());
                            statusOfRegistration.postValue("SUCCESS");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e(TAG, "Error adding document to the store " + e);
                            statusOfRegistration.postValue(e.getLocalizedMessage());
                        }
                    });

        } catch (Exception ex) {
            Log.e(TAG, ex.toString());
            Log.e(TAG, ex.getLocalizedMessage());
            statusOfRegistration.postValue(ex.getLocalizedMessage());
        }
    }

    public void isValidUser(String email, String password) {
        try {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            Log.e(TAG, "USER AUTHENTICATION SUCCESS");
                            signInStatus.postValue("SUCCESS");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e(TAG, "USER AUTHENTICATION FAIL: "+ e.getLocalizedMessage());
                            signInStatus.postValue(e.getLocalizedMessage());
                        }
                    });
        } catch(Exception ex) {
            Log.e(TAG, ex.toString());
            Log.e(TAG, ex.getLocalizedMessage());
            signInStatus.postValue(ex.getLocalizedMessage());
        }
    }
}
