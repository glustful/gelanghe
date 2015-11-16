package com.maybe.mh;

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
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.maybe.mh.pojo.ArticleDetail;
import com.maybe.mh.pojo.DoWorkAttach;
import com.maybe.mh.sqlite.ArticleDetailDao;
import com.maybe.mh.sqlite.DatabaseManager;
import com.maybe.mh.sqlite.DoWorkAttachDao;
import com.maybe.mh.sqlite.SqliteHelper;
import com.maybe.mh.util.HttpDownloader;
import com.maybe.mh.util.ShowToast;

@SuppressLint("SetJavaScriptEnabled")
public class DoWorkShowPageActivity extends MyActivity {

	private Button titleBut;
	private Button bottomBut;

	private ImageButton backIV;

	private int articleId;

	private ArticleDetail articleDetail;

	private Context context;

	private TextView nameTV;
	private TextView tiaojianTV;
	private TextView chengxuTV;
	private TextView shixianTV;
	private TextView shoufeiTV;
	private TextView cailiaoTV;

	private ListView attachLV;

	private List<DoWorkAttach> attachList;

	private LVAdapter adapter;

	public static final int DIALOG_DOWNLOAD_PROGRESS = 0;

	private ProgressDialog dialog = null;
	
	private WebView doWorkWV;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.do_work_show_page);

		context = DoWorkShowPageActivity.this;

		Bundle bundle = getIntent().getExtras();

		articleId = bundle.getInt("id");

		SqliteHelper sqliteHelper = new SqliteHelper(context);
		DatabaseManager.initializeInstance(sqliteHelper);

		articleDetail = new ArticleDetailDao().getAllArticleDetailById(articleId);
		DatabaseManager.getInstance().closeDatabase();

		attachList = new DoWorkAttachDao().getDoWorkAttachByArticleId(articleId);
		DatabaseManager.getInstance().closeDatabase();

		titleBut = (Button) super.findViewById(R.id.do_work_show_page_title_but);
		bottomBut = (Button) super.findViewById(R.id.do_work_show_page_bottom_but);
		backIV = (ImageButton) super.findViewById(R.id.do_work_show_page_back_ib);
		nameTV = (TextView) super.findViewById(R.id.do_work_show_page_name_tv);
		tiaojianTV = (TextView) super.findViewById(R.id.do_work_show_page_tiaojian_tv);
		chengxuTV = (TextView) super.findViewById(R.id.do_work_show_page_chengxu_tv);
		shixianTV = (TextView) super.findViewById(R.id.do_work_show_page_shixian_tv);
		shoufeiTV = (TextView) super.findViewById(R.id.do_work_show_page_shoufei_tv);
		cailiaoTV = (TextView) super.findViewById(R.id.do_work_show_page_cailiao_tv);

		attachLV = (ListView) super.findViewById(R.id.do_work_show_page_attach_lv);

		doWorkWV = (WebView) super.findViewById(R.id.do_work_web_view);
		
		if(articleDetail.getContents().length() < 1){
			doWorkWV.setVisibility(View.GONE);
		}else{
			
			doWorkWV.getSettings().setJavaScriptEnabled(true);
			
			
			doWorkWV.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
			
			String webViewNoFmtStr = "<html><head><meta http-equiv='Content-Type' content='text/html; charset=utf-8'/></head><body>" + articleDetail.getContents() + "</body></html>";
			
			doWorkWV.loadDataWithBaseURL(serverUrl, webViewNoFmtStr, "text/html", "utf-8", null);
			
			doWorkWV.addJavascriptInterface(new JavascriptInterfaceS(this), "imagelistner");
			
			doWorkWV.setWebViewClient(new MyWebViewClient());
			
		}
		
		
		

		
		
		if (attachList.size() > 0) {
			adapter = new LVAdapter();
			attachLV.setAdapter(adapter);
			setListViewHeightBasedOnChildren(attachLV);
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
						HttpDownloader httpDownloader = new HttpDownloader();

						int result = httpDownloader.download(DoWorkShowPageActivity.this, MyApplication.getServerurl() + attachList.get(position).getPath(), "格朗和下载文件/", attachList.get(position).getAttach());

						sendMsg(-2, result);
					}
				}).start();

			}
		});

		nameTV.setText(Html.fromHtml(articleDetail.getTitle()));
		tiaojianTV.setText(Html.fromHtml(articleDetail.getF_tiaojian()));
		chengxuTV.setText(Html.fromHtml(articleDetail.getF_chengxu()));
		shixianTV.setText(Html.fromHtml(articleDetail.getF_shixian()));
		shoufeiTV.setText(Html.fromHtml(articleDetail.getF_shoufei()));
		cailiaoTV.setText(Html.fromHtml(articleDetail.getF_cailiao()));

		backIV.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		titleBut.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent intent = new Intent();
				intent.putExtra("id", articleDetail.getArticle_id()).putExtra("category", articleDetail.getCategory());
				MyApplication.getMyApplication().resetFileNameList();
				intent.setClass(DoWorkShowPageActivity.this, DoWorkDoPageActivity.class);
				startActivity(intent);

			}
		});

		bottomBut.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent intent = new Intent();
				intent.putExtra("id", articleDetail.getArticle_id()).putExtra("category", articleDetail.getCategory());
				MyApplication.getMyApplication().resetFileNameList();
				intent.setClass(DoWorkShowPageActivity.this, DoWorkDoPageActivity.class);
				startActivity(intent);

			}
		});

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

	@SuppressLint("HandlerLeak")
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
				view = getLayoutInflater().inflate(R.layout.do_work_attach_list_item, null);
				fileName = new FileName();
				fileName.fileNameTV = (TextView) view.findViewById(R.id.do_work_attach_list_item_name_tv);
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
		doWorkWV.loadUrl("javascript:(function(){" +
		"var objs = document.getElementsByTagName(\"img\"); " + 
				"for(var i=0;i<objs.length;i++)  " + 
		"{"
				+ "    objs[i].onclick=function()  " + 
		"    {  " 
				+ "        window.imagelistner.openImage(this.src);  " + 
		"    }  " + 
		"}" + 
		"})()");
	}

	// js通信接口
	public class JavascriptInterfaceS {

		private Context context;
		
		
		public JavascriptInterfaceS(Context context) {
			this.context = context;
		}
		
		@JavascriptInterface
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
