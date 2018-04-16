package com.example.prince.jobhunt.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.prince.jobhunt.MoreImageAdapter;
import com.example.prince.jobhunt.R;
import com.example.prince.jobhunt.engine.AuthManager;
import com.example.prince.jobhunt.engine.Constants;
import com.example.prince.jobhunt.engine.FirebaseAgent;
import com.example.prince.jobhunt.model.ImageItem;
import com.example.prince.jobhunt.model.User;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class JobInfo extends AppCompatActivity {

	private FirebaseAgent agent;
	private AuthManager authManager;
	private MoreImageAdapter adapter;
	private FirebaseFirestore database;
	@BindView(R.id.toolbar)Toolbar toolbar;
	@BindView(R.id.toolbarImage)ImageView toolbarImage;
	@BindView(R.id.collapse)CollapsingToolbarLayout collapse;
	@BindView(R.id.progress)ProgressBar progressBar;
	@BindView(R.id.nested)NestedScrollView nestedScrollView;
	@BindView(R.id.job_title)TextView jobTitle;
	@BindView(R.id.job_desc)TextView jobDesc;
	@BindView(R.id.user_name)TextView user_name;
	@BindView(R.id.user_name2)TextView user_name2;
	@BindView(R.id.user_career)TextView user_career;
	@BindView(R.id.user_image)CircleImageView user_image;
	@BindView(R.id.contents)LinearLayout contents;
	@BindView(R.id.image_list)RecyclerView imageList;
	@BindView(R.id.timestamp)TextView timeStamp;
	@BindView(R.id.location)TextView job_location;
	@BindView(R.id.locate)View locate;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_job_info);
		ButterKnife.bind(this);
		agent = new FirebaseAgent(this);
		authManager = new AuthManager();
		database = FirebaseFirestore.getInstance();
		setSupportActionBar(toolbar);
		setViews();
	}


	public void setViews(){

		final String job_id = getIntent().getStringExtra("job_id");

		progressBar.setVisibility(View.VISIBLE);
		contents.setVisibility(View.GONE);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		collapse.setTitle(getIntent().getStringExtra("job_title"));
		collapse.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));

		agent.downloadImage(job_id, Constants.IMAGE_PATH_JOBS_MAIN, new FirebaseAgent.OnImageDownload() {
			@Override
			public void isDownloaded(Boolean status, String url) {
				if (status && url != null){
					progressBar.setVisibility(View.GONE);
					contents.setVisibility(View.VISIBLE);
					Glide.with(JobInfo.this).load(url).into(toolbarImage);
					jobTitle.setText(getIntent().getStringExtra("job_title"));
					jobDesc.setText(getIntent().getStringExtra("job_desc"));
					timeStamp.setText(getIntent().getStringExtra("job_timestamp"));
					job_location.setText(getIntent().getStringExtra("job_location"));
					loadMoreImages(job_id);
				}
			}
		});
		agent.getUserByUID(authManager.getCurrentUID(), new FirebaseAgent.getUser() {
			@Override
			public void gottenUser(final User user) {
				user_name.setText(user.getUsername());
				user_career.setText(user.getCareer());
				agent.downloadImage(user.getUid(), Constants.USER_PROFILE_IMAGE_PATH, new FirebaseAgent.OnImageDownload() {
					@Override
					public void isDownloaded(Boolean status, String url) {
						if (status && url != null){
							Glide.with(JobInfo.this).load(url).into(user_image);
						}
					}
				});
			}
		});

		locate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				startActivity(new Intent(JobInfo.this, MapsActivity.class));
			}
		});
	}


	//load more images
	public void loadMoreImages(String jobId){
		imageList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
		imageList.setHasFixedSize(true);

		DocumentReference otherRef = database.collection("images").document("jobs").collection(jobId).document("others");
		otherRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
			@Override
			public void onEvent(DocumentSnapshot snapshot, FirebaseFirestoreException e) {
				if (e == null)
					return;

				ImageItem item = snapshot.toObject(ImageItem.class);
				Toast.makeText(JobInfo.this, item.getName(), Toast.LENGTH_SHORT).show();
			}
		});
	}
}
