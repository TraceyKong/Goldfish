package com.example.piggybank.ParentActivities;

import android.os.Bundle;

import com.example.piggybank.Firebase.Post;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.piggybank.R;

import java.text.DecimalFormat;

public class TaskDetailsParentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details_parent);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //title toolbar
        String name = getIntent().getExtras().getString("name");
        getSupportActionBar().setTitle(name+"'s Tasks");
        //set task details to textviews
        TextView taskName = findViewById(R.id.taskDetailedName);
        TextView taskPayment = findViewById(R.id.taskDetailedPayment);
        TextView taskStatus = findViewById(R.id.taskDetailedStatus);
        TextView taskDescription = findViewById(R.id.taskDetailedDescription);
        //formats money to have 2 decimal places
        DecimalFormat df = new DecimalFormat("0.00");
        taskName.setText(getIntent().getExtras().getString("taskName"));
        taskPayment.setText("$"+df.format(getIntent().getExtras().getDouble("payment")));
        taskStatus.setText(getIntent().getExtras().getString("status"));
        taskDescription.setText(getIntent().getExtras().getString("description"));
    }

    //if the child has completed the tas this marks the task as confirmed and the adds the payment to the child's balance account
    public void markConfirmed(View view)
    {
        //check if task is completed
        if(getIntent().getExtras().getString("status").equals("completed"))
        {
            //mark confirmed
            final Post post = new Post();
            post.markTaskConfirmed(getIntent().getExtras().getString("id"), new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    //send payment
                    post.sendPaymentToChild(getIntent().getExtras().getString("childId"), getIntent().getExtras().getDouble("payment"), new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            finish();//go back to previous activity
                        }
                    }, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            System.out.println(e);
                        }
                    });
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
            Toast toast = Toast.makeText(getApplicationContext(), "Task not yet completed or already confirmed.", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

}
