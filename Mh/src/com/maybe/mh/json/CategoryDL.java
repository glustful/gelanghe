package com.maybe.mh.json;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;

import com.alibaba.fastjson.JSON;
import com.maybe.mh.MyApplication;
import com.maybe.mh.pojo.Category;
import com.maybe.mh.sqlite.ArticleDetailDao;
import com.maybe.mh.sqlite.CategoryDao;
import com.maybe.mh.sqlite.DatabaseManager;
import com.maybe.mh.util.MyHttpPost;

public class CategoryDL {

	private static final String categoryUrl = MyApplication.getServerurl() + "/getCategoryListNew.php";

	@SuppressLint("NewApi")
	public static boolean downloadCategory() {

		String lastId = new CategoryDao().getLastCategory() + "";

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("category_id", 0 + ""));

		String jsonStr = MyHttpPost.doPost(categoryUrl, params);

		if (jsonStr.indexOf("category_id") == -1) {
			return false;
		} else {

			List<Category> list = new ArrayList<Category>();

			list = JSON.parseArray(jsonStr, Category.class);
			Set<String> alias = new HashSet<String>();
			for(Category c: list){
				if(c.getParent_id()==0){
					alias.add(c.getAlias().trim());
					
					
				}
			}
			
			if (list.size() > 0) {

				int listSize = list.size();
				CategoryDao categoryDao = new CategoryDao();
				categoryDao.deleteAll();

				for (int i = 0; i < listSize; i++) {
					categoryDao.addCategory(list.get(i));
				}
				DatabaseManager.getInstance().closeDatabase();
				MyApplication.getMyApplication().setCategoryDLOK(true);
				
			}

			return true;

		}

	}

}
