package com.example.piggybank.Firebase;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;
import java.util.HashMap;
public class Post {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final static String TAG = Post.class.getName();

    //creates user
    public void createUser(final String userId,
                           final String name,
                           final String userType,
                           final String parentId,
                           final OnSuccessListener<Void> successListener,
                           final OnFailureListener failureListener) {
        Map<String, Object> user = new HashMap<>();
        user.put("name", name);
        user.put("userType", userType);
        user.put("parentId", parentId);
        user.put("balance", 0.00);
        db.collection("users").document(userId).set(user).addOnSuccessListener(successListener).addOnFailureListener(failureListener);
    }
    //creates task
    //a child should only be able to mark a status as completed. only a parent can confirm
    public void createTask(final String name,
                           final String description,
                           final double payment,
                           final String childId,
                           final OnSuccessListener<String> successListener,
                           final OnFailureListener failureListener) {
        Map<String, Object> task = new HashMap<>();
        task.put("name", name);
        task.put("description", description);
        task.put("payment", payment);
        task.put("childId", childId);
        task.put("status", "incomplete");
        task.put("timeAssigned", FieldValue.serverTimestamp());
        db.collection("tasks").add(task).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                successListener.onSuccess(documentReference.getId());
            }
        }).addOnFailureListener(failureListener);
    }

    //changes a task's status field to confirmed given the task's id
    public void markTaskConfirmed(final String taskId,
                                  final OnSuccessListener<Void> onSuccessListener,
                                  final OnFailureListener onFailureListener) {
        db.collection("tasks").document(taskId).update("status", "confirmed").addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                onSuccessListener.onSuccess(aVoid);
            }
        }).addOnFailureListener(onFailureListener);
    }

    //changes a task's status field to completed given the task's id
    public void markTaskCompleted(final String taskId,
                                  final OnSuccessListener<Void> onSuccessListener,
                                  final OnFailureListener onFailureListener) {
        db.collection("tasks").document(taskId).update("status", "completed").addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                onSuccessListener.onSuccess(aVoid);
            }
        }).addOnFailureListener(onFailureListener);
    }

    //increments a user's balance by a specified amount given the user's id
    public void sendPaymentToChild(final String childId,
                                   final double payment,
                                   final OnSuccessListener<Void> onSuccessListener,
                                   final OnFailureListener onFailureListener) {
        db.collection("users").document(childId).update("balance", FieldValue.increment(payment)).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                onSuccessListener.onSuccess(aVoid);
            }
        }).addOnFailureListener(onFailureListener);
    }

    //creates a transaction
    //a child can only mark status as requested. on;y a parent can confirm
    //type should either be positive or negative. so far only positive transactions come from tasks
    public void createTransaction(final String item,
                                  final double cost,
                                  final String type,
                                  final String status,
                                  final String childId,
                                  final OnSuccessListener<String> onSuccessListener,
                                  final OnFailureListener onFailureListener) {
        Map<String, Object> transaction = new HashMap<>();
        transaction.put("item", item);
        transaction.put("cost", cost);
        transaction.put("type", type);
        transaction.put("status", status);
        transaction.put("childId", childId);
        transaction.put("timeStamp", FieldValue.serverTimestamp());
        db.collection("transactions").add(transaction).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                onSuccessListener.onSuccess(documentReference.getId());
            }
        }).addOnFailureListener(onFailureListener);
    }

    //decreases a user's balance by a specified amount given the user's id
    public void subtractBalanceFromChild(final String childId,
                                   final double payment,
                                   final OnSuccessListener<Void> onSuccessListener,
                                   final OnFailureListener onFailureListener) {
        db.collection("users").document(childId).update("balance", FieldValue.increment(payment*(-1))).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                onSuccessListener.onSuccess(aVoid);
            }
        }).addOnFailureListener(onFailureListener);
    }

    //changes a transaction's status field to confirmed given the task's id
    public void markTransactionConfirmed(final String transactionId,
                                  final OnSuccessListener<Void> onSuccessListener,
                                  final OnFailureListener onFailureListener) {
        db.collection("transactions").document(transactionId).update("status", "confirmed").addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                onSuccessListener.onSuccess(aVoid);
            }
        }).addOnFailureListener(onFailureListener);
    }

    //creates a wish list item
    public void createWishListItem(final String item,
                                   final double cost,
                                   final String childId,
                                   final OnSuccessListener<String> onSuccessListener,
                                   final OnFailureListener onFailureListener) {
        Map<String, Object> wish = new HashMap<>();
        wish.put("item", item);
        wish.put("cost", cost);
        wish.put("childId", childId);
        db.collection("wishList").add(wish).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                onSuccessListener.onSuccess(documentReference.getId());
            }
        }).addOnFailureListener(onFailureListener);
    }

    //deletes a task
    public void deleteTask(final String taskId,
                           final OnSuccessListener<Void> onSuccessListener,
                           final OnFailureListener onFailureListener) {
        db.collection("tasks").document(taskId).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                onSuccessListener.onSuccess(aVoid);
            }
        }).addOnFailureListener(onFailureListener);
    }
}

