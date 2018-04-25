package com.example.prince.jobhunt;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.prince.jobhunt.ViewHolders.JobViewHolder;
import com.example.prince.jobhunt.activities.JobInfo;
import com.example.prince.jobhunt.engine.Constants;
import com.example.prince.jobhunt.engine.FirebaseAgent;
import com.example.prince.jobhunt.engine.TimeUtils;
import com.example.prince.jobhunt.model.Job;

import java.util.List;

/**
 * Created by Prince on 4/5/2018.
 */

public class JobAdapter extends RecyclerView.Adapter<JobViewHolder>{

	private String title;
	private List<Job> jobs;
	private List<String> ids;
	private Context ctx;
	private FirebaseAgent agnt;
	private TimeUtils timeUtils;


	public JobAdapter(Context ctx, List<Job> jobs, List<String> ids) {
		this.jobs = jobs;
		this.title = title;
		this.ctx = ctx;
		this.ids = ids;
		this.agnt = new FirebaseAgent(ctx);
		this.timeUtils = new TimeUtils();
	}

	@Override
	public JobViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_job_item_vertical, parent, false);
		return new JobViewHolder(view, ctx);
	}

	@Override
	public void onBindViewHolder(final JobViewHolder holder, int position) {
		final Job model = jobs.get(position);
		final String id = ids.get(position);

		agnt.downloadImage(id, Constants.IMAGE_PATH_JOBS_MAIN, new FirebaseAgent.OnImageDownload() {
			@Override
			public void isDownloaded(Boolean status, final String url) {
				holder.setJob(model, url);
			}
		});

		holder.itemView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent i = new Intent(ctx, JobInfo.class);
				i.putExtra("job_title", model.getTitle());
				i.putExtra("job_image", model.getImage());
				i.putExtra("job_id", id);
				i.putExtra("job_desc", model.getDesc());
				i.putExtra("job_timestamp", timeUtils.extractFromTimestamp(Long.parseLong(model.getTimeStamp())));
				i.putExtra("job_location", model.getLocation());
				ctx.startActivity(i);
			}
		});

	}

	@Override
	public int getItemCount() {
		return jobs.size();
	}



}
