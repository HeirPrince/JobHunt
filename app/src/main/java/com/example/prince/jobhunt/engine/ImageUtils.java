package com.example.prince.jobhunt.engine;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.widget.ImageView;

/**
 * Created by Prince on 4/11/2018.
 */

public class ImageUtils {

	public void darkenImage(ImageView imageView){
		imageView.setColorFilter(Color.rgb(130, 130, 130), PorterDuff.Mode.MULTIPLY);
	}

	public void unDarkenImage(ImageView imageView){
		imageView.clearColorFilter();
	}

	public boolean isImageDark(ImageView imageView){
		return imageView.getColorFilter() != null;
	}
}
