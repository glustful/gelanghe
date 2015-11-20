package com.maybe.mh;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.maybe.mh.pojo.ArticleDetail;
import com.maybe.mh.pojo.Category;
import com.maybe.mh.sqlite.ArticleDetailDao;
import com.maybe.mh.sqlite.CategoryDao;
import com.maybe.mh.sqlite.DatabaseManager;
import com.maybe.mh.sqlite.SqliteHelper;
import com.tiandu.mh.R;

public class DoWorkListActivity extends MyActivity {

	private ListView doWorkLV;

	private ArrayList<String> alias;

	private List<ArticleDetail> dataList;

	private Context context;

	private ImageButton backIB;

	private TextView categoryTV;
	
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
				
				dataList = new ArticleDetailDao().getAllArticleDetailByCategoryAndGroupId(alias.get(0), groupId);
				
				DatabaseManager.getInstance().closeDatabase();
				
				doWorkLV.setAdapter(new DoWorkListAdapter());

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
		super.setContentView(R.layout.do_work_list);

		context = DoWorkListActivity.this;
		
		myHandle = new MyHandle();

		Bundle bundle = getIntent().getExtras();
		alias = bundle.getStringArrayList("alias");

		SqliteHelper sqliteHelper = new SqliteHelper(context);
		DatabaseManager.initializeInstance(sqliteHelper);

		
		doWorkLV = (ListView) super.findViewById(R.id.do_work_lv);
		backIB = (ImageButton) super.findViewById(R.id.do_work_list_back_ib);
		categoryTV = (TextView) super.findViewById(R.id.do_work_list_category_name_tv);
		
		Category category = new CategoryDao().getCategoryByAlias(alias.get(0));
		
		categoryTV.setText(category.getCategory());
		

		backIB.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();

			}
		});
		
		
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
		

		

		doWorkLV.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> paremt, View view, int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.putExtra("id", dataList.get(position).getArticle_id());
				intent.setClass(DoWorkListActivity.this, DoWorkShowPageActivity.class);
				startActivity(intent);
			}
		});

	}

	class DoWorkListAdapter extends BaseAdapter {

		class ViewHolder {
			public TextView nameTV;

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
				view = getLayoutInflater().inflate(R.layout.do_work_list_item, null);
				holder = new ViewHolder();
				holder.nameTV = (TextView) view.findViewById(R.id.do_work_list_item_tv);
				view.setTag(holder);
			} else
				holder = (ViewHolder) view.getTag();

			holder.nameTV.setText(dataList.get(position).getTitle());

			return view;
		}
	}

}
