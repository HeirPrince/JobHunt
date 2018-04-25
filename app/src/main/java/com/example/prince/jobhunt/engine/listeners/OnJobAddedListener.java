package com.example.prince.jobhunt.engine.listeners;

import com.example.prince.jobhunt.model.Job;

/**
 * Created by Prince on 4/24/2018.
 */

public interface OnJobAddedListener {
	void addedJob(Job job);
	void onCanceled(String message);
}
