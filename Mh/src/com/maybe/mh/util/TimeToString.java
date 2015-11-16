package com.maybe.mh.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeToString {

	public static String nowTimeToString() {
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String nowDateString = dateFormat.format(now);
		return nowDateString;
	}

}
