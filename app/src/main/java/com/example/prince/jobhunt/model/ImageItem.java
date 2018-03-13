package com.example.prince.jobhunt.model;

import android.net.Uri;

/**
 * Created by Prince on 3/7/2018.
 */

public class ImageItem {

	private String name;
	private Uri fileName;
	private String timestamp;

	public ImageItem() {
	}

	public ImageItem(String name, Uri fileName, String timestamp) {
		this.name = name;
		this.fileName = fileName;
		this.timestamp = timestamp;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Uri getFileName() {
		return fileName;
	}

	public void setFileName(Uri fileName) {
		this.fileName = fileName;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
}
