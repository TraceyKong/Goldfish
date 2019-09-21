package com.example.piggybank.ChildActivities;

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

public class RequestTransactionActivity extends AppCompatActivity {

    private String childId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_transaction);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Request Transaction");
        this.childId = getIntent().getExtras().getString("childId");
    }

    //create a new negative transaction
    public void createTransaction(View view) {
        //get user inputs
        EditText itemText = findViewById(R.id.itemNameChild);
        EditText costText = findViewById(R.id.itemCostChild);
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
                        post.createTransaction(item, costDouble, "negative", "requested", childId, new OnSuccessListener<String>() {
                            @Override
                            public void onSuccess(String s) {
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
