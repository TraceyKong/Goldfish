package com.example.piggybank.ParentActivities;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.TextView;

import com.example.piggybank.R;

import java.text.DecimalFormat;

public class ChildOverviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_overview);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView balanceText = findViewById(R.id.balanceParentView);
        DecimalFormat df = new DecimalFormat("#.##");
        String balance = df.format(getIntent().getExtras().getDouble("balance"));
        balanceText.setText("$"+balance);
        String name = getIntent().getExtras().getString("name");
        getSupportActionBar().setTitle(name);
    }

}
