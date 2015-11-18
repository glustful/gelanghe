package com.maybe.mh.json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.database.sqlite.SQLiteDatabase;

import com.alibaba.fastjson.JSON;
import com.maybe.mh.MyApplication;
import com.maybe.mh.pojo.ArticleDetail;
import com.maybe.mh.sqlite.ArticleDetailDao;
import com.maybe.mh.sqlite.DatabaseManager;
import com.maybe.mh.util.MyHttpPost;

public class ArticleDetailDL {

	private static final String articleDetailsUrl = MyApplication
			.getServerurl() + "/getArticleListNew.php";

	public static boolean downloadArticleDetail() {

		String lastId = new ArticleDetailDao().getLastArticle() + "";

		List<NameValuePair> params = new ArrayList<NameValuePair>();

		params.add(new BasicNameValuePair("article_id", 0 + ""));

		String jsonStr = MyHttpPost.doPost(articleDetailsUrl, params);

		if (jsonStr.indexOf("article_id") == -1) {
			return false;
		} else {
			List<ArticleDetail> list = new ArrayList<ArticleDetail>();

			list = JSON.parseArray(jsonStr, ArticleDetail.class);

			if (list.size() > 0) {

				int listSize = list.size();
				ArticleDetailDao articleDetailDao = new ArticleDetailDao();
				articleDetailDao.deleteAll();

				for (int i = 0; i < listSize; i++) {
					articleDetailDao.addArticleDetail(list.get(i));
				}
				DatabaseManager.getInstance().closeDatabase();
				MyApplication.getMyApplication().setArticleDetailDLOK(true);

			}

			return true;
		}
	}

	public static boolean downloadArticleDetail(HashMap<String, String> args) {

		String newUrl = MyApplication.getServerurl()
				+ "/api/getArticleList.php";

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		for (Map.Entry<String, String> entry : args.entrySet()) {
			params.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
		}

		int page = 1;
		while (true) {
			String jsonStr = MyHttpPost.doGet(newUrl, args.get("alias"),
					String.valueOf(page));

			if (jsonStr.indexOf("article_id") == -1) {
				if (increatment() == 0) {
					MyApplication.getMyApplication().setArticleDetailDLOK(true);
				}
				return false;
			} else {
				List<ArticleDetail> list = new ArrayList<ArticleDetail>();

				list = JSON.parseArray(jsonStr, ArticleDetail.class);
				if (list.size() > 0) {

					int listSize = list.size();
					ArticleDetailDao articleDetailDao = new ArticleDetailDao();
					articleDetailDao.deleteAlls();
					SQLiteDatabase sqliteDatabase = DatabaseManager
							.getInstance().openDatabase();
					sqliteDatabase.beginTransaction();
					for (int i = 0; i < listSize; i++) {
						articleDetailDao.addArticleDetail(list.get(i),
								sqliteDatabase);
					}
					sqliteDatabase.setTransactionSuccessful();
					sqliteDatabase.endTransaction();
					DatabaseManager.getInstance().closeDatabase();
					// MyApplication.getMyApplication().setArticleDetailDLOK(true);
					if(list.size()<10){
						if (increatment() == 0){
							MyApplication.getMyApplication().setArticleDetailDLOK(true);
						}
						//return true;
						break;
					}

				} else {
					if (increatment() == 0) {
						MyApplication.getMyApplication().setArticleDetailDLOK(
								true);
					}
					return true;
				}

			}
			page++;
		}
		System.out.println(args.get("alias")+" page="+page);
		return true;
	}

	private static synchronized int increatment() {
		MyApplication.lock = MyApplication.lock - 1;
		System.out.println("lock count=" + MyApplication.lock);
		return MyApplication.lock;
	}

}
