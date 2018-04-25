package com.example.prince.jobhunt.engines;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.example.prince.jobhunt.R;
import com.example.prince.jobhunt.ViewHolders.JobHeaderViewHolder;
import com.example.prince.jobhunt.ViewHolders.JobViewHolder;
import com.example.prince.jobhunt.activities.JobInfo;
import com.example.prince.jobhunt.engine.Constants;
import com.example.prince.jobhunt.engine.FirebaseAgent;
import com.example.prince.jobhunt.engine.TimeUtils;
import com.example.prince.jobhunt.model.Job;
import com.example.prince.jobhunt.model.User;

import java.util.List;

import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;

/**
 * Created by Prince on 3/26/2018.
 */

public class NewSection extends StatelessSection {

	public static final String TAG = NewSection.class.getSimpleName();
	private String title;
	private List<Job> jobs;
	private List<String> ids;
	private Context ctx;
	private FirebaseAgent agnt;
	private TimeUtils timeUtils;


	public NewSection(Context ctx, String title, List<Job> jobs, List<String> ids) {
		super(R.layout.layout_job_item_header, R.layout.layout_job_item_vertical);
		this.jobs = jobs;
		this.title = title;
		this.ctx = ctx;
		this.ids = ids;
		this.agnt = new FirebaseAgent(ctx);
		this.timeUtils = new TimeUtils();
	}

	@Override
	public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
		return new JobHeaderViewHolder(view, ctx);
	}

	@Override
	public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {
		JobHeaderViewHolder viewHolder = (JobHeaderViewHolder)holder;
		viewHolder.headerTitle.setText(title);
	}

	@Override
	public int getContentItemsTotal() {
		return jobs.size();
	}

	@Override
	public RecyclerView.ViewHolder getItemViewHolder(View view) {
		return new JobViewHolder(view ,ctx);
	}

	@Override
	public void onBindItemViewHolder(final RecyclerView.ViewHolder holder, int position) {
		final JobViewHolder Jholder = (JobViewHolder)holder;
		final Job model = jobs.get(position);
		final String id = ids.get(position);

		if (jobs != null && ids != null){
			agnt.downloadImage(id, Constants.IMAGE_PATH_JOBS_MAIN, new FirebaseAgent.OnImageDownload() {
				@Override
				public void isDownloaded(Boolean status, final String url) {
					if (url != null){
						agnt.getUserByUID(model.getOwner(), new FirebaseAgent.getUser() {
							@Override
							public void gottenUser(User user) {
								Jholder.setJob(model, url);
							}
						});
					}else {
						Toast.makeText(ctx, "user image not found", Toast.LENGTH_SHORT).show();
					}
				}
			});

			Jholder.itemView.setOnClickListener(new View.OnClickListener() {
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
		}else {
			Toast.makeText(ctx, "empty lists", Toast.LENGTH_SHORT).show();
		}
	}
}
