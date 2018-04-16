package com.example.prince.jobhunt.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.prince.jobhunt.R;
import com.example.prince.jobhunt.activities.Home;
import com.example.prince.jobhunt.engine.AuthManager;
import com.example.prince.jobhunt.engine.FirebaseAgent;
import com.example.prince.jobhunt.engines.NewSection;
import com.example.prince.jobhunt.model.Job;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewJobs extends Fragment {


	public static final String TITLE = "New";
	private int page;
	private String title;

	private FirebaseFirestore database;
	private ProgressBar progressBar;
	private RecyclerView new_list;
	private FirebaseAgent agnt;
	private FirestoreRecyclerAdapter adapter;
	private AuthManager authManager;
	private List<Job> latest;
	private List<Job> cat;
	private List<String> id_list;
	private SectionedRecyclerViewAdapter sectionAdapter;
	private NewSection latestJobs;
	private NewSection catSection;
	private FirebaseFirestore firestore;


	public NewJobs() {
		// Required empty public constructor
		database = FirebaseFirestore.getInstance();
		agnt = new FirebaseAgent(getContext());
		authManager = new AuthManager();
		firestore = FirebaseFirestore.getInstance();
	}

	public static NewJobs newInstance() {
		return new NewJobs();
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View v = inflater.inflate(R.layout.fragment_new_jobs, container, false);

		new_list = v.findViewById(R.id.new_list);
		progressBar = v.findViewById(R.id.progress);
		progressBar.setVisibility(View.VISIBLE);
		new_list.setLayoutManager(new LinearLayoutManager(getContext()));
		new_list.setHasFixedSize(true);
		latest = new ArrayList<>();
		cat = new ArrayList<>();
		id_list = new ArrayList<>();
		sectionAdapter = new SectionedRecyclerViewAdapter();
		getJobs();

		new_list.addOnScrollListener(new RecyclerView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
				super.onScrollStateChanged(recyclerView, newState);
				if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
					// Hiding FAB
					((Home) getActivity()).hideFloatingActionButton();
				} else if (newState == RecyclerView.SCROLL_STATE_IDLE) {
					// Showing FAB
					((Home) getActivity()).showFloatingActionButton();
				}

			}

			@Override
			public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
				super.onScrolled(recyclerView, dx, dy);
			}
		});

		return v;
	}

	public void getJobs() {
		//latest jobs
		agnt.getNewJobs(new FirebaseAgent.jobList() {
			@Override
			public void jobList(List<Job> jobs, List<String> ids) {
				if (jobs != null && ids != null){
					latestJobs = new NewSection(getContext(), "New", jobs, ids);
					latestJobs.setVisible(true);
					sectionAdapter.addSection(latestJobs);
					sectionAdapter.notifyDataSetChanged();
				}else {
					jobs.clear();
					latestJobs.setVisible(false);
				}

			}
		});

		//based on category
		agnt.getAllJobsBCat(new FirebaseAgent.jobList() {
			@Override
			public void jobList(List<Job> jobs, List<String> ids) {
				if (jobs != null && ids != null) {
					catSection = new NewSection(getContext(), "Based on your Category", jobs, ids);
					catSection.setVisible(true);
					sectionAdapter.addSection(catSection);
					sectionAdapter.notifyDataSetChanged();
					progressBar.setVisibility(View.GONE);
				} else {
					jobs.clear();
					catSection.setVisible(false);
					progressBar.setVisibility(View.VISIBLE);
				}
			}
		});

		new_list.setAdapter(sectionAdapter);


	}

	public void flushNew(Boolean on) {

		if (on) {
			id_list.clear();
			latest.clear();
		}else {
			return;
		}
	}

	public void flushCat(Boolean on) {

		if (on) {
			id_list.clear();
			cat.clear();
		}else {
			return;
		}
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
