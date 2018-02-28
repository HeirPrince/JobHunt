package com.example.prince.jobhunt.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.prince.jobhunt.R;
import com.example.prince.jobhunt.engine.AuthManager;
import com.example.prince.jobhunt.engine.Constants;
import com.example.prince.jobhunt.engine.FirebaseAgent;
import com.example.prince.jobhunt.model.User;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class edit_Profile extends AppCompatActivity {

	public static final int GALLERY_CODE = 123;
	@BindView(R.id.profile_photo)CircleImageView profile_photo;
	@BindView(R.id.username)TextView usernameText;
	@BindView(R.id.edit_name)View edit_name;
	@BindView(R.id.toolbar_normal)Toolbar toolbar;
	@BindView(R.id.loadImage)View load;

	String str = "";
	Uri file;

	private FirebaseAgent agent;
	private AuthManager authManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit__profile);
		ButterKnife.bind(this);
		setSupportActionBar(toolbar);

		agent = new FirebaseAgent(this);
		authManager = new AuthManager(this);
		initViews();

		load.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				//load image from gallery
				getImage();
			}
		});

		edit_name.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				showInputDialog();
			}
		});

	}

	public void initViews(){
		agent.getUserByUID(authManager.getCurrentUID(), new FirebaseAgent.getUser() {
			@Override
			public void gottenUser(User user) {
				usernameText.setText(user.getUsername());
				String path = Constants.USER_PROFILE_IMAGE_PATH;
				agent.downloadImage(authManager.getCurrentUID(), path, new FirebaseAgent.OnImageDownload() {
					@Override
					public void isDownloaded(Boolean status, String url) {
						if (status && url != null) {
							Glide.with(edit_Profile.this)
									.load(url)
									.into(profile_photo);
						}
					}

					@Override
					public void isFailed(Boolean status) {
						Toast.makeText(edit_Profile.this, "image can't be downloaded", Toast.LENGTH_SHORT).show();
					}
				});
			}
		});
	}

	public void showInputDialog(){
		final AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Enter new name");

		//setup input
		final EditText editText = new EditText(this);
		//Specify input type
		editText.setInputType(InputType.TYPE_CLASS_TEXT);
		builder.setView(editText);

		//setup buttons
		builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialogInterface, int i) {
				str = editText.getText().toString();
				agent.updateUsername(authManager.getCurrentUID(), str, new FirebaseAgent.isUsernameUpdated() {
					@Override
					public void isUpdated(Boolean status) {
						if (status){
							Toast.makeText(edit_Profile.this, "username updated successfully", Toast.LENGTH_SHORT).show();
						}else {
							Toast.makeText(edit_Profile.this, "username couldn't be updated", Toast.LENGTH_SHORT).show();
						}
					}
				});
			}
		});

		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialogInterface, int i) {
				dialogInterface.cancel();
			}
		});

		builder.show();
	}

	//selecting image from gallery
	public void getImage(){
		Intent i = new Intent();
		i.setAction(Intent.ACTION_GET_CONTENT);
		i.setType("image/*");
		startActivityForResult(Intent.createChooser(i, "Select image"), GALLERY_CODE);
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
				Bitmap bmap;
				try {
					bmap = MediaStore.Images.Media.getBitmap(getContentResolver(), cropped);
					profile_photo.setImageBitmap(bmap);
					agent.updateImage(authManager.getCurrentUID(), cropped, new FirebaseAgent.isImageUpdated() {
						@Override
						public void isUpdated(Boolean status) {
							Toast.makeText(edit_Profile.this, "image uploaded", Toast.LENGTH_SHORT).show();
						}

						@Override
						public void progress(int progress) {
							Toast.makeText(edit_Profile.this, String.valueOf(progress), Toast.LENGTH_SHORT).show();
						}
					});
				} catch (IOException e) {
					Toast.makeText(this, "Image cant't be loaded", Toast.LENGTH_SHORT).show();
				}
			}
		}
	}

}
