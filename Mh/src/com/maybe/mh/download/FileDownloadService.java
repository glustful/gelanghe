package com.maybe.mh.download;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.widget.RemoteViews;

import com.maybe.mh.MyApplication;
import com.maybe.mh.NotificationUpdateActivity;
import com.maybe.mh.NotificationUpdateActivity.ICallbackResult;
import com.tiandu.mh.R;
import com.maybe.mh.util.ShowToast;

public class FileDownloadService extends Service {
	private static final int NOTIFY_ID = 0;
	private int progress;
	private NotificationManager mNotificationManager;
	private boolean canceled;

	private String fileUrl;

	private static final String savePath = MyApplication.getMyApplication().getLocalPath() + "/格朗和办事附件/";

	private String saveName;

	private ICallbackResult callback;

	private DownloadBinder binder;

	private MyApplication app;

	private boolean serviceIsDestroy = false;

	private Context context = this;
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				app.setNewApkDLOK(false);
				mNotificationManager.cancel(NOTIFY_ID);
				ShowToast.showToastShort(context, "文件下载完成");
				break;
			case 2:
				app.setNewApkDLOK(false);
				mNotificationManager.cancel(NOTIFY_ID);
				break;
			case 1:
				int rate = msg.arg1;
				app.setNewApkDLOK(true);
				if (rate < 100) {
					RemoteViews contentview = mNotification.contentView;
					contentview.setTextViewText(R.id.tv_progress, rate + "%");
					contentview.setProgressBar(R.id.progressbar, 100, rate, false);
				} else {
					mNotification.flags = Notification.FLAG_AUTO_CANCEL;
					mNotification.contentView = null;
					Intent intent = new Intent(context, NotificationUpdateActivity.class);
					intent.putExtra("completed", "yes");
					// 更新参数,注意flags要使用FLAG_UPDATE_CURRENT
					PendingIntent contentIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
					mNotification.setLatestEventInfo(context, "下载完成", "文件已下载完毕", contentIntent);
					serviceIsDestroy = true;
					stopSelf();// 停掉服务自身
				}
				mNotificationManager.notify(NOTIFY_ID, mNotification);
				break;
			}
		}
	};

	@Override
	@Deprecated
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);

		System.out.println("服务启动");

		fileUrl = intent.getStringExtra("fileUrl");
		saveName = intent.getStringExtra(savePath + "saveName");

	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		System.out.println("是否执行了 onBind");
		return binder;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		app.setNewApkDLOK(false);
	}

	@Override
	public boolean onUnbind(Intent intent) {
		// TODO Auto-generated method stub
		return super.onUnbind(intent);
	}

	@Override
	public void onRebind(Intent intent) {
		// TODO Auto-generated method stub
		super.onRebind(intent);
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		binder = new DownloadBinder();
		mNotificationManager = (NotificationManager) getSystemService(android.content.Context.NOTIFICATION_SERVICE);
		setForeground(true);// 这个不确定是否有作用
		app = (MyApplication) getApplication();
	}

	private void setForeground(boolean b) {
		// TODO Auto-generated method stub
	}

	public class DownloadBinder extends Binder {
		public void start() {
			if (downLoadThread == null || !downLoadThread.isAlive()) {
				progress = 0;
				setUpNotification();
				new Thread() {
					public void run() {
						// 下载
						startDownload();
					};
				}.start();
			}
		}

		public void cancel() {
			canceled = true;
		}

		public int getProgress() {
			return progress;
		}

		public boolean isCanceled() {
			return canceled;
		}

		public boolean serviceIsDestroy() {
			return serviceIsDestroy;
		}

		public void cancelNotification() {
			mHandler.sendEmptyMessage(2);
		}

		public void addCallback(ICallbackResult callback) {
			FileDownloadService.this.callback = callback;
		}
	}

	private void startDownload() {
		// TODO Auto-generated method stub
		canceled = false;
		downloadFile();
	}

	Notification mNotification;

	private void setUpNotification() {
		int icon = R.drawable.icon;
		CharSequence tickerText = "开始下载";
		long when = System.currentTimeMillis();
		mNotification = new Notification(icon, tickerText, when);
		;
		// 放置在"正在运行"栏目中
		mNotification.flags = Notification.FLAG_ONGOING_EVENT;

		RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.download_notification_layout);
		contentView.setTextViewText(R.id.name, "文件 正在下载...");
		// 指定个性化视图
		mNotification.contentView = contentView;

		Intent intent = new Intent(this, NotificationUpdateActivity.class);
		// 下面两句是 在按home后，点击通知栏，返回之前activity 状态;
		// 有下面两句的话，假如service还在后台下载， 在点击程序图片重新进入程序时，直接到下载界面，相当于把程序MAIN 入口改了 - -
		// 是这么理解么。。。
		// intent.setAction(Intent.ACTION_MAIN);
		// intent.addCategory(Intent.CATEGORY_LAUNCHER);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

		// 指定内容意图
		mNotification.contentIntent = contentIntent;
		mNotificationManager.notify(NOTIFY_ID, mNotification);
	}

	private Thread downLoadThread;

	private void downloadFile() {
		downLoadThread = new Thread(downloadRunnable);
		downLoadThread.start();
	}

	private int lastRate = 0;

	private Runnable downloadRunnable = new Runnable() {
		@Override
		public void run() {
			try {
				URL url = new URL(fileUrl);

				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.connect();
				int length = conn.getContentLength();
				InputStream is = conn.getInputStream();

				File file = new File(savePath);
				if (!file.exists()) {
					file.mkdirs();
				}
				String apkFile = saveName;
				File ApkFile = new File(apkFile);
				FileOutputStream fos = new FileOutputStream(ApkFile);

				int count = 0;
				byte buf[] = new byte[1024];

				do {
					int numread = is.read(buf);
					count += numread;
					progress = (int) (((float) count / length) * 100);
					// 更新进度
					Message msg = mHandler.obtainMessage();
					msg.what = 1;
					msg.arg1 = progress;
					if (progress >= lastRate + 1) {
						mHandler.sendMessage(msg);
						lastRate = progress;
						if (callback != null)
							callback.OnBackResult(progress);
					}
					if (numread <= 0) {
						// 下载完成通知安装
						mHandler.sendEmptyMessage(0);
						// 下载完了，cancelled也要设置
						canceled = true;
						break;
					}
					fos.write(buf, 0, numread);
				} while (!canceled);// 点击取消就停止下载.

				fos.close();
				is.close();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	};

}
