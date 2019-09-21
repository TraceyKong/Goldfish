package com.example.piggybank.ChildActivities;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.TextView;

import com.example.piggybank.R;

public class WishListItemDetailsChildActivity extends AppCompatActivity {

    private String childId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wish_list_item_details_child);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //title toolbar
        getSupportActionBar().setTitle("Wish List");
        this.childId = getIntent().getExtras().getString("childId");

        TextView item = findViewById(R.id.wishListItemNameDetailsChild);
        TextView cost = findViewById(R.id.wishListItemCostDetailsChild);

        item.setText(getIntent().getExtras().getString("item"));
        cost.setText("$"+getIntent().getExtras().getDouble("cost"));
    }

}
