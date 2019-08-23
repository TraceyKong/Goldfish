package com.example.piggybank.Firebase;

import android.util.Log;

import com.example.piggybank.Firebase.Models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

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

    public void getChildrenByParentId(final String parentId,
                            final OnSuccessListener<ArrayList<User>> onSuccessListener,
                            final OnFailureListener onFailureListener) {
        db.collection("users").whereEqualTo("parentId", parentId).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                ArrayList<User> lst = new ArrayList<>();
                for (DocumentSnapshot doc : queryDocumentSnapshots) {
                    lst.add(User.SNAPSHOTPARSER.parseSnapshot(doc));
                }
                onSuccessListener.onSuccess(lst);
            }
        }).addOnFailureListener(onFailureListener);
    }
}
