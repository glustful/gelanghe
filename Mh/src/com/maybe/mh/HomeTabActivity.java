package com.maybe.mh;

import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;

import com.maybe.mh.pojo.TopAd;
import com.maybe.mh.util.ShowToast;
import com.tiandu.mh.R;

@SuppressWarnings("deprecation")
public class HomeTabActivity extends MyTabActivity {

	private View homeTabHost1 = null;
	private View homeTabHost2 = null;
	private View homeTabHost3 = null;
	private View homeTabHost4 = null;
	private View homeTabHost5 = null;
	private View homeTabHost6 = null;

	private TextView homeTab1;
	private TextView homeTab2;
	private TextView homeTab3;
	private TextView homeTab4;
	private TextView homeTab5;

	private TabHost homeTabHost;

	private ImageButton tabLeftBut;
	private ImageButton tabRightBut;

	private EditText searchET;
	private ImageButton searchIB;

	private ImageView adIV;

	private Button loginBut;
	
	private ProgressBar pb;
	private HorizontalScrollView mHorizontalScrollView;
	
	@SuppressLint("HandlerLeak")
	private class MyHandle extends Handler {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case 1:
				pb.setVisibility(View.GONE);
				break;

			default:

				break;
			}

		}

	}

	private MyHandle myHandle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.home_tab);
		myHandle =new MyHandle();
		
		pb = (ProgressBar) super.findViewById(R.id.home_tab_pgb);
		mHorizontalScrollView = (HorizontalScrollView) findViewById(R.id.horizontalScrollView);
		/*new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				while (true) {
					if (MyApplication.getMyApplication().isArticleDetailDLOK()) {

						myHandle.sendEmptyMessage(1);
						break;
					}

				}
			}
		}).start();*/

		tabLeftBut = (ImageButton) super.findViewById(R.id.home_tab_left_but);
		tabRightBut = (ImageButton) super.findViewById(R.id.home_tab_right_but);

		searchET = (EditText) super.findViewById(R.id.home_tab_search_et);
		searchIB = (ImageButton) super.findViewById(R.id.home_tab_search_but);
		loginBut = (Button) super.findViewById(R.id.home_tab_login_but);

		if (MyApplication.getMyApplication().getLoginUser() != null) {

			loginBut.setText(MyApplication.getMyApplication().getLoginUser().getUsername());

		}

		loginBut.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();

				if (MyApplication.getMyApplication().getLoginUser() != null) {
					intent.setClass(HomeTabActivity.this, LoginView.class);
				} else {
					intent.setClass(HomeTabActivity.this, UserLoginActivity.class);
				}

				startActivity(intent);
			}
		});

		adIV = (ImageView) super.findViewById(R.id.home_tab_ad_iv);

		adIV.setImageResource(R.drawable.tab_1_ad);

		searchIB.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (searchET.getText().toString().length() < 1) {
					ShowToast.showToastShort(HomeTabActivity.this, "请输入搜索内容");
				} else {
					Intent intent = new Intent();
					intent.putExtra("searchKey", searchET.getText().toString());
					intent.setClass(HomeTabActivity.this, SearchResultListActivity.class);
					startActivity(intent);
				}

			}
		});

		homeTabHost1 = getLayoutInflater().inflate(R.layout.home_tab_indicator_1, null);
		homeTabHost2 = getLayoutInflater().inflate(R.layout.home_tab_indicator_2, null);
		homeTabHost3 = getLayoutInflater().inflate(R.layout.home_tab_indicator_3, null);
		homeTabHost4 = getLayoutInflater().inflate(R.layout.home_tab_indicator_4, null);
		homeTabHost5 = getLayoutInflater().inflate(R.layout.home_tab_indicator_5, null);
		//homeTabHost6 = getLayoutInflater().inflate(R.layout.home_tab_indicator_1, null);

		homeTab1 = (TextView) homeTabHost1.findViewById(R.id.home_tab_indicator_1_tv);
		homeTab2 = (TextView) homeTabHost2.findViewById(R.id.home_tab_indicator_2_tv);
		homeTab3 = (TextView) homeTabHost3.findViewById(R.id.home_tab_indicator_3_tv);
		homeTab4 = (TextView) homeTabHost4.findViewById(R.id.home_tab_indicator_4_tv);
		homeTab5 = (TextView) homeTabHost5.findViewById(R.id.home_tab_indicator_5_tv);

		homeTab1.setText("推荐");
		homeTab2.setText("基层党建");
		homeTab3.setText("政务公开");
		homeTab4.setText("茶乡视频");
		homeTab5.setText("个人中心");
		//((TextView)homeTabHost6.findViewById(R.id.home_tab_indicator_1_tv)).setText("在格朗河");

		homeTabHost = getTabHost();

		homeTabHost.addTab(homeTabHost.newTabSpec("hometab1").setIndicator(homeTabHost1).setContent(new Intent(this, RecmmendListActivity.class)));
		homeTabHost.addTab(homeTabHost.newTabSpec("hometab2").setIndicator(homeTabHost2).setContent(new Intent(this, BasePartyLExpandListActivity.class)));
		homeTabHost.addTab(homeTabHost.newTabSpec("hometab3").setIndicator(homeTabHost3).setContent(new Intent(this, OpenJobExpandListActivity.class)));
		homeTabHost.addTab(homeTabHost.newTabSpec("hometab4").setIndicator(homeTabHost4).setContent(new Intent(this, TeaVideoActivity.class)));
		
		//homeTabHost.addTab(homeTabHost.newTabSpec("hometab6").setIndicator(homeTabHost6).setContent(new Intent(this, InHomeActivity.class)));
		homeTabHost.addTab(homeTabHost.newTabSpec("hometab5").setIndicator(homeTabHost5).setContent(new Intent(this, UserCenter.class)));
		homeTabHost.setCurrentTab(0);

		homeTabHost.setOnTabChangedListener(new OnTabChangeListener() {

			@Override
			public void onTabChanged(String tabId) {
				
				if(MyApplication.getMyApplication().map.get("topAds") == null){
					if (tabId.equalsIgnoreCase("hometab1")) {
						adIV.setImageResource(R.drawable.ic_launcher);
					} else if (tabId.equalsIgnoreCase("hometab2")) {
						adIV.setImageResource(R.drawable.ic_launcher);
					} else if (tabId.equalsIgnoreCase("hometab3")) {
						adIV.setImageResource(R.drawable.ic_launcher);
					} else if (tabId.equalsIgnoreCase("hometab4")) {
						adIV.setImageResource(R.drawable.ic_launcher);
					} else if (tabId.equalsIgnoreCase("hometab5")) {
						adIV.setImageResource(R.drawable.ic_launcher);
					}
				}else{
					List<TopAd> topAds = (List<TopAd>) MyApplication.getMyApplication().map.get("topAds");
					
					if (tabId.equalsIgnoreCase("hometab1")) {
						imageLoader.displayImage( MyApplication.getServerurl() + topAds.get(0).getLogo(), adIV);
					} else if (tabId.equalsIgnoreCase("hometab2")) {
						imageLoader.displayImage( MyApplication.getServerurl() + topAds.get(1).getLogo(), adIV);
					} else if (tabId.equalsIgnoreCase("hometab3")) {
						imageLoader.displayImage( MyApplication.getServerurl() + topAds.get(2).getLogo(), adIV);
					} else if (tabId.equalsIgnoreCase("hometab4")) {
						imageLoader.displayImage( MyApplication.getServerurl() + topAds.get(3).getLogo(), adIV);
					} 
					else if (tabId.equalsIgnoreCase("hometab5")) {
						imageLoader.displayImage( MyApplication.getServerurl() + topAds.get(topAds.size()-1).getLogo(), adIV);
					}
				}
				

				
			}
		});

		tabLeftBut.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int currentTab = homeTabHost.getCurrentTab();
				if (currentTab != 0) {
					homeTabHost.setCurrentTab(currentTab - 1);
					scrollToLocation(homeTabHost.getTabWidget().getChildTabViewAt(currentTab-1));
				}

			}
		});

		tabRightBut.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int currentTab = homeTabHost.getCurrentTab();
				if (currentTab != 4) {
					homeTabHost.setCurrentTab(currentTab + 1);
					scrollToLocation(homeTabHost.getTabWidget().getChildTabViewAt(currentTab+1));
				}
			}
		});

	}
	
	/*
	 * 
	 * 滚动到指定位置
	 */
	private void scrollToLocation(final View titleTwo){
		 mHorizontalScrollView.post(new Runnable() {
             @Override
             public void run() {
                
                 mHorizontalScrollView.smoothScrollTo(titleTwo.getMeasuredWidth()*(homeTabHost.getCurrentTab()-1), 0);
             }
         });
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
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		if (MyApplication.getMyApplication().getLoginUser() != null) {

			loginBut.setText(MyApplication.getMyApplication().getLoginUser().getUsername());

		}else{
			
			loginBut.setText("登录");
		}
		super.onResume();
	}

}
