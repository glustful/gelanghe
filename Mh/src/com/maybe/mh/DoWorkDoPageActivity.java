package com.maybe.mh;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.maybe.mh.sqlite.CategoryDao;
import com.maybe.mh.sqlite.DatabaseManager;
import com.maybe.mh.sqlite.SqliteHelper;
import com.maybe.mh.util.FormatStr;
import com.maybe.mh.util.MyHttpPost;
import com.maybe.mh.util.ShowToast;

public class DoWorkDoPageActivity extends MyActivity {

	private ImageButton backIB;

	private EditText nameET;
	private EditText telET;
	private EditText contentsET;

	private Button selectFileBut;
	private Button submitBut;

	private ListView fileLV;

	public static final int FILE_RESULT_CODE = 1;

	private ListViewAdapter listViewAdapter;

	private List<NameValuePair> params;

	private String uploadStatus;

	private class MyHandle extends Handler {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {

			case 1:
				ShowToast.showToastShort(MyApplication.getMyApplication().getApplicationContext(), "提交失败");
				break;
			case 2:
				ShowToast.showToastShort(MyApplication.getMyApplication().getApplicationContext(), "提交成功");

				finish();
				break;

			default:
				break;
			}
		}

	}

	private MyHandle myHandle;

	private int articleId;

	private String category;

	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.do_work_do_page);
		context = DoWorkDoPageActivity.this;

		SqliteHelper sqliteHelper = new SqliteHelper(context);
		DatabaseManager.initializeInstance(sqliteHelper);

		myHandle = new MyHandle();

		Bundle bundle = getIntent().getExtras();

		articleId = bundle.getInt("id");
		category = bundle.getString("category");

		nameET = (EditText) super.findViewById(R.id.do_work_do_page_name_et);
		telET = (EditText) super.findViewById(R.id.do_work_do_page_tel_et);
		contentsET = (EditText) super.findViewById(R.id.do_work_do_page_contents_et);

		selectFileBut = (Button) super.findViewById(R.id.do_work_do_page_select_file_but);
		submitBut = (Button) super.findViewById(R.id.do_work_do_page_submit_but);

		fileLV = (ListView) super.findViewById(R.id.do_work_do_page_file_lv);
		listViewAdapter = new ListViewAdapter();

		backIB = (ImageButton) super.findViewById(R.id.do_work_do_page_back_ib);

		backIB.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		submitBut.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				String tempString = "";
				String imageString = "";

				for (int i = 0; i < MyApplication.getMyApplication().getFileNameList().size(); i++) {
					tempString = tempString + MyApplication.getMyApplication().getFileNameList().get(i).toString() + "|";
				}

				if (tempString.length() > 0) {
					imageString = tempString.substring(0, tempString.length() - 1);
				}

				imageString = FormatStr.replaceBlank(imageString);

				System.out.println(imageString);

				int categoryId = new CategoryDao().getCategoryIdByCategory(category);
				DatabaseManager.getInstance().closeDatabase();

				if (nameET.getText().toString().trim().length() == 0) {
					ShowToast.showToastShort(DoWorkDoPageActivity.this, "姓名不能为空");
				} else if (telET.getText().toString().trim().length() == 0) {
					ShowToast.showToastShort(DoWorkDoPageActivity.this, "电话不能为空");
				} else if (contentsET.getText().toString().trim().length() == 0) {
					ShowToast.showToastShort(DoWorkDoPageActivity.this, "备注不能为空");
				} else {
					params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("name", nameET.getText().toString().trim()));
					params.add(new BasicNameValuePair("tel", telET.getText().toString().trim()));
					params.add(new BasicNameValuePair("category_id", categoryId + ""));
					params.add(new BasicNameValuePair("article_id", articleId + ""));
					params.add(new BasicNameValuePair("contents", contentsET.getText().toString().trim()));
					params.add(new BasicNameValuePair("image", imageString));
//					MyApplication.getMyApplication().setArticleId(categoryId+"");
					new Thread(new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							uploadStatus = MyHttpPost.doPost("http://www.gelanghe.gov.cn/getFeedback.php", params);

							if (uploadStatus.equals("{\"success\":\"false\"}")) {
								myHandle.sendEmptyMessage(1);
								MyApplication.getMyApplication().resetFileNameList();
							} else {
								myHandle.sendEmptyMessage(2);
								MyApplication.getMyApplication().resetFileNameList();
							}
						}
					}).start();

				}

			}
		});

		selectFileBut.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(DoWorkDoPageActivity.this, MyFileManager.class);
				startActivityForResult(intent, FILE_RESULT_CODE);
			}
		});

	}

	public static void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}
		int totalHeight = 0;
		for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (FILE_RESULT_CODE == requestCode) {

			Bundle bundle = null;
			if (data != null && (bundle = data.getExtras()) != null) {

				fileLV.setAdapter(listViewAdapter);
				setListViewHeightBasedOnChildren(fileLV);
			}

		}
	}

	class ListViewAdapter extends BaseAdapter {

		class FileName {

			TextView fileNameTV;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return MyApplication.getMyApplication().getFileNameList().size();
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
			FileName fileName;
			if (convertView == null) {
				view = getLayoutInflater().inflate(R.layout.do_work_do_page_file_name_list_item, null);
				fileName = new FileName();
				fileName.fileNameTV = (TextView) view.findViewById(R.id.do_work_do_page_file_name_list_item_file_name_tv);
				view.setTag(fileName);
			} else {
				fileName = (FileName) view.getTag();
			}

			fileName.fileNameTV.setText(MyApplication.getMyApplication().getFileNameList().get(position));

			return view;
		}

	}

}
