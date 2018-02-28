package com.example.prince.jobhunt.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.prince.jobhunt.R;
import com.example.prince.jobhunt.engine.AuthManager;
import com.example.prince.jobhunt.engine.FirebaseAgent;
import com.example.prince.jobhunt.model.User;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Prince on 2/23/2018.
 */

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder>{

	private List<User> users;
	private Context context;
	private FirebaseAgent agent;
	private AuthManager authManager;

	public UserAdapter(List<User> users, Context context) {
		this.users = users;
		this.context = context;
		this.agent = new FirebaseAgent(context);
		this.authManager = new AuthManager(context);
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_user_item, parent, false);
		return new ViewHolder(v);
	}

	@Override
	public void onBindViewHolder(final ViewHolder holder, int position) {
		if (authManager.checkAuth()){
			final User user = users.get(position);
			if (user != null){
				holder.setData(user.getUsername(), user.getCareer());
			}else {
				//no users
			}
		}else {
			//auth user
		}

	}

	@Override
	public int getItemCount() {
		return users.size();
	}

	public class ViewHolder extends RecyclerView.ViewHolder {

		private TextView user_name, user_career;
		private CircleImageView user_photo;

		public ViewHolder(View itemView) {
			super(itemView);
			user_career = itemView.findViewById(R.id.user_career);
			user_name = itemView.findViewById(R.id.user_name);
			user_photo = itemView.findViewById(R.id.user_photo);
		}

		public void setData(String name, String career){
			user_name.setText(name);
			user_career.setText(career);
		}

	}
}
