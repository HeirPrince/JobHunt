package com.example.prince.jobhunt.engine;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.prince.jobhunt.fragments.Categories;
import com.example.prince.jobhunt.fragments.NearBy;
import com.example.prince.jobhunt.fragments.NewJobs;
import com.example.prince.jobhunt.fragments.Popular;

/**
 * Created by Prince on 3/24/2018.
 */

public class SlideAdapter extends FragmentStatePagerAdapter {

	//tab count
	private static int TAB_COUNT = 4;

	public SlideAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int position) {
		switch (position){
			case 0:
				return new NewJobs();
			case 1:
				return Popular.newInstance();
			case 2:
				return NearBy.newInstance();
			case 3:
				return Categories.newInstance();
		}

		return null;
	}

	@Override
	public int getCount() {
		return TAB_COUNT;
	}

	@Nullable
	@Override
	public CharSequence getPageTitle(int position) {
		switch (position){
			case 0:
				return NewJobs.TITLE;
			case 1:
				return Popular.TITLE;
			case 2:
				return NearBy.TITLE;
			case 3:
				return Categories.TITLE;
		}

		return super.getPageTitle(position);
	}
}
