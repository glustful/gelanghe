package com.maybe.mh;

import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.maybe.mh.pojo.ArticleDetail;
import com.maybe.mh.sqlite.ArticleDetailDao;
import com.maybe.mh.sqlite.DatabaseManager;
import com.maybe.mh.sqlite.SqliteHelper;
import com.maybe.mh.util.ShowToast;
import com.tiandu.mh.R;

public class TeaVideoActivity extends MyActivity {

	private List<ArticleDetail> dataList;

	private Context context;

	private GridView teaVideoGV;
	
	@SuppressLint("HandlerLeak")
	private class MyHandle extends Handler {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case 1:
				
				String groupId = "0";
				if(MyApplication.getMyApplication().getLoginUser() != null)
				{
					groupId = MyApplication.getMyApplication().getLoginUser().getGroup_id();
				}
				
				dataList = new ArticleDetailDao().getAllArticleDetailByCategoryAndGroupId("chaxiang", groupId);
				DatabaseManager.getInstance().closeDatabase();
				
				teaVideoGV.setAdapter(new TeaGridViewAdapter());
				if(dataList.size()==0 ){
					myHandle.sendEmptyMessageDelayed(1, 200);
					
				}
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
		super.setContentView(R.layout.tea_video_grid);

		context = TeaVideoActivity.this;
		myHandle = new MyHandle();


		SqliteHelper sqliteHelper = new SqliteHelper(context);
		DatabaseManager.initializeInstance(sqliteHelper);

		
		teaVideoGV = (GridView) super.findViewById(R.id.tea_video_gv);
		
		
		/*new Thread(new Runnable() {

			@Override
			public void run() {*/
				
						myHandle.sendEmptyMessage(1);
			/*	
			}
		}).start();*/
		

		

		teaVideoGV.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.putExtra("url", serverUrl + dataList.get(dataList.size() - position - 1).getVideourl());
				intent.setClass(TeaVideoActivity.this, VideoPlayer.class);
				startActivity(intent);
			}
		});

	}

	class TeaGridViewAdapter extends BaseAdapter {

		class ViewHolder {
			public TextView titleTV;
			public ImageView profileIV;
		}

		@Override
		public int getCount() {
			return dataList.size();
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			View view = convertView;
			final ViewHolder holder;
			if (convertView == null) {
				view = getLayoutInflater().inflate(R.layout.tea_video_grid_item, null);
				holder = new ViewHolder();
				holder.titleTV = (TextView) view.findViewById(R.id.tea_video_grid_item_tv);
				holder.profileIV = (ImageView) view.findViewById(R.id.tea_video_grid_item_iv);
				view.setTag(holder);
			} else
				holder = (ViewHolder) view.getTag();

			holder.titleTV.setText(dataList.get(dataList.size() - position - 1).getTitle());

			imageLoader.displayImage(serverUrl + dataList.get(dataList.size() - position - 1).getImageurl(), holder.profileIV);

			return view;
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
