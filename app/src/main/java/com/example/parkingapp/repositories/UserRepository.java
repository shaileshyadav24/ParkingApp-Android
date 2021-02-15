package com.example.parkingapp.repositories;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.parkingapp.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class UserRepository {
    private final String TAG = this.getClass().getCanonicalName();
    private final String COLLECTION_NAME = "users";
    private final FirebaseFirestore db;
    public MutableLiveData<String> signInStatus = new MutableLiveData<String>();
    public MutableLiveData<String> userId = new MutableLiveData<String>();
    public MutableLiveData<User> profileInfo = new MutableLiveData<User>();
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
                            Log.e(TAG, "USER AUTHENTICATION FAIL: " + e.getLocalizedMessage());
                            signInStatus.postValue(e.getLocalizedMessage());
                        }
                    });
        } catch (Exception ex) {
            Log.e(TAG, ex.toString());
            Log.e(TAG, ex.getLocalizedMessage());
            signInStatus.postValue(ex.getLocalizedMessage());
        }
    }

    public void getUser(String email) {
        try {
            db.collection(COLLECTION_NAME)
                    .whereEqualTo("email", email)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d(TAG, document.getId() + "---" + document.getData());
                                    userId.postValue(task.getResult().getDocuments().get(0).getId());
//                                    String name, String email, String contactNumber, String carPlateNumber
                                    profileInfo.setValue(
                                            new User(
                                                    task.getResult().getDocuments().get(0).get("name").toString(),
                                                    task.getResult().getDocuments().get(0).get("email").toString(),
                                                    task.getResult().getDocuments().get(0).get("contactNumber").toString(),
                                                    task.getResult().getDocuments().get(0).get("carPlateNumber").toString()
                                            ));
                                }
                            } else {
                                Log.e(TAG, "Error fetching document" + task.getException());
                            }
                        }
                    });
        } catch (Exception ex) {
            Log.e(TAG, ex.toString());
            Log.e(TAG, ex.getLocalizedMessage());
        }
    }
}
