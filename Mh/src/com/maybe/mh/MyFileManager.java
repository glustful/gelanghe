package com.maybe.mh;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.maybe.mh.upload.HttpMultipartPost;
import com.maybe.mh.util.ShowToast;

public class MyFileManager extends MyActivity {
	private List<String> items = null;
	private List<String> paths = null;
	private String rootPath = MyApplication.getMyApplication().getLocalPath();
	private String curPath = MyApplication.getMyApplication().getLocalPath();
	private TextView mPath;

	private ListView fileLV;

	private Context context;

	private List<String> fileNameList = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.fileselect);
		context = MyFileManager.this;

		mPath = (TextView) findViewById(R.id.my_path);
		fileLV = (ListView) findViewById(R.id.file_select_lv);

		Button buttonCancle = (Button) findViewById(R.id.button_cancle);
		buttonCancle.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent data = new Intent(MyFileManager.this, DoWorkDoPageActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("file", curPath);
				data.putExtras(bundle);
				setResult(2, data);
				finish();
			}
		});
		getFileDir(rootPath);

		fileLV.setOnItemClickListener(new OnItemClickListener() {

			String filePath;

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				File file = new File(paths.get(position));
				if (file.isDirectory()) {
					curPath = paths.get(position);
					getFileDir(paths.get(position));
				} else {

					filePath = curPath + "/" + file.getName();
					System.out.println("filePath = " + filePath);

					File uploadFile = new File(filePath);
					if (uploadFile.exists()) {

						AlertDialog.Builder builder = new Builder(MyFileManager.this);
						builder.setTitle("确定");
						builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub

								HttpMultipartPost post = new HttpMultipartPost(context, filePath);
								post.execute();
							}
						});
						builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub

							}
						});
						builder.setIcon(android.R.drawable.ic_dialog_info);
						builder.setMessage("确定传输" + file.getName() + "吗？");
						builder.show();

					} else {

						ShowToast.showToastShort(MyFileManager.this, "文件不存在");

					}
				}

			}
		});

	}

	private void getFileDir(String filePath) {
		mPath.setText(filePath);
		items = new ArrayList<String>();
		paths = new ArrayList<String>();
		File f = new File(filePath);
		File[] files = f.listFiles();

		if (!filePath.equals(rootPath)) {
			items.add("b1");
			paths.add(rootPath);
			items.add("b2");
			paths.add(f.getParent());
		}
		for (int i = 0; i < files.length; i++) {
			File file = files[i];
			items.add(file.getName());
			paths.add(file.getPath());
		}

		fileLV.setAdapter(new MyAdapter(this, items, paths));
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub

		if ((keyCode == KeyEvent.KEYCODE_BACK) && curPath == MyApplication.getMyApplication().getLocalPath()) {

			Intent data = new Intent(MyFileManager.this, DoWorkDoPageActivity.class);
			Bundle bundle = new Bundle();
			bundle.putString("file", curPath);
			data.putExtras(bundle);
			setResult(2, data);
			finish();
		}

		return true;
	}

}