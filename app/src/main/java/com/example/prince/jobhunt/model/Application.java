package com.example.prince.jobhunt.model;

/**
 * Created by Prince on 2/28/2018.
 */

public class Application {

	private String uid;
	private String desc;
	private String salary;

	public Application() {
	}

	public Application(String uid, String desc, String salary) {
		this.uid = uid;
		this.desc = desc;
		this.salary = salary;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getSalary() {
		return salary;
	}

	public void setSalary(String salary) {
		this.salary = salary;
	}
}
