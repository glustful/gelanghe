package com.maybe.mh;

import java.util.List;

import com.maybe.mh.pojo.ArticleDetail;
import com.maybe.mh.sqlite.ArticleDetailDao;
import com.maybe.mh.sqlite.CategoryDao;
import com.maybe.mh.sqlite.DatabaseManager;
import com.maybe.mh.sqlite.SqliteHelper;
import com.maybe.mh.util.ShowToast;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class SearchResultListActivity extends MyActivity {

	private EditText searchET;
	private ImageButton searchIB;

	private ListView resultLV;

	private List<ArticleDetail> dataList;

	private Context context;

	private String searchKey;

	private ListViewAdapter listViewAdapter;

	private ImageButton backIB;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.search_result_list_view);
		context = SearchResultListActivity.this;

		Bundle bundle = getIntent().getExtras();

		searchKey = bundle.getString("searchKey");

		SqliteHelper sqliteHelper = new SqliteHelper(context);
		DatabaseManager.initializeInstance(sqliteHelper);

		dataList = new ArticleDetailDao().getArticleDetailBySearchKey(searchKey);
		DatabaseManager.getInstance().closeDatabase();

		searchET = (EditText) super.findViewById(R.id.search_result_search_et);
		searchIB = (ImageButton) super.findViewById(R.id.search_result_search_but);
		backIB = (ImageButton) super.findViewById(R.id.search_back_ib);

		backIB.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		resultLV = (ListView) super.findViewById(R.id.search_result_lv);

		listViewAdapter = new ListViewAdapter();

		resultLV.setAdapter(listViewAdapter);

		resultLV.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.putExtra("id", dataList.get(position).getArticle_id());

				CategoryDao categoryDao = new CategoryDao();

				if (categoryDao.getParentIdByCategory(dataList.get(position).getCategory()) == 5) {
					intent.setClass(SearchResultListActivity.this, DoWorkShowPageActivity.class);
				} else {
					intent.setClass(SearchResultListActivity.this, RecommendShowPageActivity.class);
				}

				startActivity(intent);
			}
		});

		searchIB.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (searchET.getText().toString().length() < 1) {
					ShowToast.showToastShort(SearchResultListActivity.this, "请输入搜索内容");
				} else {
					dataList = new ArticleDetailDao().getArticleDetailBySearchKey(searchET.getText().toString());
					DatabaseManager.getInstance().closeDatabase();
					listViewAdapter.notifyDataSetChanged();
				}
			}
		});

	}

	class ListViewAdapter extends BaseAdapter {

		class ItemHolder {
			TextView categoryTV;
			TextView titleTV;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return dataList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View view = convertView;
			ItemHolder itemHolder = new ItemHolder();

			if (convertView == null) {
				view = getLayoutInflater().inflate(R.layout.search_result_list_item, null);
				itemHolder.categoryTV = (TextView) view.findViewById(R.id.search_result_list_item_category_tv);
				itemHolder.titleTV = (TextView) view.findViewById(R.id.search_result_list_item_title_tv);
				view.setTag(itemHolder);

			} else {
				itemHolder = (ItemHolder) view.getTag();
			}

			itemHolder.categoryTV.setText(dataList.get(position).getCategory());
			itemHolder.titleTV.setText(dataList.get(position).getTitle());

			return view;
		}

	}

}
