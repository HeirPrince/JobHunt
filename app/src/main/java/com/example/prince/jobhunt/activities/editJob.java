package com.example.prince.jobhunt.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.prince.jobhunt.R;

public class editJob extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_job);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.job_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()){
			case R.id.send:
				updateJob();
				break;
		}

		return super.onOptionsItemSelected(item);
	}

	private void updateJob() {
		ProgressDialog dialog = new ProgressDialog(this);
		dialog.setMessage("Updating");

	}

	public void uploadImage(View view) {

	}
}
