package com.example.prince.jobhunt.model;

/**
 * Created by Prince on 4/22/2018.
 */

public class View_job {
	private String uid;
	private String id;
	private Boolean viewed;

	public View_job() {
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Boolean getViewed() {
		return viewed;
	}

	public void setViewed(Boolean viewed) {
		this.viewed = viewed;
	}
}
