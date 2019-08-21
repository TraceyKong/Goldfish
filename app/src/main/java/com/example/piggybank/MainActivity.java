package com.example.piggybank;

import android.content.Intent;
import android.os.Bundle;

import com.example.piggybank.Firebase.Get;
import com.example.piggybank.Firebase.Post;
import com.example.piggybank.Firebase.Models.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.util.Log;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 773;
    private static final String TAG = MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            checkSignIn();
        } else {
            startActivityForResult(
                    // Get an instance of AuthUI based on the default app
                    AuthUI.getInstance().createSignInIntentBuilder()
                            .setAvailableProviders(Arrays.asList(
                                    new AuthUI.IdpConfig.EmailBuilder().build()))
                            .build(),
                    RC_SIGN_IN);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        Log.v(TAG, "Request Code: " + requestCode);
        Log.v(TAG, "Result Code: " + resultCode);

        // RC_SIGN_IN is the request code you passed into startActivityForResult(...) when starting the sign in flow.
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            // Successfully signed in
            if (resultCode == RESULT_OK) {
                Log.i(TAG, "Successfully logged in");
                checkSignIn();
            } else {
                // Sign in failed
                if (response == null) {
                    // User pressed back button
                    Log.d(TAG, "User did not sign in");
                    return;
                }

                if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                    Log.d(TAG, "No internet connection");
                    return;
                }

                Log.e(TAG, "Sign-in error: ", response.getError());
            }

        }
    }

    private void checkSignIn() {
        final FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            Get get = new Get();

            get.getUserById(auth.getCurrentUser().getUid(), new OnSuccessListener<User>() {
                @Override
                public void onSuccess(User user) {
                    System.out.println(user);
                    if (user == null) {
                        Log.v(TAG, "First time user signs in to this app");

                        final String userName = auth.getCurrentUser().getDisplayName();

                        Post post = new Post();
                        post.createUser(auth.getCurrentUser().getUid(),
                                userName,
                                "parent",
                                new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {System.out.println("test1");
                                        Intent intent = new Intent(MainActivity.this, UserSelectionActivity.class);
                                        intent.putExtra("userId",auth.getCurrentUser().getUid());
                                        startActivity(intent);
                                        finish();
                                    }
                                }, new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.e(TAG, "Error creating the user");
                                    }
                                });

                    } else {
                        Intent intent = new Intent(MainActivity.this, UserSelectionActivity.class);
                        intent.putExtra("userId", auth.getCurrentUser().getUid());
                        startActivity(intent);
                        finish();
                    }
                }
            }, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e(TAG, "Couldn't check if user exists", e);
                }
            });

        } else {
            // This should never happen
            NullPointerException exc = new NullPointerException("User signed in but null user");
            Log.e(TAG, "User attempted to sign in but auth wasn't initialized", exc);
            throw exc;
        }
    }
}
