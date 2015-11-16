package com.maybe.mh.eb;

import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.maybe.mh.DoWorkActivity;
import com.maybe.mh.DoWorkListActivity;
import com.maybe.mh.DoWorkShowPageActivity;
import com.maybe.mh.MainTabActivity;
import com.maybe.mh.MyActivity;
import com.maybe.mh.MyApplication;
import com.maybe.mh.R;
import com.maybe.mh.DoWorkActivity.IVOnClickListener;
import com.maybe.mh.DoWorkActivity.IVOnTouchListener;
import com.maybe.mh.pojo.ArticleDetail;
import com.maybe.mh.pojo.Category;
import com.maybe.mh.sqlite.ArticleDetailDao;
import com.maybe.mh.sqlite.CategoryDao;
import com.maybe.mh.sqlite.DatabaseManager;
import com.maybe.mh.sqlite.SqliteHelper;
import com.maybe.mh.util.ShowToast;

/*
 * 电商中心主Activity入口
 */
public class EBActivity extends MyActivity {
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

	private Animation myAnimation;

	private IVOnTouchListener onTouchListener;

	private IVOnClickListener onClickListener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.electronic_business);

		iv01 = (ImageView) super.findViewById(R.id.eb1);
		iv02 = (ImageView) super.findViewById(R.id.eb2);
		iv03 = (ImageView) super.findViewById(R.id.eb3);
		iv04 = (ImageView) super.findViewById(R.id.eb4);
		iv05 = (ImageView) super.findViewById(R.id.eb5);
		iv06 = (ImageView) super.findViewById(R.id.eb6);
		iv07 = (ImageView) super.findViewById(R.id.eb7);
		iv08 = (ImageView) super.findViewById(R.id.eb8);
		iv09 = (ImageView) super.findViewById(R.id.eb9);
		iv10 = (ImageView) super.findViewById(R.id.eb10);
		iv11 = (ImageView) super.findViewById(R.id.eb11);
		iv12 = (ImageView) super.findViewById(R.id.eb12);

		myAnimation = AnimationUtils.loadAnimation(this, R.anim.anim_scale);

		onTouchListener = new IVOnTouchListener();
		onClickListener = new IVOnClickListener();

		findViewById(R.id.eb_button1).setOnClickListener(onClickListener);
		findViewById(R.id.eb_button2).setOnClickListener(onClickListener);

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
			switch(v.getId()){
			case R.id.eb_button1: //为民中心
				((MainTabActivity)getParent()).getMainTabHost().setCurrentTab(1);
				break;
			case R.id.eb_button2: //会员中心
				break;
			case R.id.eb1:
				break;
			case R.id.eb2:
				break;
			case R.id.eb3:
				break;
			case R.id.eb4:
				break;
			case R.id.eb5:
				break;
			case R.id.eb6:
				break;
			case R.id.eb7:
				break;
			case R.id.eb8:
				break;
			case R.id.eb9:
				break;
			case R.id.eb10:
				break;
			case R.id.eb11:
				break;
			case R.id.eb12:
				break;
			}
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
