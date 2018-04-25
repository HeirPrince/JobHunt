package com.example.prince.jobhunt.engine;

import android.content.Context;

import com.example.prince.jobhunt.model.Job;

/**
 * Created by Prince on 4/24/2018.
 */

public class PostManager {
	private static final String TAG = PostManager.class.getSimpleName();
	private static PostManager instance;

	private Context context;

	public static PostManager getInstance(Context context){
		if (instance == null){
			instance = new PostManager(context);
		}

		return instance;
	}

	private PostManager(Context context){
		this.context = context;
	}

	public void postOrUpdateJob(Job job){
		//access db method to do so.
	}
}
