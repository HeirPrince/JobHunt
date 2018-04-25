package com.example.prince.jobhunt.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.prince.jobhunt.ApplicationHelper;
import com.example.prince.jobhunt.R;
import com.example.prince.jobhunt.engine.AuthManager;
import com.example.prince.jobhunt.engine.FirebaseAgent;
import com.example.prince.jobhunt.engine.listeners.OnObjectExitListener;
import com.example.prince.jobhunt.model.User;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUserMetadata;

import java.util.Arrays;

import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    public static final int RC_SIGN_IN = 123;

    private AuthManager idManager;
    private FirebaseAgent agent;
    private FirebaseAuth auth;
    public ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        auth = FirebaseAuth.getInstance();
        idManager = new AuthManager();
        progressDialog = new ProgressDialog(this);
        agent = ApplicationHelper.getFirebaseAgent();

        //check net state
        if (checkInternetConnection()){

            //checking if a user is authenticated
            if (idManager.checkAuth()){
                agent.checkIfUserExists(new OnObjectExitListener<User>() {
                    @Override
                    public void onDataChanged(boolean exist) {
                        if (exist){
                            finish();
                            agent.setDutyStatus(false);
                            startActivity(new Intent(MainActivity.this, Home.class));
                        }else {
                            startActivity(new Intent(MainActivity.this, Register.class));
                        }
                    }
                });

            }else {
                authUser();
            }
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
            agent.checkIfUserExists(new OnObjectExitListener<User>() {
                @Override
                public void onDataChanged(boolean exist) {
                    if (exist){
                        finish();
                        agent.setDutyStatus(false);
                        startActivity(new Intent(MainActivity.this, Home.class));
                    }else {
                        startActivity(new Intent(MainActivity.this, Register.class));
                    }
                }
            });
        }
    }

    public void showProgress() {
        showProgress(R.string.loading);
    }

    public void showProgress(int message) {
        hideProgress();
        progressDialog.setMessage(getString(message));
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public void hideProgress() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    public void showWarningDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message);
        builder.setPositiveButton(R.string.button_ok, null);
        builder.show();
    }

    public boolean checkNetworkState(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    public boolean checkInternetConnection() {
        boolean hasInternet = checkNetworkState();
        if (!hasInternet){
            showWarningDialog("No internet Connection");
        }
        return hasInternet;
    }

}
