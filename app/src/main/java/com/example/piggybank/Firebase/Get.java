package com.example.piggybank.Firebase;

import android.util.Log;

import com.example.piggybank.Firebase.Models.Task;
import com.example.piggybank.Firebase.Models.Transaction;
import com.example.piggybank.Firebase.Models.User;
import com.example.piggybank.Firebase.Models.WishListItem;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Get {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final static String TAG = Get.class.getName();

    //gets a user object given the user's id
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
            }
        }).addOnFailureListener(onFailureListener);
    }

    //gets user object given the parent's id
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

    //gets an arraylist of tasks given the user's id
    public void getTasksByChildId(final String childId,
                                  final OnSuccessListener<ArrayList<Task>> onSuccessListener,
                                  final OnFailureListener onFailureListener) {
        db.collection("tasks").whereEqualTo("childId", childId).orderBy("timeAssigned", Query.Direction.DESCENDING).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                ArrayList<Task> tasks = new ArrayList<>();
                for(QueryDocumentSnapshot docSnapshot : queryDocumentSnapshots)
                {
                    Task task = Task.SNAPSHOTPARSER.parseSnapshot(docSnapshot);
                    tasks.add(task);
                }
                onSuccessListener.onSuccess(tasks);
            }
        }).addOnFailureListener(onFailureListener);
    }
    //gets a limited number of tasks in an arraylist given the user's id and the limit for how many tasks should be queried
    public void getTasksByChildId(final String childId,
                                  final int limit,
                                  final OnSuccessListener<ArrayList<Task>> onSuccessListener,
                                  final OnFailureListener onFailureListener) {
        db.collection("tasks").whereEqualTo("childId", childId).orderBy("timeAssigned", Query.Direction.DESCENDING).limit(limit).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                ArrayList<Task> tasks = new ArrayList<>();
                for(QueryDocumentSnapshot docSnapshot : queryDocumentSnapshots)
                {
                    Task task = Task.SNAPSHOTPARSER.parseSnapshot(docSnapshot);
                    tasks.add(task);
                }
                onSuccessListener.onSuccess(tasks);
            }
        }).addOnFailureListener(onFailureListener);
    }

    //gets an arraylist of transactions given the user's id
    public void getTransactionsByChildId(final String childId,
                                  final OnSuccessListener<ArrayList<Transaction>> onSuccessListener,
                                  final OnFailureListener onFailureListener) {
        db.collection("transactions").whereEqualTo("childId", childId).orderBy("timeStamp", Query.Direction.DESCENDING).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                ArrayList<Transaction> transactions = new ArrayList<>();
                for(QueryDocumentSnapshot docSnapshot : queryDocumentSnapshots)
                {
                    Transaction task = Transaction.SNAPSHOTPARSER.parseSnapshot(docSnapshot);
                    transactions.add(task);
                }
                onSuccessListener.onSuccess(transactions);
            }
        }).addOnFailureListener(onFailureListener);
    }

    //gets a limited number of transactions in an arraylist given the user's id and the limit for how many tasks should be queried
    public void getTransactionsByChildId(final String childId,
                                         final int limit,
                                         final OnSuccessListener<ArrayList<Transaction>> onSuccessListener,
                                         final OnFailureListener onFailureListener) {
        db.collection("transactions").whereEqualTo("childId", childId).orderBy("timeStamp", Query.Direction.DESCENDING).limit(limit).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                ArrayList<Transaction> transactions = new ArrayList<>();
                for(QueryDocumentSnapshot docSnapshot : queryDocumentSnapshots)
                {
                    Transaction task = Transaction.SNAPSHOTPARSER.parseSnapshot(docSnapshot);
                    transactions.add(task);
                }
                onSuccessListener.onSuccess(transactions);
            }
        }).addOnFailureListener(onFailureListener);
    }

    //gets an arraylist of wish list items given the user's id
    public void getWishListItemsByChildId(final String childId,
                                         final OnSuccessListener<ArrayList<WishListItem>> onSuccessListener,
                                         final OnFailureListener onFailureListener) {
        db.collection("wishList").whereEqualTo("childId", childId).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                ArrayList<WishListItem> items = new ArrayList<>();
                for(QueryDocumentSnapshot docSnapshot : queryDocumentSnapshots)
                {
                    WishListItem item = WishListItem.SNAPSHOTPARSER.parseSnapshot(docSnapshot);
                    items.add(item);
                }
                onSuccessListener.onSuccess(items);
            }
        }).addOnFailureListener(onFailureListener);
    }

    //gets an arraylist of wish list items given the user's id limited to limit results
    public void getWishListItemsByChildId(final String childId,
                                          final int limit,
                                          final OnSuccessListener<ArrayList<WishListItem>> onSuccessListener,
                                          final OnFailureListener onFailureListener) {
        db.collection("wishList").whereEqualTo("childId", childId).limit(limit).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                ArrayList<WishListItem> items = new ArrayList<>();
                for(QueryDocumentSnapshot docSnapshot : queryDocumentSnapshots)
                {
                    WishListItem item = WishListItem.SNAPSHOTPARSER.parseSnapshot(docSnapshot);
                    items.add(item);
                }
                onSuccessListener.onSuccess(items);
            }
        }).addOnFailureListener(onFailureListener);
    }
}
