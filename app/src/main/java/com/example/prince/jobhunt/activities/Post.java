package com.example.prince.jobhunt.activities;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prince.jobhunt.R;
import com.example.prince.jobhunt.engine.AuthManager;
import com.example.prince.jobhunt.engine.FirebaseAgent;
import com.example.prince.jobhunt.engine.StorageService;
import com.example.prince.jobhunt.engine.TimeUtils;
import com.example.prince.jobhunt.model.ImageItem;
import com.example.prince.jobhunt.model.Job;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.theartofdev.edmodo.cropper.CropImage;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Post extends AppCompatActivity implements StorageService {

	public static final int GALLERY_CODE = 123;
	private static final int REQUEST_CODE = 12;

	@BindView(R.id.toolbar_home)Toolbar toolbar;
	@BindView(R.id.title)EditText postTitle;
	@BindView(R.id.category)MaterialSpinner postCategory;
	@BindView(R.id.salary)EditText postSalary;
	@BindView(R.id.salary_type)MaterialSpinner postSalaryType;
	@BindView(R.id.address)EditText postLocation;
	@BindView(R.id.desc)EditText postDescription;
	@BindView(R.id.count)TextView fileNumber;
	@BindView(R.id.big_img)ImageView big_img;

	private List<ImageItem> imageItems;

	private FirebaseFirestore firestore;
	private FirebaseAuth auth;
	private FirebaseUser user;
	private FirebaseAgent agent;
	private TimeUtils timeUtils;

	private Uri file, cropped;
	private List<String> categories;
	private List<String> paymentTypes;
	private Job job;
	private AuthManager authManager;


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
		imageItems = new ArrayList<>();
		timeUtils = new TimeUtils();
		authManager = new AuthManager();
		if (user != null) {
			setLists();
		}else {

		}
	}

	public void setLists(){
		categories = new ArrayList<>();//TODO create a class to hold categories
		categories.add("Engineer");
		categories.add("Android Programmer");
		categories.add("Arduino Programmer");
		categories.add("Designer");

		paymentTypes = new ArrayList<>();
		paymentTypes.add("Hourly");
		paymentTypes.add("Daily");
		paymentTypes.add("Monthly");

		postCategory.setItems(categories);
		postSalaryType.setItems(paymentTypes);

	}

	public void addTODB(){
		job.setCategory(categories.get(postCategory.getSelectedIndex()));
		job.setOwner(authManager.getCurrentUID());
		job.setDesc(postDescription.getText().toString());
		job.setLocation(postLocation.getText().toString());
		job.setSalary(Integer.valueOf(postSalary.getText().toString()));
		job.setSalary_type(paymentTypes.get(postSalaryType.getSelectedIndex()));
		job.setTitle(postTitle.getText().toString());
		job.setImage(getFileName(cropped));

		//add timestammp
		String timeStamp = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()) + "";
		job.setTimeStamp(timeStamp);

        //TODO ask, if not needed/forgot("pop up dialog to remind") just save them
		if (imageItems != null){
			agent.addJob(job, file, imageItems);
		}else {
			Toast.makeText(this, "add images first", Toast.LENGTH_SHORT).show();
		}
	}

	@Subscribe
	public void imageUploaded(Boolean state){
		if (state){
			Toast.makeText(this, "IMAGE UPLOADED", Toast.LENGTH_SHORT).show();
		}else {
			Toast.makeText(this, "UPLOAD FAILED", Toast.LENGTH_SHORT).show();
		}
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
			case R.id.save:
				addTODB();
				break;
		}
		return super.onOptionsItemSelected(item);
	}

//	//getting activity result
//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		if (requestCode == GALLERY_CODE && resultCode == RESULT_OK && data != null && data.getData() != null){
//			file = data.getData();
//			//starting crop image activity
//			CropImage.activity(file).start(this);
//		}else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
//			CropImage.ActivityResult result = CropImage.getActivityResult(data);
//			if (resultCode == RESULT_OK){
//				Uri cropped = result.getUri();//return cropped image
//				file = cropped;
//				Bitmap bmap;
//				try {
//					bmap = MediaStore.Images.Media.getBitmap(getContentResolver(), cropped);
////					postImage.setImageBitmap(bmap);
//				} catch (IOException e) {
//					Toast.makeText(this, "Image cant't be loaded", Toast.LENGTH_SHORT).show();
//				}
//			}
//		}
//	}

	public void loadImage(View view) {
		Intent i = new Intent();
		i.setType("image/*");
		i.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
		i.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(i.createChooser(i, "Select Images"), REQUEST_CODE);
	}

	public void loadImage2(View view) {
		Intent i = new Intent();
		i.setType("image/*");
		i.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(i.createChooser(i, "Select Images"), GALLERY_CODE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_CODE && resultCode == RESULT_OK){
			if (data.getClipData() != null){
				int total = data.getClipData().getItemCount();
				fileNumber.setText("Selected : "+String.valueOf(total));
				for (int i = 0; i < total; i++){

					Uri uri = data.getClipData().getItemAt(i).getUri();
					String fileName = getFileName(uri);

					//timestamp
					ImageItem item = new ImageItem();
					item.setFileName(uri);
					item.setName(fileName);
					item.setTimestamp(timeUtils.generateTimeStamp());

					imageItems.add(item);

				}

			}else {
				Toast.makeText(this, "false", Toast.LENGTH_SHORT).show();
			}
		}else if (requestCode == GALLERY_CODE && resultCode == RESULT_OK && data != null && data.getData() != null){
			file = data.getData();
			//starting crop image activity
			CropImage.activity(file).start(this);
		}else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
			CropImage.ActivityResult result = CropImage.getActivityResult(data);
			if (resultCode == RESULT_OK){
				cropped = result.getUri();//return cropped image
				Bitmap bmap;
				try {
					bmap = MediaStore.Images.Media.getBitmap(getContentResolver(), cropped);
					big_img.setImageBitmap(bmap);
				} catch (IOException e) {
					Toast.makeText(this, "Image cant't be loaded", Toast.LENGTH_SHORT).show();
				}
			}
		}
	}

	public String getFileName(Uri uri){
		String result = null;
		String scheme =uri.getScheme();
		if (scheme.equals("file")){
			result = uri.getLastPathSegment();
		}else if (scheme.contains("content")){
			Cursor c = MediaStore.Images.Media.query(getContentResolver(), uri, null, null, null);

			try {
				if (c != null && c.moveToFirst()){
					result = c.getString(c.getColumnIndex(OpenableColumns.DISPLAY_NAME));
				}
			}finally {
				c.close();
			}
		}

		if (result == null){
			result = uri.getPath();
			int x = result.lastIndexOf("%");
			if (x !=-1){
				result = result.substring(x+ 1);
			}
		}

		return result;
	}

	@Override
	public void uploadComplete(Boolean status) {
		if (status){
			Toast.makeText(this, "image uploaded successfully from interface", Toast.LENGTH_SHORT).show();
		}else
			Toast.makeText(this, "upload failed", Toast.LENGTH_SHORT).show();
	}

	@Override
	protected void onStart() {
		super.onStart();
		EventBus.getDefault().register(this);
	}

	@Override
	protected void onStop() {
		super.onStop();
		EventBus.getDefault().unregister(this);
	}
}
