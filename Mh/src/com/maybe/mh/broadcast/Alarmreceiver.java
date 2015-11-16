package com.maybe.mh.broadcast;

import com.maybe.mh.download.GetNewWorkService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class Alarmreceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		if (intent.getAction().equals("com.maybe.mh.alarm.action")) {
			Intent i = new Intent();
			i.setClass(context, GetNewWorkService.class);
			context.startService(i);
		}
	}

}
