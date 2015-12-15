package com.maybe.mh.download;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.maybe.mh.MyApplication;
import com.tiandu.mh.R;
import com.maybe.mh.RecommendShowPageActivity;
import com.maybe.mh.WorkDetailActivity;
import com.maybe.mh.eb.HTML5Activity;
import com.maybe.mh.pojo.ArticleDetail;
import com.maybe.mh.pojo.DoWork;
import com.maybe.mh.pojo.User;
import com.maybe.mh.sqlite.ArticleDetailDao;
import com.maybe.mh.sqlite.DatabaseManager;
import com.maybe.mh.sqlite.DoWorkDao;
import com.maybe.mh.sqlite.FeedbackAttachDao;
import com.maybe.mh.sqlite.LogInUserDao;
import com.maybe.mh.sqlite.SqliteHelper;
import com.maybe.mh.util.MyHttpPost;

public class GetNewWorkService extends Service {

	private Context context;

	private ArticleDetail articleDetail;

	private DoWork doWork;

	private static final String articleDetailsUrl = "http://www.gelanghe.gov.cn/getArticleListNew.php";

	private static final String myDoWorkUrl = "http://www.gelanghe.gov.cn/getUserLogin.php";

	private class MyHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				showNotification1();
				break;
			case 2:
				showNotification2();
				break;
			case 3:
				showNotification3(msg.getData());
				break;
			default:
				break;
			}
		}

	}

	private MyHandler myHandler;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub

		super.onCreate();

		context = GetNewWorkService.this;
	}

	@Override
	@Deprecated
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);

		myHandler = new MyHandler();

		SqliteHelper sqliteHelper = new SqliteHelper(context);
		DatabaseManager.initializeInstance(sqliteHelper);

		System.out.println("服务已启动");

		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				String lastId = new ArticleDetailDao().getLastArticle() + "";
				DatabaseManager.getInstance().closeDatabase();

				List<NameValuePair> params = new ArrayList<NameValuePair>();

				params.add(new BasicNameValuePair("article_id", lastId));

				String jsonStr = MyHttpPost.doPost(articleDetailsUrl, params);

				if (jsonStr.indexOf("article_id") != -1) {
					List<ArticleDetail> list = new ArrayList<ArticleDetail>();
					list = JSON.parseArray(jsonStr, ArticleDetail.class);
					if (list.size() > 0) {

						int listSize = list.size();
						ArticleDetailDao articleDetailDao = new ArticleDetailDao();

						for (int i = 0; i < listSize; i++) {
							articleDetailDao.addArticleDetail(list.get(i));
						}

						int newLastId = articleDetailDao.getLastArticle();

						articleDetail = articleDetailDao.getAllArticleDetailById(newLastId);
						DatabaseManager.getInstance().closeDatabase();
						
						if(MyApplication.getMyApplication().getLoginUser() == null){
							
							if(articleDetail.getGroups().equals("0")){
								myHandler.sendEmptyMessage(1);
							}
						}else{
							String userGroup = MyApplication.getMyApplication().getLoginUser().getGroup_id();
							
							if(articleDetail.getGroups().equals("0")){
								myHandler.sendEmptyMessage(1);
							}else if(articleDetail.getGroups().equals(userGroup)){
								myHandler.sendEmptyMessage(1);
							}else if(articleDetail.getGroups().indexOf(userGroup) != -1){
								myHandler.sendEmptyMessage(1);
							}
						}
						
						
						
					}

					

				}
			}
		}).start();

		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					
					if(MyApplication.getMyApplication().getSellerUser() != null){
						sellerLogin();
					}
					if (MyApplication.getMyApplication().getLoginUser() != null) {
						
						List<NameValuePair> params = new ArrayList<NameValuePair>();
						
						String lastId = new FeedbackAttachDao().getLastFeedbackAttach() + "";
	
						params.add(new BasicNameValuePair("username", MyApplication.getMyApplication().getLoginUser().getUsername()));
						params.add(new BasicNameValuePair("password", MyApplication.getMyApplication().getLoginUser().getPwd()));
						params.add(new BasicNameValuePair("feedback_id", lastId));
	
						String jsonStr = MyHttpPost.doPost(myDoWorkUrl, params);
	
						if (jsonStr.indexOf("feedback_id") != -1) {
							List<DoWork> list = new ArrayList<DoWork>();
	
							JSONObject jsonObject = JSON.parseObject(jsonStr);
	
							Object object = jsonObject.get("info");
	
							String myWork = object.toString();
	
							list = JSON.parseArray(myWork, DoWork.class);
							if (list.size() > 0) {
	
								int listSize = list.size();
								DoWorkDao doWorkDao = new DoWorkDao();
								doWorkDao.deleteAll();
	
								for (int i = 0; i < listSize; i++) {
									doWorkDao.addDoWork(list.get(i));
								}
	
								int newLastId = doWorkDao.getLastDoWork();
	
								doWork = doWorkDao.getDoWorkById(newLastId);
								DatabaseManager.getInstance().closeDatabase();
								
								
								myHandler.sendEmptyMessage(2);
							}
	
						}
	
					}
				} catch (Exception e) {
					// TODO: handle exception
					System.err.println("===GetNewWorkService==="+e.getMessage());
				}
			}
				
		}).start();
		
		
		
		

	}

	protected void sellerLogin() {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("username", MyApplication.getMyApplication().getSellerUser().getUsername()));
		params.add(new BasicNameValuePair("password", MyApplication.getMyApplication().getSellerUser().getPwd()));
		params.add(new BasicNameValuePair("oid", "1"));
		String loginStatus = MyHttpPost.doPost("http://www.gelanghe.gov.cn/glh2015/api/getSellerLogin.php", params);
		
		JSONArray infoStr = null;
		if (loginStatus.indexOf("username") != -1) {
			try{
			org.json.JSONObject resObj = new org.json.JSONObject(loginStatus);//JSONObject.parseObject(loginStatus);

			//Object jsonUser = resObj.get("user");
			org.json.JSONObject userObj = resObj.optJSONObject("user");//JSONObject.parseObject(jsonUser.toString());

			infoStr = resObj.optJSONArray("info");
			
			MyApplication.getMyApplication().getSellerUser().setSellerUrl(userObj.optString("url"));
			
			ArrayList<String> orders = new ArrayList<String>();
			if(infoStr != null){

				
				for(int i=0;i<infoStr.length();i++){
					org.json.JSONObject item = infoStr.optJSONObject(i);
					if(item != null && item.optString("notice").equals("0")){
						orders.add(item.optString("order_id"));
					}
				}
				
			}
			Message msg = Message.obtain();
			msg.what = 3;
			Bundle bundle = new Bundle();
			bundle.putString("url", userObj.optString("url"));
			bundle.putStringArrayList("orders", orders);
			msg.obj = userObj.optString("url");
			msg.setData(bundle);
			myHandler.sendMessage(msg);
			}catch(Exception e){
				e.printStackTrace();
				System.out.println("exception login");
			}

		} 
		
	}

	@SuppressWarnings("deprecation")
	private void showNotification1() {
		NotificationManager notificationManager = (NotificationManager) this.getSystemService(android.content.Context.NOTIFICATION_SERVICE);
		Notification notification = new Notification();

		notification.icon = R.drawable.ic_launcher;
		notification.tickerText = "格朗和";
		notification.when = System.currentTimeMillis();

		notification.flags |= Notification.FLAG_AUTO_CANCEL; // 表明在点击了通知栏中的"清除通知"后，此通知不清除，经常与FLAG_ONGOING_EVENT一起使用
		notification.flags |= Notification.FLAG_SHOW_LIGHTS;
		notification.defaults = Notification.DEFAULT_ALL;

		Intent intent = new Intent();
		// ComponentName componentName = new ComponentName("com.maybe.mh",
		// "com.maybe.mh.RecommendShowPageActivity");
		// intent.setComponent(componentName);
		// intent.setAction("android.intent.action.MAIN");
		// intent.addCategory("android.intent.category.LAUNCHER");
		// intent.addFlags(Notification.FLAG_ONGOING_EVENT);

		CharSequence contentTitle = "格朗和有新消息"; 
		CharSequence contentText = articleDetail.getTitle(); 
		intent.setClass(GetNewWorkService.this, RecommendShowPageActivity.class)
		.putExtra("id", articleDetail.getArticle_id()); 
		
		PendingIntent contentItent = PendingIntent.getActivity(this, 0, intent, 0);
		notification.setLatestEventInfo(this, contentTitle, contentText, contentItent);
		notification.contentIntent = contentItent;
		// 把Notification传递给NotificationManager
		notificationManager.notify(0, notification);
	}

	private void showNotification2() {
		NotificationManager notificationManager = (NotificationManager) this.getSystemService(android.content.Context.NOTIFICATION_SERVICE);
		Notification notification = new Notification();

		notification.icon = R.drawable.ic_launcher;
		notification.tickerText = "格朗和";
		notification.when = System.currentTimeMillis();

		notification.flags |= Notification.FLAG_AUTO_CANCEL; // 表明在点击了通知栏中的"清除通知"后，此通知不清除，经常与FLAG_ONGOING_EVENT一起使用
		notification.flags |= Notification.FLAG_SHOW_LIGHTS;
		notification.defaults = Notification.DEFAULT_ALL;

		Intent intent = new Intent();
		// ComponentName componentName = new ComponentName("com.maybe.mh",
		// "com.maybe.mh.RecommendShowPageActivity");
		// intent.setComponent(componentName);
		// intent.setAction("android.intent.action.MAIN");
		// intent.addCategory("android.intent.category.LAUNCHER");
		// intent.addFlags(Notification.FLAG_ONGOING_EVENT);

		CharSequence contentTitle = "你有新的待办事项"; // 通知栏标题
		CharSequence contentText = doWork.getTitle(); // 通知栏内容
		intent.setClass(GetNewWorkService.this, WorkDetailActivity.class).putExtra("id", doWork.getFeedback_id()); // 点击该通知后要跳转的Activity
		PendingIntent contentItent = PendingIntent.getActivity(this, 0, intent, 0);
		notification.setLatestEventInfo(this, contentTitle, contentText, contentItent);
		notification.contentIntent = contentItent;
		// 把Notification传递给NotificationManager
		notificationManager.notify(1, notification);
	}
	
	private void showNotification3(Bundle data) {
		int size = data.getStringArrayList("orders").size();
		if(size<1)
			return;
		NotificationManager notificationManager = (NotificationManager) this.getSystemService(android.content.Context.NOTIFICATION_SERVICE);
		Notification notification = new Notification();

		notification.icon = R.drawable.ic_launcher;
		notification.tickerText = "格朗和";
		notification.when = System.currentTimeMillis();

		notification.flags |= Notification.FLAG_AUTO_CANCEL; // 表明在点击了通知栏中的"清除通知"后，此通知不清除，经常与FLAG_ONGOING_EVENT一起使用
		notification.flags |= Notification.FLAG_SHOW_LIGHTS;
		notification.defaults = Notification.DEFAULT_ALL;

		Intent intent = new Intent();
		
		
		CharSequence contentTitle = "你有新的订单"; // 通知栏标题
		CharSequence contentText = "你有"+size + "条新的订单"; // 通知栏内容
		intent.setClass(GetNewWorkService.this, HTML5Activity.class).putExtra("url", data.getString("url")).putExtra("data", data.getStringArrayList("orders")); // 点击该通知后要跳转的Activity
		PendingIntent contentItent = PendingIntent.getActivity(this, 0, intent, 0);
		notification.setLatestEventInfo(this, contentTitle, contentText, contentItent);
		notification.contentIntent = contentItent;
		// 把Notification传递给NotificationManager
		notificationManager.notify(2, notification);
		
	}

}
