package com.example.piggybank.ChildActivities;

import android.os.Bundle;

import com.example.piggybank.Firebase.Post;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.piggybank.R;

import java.text.DecimalFormat;

public class TaskDetailsChildActivity extends AppCompatActivity {
    private String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details_child);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //add return button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //title toolbar
        String name = getIntent().getExtras().getString("name");
        getSupportActionBar().setTitle("My Tasks");
        //set task details to textviews
        TextView taskName = findViewById(R.id.taskDetailedNameChild);
        TextView taskPayment = findViewById(R.id.taskDetailedPaymentChild);
        TextView taskStatus = findViewById(R.id.taskDetailedStatusChild);
        TextView taskDescription = findViewById(R.id.taskDetailedDescriptionChild);
        //formats money to have 2 decimal places
        DecimalFormat df = new DecimalFormat("0.00");
        taskName.setText(getIntent().getExtras().getString("taskName"));
        taskPayment.setText("$"+df.format(getIntent().getExtras().getDouble("payment")));
        taskStatus.setText(getIntent().getExtras().getString("status"));
        taskDescription.setText(getIntent().getExtras().getString("description"));

        this.id = getIntent().getExtras().getString("id");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void markCompleted(View view) {
        if(getIntent().getExtras().getString("status").equals("incomplete"))
        {
            //mark completed
            final Post post = new Post();
            post.markTaskCompleted(getIntent().getExtras().getString("id"), new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    finish();
                }
            }, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    System.out.println(e);
                }
            });
        }
        else
        {
            Toast toast = Toast.makeText(getApplicationContext(), "Task is already marked as completed.", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}
