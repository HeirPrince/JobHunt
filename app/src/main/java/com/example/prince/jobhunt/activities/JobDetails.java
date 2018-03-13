package com.example.prince.jobhunt.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.prince.jobhunt.R;
import com.example.prince.jobhunt.engine.AuthManager;
import com.example.prince.jobhunt.engine.Constants;
import com.example.prince.jobhunt.engine.DialogHelper;
import com.example.prince.jobhunt.engine.FirebaseAgent;
import com.example.prince.jobhunt.model.Job;
import com.example.prince.jobhunt.model.User;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class JobDetails extends AppCompatActivity {

	@BindView(R.id.j_details)RelativeLayout layout;
	@BindView(R.id.user_name)TextView user_name;
	@BindView(R.id.profile_photo)CircleImageView pic;
	@BindView(R.id.user_career)TextView user_career;
	@BindView(R.id.salary) TextView salary;
	@BindView(R.id.title)TextView job_title;
	@BindView(R.id.cat)TextView job_category;
	@BindView(R.id.location)TextView job_location;
	@BindView(R.id.desc)TextView job_description;
	@BindView(R.id.job_image)ImageView imageView;

	private FirebaseFirestore firestore;
	private CollectionReference applicationRef;
	private FirebaseAgent agent;
	private AuthManager authManager;
	private DialogHelper dialogHelper;
	String id = "";


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_job_details);
		ButterKnife.bind(this);
		agent = new FirebaseAgent(this);
		dialogHelper = new DialogHelper(this);
		firestore = FirebaseFirestore.getInstance();
		authManager = new AuthManager(this);
		id = getIntent().getStringExtra("job_id");

		setupViews();
	}

	public void setupViews(){
		agent.getJobByID(id, new FirebaseAgent.getJob() {
			@Override
			public void job(final Job job) {
				//fetch user
				agent.getUserByUID(job.getOwner(), new FirebaseAgent.getUser() {
					@Override
					public void gottenUser(User user) {
						user_name.setText(user.getUsername());
						user_career.setText(user.getCareer());
						agent.downloadImage(job.getOwner(), Constants.USER_PROFILE_IMAGE_PATH, new FirebaseAgent.OnImageDownload() {
							@Override
							public void isDownloaded(Boolean status, String url) {
								Glide.with(JobDetails.this)
										.load(url)
										.into(pic);
							}

							@Override
							public void isFailed(Boolean status) {

							}
						});
					}
				});
				//end fetching user

				//fetch job details
				salary.setText(String.valueOf(job.getSalary()));
				job_title.setText(job.getTitle());
				job_description.setText(job.getDesc());
				job_location.setText(job.getLocation());
				job_category.setText(job.getCategory());

				agent.downloadImage(id, Constants.IMAGE_PATH_JOBS_MAIN, new FirebaseAgent.OnImageDownload() {
					@Override
					public void isDownloaded(Boolean status, String url) {
						if (status && url != null) {
							Glide.with(JobDetails.this)
									.load(url)
									.into(imageView);
						} else {
							//no image set default image
						}
					}

					@Override
					public void isFailed(Boolean status) {

					}
				});

			}
		});


	}

	public void applyJob(View view) {
		Intent i = new Intent(JobDetails.this, AddApplication.class);
		i.putExtra("job_owner", user_name.getText().toString());
		i.putExtra("job_category", user_name.getText().toString());
		i.putExtra("job_salary", user_name.getText().toString());
		i.putExtra("job_id", id);
		startActivity(i);
	}
}
