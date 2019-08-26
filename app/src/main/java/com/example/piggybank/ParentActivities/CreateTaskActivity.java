package com.example.piggybank.ParentActivities;

import android.os.Bundle;

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

public class CreateTaskActivity extends AppCompatActivity {

    private String childId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String name = getIntent().getExtras().getString("name");
        getSupportActionBar().setTitle("Create a task for "+name);
        this.childId = getIntent().getExtras().getString("childId");
    }

    public void createTask(View view) {
        EditText nameInput = findViewById(R.id.nameInputTask);
        EditText descriptionInput = findViewById(R.id.descriptionInput);
        EditText paymentInput = findViewById(R.id.paymentInput);
        String name = nameInput.getText().toString().trim();
        String description = descriptionInput.getText().toString().trim();
        String payment = paymentInput.getText().toString().trim();
        boolean valid = true;
        if(name.equals(""))
            valid = false;
        if(description.equals(""))
            valid = false;
        if(payment.equals(""))
            valid = false;
        if(valid)
        {
            double paymentDouble = Double.parseDouble(payment);
            Post post = new Post();
            post.createTask(name, description, paymentDouble, childId, new OnSuccessListener<String>() {
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
            Toast toast = Toast.makeText(getApplicationContext(), "Improper entries in fields.", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

}
