package com.maybe.mh.util;

import android.view.Gravity;
import android.widget.Toast;
import android.content.Context;

public class ShowToast {

	public static void showToastShort(Context context, String msg) {
		Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}

	public static void showToastLong(Context context, String msg) {
		Toast toast = Toast.makeText(context, msg, Toast.LENGTH_LONG);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}
}