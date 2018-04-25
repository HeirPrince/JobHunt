package com.example.prince.jobhunt.engine.listeners;

/**
 * Created by Prince on 4/24/2018.
 */

public interface OnImageUploadedListener {
	void onImageUploaded(boolean state, String downloadUrl);
	void progress(double progress);
}

