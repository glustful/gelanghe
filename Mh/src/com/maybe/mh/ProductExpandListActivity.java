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
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.maybe.mh.pojo.ArticleDetail;
import com.maybe.mh.pojo.Category;
import com.maybe.mh.sqlite.ArticleDetailDao;
import com.maybe.mh.sqlite.CategoryDao;
import com.maybe.mh.sqlite.DatabaseManager;
import com.maybe.mh.sqlite.SqliteHelper;
import com.maybe.mh.util.ShowToast;
import com.tiandu.mh.R;

public class ProductExpandListActivity extends MyActivity {

	private List<Category> categoryList;

	private Context context;

	private ExpandableListView expandLV;

	private List<ArticleDetail> dataList;

	private List<List<ArticleDetail>> articleGroupList = new ArrayList<List<ArticleDetail>>();
	
	

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
				
				categoryList = new CategoryDao().getCategoryByParentId(4);
				DatabaseManager.getInstance().closeDatabase();

				if (categoryList.size() > 0) {
					int listSize = categoryList.size();

					ArticleDetailDao detailDao = new ArticleDetailDao();

					for (int i = 0; i < listSize; i++) {

						dataList = detailDao.getAllArticleDetailByCategoryAndGroupId(categoryList.get(i).getAlias(), groupId);

						articleGroupList.add(dataList);
					}

					DatabaseManager.getInstance().closeDatabase();

				}

				
				expandLV.setAdapter(new ExpandListViewAdapter());

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
		super.setContentView(R.layout.product_expand_list);
		
		myHandle = new MyHandle();

		context = ProductExpandListActivity.this;

		SqliteHelper sqliteHelper = new SqliteHelper(context);
		DatabaseManager.initializeInstance(sqliteHelper);



		expandLV = (ExpandableListView) super.findViewById(R.id.product_elv);


		new Thread(new Runnable() {

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
		}).start();

	}

	class ExpandListViewAdapter extends BaseExpandableListAdapter {

		class ParentView {
			TextView pTV;
			TextView countTV;
		}

		class ChildView {
			ListView cLV;
		}

		@Override
		public Object getChild(int groupPosition, int childPosition) {
			// TODO Auto-generated method stub
			return groupPosition;
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			// TODO Auto-generated method stub
			return childPosition;
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			// TODO Auto-generated method stub
			if (articleGroupList.size() > 0) {
				return articleGroupList.get(groupPosition).size();
			} else {
				return 0;
			}
		}

		@Override
		public Object getGroup(int groupPosition) {
			// TODO Auto-generated method stub
			return groupPosition;
		}

		@Override
		public int getGroupCount() {
			// TODO Auto-generated method stub
			return categoryList.size();
		}

		@Override
		public long getGroupId(int groupPosition) {
			// TODO Auto-generated method stub
			return groupPosition;
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View view = convertView;
			ParentView parentView;
			if (convertView == null) {
				view = getLayoutInflater().inflate(R.layout.open_job_expand_list_item, null);
				parentView = new ParentView();
				parentView.pTV = (TextView) view.findViewById(R.id.open_job_expand_list_item_tv);
				parentView.countTV = (TextView) view.findViewById(R.id.open_job_expand_list_item_count_tv);
				view.setTag(parentView);
			} else
				parentView = (ParentView) view.getTag();

			parentView.pTV.setText(categoryList.get(groupPosition).getCategory());

			parentView.countTV.setText("(" + getChildrenCount(groupPosition) + ")");

			return view;

		}

		@Override
		public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub

			final int gPosition = groupPosition;
			final int cPosition = childPosition;
			View view = convertView;
			ChildView childView;
			if (convertView == null) {
				view = getLayoutInflater().inflate(R.layout.product_list, null);
				childView = new ChildView();
				childView.cLV = (ListView) view.findViewById(R.id.product_lv);
				view.setTag(childView);
			} else
				childView = (ChildView) view.getTag();

			childView.cLV.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

					Intent intent = new Intent();
					intent.putExtra("id", articleGroupList.get(gPosition).get(cPosition).getArticle_id());
					intent.setClass(ProductExpandListActivity.this, RecommendShowPageActivity.class);
					startActivity(intent);

				}
			});

			childView.cLV.setAdapter(new BaseAdapter() {

				class ViewHolder {
					public TextView nameTV;
					public TextView profileTV;
					public ImageView titleIV;
				}

				@Override
				public View getView(int position, View convertView, ViewGroup parent) {
					// TODO Auto-generated method stub

					View view = convertView;
					final ViewHolder holder;
					if (convertView == null) {
						view = getLayoutInflater().inflate(R.layout.product_list_item, null);
						holder = new ViewHolder();
						holder.nameTV = (TextView) view.findViewById(R.id.product_list_item_product_name_tv);
						holder.profileTV = (TextView) view.findViewById(R.id.product_list_item_product_profile_tv);
						holder.titleIV = (ImageView) view.findViewById(R.id.product_list_item_product_image_iv);
						view.setTag(holder);
					} else
						holder = (ViewHolder) view.getTag();

					if (articleGroupList.size() > 0) {

						if (articleGroupList.get(gPosition).size() > 0) {

							holder.nameTV.setText(articleGroupList.get(gPosition).get(cPosition).getTitle());
							holder.profileTV.setText(articleGroupList.get(gPosition).get(cPosition).getShortcon());

							imageLoader.displayImage(serverUrl + articleGroupList.get(gPosition).get(cPosition).getImageurl(), holder.titleIV);
						}

					}

					return view;
				}

				@Override
				public long getItemId(int position) {
					// TODO Auto-generated method stub
					return position;
				}

				@Override
				public Object getItem(int position) {
					// TODO Auto-generated method stub
					return position;
				}

				@Override
				public int getCount() {
					// TODO Auto-generated method stub
					if (articleGroupList.size() > 0) {
						return articleGroupList.get(gPosition).size();
					} else {
						return 0;
					}
				}
			});

			return view;
		}

		@Override
		public boolean hasStableIds() {
			// TODO Auto-generated method stub
			return true;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			// TODO Auto-generated method stub

			return true;
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
