package com.example.piggybank.ChildActivities;

import android.graphics.Color;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.piggybank.R;

import java.text.DecimalFormat;

public class TransactionDetailsChildActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_details_child);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //title toolbar
        getSupportActionBar().setTitle("Transactions");
        //set task details to textviews
        TextView item = findViewById(R.id.transactionItemNameDetailsChild);
        TextView date = findViewById(R.id.transactionDateDetailsChild);
        TextView cost = findViewById(R.id.transactionCostDetailsChild);
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
        LinearLayout layout = findViewById(R.id.requestedTransactionLayoutChild);
        if(getIntent().getExtras().getString("status").equals("requested"))
        {
            layout.setVisibility(View.VISIBLE);
        }
        else
        {
            layout.setVisibility(View.GONE);
        }
    }

}
