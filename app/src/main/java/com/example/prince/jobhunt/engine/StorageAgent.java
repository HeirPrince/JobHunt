package com.example.prince.jobhunt.engine;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.example.prince.jobhunt.engine.listeners.OnImageUploadedListener;
import com.example.prince.jobhunt.model.ImageItem;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.List;

/**
 * Created by Prince on 4/12/2018.
 */

public class StorageAgent {

	public static final String TAG = StorageAgent.class.getSimpleName();
	public static  StorageAgent instance;

	private Context ctx;
	private StorageReference storageReference;

	public StorageAgent(Context ctx) {
		this.ctx = ctx;

	}

	public static StorageAgent getInstance(Context context){
		if (instance ==  null)
			return new StorageAgent(context);

		return instance;
	}

	public void init() {

	}

	public StorageReference getStorageReference(){
		storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(Constants.STORAGE_PATH);
		return storageReference;
	}

	public void uploadImage(String id, Uri file, final OnImageUploadedListener onImageUploadedListener) {

		UploadTask uploadTask = uploadUserImage(file, id);

		if (uploadTask != null){
			uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
				@Override
				public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
					if (task.isSuccessful()){
						Uri file = task.getResult().getDownloadUrl();
						onImageUploadedListener.onImageUploaded(true, String.valueOf(file));
					}else {
						onImageUploadedListener.onImageUploaded(false, null);
					}
				}
			}).addOnFailureListener(new OnFailureListener() {
				@Override
				public void onFailure(@NonNull Exception e) {
					onImageUploadedListener.onImageUploaded(false, null);
				}
			}).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
				@Override
				public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
					double p = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
					onImageUploadedListener.progress(p);
				}
			});
		}
	}

	public void uploadMultipleImages(String id, List<ImageItem> imageItems){
		for (ImageItem item : imageItems){
			if (item != null){
				uploadImage(id, item.getFileName(), new OnImageUploadedListener() {
					@Override
					public void onImageUploaded(boolean state, String url) {
						if (state && url != null){
							Toast.makeText(ctx, "image uploaded url : "+url , Toast.LENGTH_SHORT).show();
						}else {
							Toast.makeText(ctx, "image upload failed", Toast.LENGTH_SHORT).show();
						}
					}

					@Override
					public void progress(double progress) {
						//track progress here
					}
				});
			}
		}
	}

	public UploadTask uploadUserImage(Uri uri, String imageTitle) {
		StorageReference storageRef = getStorageReference();
		StorageReference riversRef = storageRef.child(Constants.USER_PROFILE_IMAGE_PATH + imageTitle);
		// Create file metadata including the content type
		StorageMetadata metadata = new StorageMetadata.Builder()
				.setCacheControl("max-age=7776000, Expires=7776000, public, must-revalidate")
				.build();

		return riversRef.putFile(uri, metadata);
	}


}
