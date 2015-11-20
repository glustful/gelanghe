package com.maybe.mh;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.maybe.mh.pojo.ArticleDetail;
import com.maybe.mh.pojo.Category;
import com.maybe.mh.pojo.DoWork;
import com.maybe.mh.pojo.FeedbackAttach;
import com.maybe.mh.sqlite.ArticleDetailDao;
import com.maybe.mh.sqlite.CategoryDao;
import com.maybe.mh.sqlite.DatabaseManager;
import com.maybe.mh.sqlite.DoWorkDao;
import com.maybe.mh.sqlite.FeedbackAttachDao;
import com.maybe.mh.sqlite.SqliteHelper;
import com.maybe.mh.util.CopyOfHttpDownloader;
import com.maybe.mh.util.ShowToast;
import com.tiandu.mh.R;

@SuppressLint({ "HandlerLeak", "JavascriptInterface", "SetJavaScriptEnabled" })
public class WorkDetailActivity extends MyActivity {

	private TextView categoryTV;
	private TextView articleTV;
	private TextView nameTV;
	private TextView telTV;
//	private WebView contentWV;
	private TextView contentWV;
	private TextView timeTV;

	private Context context;

	private int feedbackId;

	private DoWork doWork;

	private Category category;

	private ArticleDetail articleDetail;

	private ImageButton backIB;

	private ListView attachLV;

	public static final int DIALOG_DOWNLOAD_PROGRESS = 0;

	private ProgressDialog dialog = null;

	private List<FeedbackAttach> attachList;

	private LVAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.my_work_show_page);

		context = WorkDetailActivity.this;

		SqliteHelper sqliteHelper = new SqliteHelper(context);
		DatabaseManager.initializeInstance(sqliteHelper);

		Bundle bundle = getIntent().getExtras();

		feedbackId = bundle.getInt("id");

		doWork = new DoWorkDao().getDoWorkById(feedbackId);
		DatabaseManager.getInstance().closeDatabase();

		category = new CategoryDao().getCategoryById(doWork.getCategory_id());
		DatabaseManager.getInstance().closeDatabase();

		articleDetail = new ArticleDetailDao().getAllArticleDetailById(doWork.getArticle_id());
		DatabaseManager.getInstance().closeDatabase();

		attachList = new FeedbackAttachDao().getFeedbackAttachByFeedbackId(feedbackId);
		DatabaseManager.getInstance().closeDatabase();

		categoryTV = (TextView) super.findViewById(R.id.my_work_show_page_category_tv);
		articleTV = (TextView) super.findViewById(R.id.my_work_show_page_article_tv);
		nameTV = (TextView) super.findViewById(R.id.my_work_show_page_name_tv);
		telTV = (TextView) super.findViewById(R.id.my_work_show_page_phone_tv);
