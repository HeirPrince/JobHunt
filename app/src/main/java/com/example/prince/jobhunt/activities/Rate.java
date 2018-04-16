package com.example.prince.jobhunt.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prince.jobhunt.R;
import com.example.prince.jobhunt.engine.AuthManager;
import com.example.prince.jobhunt.engine.FirebaseAgent;
import com.example.prince.jobhunt.model.Rating;
import com.example.prince.jobhunt.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Rate extends AppCompatActivity {

	@BindView(R.id.toolbar_home)
	Toolbar toolbar;
	@BindView(R.id.rateText)
	TextView rateText;
	@BindView(R.id.ratingBar)
	RatingBar ratingBar;
	@BindView(R.id.review)
	EditText review;
	@BindView(R.id.reactions)
	MaterialSpinner reactions;

	private List<String> rct;
	private FirebaseFirestore firestore;
	private AuthManager authManager;
	private FirebaseAgent agent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rate);
		ButterKnife.bind(this);
		setSupportActionBar(toolbar);
		getSupportActionBar().setTitle("Rate Prince");

		authManager = new AuthManager();
		agent = new FirebaseAgent(this);
		firestore = FirebaseFirestore.getInstance();

		setSpinner();

	}

	private void setSpinner() {

		rct = new ArrayList<>();

		rct.add("Happy");
		rct.add("Sad");
		rct.add("Incompetent");
		rct.add("Failed");

		reactions.setItems(rct);

	}


	public void Review(View view) {

		agent.getUserByUID(authManager.getCurrentUID(), new FirebaseAgent.getUser() {
			@Override
			public void gottenUser(final User user) {
				if (user != null){
					final ProgressDialog dialog = new ProgressDialog(Rate.this);
					dialog.setMessage("Rating " + user.getUsername());
					dialog.show();

					Rating rating = new Rating();
					rating.setNmStars(ratingBar.getRating());
					rating.setReaction(rct.get(reactions.getSelectedIndex()));
					rating.setId(authManager.getCurrentUID());

					firestore.collection("rating").document(authManager.getCurrentUID())
							.set(rating)
							.addOnCompleteListener(new OnCompleteListener<Void>() {
								@Override
								public void onComplete(@NonNull Task<Void> task) {
									dialog.dismiss();
									Toast.makeText(Rate.this, user.getUsername() + " rated successfully", Toast.LENGTH_SHORT).show();
								}
							})
							  .addOnFailureListener(new OnFailureListener() {
								  @Override
								  public void onFailure(@NonNull Exception e) {
									  Toast.makeText(Rate.this, e.getMessage(), Toast.LENGTH_SHORT).show();
								  }
							  });
				}
			}
		});

	}
}
