package com.example.prince.jobhunt.engine;

import com.github.thunder413.datetimeutils.DateTimeUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * Created by Prince on 4/12/2018.
 */

public class TimeUtils extends DateTimeUtils {

	public String generateTimeStamp(){
		return TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()) + "";
	}

	public String extractFromTimestamp(long timeStamp){
		try{
			Calendar calendar = Calendar.getInstance();
			TimeZone tz = TimeZone.getDefault();
			calendar.setTimeInMillis(timeStamp * 1000);
			calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date currenTimeZone = (Date) calendar.getTime();
			return sdf.format(currenTimeZone);
		}catch (Exception e) {
		}
		return "";
	}
}
