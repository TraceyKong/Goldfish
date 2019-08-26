package com.example.piggybank.ParentActivities;

import android.content.Intent;
import android.os.Bundle;

import com.example.piggybank.Firebase.Get;
import com.example.piggybank.Firebase.Models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.piggybank.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class ParentMainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.childrenRV);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        makeRV();
    }

    public void openCreateChildAccountActivity(View view) {
        Intent intent = new Intent(ParentMainActivity.this, CreateChildAccountActivity.class);
        startActivity(intent);
    }


    @Override
    public void onResume()
    {  // After a pause OR at startup
        super.onResume();
        makeRV();
    }


    public void makeRV() {
        Get get = new Get();
        String parentId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        get.getChildrenByParentId(parentId, new OnSuccessListener<ArrayList<User>>() {
            @Override
            public void onSuccess(ArrayList<User> users) {
                adapter = new ChildrenAdapter(users);
                recyclerView.setAdapter(adapter);
            }
        }, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println(e);
            }
        });
    }

    public class ChildrenAdapter extends RecyclerView.Adapter<ChildrenAdapter.ChildHolder> {

        private ArrayList<User> children;
        public ChildrenAdapter(ArrayList<User> children)
        {
            this.children = children;
        }

        @NonNull
        @Override
        public ChildHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.holder_child, parent, false);
            return new ChildHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ChildHolder holder, int position) {
            final String name = children.get(position).getName();
            final double balance = children.get(position).getBalance();
            final String childId = children.get(position).getUserId();
            holder.child.setText(name);
            holder.child.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ParentMainActivity.this, ChildOverviewActivity.class);
                    intent.putExtra("name", name);
                    intent.putExtra("balance", balance);
                    intent.putExtra("childId", childId);
                    startActivity(intent);
                    finish();
                }
            });
        }

        @Override
        public int getItemCount() {
            return children.size();
        }

        public class ChildHolder extends RecyclerView.ViewHolder {

            private Button child;
            public ChildHolder(@NonNull View itemView) {
                super(itemView);
                this.child = itemView.findViewById(R.id.childNameButton);
            }
        }
    }
}
