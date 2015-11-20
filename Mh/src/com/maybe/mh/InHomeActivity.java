package com.maybe.mh;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.maybe.mh.util.ShowToast;
import com.tiandu.mh.R;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class InHomeActivity extends MyActivity implements OnPageChangeListener{

	private ImageView iv01;
	private ImageView iv02;
	private ImageView iv03;
	private ImageView iv04;
	private ImageView iv05;
	
	private Animation myAnimation;

	private IVOnTouchListener onTouchListener;

	private IVOnClickListener onClickListener;
	//Viewpager
	private List<ImageView> imageViewList;
	private TextView tvDescription;
	private LinearLayout llPoints;
	private String[] imageDescriptions;
	private int previousSelectPosition = 0;
	private ViewPager mViewPager;
	private boolean isLoop = true;
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.home_item_where);
		mViewPager = (ViewPager) findViewById(R.id.viewpager);
		iv01 = (ImageView) super.findViewById(R.id.ih1);
		iv02 = (ImageView) super.findViewById(R.id.ih2);
		iv03 = (ImageView) super.findViewById(R.id.ih3);
		iv04 = (ImageView) super.findViewById(R.id.ih4);
		iv05 = (ImageView) super.findViewById(R.id.ih5);
		

		myAnimation = AnimationUtils.loadAnimation(this, R.anim.anim_scale);

		onTouchListener = new IVOnTouchListener();
		onClickListener = new IVOnClickListener();

		iv01.setOnTouchListener(onTouchListener);
		iv02.setOnTouchListener(onTouchListener);
		iv03.setOnTouchListener(onTouchListener);
		iv04.setOnTouchListener(onTouchListener);
		iv05.setOnTouchListener(onTouchListener);
		

		iv01.setOnClickListener(onClickListener);
		iv02.setOnClickListener(onClickListener);
		iv03.setOnClickListener(onClickListener);
		iv04.setOnClickListener(onClickListener);
		iv05.setOnClickListener(onClickListener);
		setView();
		initView();

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
			case R.id.ih1:
				break;
			case R.id.ih2:
				break;
			case R.id.ih3:
				break;
			case R.id.ih4:
				break;
			case R.id.ih5:
				break;
			}

		}
	}
		
	public void setView() {
		// 自动切换页面功能
		new Thread(new Runnable() {

			@Override
			public void run() {
				while (isLoop) {
					SystemClock.sleep(2000);
					handler.sendEmptyMessage(0);
				}
			}
		}).start();
	}

	public void initView() {
		mViewPager = (ViewPager) findViewById(R.id.viewpager);
    	tvDescription = (TextView) findViewById(R.id.tv_image_description);
    	llPoints = (LinearLayout) findViewById(R.id.ll_points);
    	
    	prepareData();
    	
    	ViewPagerAdapter adapter = new ViewPagerAdapter(imageViewList);
    	mViewPager.setAdapter(adapter);
    	mViewPager.setOnPageChangeListener(this);
    	
    	tvDescription.setText(imageDescriptions[previousSelectPosition]);
    	llPoints.getChildAt(previousSelectPosition).setEnabled(true);
    	
    	/**
    	 * 2147483647 / 2 = 1073741820 - 1 
    	 * 设置ViewPager的当前项为一个比较大的数，以便一开始就可以左右循环滑动
    	 */
    	int n = Integer.MAX_VALUE / 2 % imageViewList.size();
    	int itemPosition = Integer.MAX_VALUE / 2 - n;
    	
    	mViewPager.setCurrentItem(itemPosition);
	}
	
	 private void prepareData() {
	    	imageViewList = new ArrayList<ImageView>();
	    	int[] imageResIDs = getImageResIDs();
	    	imageDescriptions = getImageDescription();
	    	
	    	ImageView iv;
	    	View view;
	    	for (int i = 0; i < imageResIDs.length; i++) {
				iv = new ImageView(this);
				iv.setBackgroundResource(imageResIDs[i]);
				imageViewList.add(iv);
				
				// 添加点view对象
				view = new View(this);
				//view.setBackgroundDrawable(getResources().getDrawable(R.drawable.point_background));
				LayoutParams lp = new LayoutParams(5, 5);
				lp.leftMargin = 10;
				view.setLayoutParams(lp);
				view.setEnabled(false);
				llPoints.addView(view);
			}
	    }
	    
	    private int[] getImageResIDs() {
	    	return new int[]{
	    			R.drawable.ih_cyms,
	    			R.drawable.ih_jdzs,
	    			R.drawable.ih_mzgy,
	    			R.drawable.ih_ntcp,
	    			R.drawable.ih_xxyl
	    	};
	    }
	    
	    private String[] getImageDescription() {
	    	return new String[]{
	    			"第一个引导页面",
	    			"第二个引导页面",
	    			"第三个引导页面",
	    			"第四个引导页面",
	    			"第五个引导页面"
	    	};
	    }

		@Override
		public void onPageScrollStateChanged(int arg0) {
			
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			
		}

		@Override
		public void onPageSelected(int position) {
			// 改变图片的描述信息
			tvDescription.setText(imageDescriptions[position % imageViewList.size()]);
			// 切换选中的点,把前一个点置为normal状态
			llPoints.getChildAt(previousSelectPosition).setEnabled(false);
			// 把当前选中的position对应的点置为enabled状态
			llPoints.getChildAt(position % imageViewList.size()).setEnabled(true);
			previousSelectPosition = position  % imageViewList.size();
		}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		isLoop = false;
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


