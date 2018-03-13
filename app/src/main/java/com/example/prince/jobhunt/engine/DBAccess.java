package com.example.prince.jobhunt.engine;

import com.example.prince.jobhunt.model.Job;
import com.example.prince.jobhunt.model.User;

import java.util.List;

/**
 * Created by Prince on 3/5/2018.
 */

public interface DBAccess {

	public void getAllUsers(List<User> users);

	public void getAllJibs(List<Job> jobs);

	public void getUserById(String uid, User user);

}
