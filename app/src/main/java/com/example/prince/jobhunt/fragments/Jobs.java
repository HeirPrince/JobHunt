package com.example.prince.jobhunt.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.prince.jobhunt.Adapters.JobAdapter;
import com.example.prince.jobhunt.R;
import com.example.prince.jobhunt.activities.Home;
import com.example.prince.jobhunt.activities.JobDetails;
import com.example.prince.jobhunt.activities.ViewImages;
import com.example.prince.jobhunt.activities.editJob;
import com.example.prince.jobhunt.activities.viewApplications;
import com.example.prince.jobhunt.engine.AuthManager;
import com.example.prince.jobhunt.engine.Constants;
import com.example.prince.jobhunt.engine.FirebaseAgent;
import com.example.prince.jobhunt.engine.onSearchQueryListener;
import com.example.prince.jobhunt.model.Job;
import com.example.prince.jobhunt.model.User;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class Jobs extends Fragment {

	private int page;
	private String title;

	private FirebaseFirestore database;
	private ProgressBar progressBar;
	private RecyclerView jobList;
	private FirebaseAgent agnt;
	private FirestoreRecyclerAdapter adapter;
	private FirestoreRecyclerAdapter search_adapter;
	private AuthManager authManager;
	private JobAdapter jobAdapter;

	public static Jobs newInstance(int page, String title){
		Jobs jobs = new Jobs();
		Bundle args = new Bundle();
		args.putInt("someInt", page);
		args.putString("someTitle", title);
		return jobs;
	}

	public Jobs() {
		// Required empty public constructor
		database = FirebaseFirestore.getInstance();
		agnt = new FirebaseAgent(getContext());
		authManager = new AuthManager(getContext());
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View v = inflater.inflate(R.layout.fragment_jobs, container, false);
		jobList = v.findViewById(R.id.list);
		progressBar = v.findViewById(R.id.progress);
		progressBar.setVisibility(View.VISIBLE);
		jobList.setLayoutManager(new LinearLayoutManager(getContext()));
		jobList.setHasFixedSize(true);
		adapter = fui();
		jobList.setAdapter(adapter);

		jobList.addOnScrollListener(new RecyclerView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
				super.onScrollStateChanged(recyclerView, newState);
				if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
					// Hiding FAB
					((Home)getActivity()).hideFloatingActionButton();
				} else if (newState == RecyclerView.SCROLL_STATE_IDLE) {
					// Showing FAB
					((Home)getActivity()).showFloatingActionButton();
				}

			}

			@Override
			public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
				super.onScrolled(recyclerView, dx, dy);
			}
		});

		((Home)getActivity()).onSearch(new onSearchQueryListener() {
			@Override
			public void query(String query) {
				jobList.setAdapter(search_adapter);
				search_adapter.startListening();
			}
		});

		return v;
	}

	public FirestoreRecyclerAdapter fui() {
		Query query = database.collection("jobs").document("category").collection("area");

		FirestoreRecyclerOptions<Job> response = new FirestoreRecyclerOptions.Builder<Job>()
				.setQuery(query, Job.class)
				.build();


		return new FirestoreRecyclerAdapter<Job, JobHolder>(response) {
			@Override
			public void onBindViewHolder(final JobHolder holder, int position, final Job model) {
				progressBar.setVisibility(View.GONE);
				if (model != null){
					final DocumentSnapshot snapshot = getSnapshots().getSnapshot(holder.getAdapterPosition());
					agnt.getUserByUID(model.getOwner(), new FirebaseAgent.getUser() {
						@Override
						public void gottenUser(final User user) {
							if (user != null) {
								agnt.downloadImage(user.getUid(), Constants.USER_PROFILE_IMAGE_PATH, new FirebaseAgent.OnImageDownload() {
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
					agnt.downloadImage(snapshot.getId(), Constants.IMAGE_PATH_JOBS_MAIN, new FirebaseAgent.OnImageDownload() {
						@Override
						public void isDownloaded(Boolean status, String url) {
							holder.setJob(model, url);
						}

						@Override
						public void isFailed(Boolean status) {

						}
					});


					holder.apply.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View view) {

							String id = snapshot.getId();
							Intent i = new Intent(getContext(), JobDetails.class);
							i.putExtra("job_id", id);
							startActivity(i);
						}
					});

					holder.more.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View view) {
							Intent i = new Intent(getContext(), ViewImages.class);
							i.putExtra("job_id", snapshot.getId());
							startActivity(i);
						}
					});

					holder.edit.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View view) {
							//edit activity
							startActivity(new Intent(getContext(), editJob.class));
						}
					});

					holder.applications.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View view) {
							String id = snapshot.getId();
							Intent i = new Intent(getContext(), viewApplications.class);
							i.putExtra("job_id", id);
							startActivity(i);
						}
					});


				}else {
					Toast.makeText(getContext(), "no jobs found", Toast.LENGTH_SHORT).show();
				}

			}

			@Override
			public JobHolder onCreateViewHolder(ViewGroup group, int i) {
				View view = LayoutInflater.from(group.getContext())
						.inflate(R.layout.layout_job_item, group, false);
				
				return new JobHolder(view);
			}

			@Override
			public void onError(FirebaseFirestoreException e) {
				Log.e("error", e.getMessage());
			}
		};
	}

	public class JobHolder extends RecyclerView.ViewHolder {

		public TextView user_name, user_career, job_title, job_desc, job_location, job_salary, more;
		public CircleImageView user_image;
		public ImageView big_image;
		public View apply, edit, applications;

		public JobHolder(View itemView) {
			super(itemView);
			user_name = itemView.findViewById(R.id.user_name);
			user_career = itemView.findViewById(R.id.user_career);
			user_image = itemView.findViewById(R.id.user_image);
			job_desc = itemView.findViewById(R.id.job_desc);
			big_image = itemView.findViewById(R.id.big_image);
			job_title = itemView.findViewById(R.id.job_title);
			job_location = itemView.findViewById(R.id.location);
			job_salary = itemView.findViewById(R.id.salary);
			apply = itemView.findViewById(R.id.apply);
			more = itemView.findViewById(R.id.more);
			edit = itemView.findViewById(R.id.edit);
			applications = itemView.findViewById(R.id.applications);
		}

		public void setUser(User user, String image) {
			user_name.setText(user.getUsername());
			user_career.setText(user.getCareer());
			Glide.with(getContext()).load(image).into(user_image);
		}

		public void setJob(Job job, String jobimg) {
			job_desc.setText(job.getDesc());
			job_location.setText(job.getLocation());
			job_title.setText(job.getTitle());
			job_salary.setText(job.getSalary()+" RWF");
			Glide.with(getContext()).load(jobimg).into(big_image);

			if (job.getOwner().equals(authManager.getCurrentUID())){
				//mine
				edit.setVisibility(View.VISIBLE);
				apply.setVisibility(View.GONE);//hiding apply btn
			}else {
				//!mine
				edit.setVisibility(View.GONE);
				apply.setVisibility(View.VISIBLE);//showing apply button
			}
		}

	}

	@Override
	public void onStart() {
		super.onStart();
		adapter.startListening();
	}

	@Override
	public void onStop() {
		super.onStop();
		adapter.stopListening();
	}
}
