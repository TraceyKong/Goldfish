package com.example.piggybank.ChildActivities;

import android.content.Intent;
import android.os.Bundle;

import com.example.piggybank.Firebase.Get;
import com.example.piggybank.Firebase.Models.Task;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.piggybank.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class ChildMainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);   //setup title toolbar
        getSupportActionBar().setTitle("Tasks");
        recyclerView = findViewById(R.id.tasksRV);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        makeRV();
    }

    @Override
    public void onResume() {
        super.onResume();
        makeRV();
    }

    public void makeRV() {
        Get get = new Get();
        String childId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        get.getTasksByChildId(childId, 3, new OnSuccessListener<ArrayList<Task>>() {
            @Override
            public void onSuccess(ArrayList<Task> tasks) {
                adapter = new TaskAdapter(tasks);
                recyclerView.setAdapter(adapter);
            }
        }, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println(e);
            }
        });
    }

    public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskHolder> {
        private ArrayList<Task> tasks;

        public TaskAdapter(ArrayList<Task> tasks) {
            this.tasks = tasks;
        }

        @NonNull
        @Override
        public TaskHolder onCreateViewHolder(@NonNull ViewGroup child, int viewType) {
            View view = LayoutInflater.from(child.getContext()).inflate(R.layout.holder_task, child, false);
            return new TaskHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull TaskHolder holder, int position) {
            //get task info
            final String name = tasks.get(position).getName();
            final String description = tasks.get(position).getDescription();
            final double reward = tasks.get(position).getPayment();
            final String taskId = tasks.get(position).getId();
            final String status = tasks.get(position).getStatus();

            //set task's name as button text
            holder.task.setText(name);
            //add onclick
            holder.task.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ChildMainActivity.this, TaskOverviewChildActivity.class);
                    intent.putExtra("name", name);
                    intent.putExtra("reward", reward);
                    intent.putExtra("taskId", taskId);
                    intent.putExtra("description", description);
                    intent.putExtra("status", status);
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() { return tasks.size(); }

        public class TaskHolder extends RecyclerView.ViewHolder {
            private Button task;

            public TaskHolder(@NonNull View itemView) {
                super(itemView);
                this.task = itemView.findViewById(R.id.confirmTaskButton);
            }
        }
    }
}
