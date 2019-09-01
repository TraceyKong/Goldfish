package com.example.piggybank;

import android.content.Intent;
import android.os.Bundle;

import com.example.piggybank.ChildActivities.ChildMainActivity;
import com.example.piggybank.Firebase.Get;
import com.example.piggybank.Firebase.Models.User;
import com.example.piggybank.ParentActivities.ParentMainActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class UserSelectionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_selection);
        //redirects to proper activity based on if user is parent or child
        Get get = new Get();
        get.getUserById(getIntent().getExtras().getString("userId"), new OnSuccessListener<User>() {
            @Override
            public void onSuccess(User user) {
                if(user.getUserType().equals("parent"))
                {
                    Intent intent = new Intent(UserSelectionActivity.this, ParentMainActivity.class);
                    intent.putExtra("userId", user.getUserId());
                    intent.putExtra("userName", user.getName());
                    startActivity(intent);
                    finish();
                }
                if(user.getUserType().equals("child"))
                {
                    Intent intent = new Intent(UserSelectionActivity.this, ChildMainActivity.class);
                    intent.putExtra("userId", user.getUserId());
                    intent.putExtra("userName", user.getName());
                    startActivity(intent);
                    finish();
                }
            }
        }, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println("error error");
            }
        });
    }

}
