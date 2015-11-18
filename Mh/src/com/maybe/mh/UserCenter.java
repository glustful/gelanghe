package com.maybe.mh;

import java.util.Date;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.maybe.mh.json.AppVersionDL;
import com.maybe.mh.pojo.ArticleDetail;
import com.maybe.mh.pojo.User;
import com.maybe.mh.sqlite.ArticleDetailDao;
import com.maybe.mh.sqlite.DatabaseManager;
import com.maybe.mh.sqlite.LogInUserDao;
import com.maybe.mh.sqlite.SqliteHelper;
import com.maybe.mh.util.ShowToast;

public class UserCenter extends MyActivity {

	private Button settingBut;
	private Button aboutUsBut;
	private Button loginBut;
	//private Button myWorkBut;
	private Button getNewVersionBut;

	private List<ArticleDetail> dataList;

	private Context context;

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
				ShowToast.showToastShort(MyApplication.getMyApplication().getApplicationContext(), "已安装最新版");
				break;
			default:
				break;
			}
		}
	}

	private MyHandler myHandler;
	private  TextView count,updateCount; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.user_center);

		context = UserCenter.this;

		myHandler = new MyHandler();

		SqliteHelper sqliteHelper = new SqliteHelper(context);
		DatabaseManager.initializeInstance(sqliteHelper);

		settingBut = (Button) super.findViewById(R.id.user_center_setting_but);
		aboutUsBut = (Button) super.findViewById(R.id.user_center_about_us_but);
		loginBut = (Button) super.findViewById(R.id.user_center_login_but);
		//myWorkBut = (Button) super.findViewById(R.id.user_center_my_work_but);
		getNewVersionBut = (Button) super.findViewById(R.id.user_center_check_new_version_but);
		count= (TextView) super.findViewById(R.id.installCount);
		count.setText("安装数：" + MyApplication.getMyApplication().getCount());
		updateCount= (TextView) super.findViewById(R.id.updateCount);
		updateCount.setText("更新数：" + MyApplication.getMyApplication().getCount());
		settingBut.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(UserCenter.this, SettingPageActivity.class);
				startActivity(intent);

			}
		});

		aboutUsBut.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				dataList = new ArticleDetailDao().getAllArticleDetailByAlias("about");
				DatabaseManager.getInstance().closeDatabase();
				intent.putExtra("id", dataList.get(0).getArticle_id());
				intent.setClass(UserCenter.this, RecommendShowPageActivity.class);
				startActivity(intent);
			}
		});
		loginBut.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				List<User> userList = new LogInUserDao().getAlluser();
				DatabaseManager.getInstance().closeDatabase();
				Intent intent = new Intent();
				if (userList.size() > 0) {
					intent.setClass(UserCenter.this, LoginView.class);
				} else {
					intent.setClass(UserCenter.this, UserLoginActivity.class);
				}
				startActivity(intent);
			}
		});
		/*myWorkBut.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				List<User> userList = new LogInUserDao().getAlluser();
				DatabaseManager.getInstance().closeDatabase();
				Intent intent = new Intent();
				if (userList.size() > 0) {
					intent.setClass(UserCenter.this, MyWorkListActivity.class);
				} else {
					intent.setClass(UserCenter.this, UserLoginActivity.class);
				}
				startActivity(intent);

			}
		});
*/
		getNewVersionBut.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
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
							}else{
								myHandler.sendEmptyMessage(2);
							}

						}

					}
				}).start();
			}
		});

	}

	private void showUpdateDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(getParent());
		builder.setTitle("检测到新版本");
		builder.setMessage("是否下载更新?");
		builder.setPositiveButton("下载", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				Intent it = new Intent(UserCenter.this, NotificationUpdateActivity.class);
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
	
		long preTime = 0;
	
	   @Override  
	    public boolean onKeyDown(int keyCode, KeyEvent event) {  
	        // 截获后退键  
	        if (keyCode == KeyEvent.KEYCODE_BACK) {  
	            long currentTime = new Date().getTime();  
	  
	            // 如果时间间隔大于2秒, 不处理  
	            if ((currentTime - preTime) > 2000) {  
	                // 显示消息  
	                ShowToast.showToastShort(MyApplication.getMyApplication().getApplicationContext(), "再次点击退出格朗和");
	                // 更新时间  
	                preTime = currentTime;  
	                // 截获事件,不再处理  
	                return true;  
	            }  
	        }  
	  
	        return super.onKeyDown(keyCode, event);  
	    }  
	

}
