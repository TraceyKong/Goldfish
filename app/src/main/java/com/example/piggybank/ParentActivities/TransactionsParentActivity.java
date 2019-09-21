package com.example.piggybank.ParentActivities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.example.piggybank.Firebase.Get;
import com.example.piggybank.Firebase.Models.Transaction;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.piggybank.R;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class TransactionsParentActivity extends AppCompatActivity {
    String childId;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions_parent);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //title toolbar
        String name = getIntent().getExtras().getString("name");
        getSupportActionBar().setTitle(name+"'s Transactions");
        this.childId = getIntent().getExtras().getString("childId");

        //add return button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //setup recyclerview
        recyclerView = findViewById(R.id.transactionsRV);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        makeRV();
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

    //opens the CreateTransactionActivity
    public void openCreateTaskActivity(View view) {
        Intent intent = new Intent(TransactionsParentActivity.this, CreateTransactionActivity.class);
        intent.putExtra("childId", childId);
        intent.putExtra("name", getIntent().getExtras().getString("name"));
        startActivity(intent);
    }

    public void makeRV() {
        Get get = new Get();
        get.getTransactionsByChildId(childId, new OnSuccessListener<ArrayList<Transaction>>() {
            @Override
            public void onSuccess(ArrayList<Transaction> transactions) {
                TextView text = findViewById(R.id.noTransactions2);
                //check if there are no transactions and display the no transactions textview if there are none
                if(transactions.size() == 0)
                    text.setVisibility(View.VISIBLE);
                else
                    text.setVisibility(View.GONE);
                adapter = new TransactionsAdapter(transactions);
                recyclerView.setAdapter(adapter);

            }
        }, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println(e);
            }
        });
    }

    @Override
    public void onResume()
    {  // After a pause OR at startup
        super.onResume();
        makeRV();
    }

    private class TransactionsAdapter extends RecyclerView.Adapter<TransactionsAdapter.TransactionHolder> {

        private ArrayList<Transaction> transactions;
        public TransactionsAdapter(ArrayList<Transaction> transactions) {
            this.transactions = transactions;
        }
        @NonNull
        @Override
        public TransactionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.holder_transaction, parent, false);
            return new TransactionHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull TransactionHolder holder, int position) {
            //get a transaction
            final Transaction transaction = transactions.get(position);
            //set the name of the transaction to the textview
            holder.item.setText(transaction.getItem());
            //get cost of the transactions
            double cost = transaction.getCost();
            DecimalFormat df = new DecimalFormat("0.00");
            String costString = df.format(cost);
            //determine type  of transaction
            String type = transaction.getType();
            if(type.equals("negative")) {
                //set cost textview to appropriate value
                holder.cost.setText("- $"+costString+" ");
                int color = Color.parseColor("#FF0000");
                holder.cost.setTextColor(color);
                //determine if requested and label if it is
                String status = transaction.getStatus();
                if(status.equals("requested"))
                    holder.cost.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0, R.drawable.ic_help_outline_black_18dp,0);
            }
            else {
                holder.cost.setText("+ $"+costString+" ");
                int color = Color.parseColor("#008000");
                holder.cost.setTextColor(color);
            }
            holder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(TransactionsParentActivity.this, TransactionDetailsParentActivity.class);
                    intent.putExtra("item", transaction.getItem());
                    intent.putExtra("status", transaction.getStatus());
                    intent.putExtra("type", transaction.getType());
                    intent.putExtra("cost", transaction.getCost());
                    intent.putExtra("childId", transaction.getChildId());
                    DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
                    intent.putExtra("date", df.format(transaction.getTimeStamp()));
                    intent.putExtra("id", transaction.getId());
                    intent.putExtra("name", getIntent().getExtras().getString("name"));
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return transactions.size();
        }

        private class TransactionHolder extends RecyclerView.ViewHolder {

            private TextView item;
            private TextView cost;
            private LinearLayout layout;

            public TransactionHolder(@NonNull View itemView) {
                super(itemView);
                this.item = itemView.findViewById(R.id.transactionItemName);
                this.cost = itemView.findViewById(R.id.transactionCostDetails);
                this.layout = itemView.findViewById(R.id.transactionHolderLayout);
            }
        }
    }
}
