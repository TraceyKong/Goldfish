package com.example.piggybank.Firebase.Models;

import androidx.annotation.NonNull;

import com.firebase.ui.firestore.SnapshotParser;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Date;

public class Transaction {
    private String item;
    private double cost;
    private String type;
    private String status;
    private String childId;
    private Date timeStamp;
    private String id;

    public final static SnapshotParser<Transaction> SNAPSHOTPARSER = new SnapshotParser<Transaction>() {
        @NonNull
        @Override
        public Transaction parseSnapshot(@NonNull DocumentSnapshot snapshot) {
            return new Transaction(snapshot.getString("item"),
                    snapshot.getDouble("cost"),
                    snapshot.getString("type"),
                    snapshot.getString("status"),
                    snapshot.getString("childId"),
                    snapshot.getDate("timeStamp"),
                    snapshot.getId());
        }
    };

    public Transaction(String item, double cost, String type, String status, String childId, Date timeStamp, String id) {
        this.item = item;
        this.cost = cost;
        this.type = type;
        this.status = status;
        this.childId = childId;
        this.timeStamp = timeStamp;
        this.id = id;
    }

    public String getItem() {
        return item;
    }

    public double getCost() {
        return cost;
    }

    public String getType() {
        return type;
    }

    public String getStatus() {
        return status;
    }

    public String getChildId() {
        return childId;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public String getId() {
        return id;
    }
}
