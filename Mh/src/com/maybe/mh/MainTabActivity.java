package com.maybe.mh;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;

import com.maybe.mh.download.GetNewWorkService;
import com.maybe.mh.eb.EBActivity;
import com.maybe.mh.eb.HTML5Activity;
import com.maybe.mh.json.AppVersionDL;
import com.tiandu.mh.R;

@SuppressWarnings("deprecation")
public class MainTabActivity extends TabActivity {

	private View homeTabHost1;
	private View homeTabHost2;
	private View homeTabHost3;

	private TabHost mainTabHost;

	
	private ImageView mainTab1;
	private ImageView mainTab2;
	private ImageView mainTab3;
	public TabHost getMainTabHost() {
		return mainTabHost;
	}

	@SuppressLint("HandlerLeak")
	private class MyHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);

			switch (msg.what) {

			case 1:
				showUpdateDialog();
				break;
			case 2:
				Intent i = new Intent();
				i.setClass(MainTabActivity.this, GetNewWorkService.class);
				startService(i);
				break;

			default:
				break;

			}

		}

	}

	private MyHandler myHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.main_tab);

		myHandler = new MyHandler();

		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub

				int versionID = AppVersionDL.getAppVersion();

				int currentVersionCode = 0;

				if (versionID != -1) {

					PackageManager manager = MyApplication.getMyApplication().getPackageManager();
					try {
						PackageInfo info = manager.getPackageInfo(MyApplication.getMyApplication().getPackageName(), 0);
						String appVersion = info.versionName; // 版本名
						currentVersionCode = info.versionCode; // 版本号
						System.out.println(currentVersionCode + " " + appVersion);
					} catch (NameNotFoundException e) {
						// TODO Auto-generated catch blockd
						e.printStackTrace();
					}

					if (currentVersionCode < versionID) {
						myHandler.sendEmptyMessage(1);
					}

				}

			}
		}).start();

		homeTabHost1 = getLayoutInflater().inflate(R.layout.main_tab_indicator_1, null);
		homeTabHost2 = getLayoutInflater().inflate(R.layout.main_tab_indicator_2, null);
		homeTabHost3 = getLayoutInflater().inflate(R.layout.main_tab_indicator_3, null);

		mainTab1 = (ImageView) homeTabHost1.findViewById(R.id.main_tab_indicator_1_iamge_view);
		mainTab2 = (ImageView) homeTabHost2.findViewById(R.id.main_tab_indicator_2_iamge_view);
		mainTab3 = (ImageView) homeTabHost3.findViewById(R.id.main_tab_indicator_3_iamge_view);

		mainTab1.setImageResource(R.drawable.tab_down_1_select);
		mainTab2.setImageResource(R.drawable.tab_down_2_unselect);
		mainTab3.setImageResource(R.drawable.tab_down_3_unselect);

		mainTabHost = getTabHost();

		mainTabHost.addTab(mainTabHost.newTabSpec("maintab1").setIndicator(homeTabHost1).setContent(new Intent(this, HomeTabActivity.class)));
		mainTabHost.addTab(mainTabHost.newTabSpec("maintab2").setIndicator(homeTabHost2).setContent(new Intent(this, DoWorkActivity.class)));
		mainTabHost.addTab(mainTabHost.newTabSpec("maintab3").setIndicator(homeTabHost3).setContent(new Intent(this, HTML5Activity.class)));

		mainTabHost.setCurrentTab(0);

		mainTabHost.setOnTabChangedListener(new OnTabChangeListener() {
			@SuppressLint("NewApi")
			@Override
			public void onTabChanged(String tabId) {

				mainTab1.setImageResource(R.drawable.tab_down_1_unselect);
				mainTab2.setImageResource(R.drawable.tab_down_2_unselect);
				//mainTab3.setImageResource(R.drawable.tab_down_3_unselect);
				mainTab3.setBackground(null);

				if (tabId.equalsIgnoreCase("maintab1")) {
					mainTab1.setImageResource(R.drawable.tab_down_1_select);
				} else if (tabId.equalsIgnoreCase("maintab2")) {
					mainTab2.setImageResource(R.drawable.tab_down_2_select);
				} else if (tabId.equalsIgnoreCase("maintab3")) {
					//mainTab3.setImageResource(R.drawable.tab_down_3_select);
					mainTab3.setBackgroundColor(Color.parseColor("#039ca1"));
				}
			}
		});

		// myHandler.sendEmptyMessageDelayed(2, 2000);
	}

	private void showUpdateDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("检测到新版本");
		builder.setMessage("是否下载更新?");
		builder.setPositiveButton("下载", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				Intent it = new Intent(MainTabActivity.this, NotificationUpdateActivity.class);
				startActivity(it);
				// MapApp.isDownload = true;
				MyApplication.getMyApplication().setNewApkDLOK(true);
			}
		}).setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub

			}
		});
		builder.show();
	}

}
