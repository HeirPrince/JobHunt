package com.example.prince.jobhunt.engine;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.example.prince.jobhunt.model.ImageItem;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by Prince on 4/12/2018.
 */

public class StorageAgent {

	private Context ctx;
	private StorageReference storageReference;

	public StorageAgent(Context ctx) {
		this.ctx = ctx;
		this.storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(Constants.USER_PROFILE_PATH);
	}

	public void uploadImage(String path, String id, Uri file) {
		final StorageReference ref = storageReference.child(path);
		ref.child(id).putFile(file)
				.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
					@Override
					public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
						if (task.isSuccessful()){
							EventBus.getDefault().post(true);
						}else {
							EventBus.getDefault().post(false);
						}
					}
				})
				.addOnFailureListener(new OnFailureListener() {
					@Override
					public void onFailure(@NonNull Exception e) {
						EventBus.getDefault().post(false);
					}
				});
	}

	public void uploadMultipleImages(String path, String id, List<ImageItem> imageItems){
		for (ImageItem item : imageItems){
			if (item != null){
				uploadImage(path, id, item.getFileName());
			}
		}
	}

	public void deleteImage(){

	}

	public void deleteMultipleImages(List<ImageItem> imageItems){
		for (ImageItem item : imageItems){
			//logic
		}
	}

	public void updateImage(){

	}

	public void updateMultipleImages(List<ImageItem> imageItems){
		for (ImageItem item : imageItems){
			//logic
		}
	}

	public void uploadVideo(){

	}

	public void getImageStorage(String path, String name){
		//logic
	}

	public void downloadMultipleImages(String path, List<String> fileNames){
		for (String name : fileNames){
			//logic
		}
	}
}
