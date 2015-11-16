package com.maybe.mh;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.maybe.mh.pojo.User;
import com.maybe.mh.sqlite.DatabaseManager;
import com.maybe.mh.sqlite.LogInUserDao;
import com.maybe.mh.util.ShowToast;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class DoWorkActivity extends MyActivity {

	private ImageView iv01;
	private ImageView iv02;
	private ImageView iv03;
	private ImageView iv04;
	private ImageView iv05;
	private ImageView iv06;
	private ImageView iv07;
	private ImageView iv08;
	private ImageView iv09;
	private ImageView iv10;
	private ImageView iv11;
	private ImageView iv12;
	private ImageView iv13;
	private ImageView iv14;

	private Animation myAnimation;

	private IVOnTouchListener onTouchListener;

	private IVOnClickListener onClickListener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.do_work);

		iv01 = (ImageView) super.findViewById(R.id.do_work_iv_1);
		iv02 = (ImageView) super.findViewById(R.id.do_work_iv_2);
		iv03 = (ImageView) super.findViewById(R.id.do_work_iv_3);
		iv04 = (ImageView) super.findViewById(R.id.do_work_iv_4);
		iv05 = (ImageView) super.findViewById(R.id.do_work_iv_5);
		iv06 = (ImageView) super.findViewById(R.id.do_work_iv_6);
		iv07 = (ImageView) super.findViewById(R.id.do_work_iv_7);
		iv08 = (ImageView) super.findViewById(R.id.do_work_iv_8);
		iv09 = (ImageView) super.findViewById(R.id.do_work_iv_9);
		iv10 = (ImageView) super.findViewById(R.id.do_work_iv_10);
		iv11 = (ImageView) super.findViewById(R.id.do_work_iv_11);
		iv12 = (ImageView) super.findViewById(R.id.do_work_iv_12);
		iv13 = (ImageView) super.findViewById(R.id.do_work_iv_13);
		iv14 = (ImageView) super.findViewById(R.id.do_work_iv_14);

		myAnimation = AnimationUtils.loadAnimation(this, R.anim.anim_scale);

		onTouchListener = new IVOnTouchListener();
		onClickListener = new IVOnClickListener();

		iv01.setOnTouchListener(onTouchListener);
		iv02.setOnTouchListener(onTouchListener);
		iv03.setOnTouchListener(onTouchListener);
		iv04.setOnTouchListener(onTouchListener);
		iv05.setOnTouchListener(onTouchListener);
		iv06.setOnTouchListener(onTouchListener);
		iv07.setOnTouchListener(onTouchListener);
		iv08.setOnTouchListener(onTouchListener);
		iv09.setOnTouchListener(onTouchListener);
		iv10.setOnTouchListener(onTouchListener);
		iv11.setOnTouchListener(onTouchListener);
		iv12.setOnTouchListener(onTouchListener);
		iv13.setOnTouchListener(onTouchListener);
		iv14.setOnTouchListener(onTouchListener);

		iv01.setOnClickListener(onClickListener);
		iv02.setOnClickListener(onClickListener);
		iv03.setOnClickListener(onClickListener);
		iv04.setOnClickListener(onClickListener);
		iv05.setOnClickListener(onClickListener);
		iv06.setOnClickListener(onClickListener);
		iv07.setOnClickListener(onClickListener);
		iv08.setOnClickListener(onClickListener);
		iv09.setOnClickListener(onClickListener);
		iv10.setOnClickListener(onClickListener);
		iv11.setOnClickListener(onClickListener);
		iv12.setOnClickListener(onClickListener);
		iv13.setOnClickListener(onClickListener);
		iv14.setOnClickListener(onClickListener);

	}

	public class IVOnTouchListener implements OnTouchListener {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// TODO Auto-generated method stub
			v.startAnimation(myAnimation);
			return false;
		}
	}

	public class IVOnClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent();
			ArrayList<String> category = new ArrayList<String>();
			if (v == iv01) {
				//category = "jihuashengyu or wenhua"; //1
				category.add(0,"jihuashengyu");
				category.add("wenhua");
				
			} else if (v == iv02) {
				category.add("caijingongjing");
			} else if (v == iv03) {
				category.add("shangfangshuqiu");//3 维稳中心
			} else if (v == iv04) {
				category.add("paichusuogongzuo");
			} else if (v == iv05) {
				category.add("cunzhenguihuajianshe");
			} else if (v == iv06) {
				category.add("linye");
			} else if (v == iv07) {
				category.add("guotusuogongzuo");
			} else if (v == iv08) {
				//category.add("wenhua");//2  督办中心
				List<User> userList = new LogInUserDao().getAlluser();
				DatabaseManager.getInstance().closeDatabase();
				
				if (userList.size() > 0) {
					intent.setClass(DoWorkActivity.this, MyWorkListActivity.class);
				} else {
					intent.setClass(DoWorkActivity.this, UserLoginActivity.class);
				}
				startActivity(intent);
				return;
			} else if (v == iv09) {
				category.add("shuiligongzuo");
			} else if (v == iv10) {
				category.add("minzhenggongzuo");
			} else if (v == iv11) {
				category.add(0,"sifasuo");
				category.add("shangfangshuqiu");
			} else if (v == iv12) {
				category.add("shouyi");
			} else if (v == iv13) {
				category.add("nongyezonghe");
			} else if (v == iv14) {
				category.add("laodongheshehuibaozhang");
			}
			intent.putExtra("alias", category);
			
			intent.setClass(DoWorkActivity.this, DoWorkListActivity.class);
			startActivity(intent);

		}
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
