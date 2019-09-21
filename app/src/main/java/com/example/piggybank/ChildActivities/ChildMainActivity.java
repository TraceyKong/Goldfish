package com.example.piggybank.ChildActivities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.example.piggybank.Firebase.Get;
import com.example.piggybank.Firebase.Models.Task;
import com.example.piggybank.Firebase.Models.Transaction;
import com.example.piggybank.Firebase.Models.User;
import com.example.piggybank.Firebase.Models.WishListItem;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.piggybank.R;
import com.google.firebase.auth.FirebaseAuth;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ChildMainActivity extends AppCompatActivity {
    private String childId;
    private final int LIMIT = 3;

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
        setContentView(R.layout.activity_child_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Overview");

        this.childId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        recyclerViewTasks = findViewById(R.id.tasksRVChild);
        layoutManagerTasks = new LinearLayoutManager(this);
        recyclerViewTasks.setLayoutManager(layoutManagerTasks);

        recyclerViewTransactions = findViewById(R.id.transactionsRVChild);
        layoutManagerTransactions = new LinearLayoutManager(this);
        recyclerViewTransactions.setLayoutManager(layoutManagerTransactions);

        recyclerViewWishList = findViewById(R.id.wishlistRVChild);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_logout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_logout:
                logOut();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
        makeRVTasks();
        makeRVTransactions();
        makeRVWishList();
        childBalance();
    }

    public void childBalance() {
        Get get = new Get();
        get.getUserById(childId, new OnSuccessListener<User>() {
            @Override
            public void onSuccess(User user) {
                TextView balanceText = findViewById(R.id.textChildBalance);
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

    public void openTasksActivity(View view) {
        Intent intent = new Intent(ChildMainActivity.this, TasksChildOverview.class);
        String name = getIntent().getExtras().getString("name");
        intent.putExtra("name", name);
        intent.putExtra("childId", childId);
        startActivity(intent);
    }

    public void openTransactionsActivity(View view) {
        Intent intent = new Intent(ChildMainActivity.this, TransactionsChildActivity.class);
        String name = getIntent().getExtras().getString("name");
        intent.putExtra("name", name);
        intent.putExtra("childId", childId);
        startActivity(intent);
    }

    public void openWishListActivity(View view) {
        Intent intent = new Intent(ChildMainActivity.this, WishListChildActivity.class);
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
                TextView text = findViewById(R.id.noTasksChild);
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
                TextView text = findViewById(R.id.noTransactionsChild);
                //check if there are no transactions and display the no transactions textview if there are none
                if(transactions.size() == 0)
                    text.setVisibility(View.VISIBLE);
                else
                    text.setVisibility(View.GONE);
                adapterTransactions = new ChildMainActivity.TransactionsAdapter(transactions);
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
                TextView text = findViewById(R.id.noWishlistItemsChild);
                //check if there are no transactions and display the no transactions textview if there are none
                if(items.size() == 0)
                    text.setVisibility(View.VISIBLE);
                else
                    text.setVisibility(View.GONE);
                adapterWishList = new ChildMainActivity.WishListItemAdapter(items);
                recyclerViewWishList.setAdapter(adapterWishList);

            }
        }, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println(e);
            }
        });
    }

    private void logOut() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull com.google.android.gms.tasks.Task<Void> task) {
                        finish();
                        moveTaskToBack(true);
                    }
                });
    }

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
            final Task task = tasks.get(position);
            holder.task.setText(task.getName());
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
                    Intent intent = new Intent(ChildMainActivity.this, TaskDetailsChildActivity.class);
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
                    Intent intent = new Intent(ChildMainActivity.this, TransactionDetailsChildActivity.class);
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
                    Intent intent = new Intent(ChildMainActivity.this, WishListItemDetailsChildActivity.class);
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
