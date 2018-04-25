package com.example.prince.jobhunt.ViewHolders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.prince.jobhunt.R;
import com.example.prince.jobhunt.engine.FirebaseAgent;
import com.example.prince.jobhunt.engine.ImageUtils;
import com.example.prince.jobhunt.engine.TimeUtils;
import com.example.prince.jobhunt.model.Job;
import com.example.prince.jobhunt.model.User;
import com.github.thunder413.datetimeutils.DateTimeUtils;

/**
 * Created by Prince on 3/26/2018.
 */

public class JobViewHolder extends RecyclerView.ViewHolder{
	public TextView job_title, user_name, time_ago, job_location;
	public ImageView big_image;
	private Context ctx;
	private ImageUtils imageUtils;
	private TimeUtils timeUtils;
	private FirebaseAgent agent;

	public JobViewHolder(View itemView, Context ctx) {
		super(itemView);
		user_name = itemView.findViewById(R.id.user_name);
		big_image = itemView.findViewById(R.id.job_image);
		job_title = itemView.findViewById(R.id.job_title);
		time_ago = itemView.findViewById(R.id.time_ago);
		job_location = itemView.findViewById(R.id.job_location);
		this.ctx = ctx;
		this.imageUtils = new ImageUtils();
		this.timeUtils = new TimeUtils();
		agent = new FirebaseAgent(ctx);
	}

	public void setJob(Job job, String jobimg) {
		agent.getUserByUID(job.getOwner(), new FirebaseAgent.getUser() {
			@Override
			public void gottenUser(User user) {
				user_name.setText(user.getUsername());
			}
		});
		job_title.setText(job.getTitle());
		job_location.setText(job.getLocation());
		if (jobimg != null){
//			imageUtils.darkenImage(big_image);
			Glide.with(ctx).load(jobimg).into(big_image);
		}else
			Toast.makeText(ctx, "no image found", Toast.LENGTH_SHORT).show();
		String t = DateTimeUtils.getTimeAgo(ctx, timeUtils.extractFromTimestamp(Long.parseLong(job.getTimeStamp()))+" ago");
		time_ago.setText(t);
	}


}
