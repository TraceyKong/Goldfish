package com.example.piggybank.ParentActivities;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.piggybank.Firebase.Post;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;

import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.piggybank.R;
import com.google.firebase.auth.FirebaseUser;

public class CreateChildAccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_child_account);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //title toolbar
        getSupportActionBar().setTitle("Create Child Account");
    }

    public void createChildAccount(View view) {
        //authorize user
        EditText parentPasswordInput = findViewById(R.id.parentPasswordInput);
        String parentPassword = parentPasswordInput.getText().toString();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userEmail = user.getEmail();
        final AuthCredential credential = EmailAuthProvider.getCredential(userEmail, parentPassword);

        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            processChildAccount(credential);
                        }
                        else
                        {
                            Toast toast = Toast.makeText(getApplicationContext(), "Your Password was incorrect.", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    }
                });
    }

    private void processChildAccount(final AuthCredential credential) {
        //get user inputs
        EditText nameInput = findViewById(R.id.childNameInput);
        EditText emailInput = findViewById(R.id.childEmailInput);
        EditText passwordInput = findViewById(R.id.childPasswordInput);

        final String name = nameInput.getText().toString().trim();
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString();

        //validate user inputs
        boolean valid = true;

        if(name.equals(""))
            valid = false;
        if(email.equals("") || !email.contains("@"))
            valid = false;
        if(password.equals("") || password.length()<8)
            valid = false;

        if(valid)
        {
            //create child account
            final FirebaseAuth auth = FirebaseAuth.getInstance();
            final String parentId = auth.getCurrentUser().getUid();
            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {

                                Post post = new Post();
                                post.createUser(auth.getCurrentUser().getUid(),
                                        name,
                                        "child",
                                        parentId,
                                        new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                //sign back into parent account
                                                auth.signInWithCredential(credential).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                                    @Override
                                                    public void onSuccess(AuthResult authResult) {
                                                        finish();//go back to previous activity
                                                    }
                                                });
                                            }
                                        }, new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                System.out.println("Failed to make child user.");
                                            }
                                        });
                            } else {
                                System.out.println("Failed to make child user.");
                            }
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
