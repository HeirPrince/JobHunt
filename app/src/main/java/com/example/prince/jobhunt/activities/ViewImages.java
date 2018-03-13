package com.example.prince.jobhunt.activities;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prince.jobhunt.Adapters.UploadImagesAdapter;
import com.example.prince.jobhunt.R;
import com.example.prince.jobhunt.model.ImageItem;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ViewImages extends AppCompatActivity {

	public static final int REQUEST_CODE = 123;
	@BindView(R.id.toolbar_normal)Toolbar toolbar;
	@BindView(R.id.image_list)RecyclerView imageList;
	@BindView(R.id.count)TextView fileNumber;
	private List<ImageItem> fileNames;
	private List<ImageItem> doneList;
	private UploadImagesAdapter adapter;

	//firebase
	private FirebaseStorage storage;
	private FirebaseFirestore database;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_images);
		ButterKnife.bind(this);
		setSupportActionBar(toolbar);
		fileNames = new ArrayList<>();
		doneList = new ArrayList<>();
		adapter = new UploadImagesAdapter(this, fileNames, doneList);

		imageList.setLayoutManager(new GridLayoutManager(this, 2));
		imageList.setHasFixedSize(true);
		imageList.setAdapter(adapter);

		storage = FirebaseStorage.getInstance();
		database = FirebaseFirestore.getInstance();
	}

	public void uploadImage(View view) {
		Intent i = new Intent();
		i.setType("image/*");
		i.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
		i.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(i.createChooser(i, "Select Images"), REQUEST_CODE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_CODE && resultCode == RESULT_OK){
			if (data.getClipData() != null){
				int total = data.getClipData().getItemCount();
				fileNumber.setText("Total : "+String.valueOf(total));
				for (int i = 0; i < total; i++){

					Uri uri = data.getClipData().getItemAt(i).getUri();
					String fileName = getFileName(uri);
					ImageItem item = new ImageItem();
					item.setFileName(uri);
					item.setName(fileName);

					fileNames.add(item);
					adapter.notifyDataSetChanged();

				}

			}else {
				Toast.makeText(this, "false", Toast.LENGTH_SHORT).show();
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

	private void startUpload() {

		for (int i = 0; i <= fileNames.size(); i++){

		}
	}

	public void addImagesToDb(ImageItem item){
		database.collection("jobs").document("local").collection("jobs")
				.add(item)
				.addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
					@Override
					public void onSuccess(DocumentReference documentReference) {

					}
				}).addOnFailureListener(new OnFailureListener() {
			@Override
			public void onFailure(@NonNull Exception e) {

			}
		});
	}
}
