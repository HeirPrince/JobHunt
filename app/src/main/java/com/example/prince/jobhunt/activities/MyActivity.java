package com.example.prince.jobhunt.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.prince.jobhunt.R;
import com.example.prince.jobhunt.engine.Constants;
import com.example.prince.jobhunt.engine.FirebaseAgent;
import com.example.prince.jobhunt.model.Job;
import com.example.prince.jobhunt.model.User;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class MyActivity extends AppCompatActivity {

	@BindView(R.id.toolbar_home)Toolbar toolbar;
	@BindView(R.id.job_list)RecyclerView job_list;
	@BindView(R.id.progress)ProgressBar progressBar;

	private FirestoreRecyclerAdapter adapter;
	private FirebaseFirestore database;
	private FirebaseAgent agnt;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_activity);
		ButterKnife.bind(this);
		setSupportActionBar(toolbar);
		getSupportActionBar().setTitle("My Activity");

		database = FirebaseFirestore.getInstance();
		agnt = new FirebaseAgent(this);

		job_list.setLayoutManager(new LinearLayoutManager(this));
		job_list.setHasFixedSize(true);
		adapter = getJobs();
		job_list.setAdapter(adapter);

		progressBar.setVisibility(View.VISIBLE);


	}

	private FirestoreRecyclerAdapter getJobs() {
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
					});

					holder.review.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View view) {
							startActivity(new Intent(MyActivity.this, Rate.class));
						}
					});




				}else {
					Toast.makeText(MyActivity.this, "no jobs found", Toast.LENGTH_SHORT).show();
				}

			}

			@Override
			public JobHolder onCreateViewHolder(ViewGroup group, int i) {
				View view = LayoutInflater.from(group.getContext())
						.inflate(R.layout.layout_job_item_mine, group, false);

				return new JobHolder(view);
			}

			@Override
			public void onError(FirebaseFirestoreException e) {
				Log.e("error", e.getMessage());
			}
		};



	}

	public class JobHolder extends RecyclerView.ViewHolder {

		TextView username, usercareer, salary;
		CircleImageView user_image;
		ImageView job_image;
		Button review;

		public JobHolder(View itemView) {
			super(itemView);
			username = itemView.findViewById(R.id.user_name);
			usercareer = itemView.findViewById(R.id.user_career);
			salary = itemView.findViewById(R.id.salary);
			user_image = itemView.findViewById(R.id.user_image);
			job_image = itemView.findViewById(R.id.job_image);
			review = itemView.findViewById(R.id.jobDone);
		}

		public void setUser(User user, String img){
			username.setText(user.getUsername());
			usercareer.setText(user.getCareer());
			Glide.with(MyActivity.this)
					.load(img)
					.into(user_image);
		}

		public void setJob(Job job, String img){
			salary.setText(String.valueOf(job.getSalary()));
			Glide.with(MyActivity.this)
					.load(img)
					.into(job_image);
		}

	}

	@Override
	protected void onStart() {
		super.onStart();
		adapter.startListening();
	}

	@Override
	protected void onStop() {
		super.onStop();
		adapter.stopListening();
	}
}
