package com.maybe.mh.json;

import com.alibaba.fastjson.JSONObject;
import com.maybe.mh.MyApplication;
import com.maybe.mh.util.MyHttpPost;

public class AppVersionDL {

	private static final String appVersionUrl = MyApplication.getServerurl() + "/getVersion.php";

	public static int getAppVersion() {

		String jsonStr = MyHttpPost.doPost(appVersionUrl, null);

		if (jsonStr.indexOf("link") == -1) {
			return -1;
		} else {

			JSONObject jsonObj = JSONObject.parseObject(jsonStr);

			Object versionObj = jsonObj.get("link");

			return Integer.parseInt(versionObj.toString());
		}

	}

}
