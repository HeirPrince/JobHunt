package com.example.prince.jobhunt.engine;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.example.prince.jobhunt.engine.listeners.OnObjectExitListener;
import com.example.prince.jobhunt.engine.listeners.OnProfileCreatedListener;
import com.example.prince.jobhunt.model.ImageItem;
import com.example.prince.jobhunt.model.Job;
import com.example.prince.jobhunt.model.Loc;
import com.example.prince.jobhunt.model.Notyfication;
import com.example.prince.jobhunt.model.Rating;
import com.example.prince.jobhunt.model.User;
import com.example.prince.jobhunt.model.View_job;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Prince on 2/13/2018.
 */

public class FirebaseAgent {

	public static final String TAG = FirebaseAgent.class.getSimpleName();

	private static FirebaseAgent instance;

	public Context context;
	private FirebaseFirestore firestore;
	private FirebaseAuth auth;
	private FirebaseUser active;
	private StorageReference userRef;
	private AuthManager authManager;
	private StorageAgent storageAgent;
	private TimeUtils timeUtils;

	public static FirebaseAgent getInstance(Context context) {
		if (instance == null) {
			instance = new FirebaseAgent(context);
		}

		return instance;
	}

	public FirebaseAgent(Context context) {
		this.context = context;
		this.storageAgent = new StorageAgent(context);
	}

	public void init() {
		firestore = FirebaseFirestore.getInstance();
		auth = FirebaseAuth.getInstance();
		active = auth.getCurrentUser();
		userRef = FirebaseStorage.getInstance().getReferenceFromUrl(Constants.STORAGE_PATH);
		authManager = new AuthManager();
		timeUtils = new TimeUtils();
	}

	//adding a new user to database
	public void addUser(final User user, final OnProfileCreatedListener onProfileCreatedListener) {

		final ProgressDialog dialog = new ProgressDialog(context);
		dialog.setTitle("Wait a moment...");
		dialog.setMessage("Uploading image...");
		dialog.show();

		firestore.collection("users").document(active.getUid())
				.set(user)
				.addOnSuccessListener(new OnSuccessListener<Void>() {
					@Override
					public void onSuccess(Void aVoid) {
						dialog.dismiss();
						onProfileCreatedListener.createdProfile(true);
					}
				}).addOnFailureListener(new OnFailureListener() {
			@Override
			public void onFailure(@NonNull Exception e) {
				dialog.dismiss();
			}
		});


	}

