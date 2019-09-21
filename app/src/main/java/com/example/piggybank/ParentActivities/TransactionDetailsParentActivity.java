package com.example.piggybank.ParentActivities;

import android.graphics.Color;
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

import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.piggybank.R;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TransactionDetailsParentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_details_parent);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //title toolbar
        String name = getIntent().getExtras().getString("name");
        getSupportActionBar().setTitle(name+"'s Transactions");

        //add return button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //set task details to textviews
        TextView item = findViewById(R.id.transactionItemNameDetails);
        TextView date = findViewById(R.id.transactionDateDetails);
        TextView cost = findViewById(R.id.transactionCostDetails);
        item.setText(getIntent().getExtras().getString("item"));
        date.setText(getIntent().getExtras().getString("date"));
        DecimalFormat df = new DecimalFormat("0.00");
        if(getIntent().getExtras().getString("type").equals("negative"))
        {
            cost.setText("- $"+df.format(getIntent().getExtras().getDouble("cost")));
            int color = Color.parseColor("#FF0000");
            cost.setTextColor(color);
        }
        else
        {
            cost.setText("+ $"+df.format(getIntent().getExtras().getDouble("cost")));
            int color = Color.parseColor("#008000");
            cost.setTextColor(color);
        }
        LinearLayout layout = findViewById(R.id.requestedTransactionLayout);
        if(getIntent().getExtras().getString("status").equals("requested"))
        {
            layout.setVisibility(View.VISIBLE);
        }
        else
        {
            layout.setVisibility(View.GONE);
        }
    }

    //returns to previous page
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //completes requested transaction
    public void completeTransaction(View view) {
        Get get = new Get();
        get.getUserById(getIntent().getExtras().getString("childId"), new OnSuccessListener<User>() {
            @Override
            public void onSuccess(User user) {
                if(user.getBalance() >= getIntent().getExtras().getDouble("cost"))
                {
                    final Post post = new Post();
                    post.markTransactionConfirmed(getIntent().getExtras().getString("id"), new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            post.subtractBalanceFromChild(getIntent().getExtras().getString("childId"), getIntent().getExtras().getDouble("cost"), new OnSuccessListener<Void>() {
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
}
