package com.maybe.mh;

import com.tiandu.mh.R;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class ShowWebImageActivity extends MyActivity {
	private String imagePath = null;
	private ZoomableImageView imageView = null;
	private Bitmap bitmap = null;
	
	private class MyHandler extends Handler{
		
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch(msg.what){
			case 1:
				imageView.setImageBitmap(bitmap);
				break;
			}
			
			super.handleMessage(msg);
		}
	}
	
	private MyHandler myHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_webimage);
		this.imagePath = getIntent().getStringExtra("image");
		myHandler = new MyHandler();
		
		System.out.println("imagePath" + imagePath);

		imageView = (ZoomableImageView) findViewById(R.id.show_webimage_imageview);
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				bitmap = imageLoader.loadImageSync(imagePath);
				myHandler.sendEmptyMessage(1);
			}
		}).start();
		
		
	}

}
