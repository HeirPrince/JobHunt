package com.example.prince.jobhunt.model;

/**
 * Created by Prince on 2/20/2018.
 */

public class Job {

	private String title;
	private String desc;
	private String location;
	private String category;
	private String image;
	private int salary;
	private String salary_type;
	private String owner;

	public Job() {
	}

	public int getSalary() {
		return salary;
	}

	public void setSalary(int salary) {
		this.salary = salary;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getSalary_type() {
		return salary_type;
	}

	public void setSalary_type(String salary_type) {
		this.salary_type = salary_type;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}
}
