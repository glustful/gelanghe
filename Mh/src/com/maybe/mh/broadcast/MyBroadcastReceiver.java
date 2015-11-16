package com.maybe.mh.broadcast;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

public class MyBroadcastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent mintent) {
		// TODO Auto-generated method stub

		if (Intent.ACTION_BOOT_COMPLETED.equals(mintent.getAction())) {
			// 启动完成
			Intent intent = new Intent(context, Alarmreceiver.class);
			intent.setAction("com.maybe.mh.alarm.action");
			PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
			long firstime = SystemClock.elapsedRealtime();
			AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

			// 10秒一个周期，不停的发送广播
			am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstime, 60 * 1000, sender);
		}

	}

}
