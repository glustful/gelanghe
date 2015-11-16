package com.maybe.mh;

import com.nostra13.universalimageloader.core.ImageLoader;

import android.app.TabActivity;

@SuppressWarnings("deprecation")
public class MyTabActivity extends TabActivity{
	

	protected ImageLoader imageLoader = ImageLoader.getInstance();

	protected String localPath = MyApplication.getMyApplication().getLocalPath();

	protected String serverUrl = MyApplication.getServerurl();

}
