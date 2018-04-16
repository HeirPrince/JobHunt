package com.example.prince.jobhunt.fragments;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.prince.jobhunt.R;
import com.example.prince.jobhunt.engine.NonSwipableViewPager;
import com.example.prince.jobhunt.engine.SlideAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class Jobs extends Fragment {

	private NonSwipableViewPager pager;
	private TabLayout tabs;
	private SlideAdapter adapter;


	public Jobs() {
		// Required empty public constructor
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View v = inflater.inflate(R.layout.fragment_jobs, container, false);
		tabs = v.findViewById(R.id.tab);
		pager = v.findViewById(R.id.pager);

		setViewPager();
		return v;
	}

	private void setViewPager() {
		adapter = new SlideAdapter(getActivity().getSupportFragmentManager());
		pager.setAdapter(adapter);
		tabs.setupWithViewPager(pager);
	}
}
