package com.example.prince.jobhunt.activities;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.prince.jobhunt.R;
import com.example.prince.jobhunt.engine.AuthManager;
import com.example.prince.jobhunt.engine.Constants;
import com.example.prince.jobhunt.engine.FirebaseAgent;
import com.example.prince.jobhunt.model.User;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class addWork extends AppCompatActivity {

	@BindView(R.id.toolbar_home)Toolbar toolbar;
	@BindView(R.id.profile_name)TextView profile_name;
	@BindView(R.id.profile_photo)CircleImageView profile_photo;
	@BindView(R.id.profile_career)TextView profile_carrer;
	@BindView(R.id.appbar)AppBarLayout appBarLayout;
	@BindView(R.id.work)EditText work;
	@BindView(R.id.work_desc)EditText work_desc;
	@BindView(R.id.category)MaterialSpinner wok_category;

	private AuthManager authManager;
	private FirebaseAgent agent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_work);
		ButterKnife.bind(this);
		setupToolbar();

		authManager = new AuthManager(this);
		agent = new FirebaseAgent(this);

		setupProfile();
	}

	public void setupToolbar(){
		setSupportActionBar(toolbar);
		//set padding

		// create our manager instance after the content view is set
		SystemBarTintManager tintManager = new SystemBarTintManager(this);
		// enable status bar tint
		tintManager.setStatusBarTintEnabled(true);
		// enable navigation bar tint
		tintManager.setNavigationBarTintEnabled(true);
	}

	public void setupProfile(){
		agent.getUserByUID(authManager.getCurrentUID(), new FirebaseAgent.getUser() {
			@Override
			public void gottenUser(User user) {
				profile_carrer.setText(user.getCareer());
				profile_name.setText(user.getUsername());
				agent.downloadImage(user.getUid(), Constants.USER_PROFILE_IMAGE_PATH, new FirebaseAgent.OnImageDownload() {
					@Override
					public void isDownloaded(Boolean status, String url) {
						Glide.with(addWork.this)
								.load(url)
								.into(profile_photo);
					}

					@Override
					public void isFailed(Boolean status) {

					}
				});
			}
		});
	}
}
