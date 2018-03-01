package com.example.prince.jobhunt.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.prince.jobhunt.R;
import com.example.prince.jobhunt.engine.AuthManager;
import com.example.prince.jobhunt.engine.Constants;
import com.example.prince.jobhunt.engine.DialogHelper;
import com.example.prince.jobhunt.engine.FirebaseAgent;
import com.example.prince.jobhunt.model.Application;
import com.example.prince.jobhunt.model.Job;
import com.example.prince.jobhunt.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
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


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_job_details);
		ButterKnife.bind(this);
		agent = new FirebaseAgent(this);
		dialogHelper = new DialogHelper(this);
		firestore = FirebaseFirestore.getInstance();
		authManager = new AuthManager(this);

		setupViews();
	}

	public void setupViews(){
		String id = getIntent().getStringExtra("job_id");
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

				agent.downloadImage(job.getOwner() + job.getTitle(), Constants.JOB_IMAGE_PATH, new FirebaseAgent.OnImageDownload() {
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
		final ProgressDialog progress = new ProgressDialog(this);
		progress.setMessage("Applying to "+user_name.getText().toString());
		progress.show();
		//apply
		applicationRef = firestore.collection("application");

		final Application application = new Application();
		application.setDesc("application description");
		application.setUid(authManager.getCurrentUID());
		application.setSalary(String.valueOf(salary.getText().toString()));

		applicationRef
				.add(application)
				.addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
					@Override
					public void onSuccess(DocumentReference documentReference) {
						progress.dismiss();
						dialogHelper.showSimpleDialog("Job Application", "Job Application sent to "+user_name.getText().toString(), "VIEW", "CLOSE");
					}
				}).addOnFailureListener(new OnFailureListener() {
			@Override
			public void onFailure(@NonNull Exception e) {
				progress.dismiss();
				Toast.makeText(JobDetails.this, e.getMessage(), Toast.LENGTH_SHORT).show();
			}
		});
	}
}
