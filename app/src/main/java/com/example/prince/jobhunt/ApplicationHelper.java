package com.example.prince.jobhunt;

import com.example.prince.jobhunt.engine.FirebaseAgent;
import com.example.prince.jobhunt.engine.StorageAgent;

/**
 * Created by Prince on 4/24/2018.
 */

public class ApplicationHelper {

	private static final String TAG = ApplicationHelper.class.getSimpleName();
	private static FirebaseAgent firebaseAgent;
	private static StorageAgent storageAgent;

	public static FirebaseAgent getFirebaseAgent() {
		return firebaseAgent;
	}

	public static StorageAgent getStorageAgent(){return storageAgent;}

	public static void initFirebaseAgent(Application app){
		firebaseAgent = FirebaseAgent.getInstance(app);
		firebaseAgent.init();
	}

	public static void initStorageAgent(Application app){
		storageAgent = StorageAgent.getInstance(app);
		storageAgent.init();
	}
}
