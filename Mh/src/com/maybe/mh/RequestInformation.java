package com.maybe.mh;

import java.util.HashMap;

import com.maybe.mh.json.ArticleDetailDL;

import android.os.AsyncTask;

public class RequestInformation extends AsyncTask<HashMap<String,String>, Integer, Boolean> {

	@Override
	protected void onPostExecute(Boolean result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		
	}

	@Override
	protected Boolean doInBackground(HashMap<String, String>... params) {
		System.out.println("开始下载articleDetail="+params[0].get("alias"));
		if (ArticleDetailDL.downloadArticleDetail(params[0])) {
			System.out.println("下载articleDetail完毕="+params[0].get("alias"));
			return true;
		} else {
			System.out.println("未下载articleDetail="+params[0].get("alias"));
			return false;
		}

		
	}

}
