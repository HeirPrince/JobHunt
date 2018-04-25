package com.example.prince.jobhunt.engine;

import android.app.Application;

/**
 * Created by Prince on 4/20/2018.
 */

public class App extends Application {
	private static App instance;

	public static App getInstance() {
		return instance;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
		DiscreteScrollViewOptions.init(this);
	}
}
