package com.example.prince.jobhunt.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.prince.jobhunt.R;
import com.example.prince.jobhunt.engine.AuthManager;
import com.example.prince.jobhunt.engine.Constants;
import com.example.prince.jobhunt.engine.FirebaseAgent;
import com.example.prince.jobhunt.model.Job;
import com.example.prince.jobhunt.model.User;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Prince on 2/20/2018.
 */

public class JobAdapter extends RecyclerView.Adapter<JobAdapter.JobHolder> {


	private Context ctx;
	private List<Job> jobs;
	private FirebaseAgent agent;
	private AuthManager authManager;

	public JobAdapter(Context ctx, List<Job> jobs) {
		this.ctx = ctx;
		this.jobs = jobs;
		agent = new FirebaseAgent(ctx);
		authManager = new AuthManager(ctx);
	}

	@Override
	public JobHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_job_item, parent, false);
		return new JobHolder(v);
	}

	@Override
	public void onBindViewHolder(final JobHolder holder, int position) {
		final Job model = jobs.get(position);
			agent.getUserByUID(model.getOwner(), new FirebaseAgent.getUser() {
				@Override
				public void gottenUser(final User user) {
					if (user != null) {
						agent.downloadImage(user.getUid(), Constants.USER_PROFILE_IMAGE_PATH, new FirebaseAgent.OnImageDownload() {
							@Override
							public void isDownloaded(Boolean status, String url) {
								if (status && url != null) {
									holder.setUser(user, url);
								} else {
									//no image set default image
								}
							}

							@Override
							public void isFailed(Boolean status) {

							}
						});
					} else {
						// no user
					}
				}
			});
			agent.downloadImage(model.getOwner() + model.getTitle(), Constants.JOB_IMAGE_PATH, new FirebaseAgent.OnImageDownload() {
				@Override
				public void isDownloaded(Boolean status, String url) {
					if (status && url != null) {
						holder.setJob(model, url);
					} else {
						//no image set default
					}
				}

				@Override
				public void isFailed(Boolean status) {

				}
			});

			holder.itemView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {

				}
			});

	}

	@Override
	public int getItemCount() {
		return jobs.size();
	}

	public class JobHolder extends RecyclerView.ViewHolder {

		public TextView user_name, user_career, job_title, job_desc, job_location;
		public CircleImageView user_image;
		public ImageView big_image;

		public JobHolder(View itemView) {
			super(itemView);
			user_name = itemView.findViewById(R.id.user_name);
			user_career = itemView.findViewById(R.id.user_career);
			user_image = itemView.findViewById(R.id.user_image);
			job_desc = itemView.findViewById(R.id.job_desc);
			big_image = itemView.findViewById(R.id.big_image);
			job_title = itemView.findViewById(R.id.job_title);
			job_location = itemView.findViewById(R.id.location);

			//clik listeners
			itemView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {

				}
			});
		}

		public void setUser(User user, String image) {
			user_name.setText(user.getUsername());
			user_career.setText(user.getCareer());
			Glide.with(ctx).load(image).into(user_image);
		}

		public void setJob(Job job, String jobimg) {
			job_desc.setText(job.getDesc());
			job_location.setText(job.getLocation());
			job_title.setText(job.getTitle());
			Glide.with(ctx).load(jobimg).into(big_image);
		}
	}

	public Job removeItem(int position){
		final Job job = jobs.remove(position);
		notifyItemRemoved(position);
		return job;
	}

	public void addItem(int position, Job job){
		jobs.add(position, job);
		notifyItemInserted(position);
	}

	public void moveItem(int fromPosition, int toPosition){
		final Job job = jobs.get(fromPosition);
		jobs.add(toPosition, job);
		notifyItemMoved(fromPosition, toPosition);
	}

	public void animateTo(List<Job> jobList) {
		applyAnimateRemovals(jobList);
		applyAnimateInsertions(jobList);
		applyAnimateMovements(jobList);
	}

	private void applyAnimateMovements(List<Job> jobList) {
		for (int i = jobs.size() - 1; i >= 0; i--){
			final Job job = jobs.get(i);
			if (!jobList.contains(job)){
				removeItem(i);
			}
		}
	}

	private void applyAnimateInsertions(List<Job> jobList) {
		int count = jobList.size();
		for (int i = 0; i<count; i++){
			final Job job = jobs.get(i);
			if (!jobList.contains(job)){
				addItem(i, job);
			}
		}
	}

	private void applyAnimateRemovals(List<Job> jobList) {
		for (int toPosition = jobList.size() - 1; toPosition >=0; toPosition--){
			final Job job = jobList.get(toPosition);
			final int fromPosition = jobs.indexOf(job);
			if (fromPosition >= 0 && fromPosition != toPosition){
				moveItem(fromPosition, toPosition);
			}
		}
	}
}
