package com.example.piggybank.ParentActivities;

import android.content.Intent;
import android.os.Bundle;

import com.example.piggybank.Firebase.Get;
import com.example.piggybank.Firebase.Models.Task;
import com.example.piggybank.Firebase.Models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.piggybank.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ChildOverviewParentActivity extends AppCompatActivity {

    private String childId;
    private final int LIMIT = 4;

    private RecyclerView recyclerViewTasks;
    private RecyclerView.Adapter adapterTasks;
    private RecyclerView.LayoutManager layoutManagerTasks;

    private RecyclerView recyclerViewTransactions;
    private RecyclerView.Adapter adapterTransactions;
    private RecyclerView.LayoutManager layoutManagerTransactions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_overview);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String name = getIntent().getExtras().getString("name");
        getSupportActionBar().setTitle(name+"'s Overview");

        this.childId = getIntent().getExtras().getString("childId");


        recyclerViewTasks = findViewById(R.id.tasksOverviewRV);
        layoutManagerTasks = new LinearLayoutManager(this);
        recyclerViewTasks.setLayoutManager(layoutManagerTasks);

        recyclerViewTransactions = findViewById(R.id.transactionsOverviewRV);
        layoutManagerTransactions = new LinearLayoutManager(this);
        recyclerViewTransactions.setLayoutManager(layoutManagerTransactions);

        makeRVTasks();
        makeRVTransactions();
        childBalance();
    }

    @Override
    public void onResume()
    {  // After a pause OR at startup
        super.onResume();
        makeRVTasks();
        makeRVTransactions();
        childBalance();
    }

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

    public void openTasksActivity(View view) {
        Intent intent = new Intent(ChildOverviewParentActivity.this, TasksParentActivity.class);
        String name = getIntent().getExtras().getString("name");
        intent.putExtra("name", name);
        intent.putExtra("childId", childId);
        startActivity(intent);
    }

    public void openTransactionsActivity(View view) {
        Intent intent = new Intent(ChildOverviewParentActivity.this, TasksParentActivity.class);//todo transactions
        String name = getIntent().getExtras().getString("name");
        intent.putExtra("name", name);
        intent.putExtra("childId", childId);
        startActivity(intent);
    }

    public void makeRVTasks() {
        Get get = new Get();
        get.getTasksByChildId(childId, LIMIT, new OnSuccessListener<ArrayList<Task>>() {
            @Override
            public void onSuccess(ArrayList<Task> tasks) {
                adapterTasks = new TasksAdapter(tasks);
                recyclerViewTasks.setAdapter(adapterTasks);
            }
        }, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println(e);
            }
        });
    }

    public void makeRVTransactions() {

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

}
