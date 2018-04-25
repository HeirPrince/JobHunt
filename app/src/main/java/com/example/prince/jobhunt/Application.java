package com.example.prince.jobhunt;

import com.example.prince.jobhunt.engine.FirebaseAgent;

/**
 * Created by Prince on 4/24/2018.
 */

public class Application extends android.app.Application {
	public static final String TAG = Application.class.getSimpleName();

	@Override
	public void onCreate() {
		super.onCreate();

		ApplicationHelper.initFirebaseAgent(this);
		FirebaseAgent.getInstance(this);

	}
}
