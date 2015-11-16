package com.maybe.mh;

import com.maybe.mh.download.GetNewWorkService;
import com.maybe.mh.sqlite.DatabaseManager;
import com.maybe.mh.sqlite.SqliteHelper;
import com.maybe.mh.sqlite.StartServiceDao;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;

@SuppressLint("ResourceAsColor") public class SettingPageActivity extends MyActivity {

	private ImageButton backIB;

	private Button startServiceBut;
	private Button stopServiceBut;
	
	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.setting_page);
		context = SettingPageActivity.this;

		SqliteHelper sqliteHelper = new SqliteHelper(context);
		DatabaseManager.initializeInstance(sqliteHelper);
		

		backIB = (ImageButton) super.findViewById(R.id.setting_page_back_ib);
		startServiceBut = (Button) super.findViewById(R.id.setting_page_open_service);
		stopServiceBut = (Button) super.findViewById(R.id.setting_page_close_service);
		
		int isStart = new StartServiceDao().getStart();
		DatabaseManager.getInstance().closeDatabase();
		
		if(isStart == 0){
			stopServiceBut.setClickable(false);
			stopServiceBut.setBackgroundColor(R.color.but_unclickable);
		}else{
			startServiceBut.setClickable(false);
			startServiceBut.setBackgroundColor(R.color.but_unclickable);
		}
		
		
		backIB.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		startServiceBut.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent();
				i.setClass(SettingPageActivity.this, GetNewWorkService.class);
				startService(i);
				stopServiceBut.setClickable(true);
				startServiceBut.setClickable(false);
				new StartServiceDao().addStart(1);
				startServiceBut.setBackgroundColor(R.color.but_unclickable);
				stopServiceBut.setBackgroundResource(R.drawable.do_work_do_btn);
				DatabaseManager.getInstance().closeDatabase();
			}
		});

		stopServiceBut.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent();
				i.setClass(SettingPageActivity.this, GetNewWorkService.class);
				stopService(i);
				stopServiceBut.setClickable(false);
				startServiceBut.setClickable(true);
				stopServiceBut.setBackgroundColor(R.color.but_unclickable);
				startServiceBut.setBackgroundResource(R.drawable.do_work_do_btn);
				new StartServiceDao().addStart(0);
				DatabaseManager.getInstance().closeDatabase();
			}
		});

	}

}
