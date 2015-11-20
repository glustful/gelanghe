package com.maybe.mh;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;

import com.maybe.mh.broadcast.Alarmreceiver;
import com.maybe.mh.json.ArticleDetailDL;
import com.maybe.mh.json.AttachDL;
import com.maybe.mh.json.CategoryDL;
import com.maybe.mh.json.TopAdDL;
import com.maybe.mh.pojo.User;
import com.maybe.mh.sqlite.DatabaseManager;
import com.maybe.mh.sqlite.LogInUserDao;
import com.maybe.mh.sqlite.SqliteHelper;
import com.maybe.mh.util.MyHttpPost;
import com.tiandu.mh.R;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class StartShowActivity extends Activity {

	
	private Context context;

	private String num;

	@SuppressLint("HandlerLeak")
	private class MyHandler extends Handler {
		@SuppressLint("NewApi")
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			switch (msg.what) {
			
			case 8:

				Intent intent = new Intent();
				intent.setClass(StartShowActivity.this, MainTabActivity.class);
				startActivity(intent);
				finish();

				break;

			default:
				break;
			}
		}
	}

	private MyHandler mMyHandler;

	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.start_show_main);
		context = StartShowActivity.this;

		System.out.println("getSDPath()" + getSDPath());

		MyApplication.getMyApplication().setLocalPath(getSDPath());

		mMyHandler = new MyHandler();

		SqliteHelper sqliteHelper = new SqliteHelper(context);
		DatabaseManager.initializeInstance(sqliteHelper);

		List<User> loginUser = new LogInUserDao().getAlluser();
		DatabaseManager.getInstance().closeDatabase();
		if (loginUser != null && loginUser.size() > 0) {
			MyApplication.getMyApplication().setLoginUser(loginUser.get(0));
		}
		
		MyApplication.alias.clear();
		
		MyApplication.alias.add("tuijian");
		MyApplication.alias.add("dangjian");
		MyApplication.alias.add("other");

		MyApplication.alias.add("fuwu");
		//MyApplication.alias.add("chanpin");
		MyApplication.alias.add("zhengwu");
		MyApplication.alias.add("chaxiang");
		MyApplication.alias.add("about");
	
	// 线程池下载articleDetail
	MyApplication.lock = MyApplication.alias.size();
	MyApplication.isFirst = true;
	for (String str : MyApplication.alias) {
		final HashMap<String, String> params = new HashMap<String, String>();
		params.put("alias", str);
		new Thread(){
			public void run(){
				downloadArticleDetail(params);
			}
		}.start();
		//new RequestInformation().executeOnExecutor(
				//AsyncTask.THREAD_POOL_EXECUTOR, params);
	}

		new Thread(new Runnable() {
			@Override
			public void run() {

				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("devicID", "135"));
				String jsonStr = MyHttpPost.doGet(
						"http://www.gelanghe.gov.cn/getAppInstallNum.php");
				jsonStr = jsonStr.replaceAll("\n", "").trim();

				MyApplication.getMyApplication().setCount(jsonStr);
				jsonStr = MyHttpPost.doGet(
						"http://www.gelanghe.gov.cn/getAppupdateNum.php");
				jsonStr = jsonStr.replaceAll("\n", "").trim();

				MyApplication.getMyApplication().setUpdateCount(jsonStr);
				/*
				 * System.out.println("开始下载articleDetail"); if
				 * (ArticleDetailDL.downloadArticleDetail()) {
				 * System.out.println("下载articleDetail完毕"); } else {
				 * System.out.println("未下载articleDetail"); }
				 */

			}
		}).start();

		new Thread(new Runnable() {
			@Override
			public void run() {

				System.out.println("开始下载doWorkAttach");
				if (AttachDL.downloadDoWorkAttach()) {
					System.out.println("下载doWorkAttach完毕");
				} else {
					System.out.println("未下载doWorkAttach");
				}

			}
		}).start();

		new Thread(new Runnable() {
			@Override
			public void run() {

				System.out.println("开始下载FeedbackAttach");
				if (AttachDL.downloadFeedbackAttach()) {
					System.out.println("下载FeedbackAttach完毕");
				} else {
					System.out.println("未下载FeedbackAttach");
				}

			}
		}).start();

		new Thread(new Runnable() {
			@Override
			public void run() {

				System.out.println("开始下载category");

				if (CategoryDL.downloadCategory()) {
					System.out.println("下载category完毕");
				} else {
					System.out.println("未下载category");
				}

			}
		}).start();

		new Thread(new Runnable() {
			@Override
			public void run() {

				System.out.println("开始下载topad");
				if (TopAdDL.downloadTopAd()) {
					System.out.println("下载topad完毕");
				} else {
					System.out.println("未下载topad");
				}

			}
		}).start();

		Intent intent = new Intent(context, Alarmreceiver.class);
		intent.setAction("com.maybe.mh.alarm.action");
		PendingIntent sender = PendingIntent
				.getBroadcast(context, 0, intent, 0);
		long firstime = SystemClock.elapsedRealtime() + 60 * 1000;
		AlarmManager am = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);

		// 60秒一个周期，不停的发送广播
		am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstime,
				60 * 1000, sender);

		Context context = getWindow().getContext();

		TelephonyManager telephonemanage = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);

		num = "maybe_android";
		try {
			num = telephonemanage.getDeviceId();
		} catch (Exception e) {

		}

		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				System.out.println("deviceId = " + num);
				params.add(new BasicNameValuePair("devicID", num));
				MyHttpPost.doPost(
						"http://www.gelanghe.gov.cn/getInstallnum.php", params);

			}
		}).start();

		mMyHandler.sendEmptyMessageDelayed(8, 4500);

	}

	private String getSDPath() {
		File sdDir = null;
		boolean sdCardExist = Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
		if (sdCardExist) {
			sdDir = Environment.getExternalStorageDirectory();// 获取跟目录

			System.out.println("sdDir=" + sdDir);

		}
		return sdDir.toString();

	}
	
	private void downloadArticleDetail(HashMap<String, String> params){
		System.out.println("开始下载articleDetail="+params.get("alias"));
		if (ArticleDetailDL.downloadArticleDetail(params)) {
			System.out.println("下载articleDetail完毕="+params.get("alias"));
			return ;
		} else {
			System.out.println("未下载articleDetail="+params.get("alias"));
			return ;
		}
	}

}