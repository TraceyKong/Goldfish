package com.example.piggybank.Firebase;

import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class Get {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final static String TAG = Get.class.getName();

    public void getUserById(final String userId,
                            final OnSuccessListener<User> onSuccessListener,
                            final OnFailureListener onFailureListener) {
        db.collection("users").document(userId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    User user;
                    try {
                        user = User.SNAPSHOTPARSER.parseSnapshot(documentSnapshot);
                    } catch (NullPointerException exc) {
                        user = null;
                        Log.w(TAG, "User with id " + userId + " was corrupt ", exc);
                    }
                    onSuccessListener.onSuccess(user);
                } else {
                    Log.e(TAG, "User with id " + userId + " doesn't exit");
                    onSuccessListener.onSuccess(null);
                }



                User user = User.SNAPSHOTPARSER.parseSnapshot(documentSnapshot);
                onSuccessListener.onSuccess(user);
            }
        }).addOnFailureListener(onFailureListener);
    }
}
