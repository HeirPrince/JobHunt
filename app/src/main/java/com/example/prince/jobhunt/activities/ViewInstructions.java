package com.example.prince.jobhunt.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.prince.jobhunt.R;
import com.example.prince.jobhunt.engine.AuthManager;
import com.example.prince.jobhunt.engine.FirebaseAgent;
import com.example.prince.jobhunt.model.Notyfication;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ViewInstructions extends AppCompatActivity {

	@BindView(R.id.start_time)TextView start_time;
	@BindView(R.id.end_time)TextView end_time;
	@BindView(R.id.start_date)TextView start_date;
	@BindView(R.id.end_date)TextView end_date;
	@BindView(R.id.price)TextView price;
	@BindView(R.id.type)TextView type;

	private FirebaseAgent agent;
	private AuthManager authManager;

	private FirebaseFirestore firestore;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_instructions);
		ButterKnife.bind(this);

		agent = new FirebaseAgent(this);
		authManager = new AuthManager(this);
		firestore = FirebaseFirestore.getInstance();

		setViews();
	}

	public void setViews(){
		firestore.collection("notifications")
				.get()
				.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
					@Override
					public void onComplete(@NonNull Task<QuerySnapshot> task) {
						if (task.isSuccessful()){
							for (DocumentSnapshot snapshot : task.getResult()){
								Notyfication notification = snapshot.toObject(Notyfication.class);
								start_date.setText(notification.getStart_date());
								end_date.setText(notification.getEnd_date());
								start_time.setText(notification.getStart_time());
								end_time.setText(notification.getEnd_date());

								if (notification.getPartTime()){
									type.setText("Part time");
								}else {
									type.setText("Full time");
								}

								if (notification.getNeg()){
									price.setText("Negotiable");
								}else {
									price.setText("Fixed");
								}

							}
						}
					}
				}).addOnFailureListener(new OnFailureListener() {
			@Override
			public void onFailure(@NonNull Exception e) {

			}
		});
	}

}
