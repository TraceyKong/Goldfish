package com.example.piggybank.ParentActivities;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.TextView;

import com.example.piggybank.R;

public class WishListItemDetailsParentActivity extends AppCompatActivity {

    private String childId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wish_list_item_details_parent);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //title toolbar
        String name = getIntent().getExtras().getString("name");
        getSupportActionBar().setTitle(name+"'s Wish List");
        this.childId = getIntent().getExtras().getString("childId");

        TextView item = findViewById(R.id.wishListItemNameDetails);
        TextView cost = findViewById(R.id.wishListItemCostDetails);

        item.setText(getIntent().getExtras().getString("item"));
        cost.setText("$"+getIntent().getExtras().getDouble("cost"));

    }

}
