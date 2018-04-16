package com.example.prince.jobhunt.model;

/**
 * Created by Prince on 4/12/2018.
 */

public class AppTimeStamp {
	private Object timestamp;

	public AppTimeStamp() {
	}

	public Object getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Object timestamp) {
		this.timestamp = timestamp;
	}

	public String toString(){
		return timestamp.toString();
	}
}
