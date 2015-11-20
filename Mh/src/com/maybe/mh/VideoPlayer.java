package com.maybe.mh;

import com.tiandu.mh.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

public class VideoPlayer extends Activity {

	private VideoView mVideoView;
	private String remoteUrl;
	private ProgressDialog progressDialog = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.video_player);

		this.mVideoView = (VideoView) findViewById(R.id.bbvideoview);

		Bundle bundle = getIntent().getExtras();

		remoteUrl = bundle.getString("url");

		mVideoView.setVideoPath(remoteUrl);

		showProgressDialog();

		mVideoView.setMediaController(new MediaController(this));

		mVideoView.setOnPreparedListener(new OnPreparedListener() {

			@Override
			public void onPrepared(MediaPlayer mp) {
				// TODO Auto-generated method stub
				dismissProgressDialog();

				mVideoView.start();
			}
		});

		mVideoView.setOnCompletionListener(new OnCompletionListener() {

			public void onCompletion(MediaPlayer mediaplayer) {
				mVideoView.pause();
			}
		});

	}

	private void showProgressDialog() {
		if (progressDialog == null) {
			progressDialog = ProgressDialog.show(VideoPlayer.this, "视频缓冲中", "正在努力加载中 ...", true, true);
		}
	}

	private void dismissProgressDialog() {

		if (progressDialog != null) {
			progressDialog.dismiss();
			progressDialog = null;
		}
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub

		if (mVideoView != null) {
			mVideoView = null;
		}
		super.onStop();
	}

}
