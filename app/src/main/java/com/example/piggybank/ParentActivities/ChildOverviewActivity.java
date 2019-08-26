package com.example.piggybank.ParentActivities;

import android.content.Intent;
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

    private String childId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_overview);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView balanceText = findViewById(R.id.balanceParentView);
        DecimalFormat df = new DecimalFormat("0.00");
        String balance = df.format(getIntent().getExtras().getDouble("balance"));
        balanceText.setText("$"+balance);
        String name = getIntent().getExtras().getString("name");
        getSupportActionBar().setTitle(name+"'s Overview");

        this.childId = getIntent().getExtras().getString("childId");
    }

    public void openTasksActivity(View view) {
        Intent intent = new Intent(ChildOverviewActivity.this, tasksParentActivity.class);
        String name = getIntent().getExtras().getString("name");
        intent.putExtra("name", name);
        intent.putExtra("childId", childId);
        startActivity(intent);
        finish();
    }

    public void openTransactionsActivity(View view) {
        Intent intent = new Intent(ChildOverviewActivity.this, tasksParentActivity.class);
        String name = getIntent().getExtras().getString("name");
        intent.putExtra("name", name);
        intent.putExtra("childId", childId);
        startActivity(intent);
        finish();
    }

}
