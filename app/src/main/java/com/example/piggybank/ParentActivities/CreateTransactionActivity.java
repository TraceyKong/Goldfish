package com.example.piggybank.ParentActivities;

import android.os.Bundle;

import com.example.piggybank.Firebase.Get;
import com.example.piggybank.Firebase.Models.User;
import com.example.piggybank.Firebase.Post;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.piggybank.R;

public class CreateTransactionActivity extends AppCompatActivity {

    String childId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_transaction);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        String name = getIntent().getExtras().getString("name");
        getSupportActionBar().setTitle("Create a transaction for "+name);
        this.childId = getIntent().getExtras().getString("childId");
    }

    //create a new negative transaction
    public void createTransaction(View view) {
        //get user inputs
        EditText itemText = findViewById(R.id.itemName);
        EditText costText = findViewById(R.id.itemCost);
        final String item = itemText.getText().toString().trim();
        String cost = costText.getText().toString().trim();
        //validate
        boolean valid = true;
        if(item.equals(""))
            valid = false;
        if(cost.equals(""))
            valid = false;
        if(valid)
        {
            final double costDouble = Double.parseDouble(cost);

            Get get = new Get();
            get.getUserById(childId, new OnSuccessListener<User>() {
                @Override
                public void onSuccess(User user) {
                    if(user.getBalance() >= costDouble)
                    {
                        final Post post = new Post();
                        post.createTransaction(item, costDouble, "negative", "confirmed", childId, new OnSuccessListener<String>() {
                            @Override
                            public void onSuccess(String s) {
                                post.subtractBalanceFromChild(childId, costDouble, new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        finish(); //go back to previous activity
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
                        Toast toast = Toast.makeText(getApplicationContext(), "Insufficient Balance", Toast.LENGTH_SHORT);
                        toast.show();
                    }
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
            Toast toast = Toast.makeText(getApplicationContext(), "Improper entries in fields.", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

}
