package com.example.prince.jobhunt.engine;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.DecelerateInterpolator;
import android.widget.Scroller;

import java.lang.reflect.Field;

/**
 * Created by Prince on 2/21/2018.
 */

public class NonSwipableViewPager extends ViewPager {
	public NonSwipableViewPager(@NonNull Context context) {
		super(context);
		setScrolling();
	}

	public NonSwipableViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
		setScrolling();
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		return false;
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		return false;
	}

	private void setScrolling() {
		try {
			Class<?> viewpager = ViewPager.class;
			Field scrolling = viewpager.getDeclaredField("mScroller");
			scrolling.setAccessible(true);
			scrolling.set(this, new myScroller(getContext()));
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	public class myScroller extends Scroller{

		public myScroller(Context context) {
			super(context, new DecelerateInterpolator());
		}

		@Override
		public void startScroll(int startX, int startY, int dx, int dy) {
			super.startScroll(startX, startY, dx, dy, 350/*1 sec*/);
		}
	}

}
