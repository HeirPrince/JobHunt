package com.example.prince.jobhunt.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.prince.jobhunt.R;
import com.example.prince.jobhunt.engine.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class JobDetails extends AppCompatActivity {

	@BindView(R.id.j_details)RelativeLayout layout;
	@BindView(R.id.user_name)TextView user_name;
	@BindView(R.id.user_photo)CircleImageView pic;
	@BindView(R.id.user_career)TextView user_career;
	@BindView(R.id.salary) TextView salary;
	@BindView(R.id.big_image)ImageView imageView;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_job_details);
		ButterKnife.bind(this);
		layout.setPadding(0, Constants.getStatusBarHeight(this), 0 , 0);
	}
}
