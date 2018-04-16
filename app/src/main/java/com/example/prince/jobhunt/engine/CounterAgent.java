package com.example.prince.jobhunt.engine;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Prince on 3/26/2018.
 */

public class CounterAgent {

	private Context ctx;
	private FirebaseFirestore firestore;
	private AuthManager authManager;

	public CounterAgent(Context ctx) {
		this.ctx = ctx;
		this.firestore = FirebaseFirestore.getInstance();
		this.authManager = new AuthManager();
	}


	//counts popular jobs basing on how many times a user opens JobDetails.class
	public void addToPopular(){
		DocumentReference ref = firestore.collection("count").document("popular");
		Map<String, Boolean> popMap = new HashMap<>();
		popMap.put(authManager.getCurrentUID(), true);
		ref.set(popMap)
				.addOnCompleteListener(new OnCompleteListener<Void>() {
					@Override
					public void onComplete(@NonNull Task<Void> task) {
						Toast.makeText(ctx, "added to popular", Toast.LENGTH_SHORT).show();
					}
				});

	}

	//count over all rating
	public void countRating(){

	}

}
