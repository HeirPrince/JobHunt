package com.example.prince.jobhunt;

import android.app.SearchManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class SearchActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_actovoty);
		Intent i = getIntent();
		if (i.ACTION_SEARCH.equals(i.getAction())){
			String query  =i.getStringExtra(SearchManager.QUERY);
			Toast.makeText(this, query, Toast.LENGTH_SHORT).show();
		}
	}
}
