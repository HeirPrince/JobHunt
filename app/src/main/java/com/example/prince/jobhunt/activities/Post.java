package com.example.prince.jobhunt.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.prince.jobhunt.R;
import com.example.prince.jobhunt.engine.FirebaseAgent;
import com.example.prince.jobhunt.model.Job;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Post extends AppCompatActivity {

	public static final int GALLERY_CODE = 123;

	@BindView(R.id.toolbar_normal)Toolbar toolbar;
	@BindView(R.id.title)EditText postTitle;
	@BindView(R.id.category)MaterialSpinner postCategory;
	@BindView(R.id.salary)EditText postSalary;
	@BindView(R.id.salary_type)MaterialSpinner postSalaryType;
	@BindView(R.id.address)EditText postLocation;
	@BindView(R.id.desc)EditText postDescription;
	@BindView(R.id.img)ImageView postImage;

	private FirebaseFirestore firestore;
	private FirebaseAuth auth;
	private FirebaseUser user;
	private FirebaseAgent agent;

	private Uri file;
	private List<String> categories;
	private List<String> paymentTypes;
	private Job job;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_post);
		ButterKnife.bind(this);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		firestore = FirebaseFirestore.getInstance();
		auth = FirebaseAuth.getInstance();
		user = auth.getCurrentUser();
		agent = new FirebaseAgent(this);
		job = new Job();

		if (user != null) {
			setLists();
			postImage.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					getImage();
				}
			});
		}else {

		}
	}

	public void setLists(){
		categories = new ArrayList<>();//TODO create a class to hold categories
		categories.add("Engineer");
		categories.add("Android Programmer");
		categories.add("Arduino Programmer");

		paymentTypes = new ArrayList<>();
		paymentTypes.add("Hourly");
		paymentTypes.add("Daily");
		paymentTypes.add("Monthly");

		postCategory.setItems(categories);
		postSalaryType.setItems(paymentTypes);
	}

	public void addTODB(){
		job.setCategory(categories.get(postCategory.getSelectedIndex()));
		job.setOwner(user.getUid());
		job.setDesc(postDescription.getText().toString());
		job.setLocation(postLocation.getText().toString());
		job.setSalary(Integer.valueOf(postSalary.getText().toString()));
		job.setSalary_type(paymentTypes.get(postSalaryType.getSelectedIndex()));
		job.setTitle(postTitle.getText().toString());
		job.setImage(user.getUid()+job.getTitle());

		agent.addJob(user.getUid(), job, file);

	}

	//selecting image from gallery
	public void getImage(){
		Intent i = new Intent();
		i.setAction(Intent.ACTION_GET_CONTENT);
		i.setType("image/*");

		startActivityForResult(Intent.createChooser(i, "Select image"), GALLERY_CODE);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.job_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		switch (id){
			case R.id.job:
				addTODB();
		}


		return super.onOptionsItemSelected(item);
	}

	//getting activity result
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == GALLERY_CODE && resultCode == RESULT_OK && data != null && data.getData() != null){
			file = data.getData();
			//starting crop image activity
			CropImage.activity(file).start(this);
		}else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
			CropImage.ActivityResult result = CropImage.getActivityResult(data);
			if (resultCode == RESULT_OK){
				Uri cropped = result.getUri();//return cropped image
				file = cropped;
				Bitmap bmap;
				try {
					bmap = MediaStore.Images.Media.getBitmap(getContentResolver(), cropped);
					postImage.setImageBitmap(bmap);
				} catch (IOException e) {
					Toast.makeText(this, "Image cant't be loaded", Toast.LENGTH_SHORT).show();
				}
			}
		}
	}

}
