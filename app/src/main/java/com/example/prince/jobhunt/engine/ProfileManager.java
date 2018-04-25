package com.example.prince.jobhunt.engine;

import android.content.Context;
import android.net.Uri;
import android.widget.Toast;

import com.example.prince.jobhunt.ApplicationHelper;
import com.example.prince.jobhunt.engine.listeners.OnImageUploadedListener;
import com.example.prince.jobhunt.engine.listeners.OnProfileCreatedListener;
import com.example.prince.jobhunt.model.User;

/**
 * Created by Prince on 4/24/2018.
 */

public class ProfileManager {
	public static final String TAG = ProfileManager.class.getSimpleName();
	private static ProfileManager instance;

	private Context context;
	private FirebaseAgent agent;
	private StorageAgent storageAgent;

	public static ProfileManager getInstance(Context context){
		if (instance == null){
			instance = new ProfileManager(context);
		}

		return instance;
	}

	public ProfileManager(Context context) {
		this.context = context;
		this.agent = ApplicationHelper.getFirebaseAgent();
		this.storageAgent = ApplicationHelper.getStorageAgent();
	}

	public void createProfile(User user, final OnProfileCreatedListener onProfileCreatedListener){
		createProfile(user, null, onProfileCreatedListener);
	}

	public void createProfile(User user, Uri image, final OnProfileCreatedListener onProfileCreatedListener){
		if (image == null){
			agent.addUser(user, onProfileCreatedListener);
		}

		storageAgent.uploadImage(user.getUid(), image, new OnImageUploadedListener() {
			@Override
			public void onImageUploaded(boolean state, String url) {
				if (state){
					Toast.makeText(context, "image uploaded url : "+url, Toast.LENGTH_SHORT).show();
				}else {
					Toast.makeText(context, "image upload failed", Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void progress(double progress) {
				//Track progress
			}
		});

	}
}
