package com.example.piggybank.ParentActivities;

import android.content.Intent;
import android.os.Bundle;

import com.example.piggybank.Firebase.Get;
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
import android.widget.Button;

import com.example.piggybank.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class ParentMainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;      //to display a scrolling list of elements
    private RecyclerView.Adapter adapter;   //bridge between UI and data source
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //title toolbar
        getSupportActionBar().setTitle("Children");
        //setup recyclerview
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

    //make recyclerview of children
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
    //recyclerview adapter
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
            //get child info
            final String name = children.get(position).getName();
            final double balance = children.get(position).getBalance();
            final String childId = children.get(position).getUserId();
            //set child's name as button text
            holder.child.setText(name);
            //add onclick to button that will open chld overview in another activity
            holder.child.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ParentMainActivity.this, ChildOverviewParentActivity.class);
                    intent.putExtra("name", name);
                    intent.putExtra("balance", balance);
                    intent.putExtra("childId", childId);
                    startActivity(intent);
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
