package com.example.prince.jobhunt.engine;

import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by Prince on 2/14/2018.
 */

public class AuthManager {

    private Context ctx;
    private FirebaseAuth auth;
    private FirebaseUser user;

    public AuthManager(Context ctx) {
        this.ctx = ctx;
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
    }

    public Boolean checkAuth(){
        if (user != null){
            return true;
        }else {
            return false;
        }
    }

    public String getCurrentUser(){
        return user.getPhoneNumber();
    }

    public String getCurrentUID(){return  user.getUid();}



}
