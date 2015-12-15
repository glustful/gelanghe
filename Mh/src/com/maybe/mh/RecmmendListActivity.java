package com.maybe.mh;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.maybe.mh.pojo.ArticleDetail;
import com.maybe.mh.sqlite.ArticleDetailDao;
import com.maybe.mh.sqlite.DatabaseManager;
import com.maybe.mh.sqlite.SqliteHelper;
import com.maybe.mh.util.ShowToast;
import com.tiandu.mh.R;

public class RecmmendListActivity extends MyActivity {

	private List<ArticleDetail> dataList = new ArrayList<ArticleDetail>();

	private ListView recommendLV;
	private PullToRefreshListView refreshLV;
	private int page = 1;    
	private Context context;
	private RecommendListAdapter adapter;

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
				List<ArticleDetail> result = new ArticleDetailDao().getAllArticleDetailByCategoryAndGroupId("tuijian", groupId,page,null);
				refreshLV.onRefreshComplete();
				if(dataList.size()==0 && (result==null || result.size()==0)){
					myHandle.sendEmptyMessageDelayed(1, 200);
					return;
				}
				if(result==null || result.size()<10){
					refreshLV.setMode(Mode.DISABLED);
				}else{
					page++;
				}
				dataList.addAll(result);
				DatabaseManager.getInstance().closeDatabase();
				adapter.notifyDataSetChanged();
				
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
		super.setContentView(R.layout.recommend_list);
		context = RecmmendListActivity.this;

		myHandle = new MyHandle();

		SqliteHelper sqliteHelper = new SqliteHelper(context);
		DatabaseManager.initializeInstance(sqliteHelper);
		
		initRefreshListView();
		//recommendLV = (ListView) super.findViewById(R.id.recommend_list_view);
		
		/*new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				while (true) {
					if (MyApplication.getMyApplication().isArticleDetailDLOK()) {

						
						break;
					}

				}
			}
		}).start();
*/		myHandle.sendEmptyMessage(1);
		recommendLV.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.putExtra("id", dataList.get(position-1).getArticle_id());
				intent.setClass(RecmmendListActivity.this, RecommendShowPageActivity.class);
				startActivity(intent);

			}
		});

		

	}
	
	private void initRefreshListView(){
		adapter = new RecommendListAdapter();
		refreshLV = (PullToRefreshListView) findViewById(R.id.recommend_list_view);
		refreshLV.setOnRefreshListener(new HowWillIrefresh());
		refreshLV.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
		recommendLV = refreshLV.getRefreshableView();
		
		recommendLV.setFastScrollEnabled(false);
		recommendLV.setFadingEdgeLength(0);
		recommendLV.setAdapter(adapter);
	}
	
	class HowWillIrefresh implements PullToRefreshBase.OnRefreshListener2<ListView>{

		@Override
		public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
			String label = DateUtils.formatDateTime(context,
                    System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
                            | DateUtils.FORMAT_SHOW_DATE
                            | DateUtils.FORMAT_ABBREV_ALL);
			refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
           			
		}

		@Override
		public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
			myHandle.sendEmptyMessage(1);
		}

		
	}

	class RecommendListAdapter extends BaseAdapter {

		class ViewHolder {
			public ImageView titelIV;
			public TextView titleTV;
			public TextView profileTV;
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
				view = getLayoutInflater().inflate(R.layout.recommend_list_item, null);
				holder = new ViewHolder();
				holder.titleTV = (TextView) view.findViewById(R.id.recommend_list_item_title_tv);
				holder.profileTV = (TextView) view.findViewById(R.id.recommend_list_item_profile_tv);
				holder.titelIV = (ImageView) view.findViewById(R.id.recommend_list_item_iv);
				view.setTag(holder);
			} else
				holder = (ViewHolder) view.getTag();

			holder.titleTV.setText(dataList.get(position).getTitle());
			holder.profileTV.setText("  " + dataList.get(position).getShortcon());

			imageLoader.displayImage(serverUrl + dataList.get(position).getImageurl(), holder.titelIV);

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
