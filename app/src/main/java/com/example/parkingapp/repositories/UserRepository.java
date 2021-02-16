package com.example.parkingapp.repositories;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.parkingapp.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.*;
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
    public MutableLiveData<String> statusOfUpdateProfile = new MutableLiveData<>();
    public MutableLiveData<String> statusOfUpdatePassword = new MutableLiveData<>();
    public MutableLiveData<String> statusOfDeleteAccount = new MutableLiveData<>();

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
                                    userId.setValue(task.getResult().getDocuments().get(0).getId());
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

    public void updateUserInfo(User user) {
        FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
        try {
            fUser.updateEmail(user.getEmail())
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            updateUserInfoInDb(userId.getValue(), user);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            statusOfUpdateProfile.postValue(e.getLocalizedMessage());
                        }
                    });
        } catch (Exception ex) {
            statusOfUpdateProfile.postValue(ex.getLocalizedMessage());
        }
    }

    private void updateUserInfoInDb(String userId, User user) {
        try {
            db.collection(COLLECTION_NAME)
                    .document(userId)
                    .set(user)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            statusOfUpdateProfile.postValue("SUCCESS");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            statusOfUpdateProfile.postValue(e.getLocalizedMessage());
                        }
                    });
        } catch (Exception ex) {
            statusOfUpdateProfile.postValue(ex.getLocalizedMessage());
        }
    }


    public void updatePasswordOfUser(String password, String newPassword, String email) {
        FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
        AuthCredential credential = EmailAuthProvider
                .getCredential(email, password);

        try {
            fUser.reauthenticate(credential)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            try {
                                fUser.updatePassword(newPassword)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                statusOfUpdatePassword.postValue("SUCCESS");
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                statusOfUpdatePassword.postValue(e.getLocalizedMessage());
                                            }
                                        });
                            } catch (Exception ex) {
                                statusOfUpdatePassword.postValue(ex.getLocalizedMessage());
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            statusOfUpdatePassword.postValue(e.getLocalizedMessage());
                        }
                    });
        } catch (Exception ex) {
            statusOfUpdatePassword.postValue(ex.getLocalizedMessage());
        }
    }


    public void deleteUserAccount(String email, String password) {
        FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
        AuthCredential credential = EmailAuthProvider
                .getCredential(email, password);

        try {
            fUser.reauthenticate(credential)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            try {
                                fUser.delete()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                try {
                                                    db.collection(COLLECTION_NAME)
                                                            .document(userId.getValue())
                                                            .delete()
                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {
                                                                    statusOfDeleteAccount.postValue("SUCCESS");
                                                                }
                                                            })
                                                            .addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception ex) {
                                                                    statusOfDeleteAccount.postValue(ex.getLocalizedMessage());
                                                                }
                                                            });
                                                } catch (Exception ex) {
                                                    statusOfDeleteAccount.postValue(ex.getLocalizedMessage());
                                                }
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception ex) {
                                                statusOfDeleteAccount.postValue(ex.getLocalizedMessage());
                                            }
                                        });
                            } catch (Exception ex) {
                                statusOfDeleteAccount.postValue(ex.getLocalizedMessage());
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception ex) {
                            statusOfDeleteAccount.postValue(ex.getLocalizedMessage());
                        }
                    });
        } catch (Exception ex) {
            statusOfDeleteAccount.postValue(ex.getLocalizedMessage());
        }

    }

    public void logoutUser() {
        statusOfDeleteAccount.postValue(null);
        statusOfUpdatePassword.postValue(null);
        statusOfUpdateProfile.postValue(null);
        signInStatus.postValue(null);
        userId.postValue(null);
    }
}
