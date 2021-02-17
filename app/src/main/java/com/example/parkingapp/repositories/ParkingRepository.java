package com.example.parkingapp.repositories;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.example.parkingapp.model.Parking;
import com.example.parkingapp.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ParkingRepository {
    private final String TAG = this.getClass().getCanonicalName();
    private final String COLLECTION_NAME = "users";
    private final String COLLECTION_PARKING = "parking";
    private final FirebaseFirestore db;

    public MutableLiveData<String> parkingStatus = new MutableLiveData<>();
    public MutableLiveData<String> deleteParking = new MutableLiveData<>();
    public MutableLiveData<ArrayList<Parking>> parkingList = new MutableLiveData<>();
    public MutableLiveData<Parking> parkingInfo = new MutableLiveData<Parking>();

    public ParkingRepository() {
        this.db = FirebaseFirestore.getInstance();
    }

    public void getAllParkingList(String id) {
        try {
            db.collection(COLLECTION_NAME)
                    .document(id)
                    .collection(COLLECTION_PARKING)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                            if (error != null) {
                                Log.e(TAG, "Error fetching list " + error);
                                return;
                            }

                            ArrayList<Parking> tmpList = new ArrayList<>();
                            if (value != null) {

                                for (DocumentChange documentChange : value.getDocumentChanges()) {
                                    Parking listParkingList = documentChange.getDocument().toObject(Parking.class);
                                    listParkingList.setId(documentChange.getDocument().getId());
                                    switch (documentChange.getType()) {
                                        case ADDED:
                                            tmpList.add(listParkingList);
                                            break;
                                        case REMOVED:
                                            tmpList.remove(listParkingList);
                                            break;
                                        default:
                                            break;
                                    }

                                    parkingList.postValue(tmpList);
                                }

                            }
                        }
                    });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addParkingToUser(String id, Parking parking) {
        try {
            db.collection(COLLECTION_NAME)
                    .document(id)
                    .collection(COLLECTION_PARKING)
                    .add(parking)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d(TAG, "Document added with ID : " + documentReference.getId());
                            parkingStatus.postValue("SUCCESS");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e(TAG, "Error adding document to the store " + e);
                            parkingStatus.postValue(e.getLocalizedMessage());

                        }
                    });

        } catch (Exception ex) {
            Log.e(TAG, ex.toString());
            Log.e(TAG, ex.getLocalizedMessage());
            parkingStatus.postValue(ex.getLocalizedMessage());
        }
    }

    public void getParkingInformation(String userId, String parkingId) {
//        try {
//            db.collection(COLLECTION_NAME)
//                    .document(userId)
//                    .collection(COLLECTION_PARKING)
//                    .document(parkingId)
//                    .
//        } catch (Exception ex) {
//            Log.e(TAG, ex.toString());
//            Log.e(TAG, ex.getLocalizedMessage());
//        }
    }

    public void deleteParkingFromUser(String userId, String parkingId) {
        try {
            db.collection(COLLECTION_NAME)
                    .document(userId)
                    .collection(COLLECTION_PARKING)
                    .document(parkingId)
                    .delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            deleteParking.postValue("SUCCESS");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception ex) {
                            deleteParking.postValue(ex.getLocalizedMessage());
                        }
                    });
        } catch (Exception ex) {
            deleteParking.postValue(ex.getLocalizedMessage());
        }
    }

}