//		contentWV = (WebView) findViewById(R.id.my_work_show_page_content_wv);
		contentWV = (TextView) super.findViewById(R.id.my_work_show_page_content_wv);
		timeTV = (TextView) super.findViewById(R.id.my_work_show_page_time_tv);

		backIB = (ImageButton) super.findViewById(R.id.my_work_show_page_back_ib);

		attachLV = (ListView) super.findViewById(R.id.my_work_show_page_attach_lv);

		if (attachList.size() > 0) {
			adapter = new LVAdapter();
			attachLV.setAdapter(adapter);
			setListViewHeightBasedOnChildren(attachLV);
			System.out.println(feedbackId);
			System.out.println(attachList.get(0).getAttach());

		} else {
			attachLV.setVisibility(View.GONE);
		}

		attachLV.setOnItemClickListener(new OnItemClickListener() {

			@SuppressWarnings("deprecation")
			@Override
			public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
				// TODO Auto-generated method stub

				showDialog(DIALOG_DOWNLOAD_PROGRESS);

				new Thread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						CopyOfHttpDownloader httpDownloader = new CopyOfHttpDownloader();

						int result = httpDownloader.download(WorkDetailActivity.this, MyApplication.getServerurl() + attachList.get(position).getPath(), "格朗和待办下载文件/", attachList.get(position).getAttach());

						sendMsg(-2, result);
					}
				}).start();

			}
		});

		backIB.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		categoryTV.setText(category.getCategory());//分类名
		articleTV.setText(articleDetail.getTitle());//项目名
		nameTV.setText(doWork.getName());//办理人姓名
		telTV.setText(doWork.getTel());//办理人电话
		
	/*	
		contentWV.getSettings().setJavaScriptEnabled(true);
		
		contentWV.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN); 
		
		// 随便找了个带图片的网站
		String webViewNoFmtStr = "<html><head><meta http-equiv='Content-Type' content='text/html; charset=utf-8'/></head><body>" + doWork.getContents() + "</body></html>";
		
		contentWV.loadDataWithBaseURL(serverUrl, webViewNoFmtStr, "text/html", "utf-8", null);
		// 添加js交互接口类，并起别名 imagelistner
		contentWV.addJavascriptInterface(new JavascriptInterface(this), "imagelistner");
		contentWV.setWebViewClient(new MyWebViewClient());*/
		contentWV.setText(Html.fromHtml(doWork.getContents()));
		Date date = new Date(doWork.getTime() * 1000);
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm ss");
		timeTV.setText(sdf.format(date));
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_DOWNLOAD_PROGRESS:
			dialog = new ProgressDialog(this);
			dialog.setMessage("downloading…");
			dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			dialog.setCancelable(false);
			dialog.show();
			return dialog;
		default:
			return null;
		}
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (!Thread.currentThread().isInterrupted()) {
				switch (msg.what) {
				case 0:
					dialog.setMax(msg.arg1);
					break;
				case 1:
					dialog.setProgress(msg.arg1);
					break;
				case 2:
					dialog.dismiss();
					break;
				case -1:
					String error = msg.getData().getString("error");
					ShowToast.showToastShort(context, error);
					break;
				case -2:

					String str = null;
					if (msg.arg1 == 0) {
						str = "下载成功！";
					} else if (msg.arg1 == 1) {
						str = "下载成功！";
					} else {
						str = "下载失败！";
					}

					ShowToast.showToastShort(context, str);

					break;

				}
			}
			super.handleMessage(msg);
		}
	};

	public void sendMsg(int flag, int value) {
		Message message = new Message();
		message.what = flag;
		message.arg1 = value;
		handler.sendMessage(message);
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

	private class LVAdapter extends BaseAdapter {

		class FileName {

			TextView fileNameTV;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return attachList.size();
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
				view = getLayoutInflater().inflate(R.layout.my_work_attach_list_item, null);
				fileName = new FileName();
				fileName.fileNameTV = (TextView) view.findViewById(R.id.my_work_attach_list_item_name_tv);
				view.setTag(fileName);
			} else {
				fileName = (FileName) view.getTag();
			}

			fileName.fileNameTV.setText(attachList.get(position).getAttach());

			return view;
		}

	}
	
	
	// 注入js函数监听
	private void addImageClickListner() {
		// 这段js函数的功能就是，遍历所有的img几点，并添加onclick函数，在还是执行的时候调用本地接口传递url过去
//		contentWV.loadUrl("javascript:(function(){" +
//		"var objs = document.getElementsByTagName(\"img\"); " + 
//				"for(var i=0;i<objs.length;i++)  " + 
//		"{"
//				+ "    objs[i].onclick=function()  " + 
//		"    {  " 
//				+ "        window.imagelistner.openImage(this.src);  " + 
//		"    }  " + 
//		"}" + 
//		"})()");
	}

	// js通信接口
	public class JavascriptInterface {

		private Context context;

		public JavascriptInterface(Context context) {
			this.context = context;
		}

		public void openImage(String img) {
			System.out.println(img);
			//
			Intent intent = new Intent();
			intent.putExtra("image", img);
			intent.setClass(context, ShowWebImageActivity.class);
			context.startActivity(intent);
			System.out.println(img);
		}
	}

	// 监听
	private class MyWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {

			return super.shouldOverrideUrlLoading(view, url);
		}

		@Override
		public void onPageFinished(WebView view, String url) {

			view.getSettings().setJavaScriptEnabled(true);

			super.onPageFinished(view, url);
			// html加载完成之后，添加监听图片的点击js函数
			addImageClickListner();

		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			view.getSettings().setJavaScriptEnabled(true);

			super.onPageStarted(view, url, favicon);
		}

		@Override
		public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {

			super.onReceivedError(view, errorCode, description, failingUrl);

		}
	}


}
