package com.maybe.mh.json;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.alibaba.fastjson.JSON;
import com.maybe.mh.MyApplication;
import com.maybe.mh.pojo.ArticleDetail;
import com.maybe.mh.pojo.DoWorkAttach;
import com.maybe.mh.pojo.FeedbackAttach;
import com.maybe.mh.sqlite.ArticleDetailDao;
import com.maybe.mh.sqlite.DatabaseManager;
import com.maybe.mh.sqlite.DoWorkAttachDao;
import com.maybe.mh.sqlite.FeedbackAttachDao;
import com.maybe.mh.util.MyHttpPost;

public class AttachDL {

	private static final String doWorkAttachUrl = MyApplication.getServerurl() + "/getFuwu.php";

	private static final String feedbackAttachUrl = MyApplication.getServerurl() + "/getFeedbackFiles.php";

	public static boolean downloadDoWorkAttach() {

		String lastId = new DoWorkAttachDao().getLastDoWorkAttach() + "";
		DatabaseManager.getInstance().closeDatabase();

		List<NameValuePair> params = new ArrayList<NameValuePair>();

		params.add(new BasicNameValuePair("article_id", 0+""));//MyApplication.getMyApplication().getArticleId()

		String jsonStr = MyHttpPost.doPost(doWorkAttachUrl, params);

		if (jsonStr.indexOf("article_id") == -1) {
			return false;
		} else {
			List<DoWorkAttach> list = new ArrayList<DoWorkAttach>();
			list = JSON.parseArray(jsonStr, DoWorkAttach.class);

			if (list.size() > 0) {
				int listSize = list.size();
				DoWorkAttachDao doWorkAttachDao = new DoWorkAttachDao();
				doWorkAttachDao.deleteAll();

				for (int i = 0; i < listSize; i++) {
					doWorkAttachDao.addDoWorkAttach(list.get(i));
				}
				DatabaseManager.getInstance().closeDatabase();
			}
			return true;
		}
	}

	public static boolean downloadFeedbackAttach() {

		int lastId = new FeedbackAttachDao().getLastFeedbackAttach();
		DatabaseManager.getInstance().closeDatabase();

		if (lastId == 0) {
			lastId = 1;
		}

		List<NameValuePair> params = new ArrayList<NameValuePair>();

		params.add(new BasicNameValuePair("feedback_id", lastId + ""));

		String jsonStr = MyHttpPost.doPost(feedbackAttachUrl, params);

		if (jsonStr.indexOf("feedback_id") == -1) {
			return false;
		} else {
			List<FeedbackAttach> list = new ArrayList<FeedbackAttach>();
			list = JSON.parseArray(jsonStr, FeedbackAttach.class);

			if (list.size() > 0) {
				int listSize = list.size();
				FeedbackAttachDao feedbackAttachDao = new FeedbackAttachDao();

				for (int i = 0; i < listSize; i++) {
					feedbackAttachDao.addFeedbackAttach(list.get(i));
				}
				DatabaseManager.getInstance().closeDatabase();
			}
			return true;
		}
	}

}
