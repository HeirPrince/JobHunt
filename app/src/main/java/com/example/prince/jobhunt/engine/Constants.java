package com.example.prince.jobhunt.engine;

import android.content.Context;

/**
 * Created by Prince on 2/14/2018.
 */

public class Constants {

    public static final String STORAGE_PATH = "gs://jobhunt-82189.appspot.com";
    public static final String USER_PROFILE_IMAGE_PATH = "users/images/profiles";
    public static final String JOB_IMAGE_PATH = "jobs/users";
    public static final String IMAGE_PATH_JOBS_MAIN = "images/jobs/main";
    public static final String IMAGE_PATH_JOBS_OTHERS = "images/jobs/others";
	public static final String STORAGE_CV_PATH = "images/cv";

	//methods
    public static int getStatusBarHeight(Context ctx){
        int result = 0;
        int resourceId = ctx.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0){
            result = ctx.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    //DB COLLECTIONS
    public static final String DB_COLLECTION_TRANSACTIONS = "transactions";
    public static final String DB_COLLECTION_USERS = "users";
    public static final String DB_COLLECTION_JOBS = "jobs";
    public static final String DB_COLLECTION_APPLICATIONS = "applications";
    public static final String DB_COLLECTION_IMAGES = "images";
    public static final String DB_COLLECTION_DUTY = "duty";
    public static final String DB_COLLECTION_LOCATION = "location";

}
