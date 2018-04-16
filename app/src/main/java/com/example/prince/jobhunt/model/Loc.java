package com.example.prince.jobhunt.model;

import com.example.prince.jobhunt.engine.AuthManager;

/**
 * Created by Prince on 3/19/2018.
 */

public class Loc {

	private AuthManager authManager;
	private String uid;
	private double Latitude;
	private double Longitude;

	public Loc() {
		this.authManager = new AuthManager();
		uid = authManager.getCurrentUID();
	}

	public String getUid() {
		return uid;
	}

	public double getLatitude() {
		return Latitude;
	}

	public void setLatitude(double latitude) {
		Latitude = latitude;
	}

	public double getLongitude() {
		return Longitude;
	}

	public void setLongitude(double longitude) {
		Longitude = longitude;
	}
}
