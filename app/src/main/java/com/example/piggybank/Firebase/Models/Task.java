package com.example.piggybank.Firebase.Models;

import androidx.annotation.NonNull;

import com.firebase.ui.firestore.SnapshotParser;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Date;

public class Task {
    private String name;
    private String description;
    private double payment;
    private String childId;
    private String status;
    private Date timeAssigned;
    private String id;

    //parses firebase responses into a task
    public final static SnapshotParser<Task> SNAPSHOTPARSER = new SnapshotParser<Task>() {
        @NonNull
        @Override
        public Task parseSnapshot(@NonNull DocumentSnapshot snapshot) {
            return new Task(snapshot.getString("name"),
                    snapshot.getString("description"),
                    snapshot.getDouble("payment"),
                    snapshot.getString("childId"),
                    snapshot.getString("status"),
                    snapshot.getDate("timeAssigned"),
                    snapshot.getId());
        }
    };

    public Task(String name, String description, double payment, String childId, String status, Date timeAssigned, String id) {
        this.name = name;
        this.description = description;
        this.payment = payment;
        this.childId = childId;
        this.status = status;
        this.timeAssigned = timeAssigned;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getPayment() {
        return payment;
    }

    public String getChildId() {
        return childId;
    }

    public String getStatus() {
        return status;
    }

    public Date getTimeAssigned() {
        return timeAssigned;
    }

    public String getId() {
        return id;
    }
}
