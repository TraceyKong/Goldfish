package com.example.piggybank.Firebase.Models;

import androidx.annotation.NonNull;

import com.firebase.ui.firestore.SnapshotParser;
import com.google.firebase.firestore.DocumentSnapshot;


public class WishListItem {

    private String item;
    private double cost;
    private String childId;
    private String itemId;

    public final static SnapshotParser<WishListItem> SNAPSHOTPARSER = new SnapshotParser<WishListItem>() {
        @NonNull
        @Override
        public WishListItem parseSnapshot(@NonNull DocumentSnapshot snapshot) {
            return new WishListItem(snapshot.getString("item"),
                    snapshot.getDouble("cost"),
                    snapshot.getString("childId"),
                    snapshot.getId());
        }
    };

    public WishListItem(String item, double cost, String childId, String itemId) {
        this.item = item;
        this.cost = cost;
        this.childId = childId;
        this.itemId = itemId;
    }

    public String getItem() {
        return item;
    }

    public double getCost() {
        return cost;
    }

    public String getChildId() {
        return childId;
    }

    public String getItemId() {
        return itemId;
    }
}
