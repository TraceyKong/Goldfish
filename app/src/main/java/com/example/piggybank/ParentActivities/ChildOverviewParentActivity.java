package com.example.piggybank.ParentActivities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.example.piggybank.Firebase.Get;
import com.example.piggybank.Firebase.Models.Task;
import com.example.piggybank.Firebase.Models.Transaction;
import com.example.piggybank.Firebase.Models.User;
import com.example.piggybank.Firebase.Models.WishListItem;
import com.example.piggybank.Firebase.Post;
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

public class ChildOverviewParentActivity extends AppCompatActivity {

    private String childId;
    private final int LIMIT = 3;//the maximum limit of items in a recycler view

    private RecyclerView recyclerViewTasks;
    private RecyclerView.Adapter adapterTasks;
    private RecyclerView.LayoutManager layoutManagerTasks;

    private RecyclerView recyclerViewTransactions;
    private RecyclerView.Adapter adapterTransactions;
    private RecyclerView.LayoutManager layoutManagerTransactions;

    private RecyclerView recyclerViewWishList;
    private RecyclerView.Adapter adapterWishList;
    private RecyclerView.LayoutManager layoutManagerWishList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_overview);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //title the toolbar
        String name = getIntent().getExtras().getString("name");
        getSupportActionBar().setTitle(name+"'s Overview");

        //record the child's id
        this.childId = getIntent().getExtras().getString("childId");

        //add return button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //recyclerview setup
        recyclerViewTasks = findViewById(R.id.tasksOverviewRV);
        layoutManagerTasks = new LinearLayoutManager(this);
        recyclerViewTasks.setLayoutManager(layoutManagerTasks);

        recyclerViewTransactions = findViewById(R.id.transactionsOverviewRV);
        layoutManagerTransactions = new LinearLayoutManager(this);
        recyclerViewTransactions.setLayoutManager(layoutManagerTransactions);

        recyclerViewWishList = findViewById(R.id.wishListOverviewRV);
        layoutManagerWishList = new LinearLayoutManager(this);
        recyclerViewWishList.setLayoutManager(layoutManagerWishList);

        //make the recycler views
        makeRVTasks();
        makeRVTransactions();
        makeRVWishList();

        //get the current child's balance
        childBalance();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume()
    {  // After a pause OR at startup
        super.onResume();
        makeRVTasks();
        makeRVTransactions();
        makeRVWishList();
        childBalance();
    }
    //gets the child's user object and updates the textview with the balance.
    public void childBalance() {
        Get get = new Get();
        get.getUserById(childId, new OnSuccessListener<User>() {
            @Override
            public void onSuccess(User user) {
                TextView balanceText = findViewById(R.id.balanceParentView);
                DecimalFormat df = new DecimalFormat("0.00");
                balanceText.setText("$"+df.format(user.getBalance()));
            }
        }, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println(e);
            }
        });
    }

    //opens the TasksParentActivity
    public void openTasksActivity(View view) {
        Intent intent = new Intent(ChildOverviewParentActivity.this, TasksParentActivity.class);
        String name = getIntent().getExtras().getString("name");
        intent.putExtra("name", name);
        intent.putExtra("childId", childId);
        startActivity(intent);
    }

    //opens the TransactionsParentActivity
    public void openTransactionsActivity(View view) {
        Intent intent = new Intent(ChildOverviewParentActivity.this, TransactionsParentActivity.class);
        String name = getIntent().getExtras().getString("name");
        intent.putExtra("name", name);
        intent.putExtra("childId", childId);
        startActivity(intent);
    }

    //opens the WishListParentActivity
    public void openWishListActivity(View view) {
        Intent intent = new Intent(ChildOverviewParentActivity.this, WishListParentActivity.class);
        String name = getIntent().getExtras().getString("name");
        intent.putExtra("name", name);
        intent.putExtra("childId", childId);
        startActivity(intent);
    }

    //creates the recycler view for tasks with a maximum limit of the LIMIT latest tasks
    public void makeRVTasks() {
        Get get = new Get();
        get.getTasksByChildId(childId, LIMIT, new OnSuccessListener<ArrayList<Task>>() {
            @Override
            public void onSuccess(ArrayList<Task> tasks) {
                TextView text = findViewById(R.id.noTasks);
                if(tasks.size() == 0)
                    text.setVisibility(View.VISIBLE);
                else
                    text.setVisibility(View.GONE);
                adapterTasks = new TasksAdapter(tasks);
                recyclerViewTasks.setAdapter(adapterTasks);
            }
        }, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println("Failed to get tasks "+e);
            }
        });
    }

    public void makeRVTransactions() {
        Get get = new Get();
        get.getTransactionsByChildId(childId, LIMIT, new OnSuccessListener<ArrayList<Transaction>>() {
            @Override
            public void onSuccess(ArrayList<Transaction> transactions) {
                TextView text = findViewById(R.id.noTransactions);
                //check if there are no transactions and display the no transactions textview if there are none
                if(transactions.size() == 0)
                    text.setVisibility(View.VISIBLE);
                else
                    text.setVisibility(View.GONE);
                adapterTransactions = new TransactionsAdapter(transactions);
                recyclerViewTransactions.setAdapter(adapterTransactions);

            }
        }, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println(e);
            }
        });
    }

    public void makeRVWishList() {
        Get get = new Get();
        get.getWishListItemsByChildId(childId, LIMIT, new OnSuccessListener<ArrayList<WishListItem>>() {
            @Override
            public void onSuccess(ArrayList<WishListItem> items) {
                TextView text = findViewById(R.id.noWishListItems);
                //check if there are no transactions and display the no transactions textview if there are none
                if(items.size() == 0)
                    text.setVisibility(View.VISIBLE);
                else
                    text.setVisibility(View.GONE);
                adapterWishList = new WishListItemAdapter(items);
                recyclerViewWishList.setAdapter(adapterWishList);

            }
        }, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println(e);
            }
        });
    }

    //the recyclerview adapter for tasks
    private class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.TaskHolder> {

        private ArrayList<Task> tasks;
        public TasksAdapter(ArrayList<Task> tasks) {
            this.tasks = tasks;
        }
        @NonNull
        @Override
        public TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.holder_task, parent, false);
            return new TaskHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull TaskHolder holder, int position) {
            //get a task
            final Task task = tasks.get(position);
            //set the name of the task to the textview
            holder.task.setText(task.getName());
            //add status icon to the right of the textview based on the staus of the task
            if(task.getStatus().equals("incomplete"))
                holder.task.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0, R.drawable.ic_close,0);
            if(task.getStatus().equals("completed"))
                holder.task.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0, R.drawable.ic_remove,0);
            if(task.getStatus().equals("confirmed"))
                holder.task.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0, R.drawable.ic_done,0);
            //adds onclick to the textview that will open this task's details in a new activity
            holder.task.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ChildOverviewParentActivity.this, TaskDetailsParentActivity.class);
                    intent.putExtra("name", getIntent().getExtras().getString("name"));
                    intent.putExtra("payment", task.getPayment());
                    intent.putExtra("status", task.getStatus());
                    intent.putExtra("taskName", task.getName());
                    intent.putExtra("description", task.getDescription());
                    intent.putExtra("id", task.getId());
                    intent.putExtra("childId", childId);
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return tasks.size();
        }

        private class TaskHolder extends RecyclerView.ViewHolder {
            private TextView task;

            public TaskHolder(@NonNull View itemView) {
                super(itemView);
                this.task = itemView.findViewById(R.id.taskName);
            }
        }
    }
    //the recyclerview adapter class for transactions
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
                    Intent intent = new Intent(ChildOverviewParentActivity.this, TransactionDetailsParentActivity.class);
                    intent.putExtra("name", getIntent().getExtras().getString("name"));
                    intent.putExtra("item", transaction.getItem());
                    intent.putExtra("status", transaction.getStatus());
                    intent.putExtra("type", transaction.getType());
                    intent.putExtra("cost", transaction.getCost());
                    intent.putExtra("childId", transaction.getChildId());
                    DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
                    intent.putExtra("date", df.format(transaction.getTimeStamp()));
                    intent.putExtra("id", transaction.getId());
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

    private class WishListItemAdapter extends RecyclerView.Adapter<WishListItemAdapter.ItemHolder> {

        private ArrayList<WishListItem> items;
        public WishListItemAdapter(ArrayList<WishListItem> items) {
            this.items = items;
        }
        @NonNull
        @Override
        public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.holder_wish_list_item, parent, false);
            return new ItemHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
            final WishListItem item = items.get(position);
            holder.item.setText(item.getItem());
            holder.cost.setText("$"+item.getCost());
            holder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ChildOverviewParentActivity.this, WishListItemDetailsParentActivity.class);
                    intent.putExtra("item", item.getItem());
                    intent.putExtra("cost", item.getCost());
                    intent.putExtra("id", item.getItemId());
                    intent.putExtra("childId", childId);
                    intent.putExtra("name", getIntent().getExtras().getString("name"));
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        private class ItemHolder extends RecyclerView.ViewHolder {

            private TextView item;
            private TextView cost;
            private LinearLayout layout;

            public ItemHolder(@NonNull View itemView) {
                super(itemView);
                this.item = itemView.findViewById(R.id.wishListItem);
                this.cost = itemView.findViewById(R.id.wishListItemCost);
                this.layout = itemView.findViewById(R.id.wishListHolderLayout);
            }
        }
    }
}