	//adding a new user to database
	public void addJob(final Job job, final Uri image, final List<ImageItem> imageItems) {
		if (job != null) {
			final ProgressDialog dialog = new ProgressDialog(context);
			dialog.setTitle("Wait a moment...");
			dialog.setMessage("Posting job...");
			dialog.setCancelable(false);
			dialog.show();

			firestore.collection("jobs").document("category").collection("area")
					.add(job)
					.addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
						@Override
						public void onSuccess(DocumentReference documentReference) {
							dialog.dismiss();
							String id = documentReference.getId();
//							storageAgent.uploadImage(id, image);
//							storageAgent.uploadMultipleImages(Constants.IMAGE_PATH_JOBS_MAIN, id, imageItems);
//							firstView(id);
						}
					}).addOnFailureListener(new OnFailureListener() {
				@Override
				public void onFailure(@NonNull Exception e) {
					Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
				}
			});
		}

	}

	public void addImagetoDB(final List<ImageItem> imageItems, final String id, final Uri file) {

		final Map<String, Object> images = new HashMap<>();
		for (int i = 0; i < imageItems.size(); i++) {
			images.put(imageItems.get(i).getName(), imageItems.get(i).getName());
		}

		if (imageItems != null) {
			//TODO show dialog to ask
			//if yes
			Map<String, String> big_image_map = new HashMap<>();
			big_image_map.put(id, id);
			for (final ImageItem item : imageItems) {
				firestore.collection("images").document("jobs").collection(id).document("main")
						.set(big_image_map)
						.addOnCompleteListener(new OnCompleteListener<Void>() {
							@Override
							public void onComplete(@NonNull Task<Void> task) {
//								if (item != null) {
//									firestore.collection("images").document("jobs").collection(id).document("others")
//											.set(item)
//											.addOnSuccessListener(new OnSuccessListener<Void>() {
//												@Override
//												public void onSuccess(Void aVoid) {
//													addImagetoStorage(imageItems, id, file);
//												}
//											}).addOnFailureListener(new OnFailureListener() {
//										@Override
//										public void onFailure(@NonNull Exception e) {
//											Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
//										}
//									});
//								} else {
//									//save no images
//								}
							}
						})
						.addOnFailureListener(new OnFailureListener() {
							@Override
							public void onFailure(@NonNull Exception e) {
								//TODO handle job not saved
							}
						});
			}


			//if no
		}
	}

	public void addImagetoStorage(final List<ImageItem> imageItems, String id, Uri img) {
		final StorageReference imageRefothers = userRef.child(Constants.IMAGE_PATH_JOBS_OTHERS),
				imageRefmain = userRef.child(Constants.IMAGE_PATH_JOBS_MAIN);

		imageRefmain.child(id).putFile(img)
				.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
					@Override
					public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
						if (task.isSuccessful()) {
							for (int i = 0; i < imageItems.size(); i++) {
								ImageItem item = imageItems.get(i);
								imageRefothers.child(item.getName()).putFile(item.getFileName())//TODO change path to child('job_id/image_name')
										.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
											@Override
											public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
												Toast.makeText(context, "done", Toast.LENGTH_SHORT).show();
											}
										}).addOnFailureListener(new OnFailureListener() {
									@Override
									public void onFailure(@NonNull Exception e) {
										Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
									}
								});
							}
						}
					}
				}).addOnFailureListener(new OnFailureListener() {
			@Override
			public void onFailure(@NonNull Exception e) {
				Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
			}
		});


	}

	public interface notifStatus {
		void isNotifSent(Boolean status);
	}

	public void sendNotification(final Notyfication notification, final Uri file, final notifStatus callback) {

		final StorageReference cvRef = userRef.child(Constants.STORAGE_CV_PATH);

		firestore.collection("notifications")
				.add(notification)
				.addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
					@Override
					public void onComplete(@NonNull Task<DocumentReference> task) {
						if (task.isSuccessful()) {
							cvRef.child(notification.getReceiver()).putFile(file)
									.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
										@Override
										public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
											callback.isNotifSent(true);
										}
									}).addOnFailureListener(new OnFailureListener() {
								@Override
								public void onFailure(@NonNull Exception e) {
									callback.isNotifSent(false);
								}
							});
						} else {
							callback.isNotifSent(false);
						}
					}
				}).addOnFailureListener(new OnFailureListener() {
			@Override
			public void onFailure(@NonNull Exception e) {
				callback.isNotifSent(false);
			}
		});


	}

	//update username
	public interface isUsernameUpdated {
		void isUpdated(Boolean status);
	}

	public void updateUsername(String uid, String username, final isUsernameUpdated callback) {
		DocumentReference docRef = firestore.collection("users").document(uid);

		docRef.update("username", username)
				.addOnCompleteListener(new OnCompleteListener<Void>() {
					@Override
					public void onComplete(@NonNull Task<Void> task) {
						callback.isUpdated(true);
					}
				}).addOnFailureListener(new OnFailureListener() {
			@Override
			public void onFailure(@NonNull Exception e) {
				callback.isUpdated(false);
			}
		});
	}

	//update image
	public interface isImageUpdated {
		void isUpdated(Boolean status);

		void progress(int progress);
	}

	public void updateImage(final String uid, final Uri newFile, final isImageUpdated callback) {
		final DocumentReference docRef = firestore.collection("users").document(uid);

		userRef.child(Constants.USER_PROFILE_IMAGE_PATH).child(uid).delete()
				.addOnCompleteListener(new OnCompleteListener<Void>() {
					@Override
					public void onComplete(@NonNull final Task<Void> task) {
						if (task.isSuccessful()) {
							StorageReference profileRef = userRef.child(Constants.USER_PROFILE_IMAGE_PATH).child(uid);

							profileRef.putFile(newFile)
									.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
										@Override
										public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
											if (taskSnapshot != null) {
												docRef.update("image", uid)
														.addOnCompleteListener(new OnCompleteListener<Void>() {
															@Override
															public void onComplete(@NonNull Task<Void> task) {
																if (task.isSuccessful()) {
																	callback.isUpdated(true);
																}
															}
														}).addOnFailureListener(new OnFailureListener() {
													@Override
													public void onFailure(@NonNull Exception e) {
														callback.isUpdated(false);
													}
												});
											}
										}
									}).addOnFailureListener(new OnFailureListener() {
								@Override
								public void onFailure(@NonNull Exception e) {
									callback.isUpdated(false);
								}
							}).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
								@Override
								public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
									double p = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
									callback.progress((int) p);
								}
							});
						}
					}
				}).addOnFailureListener(new OnFailureListener() {
			@Override
			public void onFailure(@NonNull Exception e) {
				callback.isUpdated(false);
			}
		});
	}

	//upload image
	public interface uploadListener {
		void isUploaded(Boolean state);
	}


	public void uploadImage(String path, Uri file, final uploadListener callback) {
		StorageReference jobRef = userRef.child(path).child(authManager.getCurrentUID());

		jobRef.putFile(file)
				.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
					@Override
					public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
						if (taskSnapshot != null) {
							callback.isUploaded(true);
						} else {
							callback.isUploaded(false);
						}
					}
				}).addOnFailureListener(new OnFailureListener() {
			@Override
			public void onFailure(@NonNull Exception e) {
				callback.isUploaded(false);
			}
		});
	}

	//checking if a user is registered
	public interface isUserRegistered {
		void isRegistered(Boolean status, DocumentSnapshot ds);
	}

	public void checkIfUserExists(final OnObjectExitListener callback) {
		DocumentReference userRef = firestore.collection("users").document(authManager.getCurrentUID());
		userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
			@Override
			public void onComplete(@NonNull Task<DocumentSnapshot> task) {
				if (task.isSuccessful()) {
					DocumentSnapshot ds = task.getResult();
					if (ds.exists()) {
						callback.onDataChanged(true);
					} else {
						callback.onDataChanged(false);
					}
				} else {
					callback.onDataChanged(false);
				}
			}
		}).addOnFailureListener(new OnFailureListener() {
			@Override
			public void onFailure(@NonNull Exception e) {
				callback.onDataChanged(false);
			}
		});
	}


	//get user by uid(identification string of a user in firebase
	public interface getUser {
		void gottenUser(User user);
	}

	public void getUserByUID(String uid, final getUser callback) {
		final DocumentReference userRef = firestore.collection("users").document(uid);
		userRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
			@Override
			public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {

				if (e != null) {
					Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
				}

				if (documentSnapshot != null) {
					User user = documentSnapshot.toObject(User.class);
					callback.gottenUser(user);
				} else {
					callback.gottenUser(null);
				}
			}
		});
	}

	//download image
	public interface OnImageDownload {
		void isDownloaded(Boolean status, String url);
	}

	public void downloadImage(String id, String path, final OnImageDownload callback) {
		if (path != null && id != null) {
			userRef.child(path).child(id).getDownloadUrl()
					.addOnCompleteListener(new OnCompleteListener<Uri>() {
						@Override
						public void onComplete(@NonNull Task<Uri> task) {
							if (task.isSuccessful()) {
								callback.isDownloaded(true, task.getResult().toString());
							} else {
								callback.isDownloaded(false, null);
							}
						}
					}).addOnFailureListener(new OnFailureListener() {
				@Override
				public void onFailure(@NonNull Exception e) {
					callback.isDownloaded(false, null);
				}
			});
		} else
			Toast.makeText(context, "null parameters", Toast.LENGTH_SHORT).show();
	}


	public interface getJob {
		void job(Job job);
	}

	public void getJobByID(String id, final getJob callback) {
		DocumentReference jobs = firestore.collection("jobs").document("category").collection("area").document(id);

		jobs.addSnapshotListener(new EventListener<DocumentSnapshot>() {
			@Override
			public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
				if (e != null && documentSnapshot.exists()) {
					Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
				}

				if (documentSnapshot != null) {
					Job job = documentSnapshot.toObject(Job.class);
					callback.job(job);
				} else {
					callback.job(null);
				}
			}
		});
	}

	//get job list
	public interface jobList {
		void All(List<Job> jobs, List<String> ids);
	}

	public void getJobList(final jobList callback) {
		final List<Job> jobs = new ArrayList<>();
		final List<String> ids = new ArrayList<>();

		firestore.collection("jobs").document("category").collection("area")
				.addSnapshotListener(new EventListener<QuerySnapshot>() {
					@Override
					public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
						if (e != null)
							return;

						for (DocumentChange change : documentSnapshots.getDocumentChanges()) {
							if (change == null) {
								Toast.makeText(context, "no jobs found", Toast.LENGTH_SHORT).show();
							}


							Job job = change.getDocument().toObject(Job.class);
							jobs.add(job);
							ids.add(change.getDocument().getId());
							callback.All(jobs, ids);
						}
					}
				});
	}

	public interface jobViewed {
		void isViewed(Boolean state);
	}

	public void viewJob(final String id, final jobViewed callback) {//Once per user
		firestore.collection(Constants.DB_COLLECTION_TRANSACTIONS).document(id)
				.get()
				.addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
					@Override
					public void onComplete(@NonNull Task<DocumentSnapshot> task) {
						if (task.isSuccessful()) {
							DocumentSnapshot snapshot = task.getResult();
							View_job view_job = snapshot.toObject(View_job.class);
							Toast.makeText(context, String.valueOf(view_job.getViewed()), Toast.LENGTH_SHORT).show();
						}
					}
				})
				.addOnFailureListener(new OnFailureListener() {
					@Override
					public void onFailure(@NonNull Exception e) {

					}
				});
	}

	//add view
	public void firstView(String id) {
		double v = 0;
		View_job view_job = new View_job();
		view_job.setViewed(true);
		view_job.setUid(authManager.getCurrentUID());
		view_job.setId(id);

		firestore.collection(Constants.DB_COLLECTION_TRANSACTIONS)
				.add(view_job)
				.addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
					@Override
					public void onComplete(@NonNull Task<DocumentReference> task) {
						Toast.makeText(context, "viewed", Toast.LENGTH_SHORT).show();
					}
				});
	}

	//nm Views
	public interface Nm_Views {
		void totalNumber(double number);
	}


	//duty status
	public interface dutyStatusListener {
		void isOnDuty(Boolean working);
	}

	public void getDutyStatus(final dutyStatusListener callback) {
		firestore.collection("duty")
				.addSnapshotListener(new EventListener<QuerySnapshot>() {
					@Override
					public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
						if (e != null)
							return;

						for (DocumentSnapshot snapshot : documentSnapshots) {
							if (snapshot != null) {
								if (snapshot.getBoolean(authManager.getCurrentUID()) != null) {
									callback.isOnDuty(true);
								} else {
									callback.isOnDuty(false);
									setDutyStatus(false);
								}
							} else {
								callback.isOnDuty(false);
							}
						}

					}
				});
	}

	public void setDutyStatus(final Boolean status) {
		Map<String, Boolean> duty = new HashMap<>();
		duty.put(authManager.getCurrentUID(), status);
		firestore.collection("duty").document(authManager.getCurrentUID()).set(duty);
	}

	public interface Counts {
		void jobCount(int count);
	}

	public void getCounts(final Counts callback) {

		//job jobCount
		firestore.collection("jobs").document("category").collection("area")
				.addSnapshotListener(new EventListener<QuerySnapshot>() {
					@Override
					public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
						if (e != null)
							Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();

						int count = 0;
						for (DocumentSnapshot snapshot : documentSnapshots) {
							count++;
							callback.jobCount(count);
						}

					}
				});


		//application jobCount

	}

	//raring
	public interface Review {
		void review(Rating rating);
	}

	public void getRating(final Review callcack) {
		firestore.collection("rating").document(authManager.getCurrentUID())
				.addSnapshotListener(new EventListener<DocumentSnapshot>() {
					@Override
					public void onEvent(DocumentSnapshot snapshot, FirebaseFirestoreException e) {
						if (e != null)
							return;


						Rating rating = snapshot.toObject(Rating.class);
						callcack.review(rating);
					}
				});
	}


	public void updateUserLocation(Loc latLng) {
		if (latLng == null) {
			Toast.makeText(context, "location not being found", Toast.LENGTH_SHORT).show();
		}

		firestore.collection("location").document(latLng.getUid())
				.set(latLng)
				.addOnCompleteListener(new OnCompleteListener<Void>() {
					@Override
					public void onComplete(@NonNull Task<Void> task) {
						if (task.isSuccessful()) {
							Toast.makeText(context, "location updated", Toast.LENGTH_SHORT).show();
						}
					}
				}).addOnFailureListener(new OnFailureListener() {
			@Override
			public void onFailure(@NonNull Exception e) {
				Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
			}
		});
	}

	public interface imLocatedAt {
		void location(Loc latLng);
	}

	public void getCurrentLocation(final String uid, final imLocatedAt callback) {
		DocumentReference location = firestore.collection("location").document(uid);
		location.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
			@Override
			public void onComplete(@NonNull Task<DocumentSnapshot> task) {
				if (task.isSuccessful()) {
					DocumentSnapshot snapshot = task.getResult();
					if (snapshot.exists()) {
						Loc loc = snapshot.toObject(Loc.class);
						callback.location(loc);
					}
				} else {
					callback.location(null);
				}
			}
		}).addOnFailureListener(new OnFailureListener() {
			@Override
			public void onFailure(@NonNull Exception e) {
				callback.location(null);
			}
		});

	}

	public interface getAllJobIds {
		void jobIds(List<String> ids);
	}

}

