package com.example.prince.jobhunt.engine;

import android.content.Context;

/**
 * Created by Prince on 2/14/2018.
 */

public class Constants {

    public static final String USER_PROFILE_PATH = "gs://jobhunt-82189.appspot.com";
    public static final String USER_PROFILE_IMAGE_PATH = "users/images/profiles";
    public static final String JOB_IMAGE_PATH = "jobs/users";

    //methods
    public static int getStatusBarHeight(Context ctx){
        int result = 0;
        int resourceId = ctx.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0){
            result = ctx.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

}
