package com.example.prince.jobhunt.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.prince.jobhunt.R;
import com.example.prince.jobhunt.engine.AuthManager;
import com.example.prince.jobhunt.engine.DialogHelper;
import com.example.prince.jobhunt.engine.FirebaseAgent;
import com.example.prince.jobhunt.model.Application;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddApplication extends AppCompatActivity {

	private FirebaseFirestore firestore;
	private CollectionReference applicationRef;
	private FirebaseAgent agent;
	private AuthManager authManager;
	private DialogHelper dialogHelper;

	@BindView(R.id.salary)EditText salary;
	@BindView(R.id.desc)EditText desc;
	@BindView(R.id.toolbar_home)Toolbar toolbar;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_application);
		ButterKnife.bind(this);
		setSupportActionBar(toolbar);
		getSupportActionBar().setTitle(getIntent().getStringExtra("job_owner"));
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		agent = new FirebaseAgent(this);
		dialogHelper = new DialogHelper(this);
		firestore = FirebaseFirestore.getInstance();
		authManager = new AuthManager(this);
	}

	public void Apply(View view) {
		final ProgressDialog progress = new ProgressDialog(this);
		final String job_owner = getIntent().getStringExtra("job_owner");
		progress.setMessage("Applying to "+ job_owner);
		progress.show();
		//apply
		String id = getIntent().getStringExtra("job_id");
		applicationRef = firestore.collection("applications").document("category").collection(id);

		final Application application = new Application();
		application.setDesc(desc.getText().toString());
		application.setUid(authManager.getCurrentUID());
		application.setSalary(String.valueOf(salary.getText().toString()));
		application.setJob_id(id);

		applicationRef
				.add(application)
				.addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
					@Override
					public void onSuccess(DocumentReference documentReference) {
						progress.dismiss();
						dialogHelper.showActionDialog("Job Application", "Job Application sent to " + job_owner, "VIEW", "CLOSE", new DialogHelper.onClickBtn() {
							@Override
							public void clicked(Boolean status) {
								if (status){
									//true
									Home h = new Home();
									h.moveTo(1);
									finish();
									Intent i = new Intent(AddApplication.this, Home.class);
									startActivity(i);
								}else {
									dialogHelper.dismissDialog();
								}
							}
						});
					}
				}).addOnFailureListener(new OnFailureListener() {
			@Override
			public void onFailure(@NonNull Exception e) {
				progress.dismiss();
				Toast.makeText(AddApplication.this, e.getMessage(), Toast.LENGTH_SHORT).show();
			}
		});
	}
}
