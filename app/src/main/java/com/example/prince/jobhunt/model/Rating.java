package com.example.prince.jobhunt.model;

/**
 * Created by Prince on 3/15/2018.
 */

public class Rating {

	private String id;
	private double nmStars;
	private String review;
	private String reaction;

	public Rating() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public double getNmStars() {
		return nmStars;
	}

	public void setNmStars(double nmStars) {
		this.nmStars = nmStars;
	}

	public String getReview() {
		return review;
	}

	public void setReview(String review) {
		this.review = review;
	}

	public String getReaction() {
		return reaction;
	}

	public void setReaction(String reaction) {
		this.reaction = reaction;
	}
}
