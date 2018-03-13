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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.prince.jobhunt.R;
import com.example.prince.jobhunt.engine.AuthManager;
import com.example.prince.jobhunt.engine.Constants;
import com.example.prince.jobhunt.engine.FirebaseAgent;
import com.example.prince.jobhunt.model.Application;
import com.example.prince.jobhunt.model.User;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class viewApplications extends AppCompatActivity {

	@BindView(R.id.toolbar_home)Toolbar toolbar;
	@BindView(R.id.app_list)RecyclerView app_list;
	@BindView(R.id.app_count)TextView app_count;
	@BindView(R.id.progress)ProgressBar progressBar;

	private FirebaseFirestore firestore;
	private FirestoreRecyclerAdapter adapter;
	private FirebaseAgent agent;
	private AuthManager authManager;

	private String job_id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_applications);
		ButterKnife.bind(this);
		setSupportActionBar(toolbar);

		firestore = FirebaseFirestore.getInstance();
		agent = new FirebaseAgent(this);
		authManager = new AuthManager(this);

		job_id = getIntent().getStringExtra("job_id");
		progressBar.setVisibility(View.VISIBLE);

		adapter = getApplications();
		app_list.setLayoutManager(new LinearLayoutManager(this));
		app_list.setHasFixedSize(true);
		app_list.setAdapter(adapter);
	}

	public FirestoreRecyclerAdapter getApplications(){

		Query query = firestore.collection("applications").document("category").collection(job_id);


		FirestoreRecyclerOptions<Application> response = new FirestoreRecyclerOptions.Builder<Application>()
				.setQuery(query, Application.class)
				.build();

		return new FirestoreRecyclerAdapter<Application, ApplicationHolder>(response) {
			@Override
			public void onBindViewHolder(final ApplicationHolder holder, int position, final Application model) {
				if (model.getJob_id().equals(job_id)){
					progressBar.setVisibility(View.GONE);
					agent.getUserByUID(model.getUid(), new FirebaseAgent.getUser() {
						@Override
						public void gottenUser(final User user) {
							if (user != null) {
								agent.downloadImage(user.getUid(), Constants.USER_PROFILE_IMAGE_PATH, new FirebaseAgent.OnImageDownload() {
									@Override
									public void isDownloaded(Boolean status, String url) {
										if (status && url != null) {
											holder.user_name.setText(user.getUsername());
											holder.user_career.setText(user.getCareer());
											holder.app_desc.setText(model.getDesc());
											holder.p_salary.setText("Est. "+model.getSalary()+" RWF");
											agent.downloadImage(model.getUid(), Constants.USER_PROFILE_IMAGE_PATH, new FirebaseAgent.OnImageDownload() {
												@Override
												public void isDownloaded(Boolean status, String url) {
													if (status && url != null) {
														Glide.with(viewApplications.this)
																.load(url)
																.into(holder.user_profile);
													}
												}

												@Override
												public void isFailed(Boolean status) {

												}
											});
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

					holder.accept.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View view) {
							Intent i = new Intent(viewApplications.this, Instructions.class);
							i.putExtra("job_id", model.getJob_id());
							i.putExtra("receiver", model.getUid());
							startActivity(i);
						}
					});

				}else {

				}

			}

			@Override
			public ApplicationHolder onCreateViewHolder(ViewGroup group, int i) {
				View view = LayoutInflater.from(group.getContext())
						.inflate(R.layout.layout_application_item, group, false);

				return new ApplicationHolder(view);
			}

			@Override
			public void onError(FirebaseFirestoreException e) {
				Log.e("error", e.getMessage());
			}
		};

	}

	public class ApplicationHolder extends RecyclerView.ViewHolder {

		public TextView user_name, user_career, app_desc, p_salary;
		public CircleImageView user_profile;
		public Button accept;

		public ApplicationHolder(View itemView) {
			super(itemView);
			user_name = itemView.findViewById(R.id.user_name);
			user_career = itemView.findViewById(R.id.user_career);
			app_desc = itemView.findViewById(R.id.desc);
			p_salary = itemView.findViewById(R.id.p_salary);
			accept = itemView.findViewById(R.id.accept);
			user_profile = itemView.findViewById(R.id.user_photo);
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
