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
import com.maybe.mh.eb.HTML5Activity;
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

public class SellerLoginActivity extends MyActivity {

	private EditText nameET;
	private EditText pwdET;
	private Button loginBut;
	private ImageButton backIB;
	private ProgressBar pb;
	
	private List<NameValuePair> params;

	private String loginStatus;

	private class MyHandle extends Handler {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			pb.setVisibility(View.GONE);
			switch (msg.what) {

			case 1:
				int size = msg.getData().getStringArrayList("orders").size();
				if(size > 0){
					ShowToast.showToastShort(MyApplication.getMyApplication().getApplicationContext(), "你有"+size+"条新订单未查看");
				}else{
					ShowToast.showToastShort(MyApplication.getMyApplication().getApplicationContext(), "登陆成功");
				}
				if(msg.obj==null || msg.obj.toString().equals("")){
					finish();
					return;
				}
					
					Intent intent = new Intent(SellerLoginActivity.this,HTML5Activity.class);
					intent.putExtra("url", msg.obj.toString());
					intent.putExtra("data", msg.getData().getStringArrayList("orders"));
					startActivity(intent);
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
		super.setContentView(R.layout.seller_login);
		context = SellerLoginActivity.this;
		
		SqliteHelper sqliteHelper = new SqliteHelper(context);
		DatabaseManager.initializeInstance(sqliteHelper);

		myHandle = new MyHandle();
		pb = (ProgressBar) findViewById(R.id.progressBar);
		nameET = (EditText) super.findViewById(R.id.login_user_name);
		pwdET = (EditText) super.findViewById(R.id.login_user_pwd);
		loginBut = (Button) super.findViewById(R.id.login_but);
		if(MyApplication.getMyApplication().getSellerUser() != null){
		nameET.setText(MyApplication.getMyApplication().getSellerUser().getUsername());
		pwdET.setText(MyApplication.getMyApplication().getSellerUser().getPwd());
		}
		backIB = (ImageButton) super.findViewById(R.id.user_login_back_ib);

		backIB.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
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
				params.add(new BasicNameValuePair("oid", "1"));
				pb.setVisibility(View.VISIBLE);
				new Thread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						loginStatus = MyHttpPost.doPost("http://www.gelanghe.gov.cn/glh2015/api/getSellerLogin.php", params);
						
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
							user.setRole("2");
							user.setSellerUrl(userObj.optString("url"));
							MyApplication.getMyApplication().setSellerUser(user);
							infoStr = resObj.optJSONArray("info");
							new LogInUserDao().addUser(user);
							DatabaseManager.getInstance().closeDatabase();
							
							
							ArrayList<String> orders = new ArrayList<String>();
							if(infoStr != null){

								
								for(int i=0;i<infoStr.length();i++){
									JSONObject item = infoStr.optJSONObject(i);
									if(item != null && item.optString("notice").equals("0")){
										orders.add(item.optString("order_id"));
									}
								}
								
							}
							Message msg = Message.obtain();
							msg.what = 1;
							Bundle bundle = new Bundle();
							bundle.putString("url", userObj.optString("url"));
							bundle.putStringArrayList("orders", orders);
							msg.obj = userObj.optString("url");
							
							msg.setData(bundle);
							myHandle.sendMessage(msg);
							}catch(Exception e){
								e.printStackTrace();
								System.out.println("exception login");
							}

						} else {
							myHandle.sendEmptyMessage(2);
						}

					}
				}).start();

			}
		});

	}

}
