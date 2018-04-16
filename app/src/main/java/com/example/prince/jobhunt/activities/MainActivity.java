package com.example.prince.jobhunt.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.prince.jobhunt.R;
import com.example.prince.jobhunt.engine.AuthManager;
import com.example.prince.jobhunt.engine.FirebaseAgent;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUserMetadata;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Arrays;

import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    public static final int RC_SIGN_IN = 123;

    private AuthManager idManager;
    private FirebaseAgent agent;
    private FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        auth = FirebaseAuth.getInstance();
        idManager = new AuthManager();
        agent = new FirebaseAgent(this);

        //checking if a user is authenticated
        if (idManager.checkAuth()){
            agent.checkIfUserExists(auth.getCurrentUser().getUid(), new FirebaseAgent.isUserRegistered() {
                @Override
                public void isRegistered(Boolean status, DocumentSnapshot ds) {
                    if (status) {
                        if (ds != null) {
                            finish();
                            agent.setDutyStatus(false);
                            startActivity(new Intent(MainActivity.this, Home.class));
                        } else {
                            startActivity(new Intent(MainActivity.this, Register.class));
                        }
                    }else {
                        startActivity(new Intent(MainActivity.this, Register.class));
                    }

                }
            });
        }else {
            authUser();
        }

    }

    //starting firebase ui auth activity
    public void authUser(){
        startActivityForResult(
                AuthUI.getInstance().createSignInIntentBuilder()
                        .setIsSmartLockEnabled(false)
                        .setAvailableProviders(Arrays.asList(
                                new AuthUI.IdpConfig.PhoneBuilder().build()
                        )).build(), RC_SIGN_IN
        );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                checkFirstUse();
                // ...
            } else {
                // Sign in failed, check response for error code
                // ...
                if (response == null){
                    finish();
                    Toast.makeText(this, R.string.sign_in_canceled, Toast.LENGTH_SHORT).show();
                    return;
                }

                if (response.getErrorCode() == ErrorCodes.NO_NETWORK){
                    finish();
                    Toast.makeText(this, R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
                    return;
                }

                if (response.getErrorCode() == ErrorCodes.UNKNOWN_ERROR){
                    finish();
                    Toast.makeText(this, R.string.unknown_error, Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        }
    }

    public void checkFirstUse(){
        FirebaseUserMetadata metadata = auth.getCurrentUser().getMetadata();
        if (metadata.getCreationTimestamp() == metadata.getLastSignInTimestamp()){
            startActivity(new Intent(MainActivity.this, Register.class));
        }else {
            agent.checkIfUserExists(auth.getCurrentUser().getUid(), new FirebaseAgent.isUserRegistered() {
                @Override
                public void isRegistered(Boolean status, DocumentSnapshot ds) {
                    if (status){
                        if (ds != null){
                            finish();
                            startActivity(new Intent(MainActivity.this, Home.class));
                        }else {
                            startActivity(new Intent(MainActivity.this, Register.class));
                        }
                    }

                }
            });
        }
    }

}
