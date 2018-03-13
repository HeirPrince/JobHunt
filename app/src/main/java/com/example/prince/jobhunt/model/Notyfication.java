package com.example.prince.jobhunt.model;

import android.net.Uri;

/**
 * Created by Prince on 3/11/2018.
 */

public class Notyfication {

	private String sender;
	private String receiver;
	private String message;
	private String job_id;
	private Boolean isNeg;
	private Boolean isPartTime;
	private String start_date;
	private String end_date;
	private String start_time;
	private String end_time;
	private String cv_image;

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public String getJob_id() {
		return job_id;
	}

	public void setJob_id(String job_id) {
		this.job_id = job_id;
	}

	public Boolean getNeg() {
		return isNeg;
	}

	public void setNeg(Boolean neg) {
		isNeg = neg;
	}

	public Boolean getPartTime() {
		return isPartTime;
	}

	public void setPartTime(Boolean partTime) {
		isPartTime = partTime;
	}

	public String getStart_date() {
		return start_date;
	}

	public void setStart_date(String start_date) {
		this.start_date = start_date;
	}

	public String getEnd_date() {
		return end_date;
	}

	public void setEnd_date(String end_date) {
		this.end_date = end_date;
	}

	public String getStart_time() {
		return start_time;
	}

	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}

	public String getEnd_time() {
		return end_time;
	}

	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}

	public String getCv_image() {
		return cv_image;
	}

	public void setCv_image(String cv_image) {
		this.cv_image = cv_image;
	}
}
