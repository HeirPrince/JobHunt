package com.example.prince.jobhunt.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.prince.jobhunt.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Popular extends Fragment {


	public static final String TITLE = "Popular";

	public Popular() {
		// Required empty public constructor
	}

	public static Popular newInstance (){
		return new Popular();
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_popular_jobs, container, false);
	}

}
