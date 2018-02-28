package com.example.prince.jobhunt.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.prince.jobhunt.Adapters.UserAdapter;
import com.example.prince.jobhunt.R;
import com.example.prince.jobhunt.engine.AuthManager;
import com.example.prince.jobhunt.engine.FirebaseAgent;
import com.example.prince.jobhunt.model.User;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class Search extends Fragment {

	private RecyclerView userList;
	private FirebaseAgent agent;
	private AuthManager authManager;

	private FirebaseFirestore database;
	private ProgressBar progressBar;

	public Search() {
		// Required empty public constructor
		agent = new FirebaseAgent(getContext());
		authManager = new AuthManager(getContext());
		database = FirebaseFirestore.getInstance();
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View v = inflater.inflate(R.layout.fragment_search, container, false);

		userList = v.findViewById(R.id.list);
		progressBar = v.findViewById(R.id.progress);
		progressBar.setVisibility(View.VISIBLE);

		return v;
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if (isVisibleToUser)
			popList();
	}

	public void popList(){
		final List<User> users = new ArrayList<>();
		database.collection("users").addSnapshotListener(new EventListener<QuerySnapshot>() {
			@Override
			public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
				if (e != null){
					//error
					progressBar.setVisibility(View.GONE);
				}

				for (DocumentChange doc : documentSnapshots.getDocumentChanges()){
					progressBar.setVisibility(View.GONE);
					User user = doc.getDocument().toObject(User.class);
					users.add(user);
				}

			}
		});

		UserAdapter adapter = new UserAdapter(users, getContext());
		userList.setAdapter(adapter);
	}

	public class UserHolder extends RecyclerView.ViewHolder {

		private TextView user_name, user_career;
		private CircleImageView user_photo;

		public UserHolder(View itemView) {
			super(itemView);
			user_career = itemView.findViewById(R.id.user_career);
			user_name = itemView.findViewById(R.id.user_name);
			user_photo = itemView.findViewById(R.id.user_photo);
		}

		public void setData(String url, String name, String career){
			user_name.setText(name);
			user_career.setText(career);
			Glide.with(getContext())
					.load(url)
					.into(user_photo);
		}

	}

}
