package com.maybe.mh.json;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.alibaba.fastjson.JSON;
import com.maybe.mh.MyApplication;
import com.maybe.mh.pojo.ArticleDetail;
import com.maybe.mh.pojo.TopAd;
import com.maybe.mh.sqlite.ArticleDetailDao;
import com.maybe.mh.sqlite.DatabaseManager;
import com.maybe.mh.util.MyHttpPost;

public class TopAdDL {
	
	private static final String topAdUrl = MyApplication.getServerurl() + "/getAd.php";

	public static boolean downloadTopAd() {

		List<NameValuePair> params = new ArrayList<NameValuePair>();

		params.add(new BasicNameValuePair("id", 0 + ""));

		String jsonStr = MyHttpPost.doPost(topAdUrl, params);

		if (jsonStr.indexOf("link_id") == -1) {
			return false;
		} else {
			List<TopAd> list = new ArrayList<TopAd>();

			list = JSON.parseArray(jsonStr, TopAd.class);
			
			MyApplication.getMyApplication().map.put("topAds", list);
			

			return true;
		}
	}


}
