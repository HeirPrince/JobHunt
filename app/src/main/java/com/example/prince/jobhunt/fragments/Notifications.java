package com.example.prince.jobhunt.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.prince.jobhunt.R;
import com.example.prince.jobhunt.activities.Home;
import com.example.prince.jobhunt.activities.JobDetails;
import com.example.prince.jobhunt.activities.ViewInstructions;
import com.example.prince.jobhunt.engine.AuthManager;
import com.example.prince.jobhunt.engine.Constants;
import com.example.prince.jobhunt.engine.FirebaseAgent;
import com.example.prince.jobhunt.engine.onSearchQueryListener;
import com.example.prince.jobhunt.model.Notyfication;
import com.example.prince.jobhunt.model.User;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class Notifications extends Fragment {

	private int page;
	private String title;

	public static final String TAG = "Applicatons";

	private FirebaseFirestore database;
	private ProgressBar progressBar;
	private RecyclerView appList;
	private FirebaseAgent agnt;
	private FirestoreRecyclerAdapter adapter;
	private FirestoreRecyclerAdapter searchAdapter;
	private FloatingActionButton fab;
	private AuthManager authManager;

	public static Notifications newInstance(int page, String title) {
		Notifications apps = new Notifications();
		Bundle args = new Bundle();
		args.putInt("someInt", page);
		args.putString("someTitle", title);
		return apps;
	}


	public Notifications() {
		// Required empty public constructor
		database = FirebaseFirestore.getInstance();
		agnt = new FirebaseAgent(getContext());
		authManager = new AuthManager();
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View v = inflater.inflate(R.layout.fragment_workers, container, false);
		appList = v.findViewById(R.id.app_list);
		fab = v.findViewById(R.id.fabApp);
		progressBar = v.findViewById(R.id.progress_bar);
		progressBar.setVisibility(View.VISIBLE);
		appList.setLayoutManager(new LinearLayoutManager(getContext()));
		appList.setHasFixedSize(true);
		adapter = fui();
		appList.setAdapter(adapter);

		appList.addOnScrollListener(new RecyclerView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
				super.onScrollStateChanged(recyclerView, newState);
				if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
					// Hiding FAB
					((Home) getActivity()).hideFloatingActionButton();
				} else if (newState == RecyclerView.SCROLL_STATE_IDLE) {
					// Showing FAB
					((Home) getActivity()).showFloatingActionButton();
				}

			}

			@Override
			public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
				super.onScrolled(recyclerView, dx, dy);
			}
		});

		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				startActivity(new Intent(getContext(), JobDetails.class));
			}
		});

		((Home) getActivity()).onSearch(new onSearchQueryListener() {
			@Override
			public void query(String query) {

			}
		});

		return v;
	}

	public FirestoreRecyclerAdapter fui() {
		Query query = database.collection("notifications");


		FirestoreRecyclerOptions<Notyfication> response = new FirestoreRecyclerOptions.Builder<Notyfication>()
				.setQuery(query, Notyfication.class)
				.build();


		return new FirestoreRecyclerAdapter<Notyfication, NotificationHolder>(response) {
			@Override
			public NotificationHolder onCreateViewHolder(ViewGroup parent, int viewType) {
				View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_notification_item, parent, false);
				return new NotificationHolder(v);
			}

			@Override
			protected void onBindViewHolder(@NonNull final NotificationHolder holder, int position, @NonNull final Notyfication model) {
				progressBar.setVisibility(View.GONE);
				agnt.getUserByUID(authManager.getCurrentUID(), new FirebaseAgent.getUser() {
					@Override
					public void gottenUser(final User user) {
						agnt.downloadImage(authManager.getCurrentUID(), Constants.USER_PROFILE_IMAGE_PATH, new FirebaseAgent.OnImageDownload() {
							@Override
							public void isDownloaded(Boolean status, String url) {
								holder.setUser(user.getUsername(), user.getCareer(), url);
								holder.setNotif(model.getMessage());
							}
						});
					}
				});

				holder.itemView.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						//go ahead with the job
						startActivity(new Intent(getContext(), ViewInstructions.class));
					}
				});

			}
		};

	}

	public class NotificationHolder extends RecyclerView.ViewHolder {

		public TextView user_name, user_career, message;
		public CircleImageView user_profile;

		public NotificationHolder(View itemView) {
			super(itemView);
			user_name = itemView.findViewById(R.id.user_name);
			user_career = itemView.findViewById(R.id.user_career);
			message = itemView.findViewById(R.id.message);
			user_profile = itemView.findViewById(R.id.user_image);
		}

		public void setUser(String name, String career, String url){
			user_name.setText(name);
			user_career.setText(career);
			Glide.with(getContext())
					.load(url)
					.into(user_profile);
		}

		public void setNotif(String msg){
			message.setText(msg);
		}
	}

	@Override
	public void onStart() {
		super.onStart();
		adapter.startListening();
	}

	@Override
	public void onStop() {
		super.onStop();
		adapter.stopListening();
	}
}
