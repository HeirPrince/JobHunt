package com.example.prince.jobhunt.constants;

/**
 * Created by Prince on 4/24/2018.
 */

public class JobStatus {

	public static final int UPDATED = 1;
	public static final int NEW = 2;
	public static final int OLD = 3;
	public static final int MINE = 4;
	public static final int BASED_ON_CATEGORY = 5;

	int status;

	public JobStatus(int status) {
		this.status = status;
	}

	public int getStatus() {
		return status;
	}
}
