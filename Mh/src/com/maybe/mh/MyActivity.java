package com.maybe.mh;

import android.app.Activity;

import com.nostra13.universalimageloader.core.ImageLoader;

public class MyActivity extends Activity {

	protected ImageLoader imageLoader = ImageLoader.getInstance();

	protected String localPath = MyApplication.getMyApplication().getLocalPath();

	protected String serverUrl = MyApplication.getServerurl();

}
