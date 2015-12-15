package com.maybe.mh;

import com.maybe.mh.sqlite.DatabaseManager;
import com.maybe.mh.sqlite.DoWorkDao;
import com.maybe.mh.sqlite.LogInUserDao;
import com.maybe.mh.sqlite.SqliteHelper;
import com.tiandu.mh.R;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class LoginView extends MyActivity {

	private TextView userGroupTV;
	private TextView userNameTV;

	private Button logoutBut;
	private ImageButton backIB;

	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.user_info_show);

		context = LoginView.this;

		SqliteHelper sqliteHelper = new SqliteHelper(context);
		DatabaseManager.initializeInstance(sqliteHelper);

		userGroupTV = (TextView) super.findViewById(R.id.user_info_group_tv);
		userNameTV = (TextView) super.findViewById(R.id.user_info_name_tv);
		logoutBut = (Button) super.findViewById(R.id.user_info_logout_but);
		backIB = (ImageButton) super.findViewById(R.id.user_info_back_ib);

		userGroupTV.setText(MyApplication.getMyApplication().getLoginUser().getGroup_id() + "");
		userNameTV.setText(MyApplication.getMyApplication().getLoginUser().getUsername() + "");

		backIB.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		logoutBut.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MyApplication.getMyApplication().setLoginUser(null);
				new LogInUserDao().deleteAll();
				DatabaseManager.getInstance().closeDatabase();
				new DoWorkDao().deleteAll();
				DatabaseManager.getInstance().closeDatabase();
				finish();
			}
		});
		
		findViewById(R.id.user_info_login_but).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(LoginView.this, SellerLoginActivity.class);
				startActivity(intent);
				finish();
				
			}
		});

	}

}
