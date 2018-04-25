package com.example.prince.jobhunt.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.prince.jobhunt.JobAdapter;
import com.example.prince.jobhunt.R;
import com.example.prince.jobhunt.engine.FirebaseAgent;
import com.example.prince.jobhunt.model.Job;
import com.tabassum.shimmerRecyclerView.ShimmerRecyclerView;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class AllJobs extends Fragment {


	public static final String TITLE = "New";
	private FirebaseAgent agent;
	private ShimmerRecyclerView job_list;
	private JobAdapter adapter;

	public AllJobs() {
		// Required empty public constructor
		agent = new FirebaseAgent(getContext());
	}

	public static AllJobs newInstance() {
		return new AllJobs();
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View v = inflater.inflate(R.layout.fragment_new_jobs, container, false);

		setUI(v);

		return v;
	}

	private void setUI(View v) {
		job_list = v.findViewById(R.id.job_list);
		getJobs();
	}


	public void getJobs() {

		job_list.setLayoutManager(new LinearLayoutManager(getContext()));
		job_list.setHasFixedSize(true);
		job_list.setDrawingCacheEnabled(true);
		job_list.showShimmerAdapter();
		agent.getJobList(new FirebaseAgent.jobList() {
			@Override
			public void All(List<Job> jobs, List<String> ids) {
				job_list.hideShimmerAdapter();
				adapter = new JobAdapter(getContext(), jobs, ids);
				job_list.setAdapter(adapter);
			}
		});


	}

	@Override
	public void onStart() {
		super.onStart();

	}

	@Override
	public void onStop() {
		super.onStop();

	}
}
