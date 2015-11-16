package com.maybe.mh;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;

import android.app.ProgressDialog;
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

import com.alibaba.fastjson.JSONObject;
import com.maybe.mh.pojo.DoWork;
import com.maybe.mh.pojo.User;
import com.maybe.mh.sqlite.DatabaseManager;
import com.maybe.mh.sqlite.DoWorkDao;
import com.maybe.mh.sqlite.LogInUserDao;
import com.maybe.mh.sqlite.SqliteHelper;
import com.maybe.mh.util.MyHttpPost;

public class MyWorkListActivity extends MyActivity {

	private ImageButton backIB;
	private ListView myWorkLV;
	private List<DoWork> dataList;
	private Context context;
	private ProgressDialog progressDialog;
	private MyWorkListAdapter listAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.my_work_list);

		context = MyWorkListActivity.this;

		SqliteHelper sqliteHelper = new SqliteHelper(context);
		DatabaseManager.initializeInstance(sqliteHelper);

		backIB = (ImageButton) super.findViewById(R.id.my_work_list_back_ib);
		myWorkLV = (ListView) super.findViewById(R.id.my_work_lv);
		progressDialog = ProgressDialog.show(MyWorkListActivity.this, "请稍等",
				"数据正在加载中...", true, false);
		new Thread() {

			@Override
			public void run() {
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				List<User> userList = new LogInUserDao().getAlluser();
				params.add(new BasicNameValuePair("username", userList.get(0)
						.getUsername()));
				params.add(new BasicNameValuePair("password", userList.get(0)
						.getPwd()));
				params.add(new BasicNameValuePair("feedback_id", "1"));
				String loginStatus = MyHttpPost.doPost(
						"http://www.gelanghe.gov.cn/getUserLogin.php", params);
				Message msg = handler.obtainMessage();
				msg.obj = loginStatus;
				handler.sendMessage(msg);
			}
		}.start();

		// dataList = new DoWorkDao().getAllDoWork();
		//
		// DatabaseManager.getInstance().closeDatabase();
		// myWorkLV.setAdapter(new MyWorkListAdapter());

		myWorkLV.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(MyWorkListActivity.this,WorkDetailActivity.class);
				intent.putExtra("id", dataList.get(position).getFeedback_id());
				startActivity(intent);

			}
		});

		backIB.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			try {
				// JSONObject resObj =
				// JSONObject.parseObject(msg.obj.toString());
				// Object jsonUser = resObj.get("user");
				// Object infoStr = resObj.get("info");
				// if(infoStr != null){
				//
				// List<DoWork> doWorkList = JSON.parseArray(infoStr.toString(),
				// DoWork.class);
				//
				// if (doWorkList.size() > 0) {
				//
				// int listSize = doWorkList.size();
				// DoWorkDao doWorkDao = new DoWorkDao();
				// doWorkDao.deleteAll();
				//
				// for (int i = 0; i < listSize; i++) {
				// doWorkDao.addDoWork(doWorkList.get(i));
				// }
				// DatabaseManager.getInstance().closeDatabase();
				// dataList =doWorkList;
				// }
				// }
				JSONObject resObj =JSONObject.parseObject(msg.obj.toString());
				Object jsonUser = resObj.get("user");
				Object infoStr = resObj.get("info");
				org.json.JSONArray jsonArray = new JSONArray((infoStr.toString()));
				org.json.JSONObject obj = null;
				DoWork doWork = null;
				List<DoWork> doWorkList = new ArrayList<DoWork>();
				for (int i = 0; i < jsonArray.length(); i++) {

					obj = jsonArray.getJSONObject(i);
					doWork = new DoWork();
					doWork.setFeedback_id(obj.getInt("feedback_id"));
					doWork.setCategory_id(obj.getInt("category_id"));
					doWork.setArticle_id(obj.getInt("article_id"));
					doWork.setTitle(obj.getString("title"));
					doWork.setName(obj.getString("name"));
					doWork.setTel(obj.getString("tel"));
					doWork.setFiles(obj.getString("files"));
					doWork.setContents(obj.getString("contents"));
					doWork.setTime(obj.getLong("time"));
					doWorkList.add(doWork);
				}

				if (doWorkList.size() > 0) {
					int listSize = doWorkList.size();
					DoWorkDao doWorkDao = new DoWorkDao();
					doWorkDao.deleteAll();

					for (int i = 0; i < listSize; i++) {
						doWorkDao.addDoWork(doWorkList.get(i));
					}
					DatabaseManager.getInstance().closeDatabase();
					dataList = doWorkList;
				}
				// ------------------------------------------------------------

			} catch (Exception e) {
				// TODO: handle exception
			}
			listAdapter = new MyWorkListAdapter();
			myWorkLV.setAdapter(listAdapter);
			progressDialog.dismiss();

		}
	};

	class MyWorkListAdapter extends BaseAdapter {

		class ViewHolder {
			public TextView titleTV;
			public TextView profileTV;
			public TextView timeTV;//my_work_list_item_time_tv
		}

		@Override
		public int getCount() {
			// return dataList.size();
			if (null != dataList)
				return dataList.size();
			return 0;
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
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			View view = convertView;
			final ViewHolder holder;
			if (convertView == null) {
				view = getLayoutInflater().inflate(R.layout.my_work_list_item,null);
				holder = new ViewHolder();
				holder.titleTV = (TextView) view.findViewById(R.id.my_work_list_item_category_tv);
				holder.profileTV = (TextView) view.findViewById(R.id.my_work_list_item_title_tv);
				holder.timeTV=(TextView) view.findViewById(R.id.my_work_list_item_time_tv);
				view.setTag(holder);
			} else
				holder = (ViewHolder) view.getTag();

			// holder.titleTV.setText(dataList.get(dataList.size() - position -
			// 1).getTitle());
			// holder.profileTV.setText("  " + dataList.get(dataList.size() -
			// position - 1).getContents());
			holder.titleTV.setText(dataList.size()-position+"."+dataList.get(position).getTitle());
			holder.profileTV.setText("  "+ dataList.get(position).getContents());
			holder.timeTV.setText( getStrTime(dataList.get(position).getTime()));
			return view;
		}
	}
	
	// 将时间戳转为字符串
	 public static String getStrTime(long cc_time) {
	  String re_StrTime = null;
	  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	  // 例如：cc_time=1291778220
	  re_StrTime = sdf.format(new Date(cc_time * 1000L));
	  return re_StrTime;
	 }

}
