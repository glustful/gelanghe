package com.maybe.mh;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;


import com.alibaba.fastjson.JSON;
import com.maybe.mh.json.AttachDL;
import com.maybe.mh.pojo.DoWork;
import com.maybe.mh.pojo.User;
import com.maybe.mh.sqlite.DatabaseManager;
import com.maybe.mh.sqlite.DoWorkDao;
import com.maybe.mh.sqlite.LogInUserDao;
import com.maybe.mh.sqlite.SqliteHelper;
import com.maybe.mh.util.MyHttpPost;
import com.maybe.mh.util.ShowToast;
import com.tiandu.mh.R;

public class UserLoginActivity extends MyActivity {

	private EditText nameET;
	private EditText pwdET;
	private Button loginBut;
	private ImageButton backIB;
	private ProgressBar pb;
	private boolean isSkipDoWorkList = false;
	private List<NameValuePair> params;

	private String loginStatus;

	private class MyHandle extends Handler {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			pb.setVisibility(View.GONE);
			switch (msg.what) {

			case 1:
				ShowToast.showToastShort(MyApplication.getMyApplication().getApplicationContext(), "登陆成功");
				if(isSkipDoWorkList){
					startActivity(new Intent(UserLoginActivity.this,MyWorkListActivity.class));
				}
				finish();
				break;
			case 2:
				ShowToast.showToastShort(MyApplication.getMyApplication().getApplicationContext(), "登陆失败");
				break;

			default:
				break;
			}
		}
	}

	private MyHandle myHandle;

	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.user_login);
		context = UserLoginActivity.this;
		isSkipDoWorkList = getIntent().getBooleanExtra("skip", false);
		SqliteHelper sqliteHelper = new SqliteHelper(context);
		DatabaseManager.initializeInstance(sqliteHelper);

		myHandle = new MyHandle();
		pb = (ProgressBar) findViewById(R.id.progressBar);
		nameET = (EditText) super.findViewById(R.id.login_user_name);
		pwdET = (EditText) super.findViewById(R.id.login_user_pwd);
		loginBut = (Button) super.findViewById(R.id.login_but);

		backIB = (ImageButton) super.findViewById(R.id.user_login_back_ib);

		backIB.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		findViewById(R.id.login_seller).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(UserLoginActivity.this, SellerLoginActivity.class);
				startActivity(intent);
				finish();
			}
		});
		loginBut.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("username", nameET.getText().toString().trim()));
				params.add(new BasicNameValuePair("password", pwdET.getText().toString().trim()));
				params.add(new BasicNameValuePair("feedback_id", "1"));
				pb.setVisibility(View.VISIBLE);
				new Thread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						loginStatus = MyHttpPost.doPost("http://www.gelanghe.gov.cn/getUserLogin.php", params);
						System.out.println("json="+loginStatus);
						JSONArray infoStr = null;
						if (loginStatus.indexOf("username") != -1) {
							try{
							JSONObject resObj = new JSONObject(loginStatus);//JSONObject.parseObject(loginStatus);

							//Object jsonUser = resObj.get("user");
							JSONObject userObj = resObj.optJSONObject("user");//JSONObject.parseObject(jsonUser.toString());

							User user = new User();
							user.setGroup_id(userObj.getString("group_id"));
							user.setUsername(userObj.getString("username"));
							user.setPwd(pwdET.getText().toString().trim());

							MyApplication.getMyApplication().setLoginUser(user);
							infoStr = resObj.optJSONArray("info");
							new LogInUserDao().addUser(user);
							DatabaseManager.getInstance().closeDatabase();
							}catch(Exception e){
								e.printStackTrace();
								System.out.println("exception login");
							}
							
							
							if(infoStr != null){

								List<DoWork> doWorkList = new ArrayList<DoWork>();//JSON.parseArray(infoStr.toString(), DoWork.class);
								for(int i=0;i<infoStr.length();i++){
									DoWork item = JSON.parseObject(infoStr.optString(i), DoWork.class);
									doWorkList.add(item);
								}
								System.out.println("size="+doWorkList.size());
								if (doWorkList.size() > 0) {

									int listSize = doWorkList.size();
									DoWorkDao doWorkDao = new DoWorkDao();
									doWorkDao.deleteAll();

									for (int i = 0; i < listSize; i++) {
										doWorkDao.addDoWork(doWorkList.get(i));
									}
									DatabaseManager.getInstance().closeDatabase();
									
								}
							}
							
							myHandle.sendEmptyMessage(1);


						} else {
							myHandle.sendEmptyMessage(2);
						}

					}
				}).start();

			}
		});

	}

}
