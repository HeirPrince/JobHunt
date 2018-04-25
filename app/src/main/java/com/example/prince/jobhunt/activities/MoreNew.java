package com.example.prince.jobhunt.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;

import com.example.prince.jobhunt.JobAdapter;
import com.example.prince.jobhunt.R;
import com.example.prince.jobhunt.engine.FirebaseAgent;
import com.example.prince.jobhunt.model.Job;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MoreNew extends AppCompatActivity {

	@BindView(R.id.job_list)RecyclerView jobList;
	@BindView(R.id.toolbar_normal)Toolbar toolbar;
	@BindView(R.id.progress)ProgressBar progressBar;

	private FirebaseAgent agent;
	private JobAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_more_new);
		ButterKnife.bind(this);
		setSupportActionBar(toolbar);
		getSupportActionBar().setTitle("More New Jobs");
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		agent = new FirebaseAgent(this);

		agent.getJobList(new FirebaseAgent.jobList() {
			@Override
			public void All(List<Job> jobs, List<String> ids) {
				if (jobs != null && ids != null){
					doneLoading(true, jobs, ids);
				}else {
					doneLoading(false, null, null);
				}
			}
		});
	}

	public void doneLoading(Boolean state, List<Job> jobs, List<String> ids){
		if (state){
			progressBar.setVisibility(View.GONE);
			jobList.setVisibility(View.VISIBLE);
			jobList.setLayoutManager(new LinearLayoutManager(this));
			jobList.setHasFixedSize(true);
			adapter = new JobAdapter(this, jobs, ids);
			jobList.setAdapter(adapter);
		}else {
			progressBar.setVisibility(View.VISIBLE);
			jobList.setVisibility(View.GONE);
		}
	}
}
