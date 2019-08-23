package com.example.piggybank.Firebase.Models;

import androidx.annotation.NonNull;

import com.firebase.ui.firestore.SnapshotParser;
import com.google.firebase.firestore.DocumentSnapshot;

public class User {
    private String name;
    private String userType;
    private String userId;
    public final static SnapshotParser<User> SNAPSHOTPARSER = new SnapshotParser<User>() {
        @NonNull
        @Override
        public User parseSnapshot(@NonNull DocumentSnapshot snapshot) {
            return new User(snapshot.getString("name"),
                    snapshot.getString("userType"),
                    snapshot.getId());
        }
    };

    public User(String name, String userType, String userId){
        this.name = name;
        this.userType = userType;
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public String getUserType() {
        return userType;
    }

    public String getUserId() {
        return userId;
    }
}