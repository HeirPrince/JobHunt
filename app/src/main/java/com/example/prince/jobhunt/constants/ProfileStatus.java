package com.example.prince.jobhunt.constants;

/**
 * Created by Prince on 4/24/2018.
 */

public class ProfileStatus {

	public static final int PROFILE_CREATED = 1;
	public static final int PROFILE_NOT_AUTHORISED = 2;
	public static final int UNKNOWN_PROFILE = 3;

	int status;

	public ProfileStatus(int status) {
		this.status = status;
	}

	public int getStatus() {
		return status;
	}
}
