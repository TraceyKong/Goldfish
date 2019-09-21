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

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.piggybank.R;

import java.text.DecimalFormat;

public class TaskDetailsParentActivity extends AppCompatActivity {

    private String id;
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

        this.id = getIntent().getExtras().getString("id");
    }

    //if the child has completed the tas this marks the task as confirmed, then adds the payment to the child's balance account, and creates a positive transaction
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
                            Post post = new Post();
                            post.createTransaction("Task: "+getIntent().getExtras().getString("taskName"), getIntent().getExtras().getDouble("payment"), "positive", "confirmed", getIntent().getExtras().getString("childId"), new OnSuccessListener<String>() {
                                @Override
                                public void onSuccess(String s) {
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_delete_task:
                deleteTask();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void deleteTask() {
        Post post = new Post();
        post.deleteTask(id, new OnSuccessListener<Void>() {
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

}
