package com.maybe.mh.sqlite;

import java.util.ArrayList;
import java.util.List;

import com.maybe.mh.MyApplication;
import com.maybe.mh.pojo.ArticleDetail;
import com.maybe.mh.pojo.Category;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class ArticleDetailDao {

	SQLiteDatabase sqliteDatabase = DatabaseManager.getInstance()
			.openDatabase();

	public boolean addArticleDetail(ArticleDetail articleDetail) {
		boolean flag = false;
		sqliteDatabase.beginTransaction();
		ContentValues values = new ContentValues();
		values.put("_id", articleDetail.getArticle_id());
		values.put("title", articleDetail.getTitle());
		values.put("time", articleDetail.getTime());
		values.put("shortcon", articleDetail.getShortcon());
		values.put("imageurl", articleDetail.getImageurl());
		values.put("videourl", articleDetail.getVideourl());
		values.put("price", articleDetail.getPrice());
		values.put("contents", articleDetail.getContents());
		values.put("f_tiaojian", articleDetail.getF_tiaojian());
		values.put("f_chengxu", articleDetail.getF_chengxu());
		values.put("f_shixian", articleDetail.getF_shixian());
		values.put("f_shoufei", articleDetail.getF_shoufei());
		values.put("f_cailiao", articleDetail.getF_cailiao());
		values.put("category", articleDetail.getCategory());
		values.put("alias", articleDetail.getAlias());
		values.put("recommend", articleDetail.getRecommend());
		values.put("groups", articleDetail.getGroups());
		values.put("timesort", articleDetail.getTimesort());

		try {
			long rowid = sqliteDatabase.insert("article_detail", null, values);
			if (rowid != 0) {
				flag = true;
			}

		} finally {
			sqliteDatabase.setTransactionSuccessful();
			sqliteDatabase.endTransaction();
		}
		return flag;
	}
	
	public boolean addArticleDetail(ArticleDetail articleDetail,SQLiteDatabase sqliteDatabase1) {
		boolean flag = false;
		//sqliteDatabase.beginTransaction();
		ContentValues values = new ContentValues();
		values.put("_id", articleDetail.getArticle_id());
		values.put("title", articleDetail.getTitle());
		values.put("time", articleDetail.getTime());
		values.put("shortcon", articleDetail.getShortcon());
		values.put("imageurl", articleDetail.getImageurl());
		values.put("videourl", articleDetail.getVideourl());
		values.put("price", articleDetail.getPrice());
		values.put("contents", articleDetail.getContents());
		values.put("f_tiaojian", articleDetail.getF_tiaojian());
		values.put("f_chengxu", articleDetail.getF_chengxu());
		values.put("f_shixian", articleDetail.getF_shixian());
		values.put("f_shoufei", articleDetail.getF_shoufei());
		values.put("f_cailiao", articleDetail.getF_cailiao());
		values.put("category", articleDetail.getCategory());
		values.put("alias", articleDetail.getAlias());
		values.put("recommend", articleDetail.getRecommend());
		values.put("groups", articleDetail.getGroups());
		values.put("timesort", articleDetail.getTimesort());

		//try {
			Cursor c = sqliteDatabase1.rawQuery("select *from article_detail where _id=?", new String[]{String.valueOf(articleDetail.getArticle_id())});
			if(c != null && c.getCount()>0){
				long rowid = sqliteDatabase1.update("article_detail", values,"_id=?",new String[]{String.valueOf(articleDetail.getArticle_id())});
				if (rowid != 0) {
					flag = true;
				}
			}else{
			long rowid = sqliteDatabase1.insert("article_detail", null, values);
			if (rowid != 0) {
				flag = true;
			}
			}
			

		//} 
		return flag;
	}

	public List<ArticleDetail> getAllArticleDetailByAlias(String category) {
		List<ArticleDetail> list = new ArrayList<ArticleDetail>();
		sqliteDatabase.beginTransaction();
		Cursor cursor = sqliteDatabase
				.rawQuery(
						"select * from article_detail where alias = ? order by timesort desc",
						new String[] { category });
		while (cursor.moveToNext()) {
			ArticleDetail articleDetail = new ArticleDetail();

			articleDetail.setArticle_id(cursor.getInt(cursor
					.getColumnIndex("_id")));
			articleDetail.setTitle(cursor.getString(cursor
					.getColumnIndex("title")));
			articleDetail.setTime(cursor.getString(cursor
					.getColumnIndex("time")));
			articleDetail.setShortcon(cursor.getString(cursor
					.getColumnIndex("shortcon")));
			articleDetail.setImageurl(cursor.getString(cursor
					.getColumnIndex("imageurl")));
			articleDetail.setVideourl(cursor.getString(cursor
					.getColumnIndex("videourl")));
			articleDetail.setPrice(cursor.getString(cursor
					.getColumnIndex("price")));
			articleDetail.setContents(cursor.getString(cursor
					.getColumnIndex("contents")));
			articleDetail.setF_tiaojian(cursor.getString(cursor
					.getColumnIndex("f_tiaojian")));
			articleDetail.setF_chengxu(cursor.getString(cursor
					.getColumnIndex("f_chengxu")));
			articleDetail.setF_shixian(cursor.getString(cursor
					.getColumnIndex("f_shixian")));
			articleDetail.setF_shoufei(cursor.getString(cursor
					.getColumnIndex("f_shoufei")));
			articleDetail.setF_cailiao(cursor.getString(cursor
					.getColumnIndex("f_cailiao")));
			articleDetail.setCategory(cursor.getString(cursor
					.getColumnIndex("category")));
			articleDetail.setAlias(cursor.getString(cursor
					.getColumnIndex("alias")));
			articleDetail.setRecommend(cursor.getInt(cursor
					.getColumnIndex("recommend")));
			articleDetail.setGroups(cursor.getString(cursor
					.getColumnIndex("groups")));
			articleDetail.setTimesort(cursor.getInt(cursor
					.getColumnIndex("timesort")));

			list.add(articleDetail);
		}
		cursor.close();
		sqliteDatabase.setTransactionSuccessful();
		sqliteDatabase.endTransaction();
		return list;
	}

	public List<ArticleDetail> getAllArticleDetailByCategoryAndGroupId(
			String category, String groupId) {
		List<ArticleDetail> list = new ArrayList<ArticleDetail>();
		Cursor cursor = null;
		int intgroupId = Integer.parseInt(groupId);

		sqliteDatabase.beginTransaction();
		String sort = "desc";
		if(category.equals("chaxiang")){
			sort = "asc";
		}
		if (intgroupId == 0) {
			
				cursor = sqliteDatabase
						.rawQuery(
								"select * from article_detail where alias = ? and groups = ? order by timesort "+sort,
								new String[] { category, "0"});
			
		} else if (intgroupId == 1) {
			
				cursor = sqliteDatabase
						.rawQuery(
								"select * from article_detail where alias = ? order by timesort " + sort,
								new String[] { category });
			
		} else if (intgroupId == 2) {
			
				cursor = sqliteDatabase
						.rawQuery(
								"select * from article_detail where alias = ? and (groups = ? or  groups = ? or  groups = ?)  order by timesort " + sort,
								new String[] { category, "0", "2", "2:3" });
			
		} else if (intgroupId == 3) {
			
				cursor = sqliteDatabase
						.rawQuery(
								"select * from article_detail where alias = ? and (groups = ? or  groups = ? or  groups = ?)  order by timesort " + sort,
								new String[] { category, "0", "3", "2:3" });
			
		}
		if(cursor != null){
		while (cursor.moveToNext()) {
			ArticleDetail articleDetail = new ArticleDetail();

			articleDetail.setArticle_id(cursor.getInt(cursor
					.getColumnIndex("_id")));
			articleDetail.setTitle(cursor.getString(cursor
					.getColumnIndex("title")));
			articleDetail.setTime(cursor.getString(cursor
					.getColumnIndex("time")));
			articleDetail.setShortcon(cursor.getString(cursor
					.getColumnIndex("shortcon")));
			articleDetail.setImageurl(cursor.getString(cursor
					.getColumnIndex("imageurl")));
			articleDetail.setVideourl(cursor.getString(cursor
					.getColumnIndex("videourl")));
			articleDetail.setPrice(cursor.getString(cursor
					.getColumnIndex("price")));
			articleDetail.setContents(cursor.getString(cursor
					.getColumnIndex("contents")));
			articleDetail.setF_tiaojian(cursor.getString(cursor
					.getColumnIndex("f_tiaojian")));
			articleDetail.setF_chengxu(cursor.getString(cursor
					.getColumnIndex("f_chengxu")));
			articleDetail.setF_shixian(cursor.getString(cursor
					.getColumnIndex("f_shixian")));
			articleDetail.setF_shoufei(cursor.getString(cursor
					.getColumnIndex("f_shoufei")));
			articleDetail.setF_cailiao(cursor.getString(cursor
					.getColumnIndex("f_cailiao")));
			articleDetail.setCategory(cursor.getString(cursor
					.getColumnIndex("category")));
			articleDetail.setAlias(cursor.getString(cursor
					.getColumnIndex("alias")));
			articleDetail.setRecommend(cursor.getInt(cursor
					.getColumnIndex("recommend")));
			articleDetail.setGroups(cursor.getString(cursor
					.getColumnIndex("groups")));
			articleDetail.setTimesort(cursor.getInt(cursor
					.getColumnIndex("timesort")));

			list.add(articleDetail);
		}
		cursor.close();
		}
		sqliteDatabase.setTransactionSuccessful();
		sqliteDatabase.endTransaction();
		return list;
	}
	
	public List<ArticleDetail> getAllArticleDetailByCategoryAndGroupId(
			String category, String groupId,int page, Object ...args) {
		List<ArticleDetail> list = new ArrayList<ArticleDetail>();
		Cursor cursor = null;
		int intgroupId = Integer.parseInt(groupId);

		sqliteDatabase.beginTransaction();
		String limit = " limit " + ((page-1)*10) +",10";
		if (intgroupId == 0) {
			if (args != null && args.length > 0) {
				cursor = sqliteDatabase
						.rawQuery(
								"select * from article_detail where (alias = ? or alias = ?) and groups = ? order by timesort desc"+limit,
								new String[] { args[0].toString().toString(),args[1].toString().toString(), "0" });
			} else {
				cursor = sqliteDatabase
						.rawQuery(
								"select * from article_detail where alias = ? and groups = ? order by timesort desc"+limit,
								new String[] { category, "0" });
			}
		} else if (intgroupId == 1) {
			if (args != null && args.length > 0) {
				cursor = sqliteDatabase
						.rawQuery(
								"select * from article_detail where (alias = ? or alias = ?) order by timesort desc"+limit,
								new String[] { args[0].toString(),args[1].toString() });
			} else {
				cursor = sqliteDatabase
						.rawQuery(
								"select * from article_detail where alias = ? order by timesort desc"+limit,
								new String[] { category });
			}
		} else if (intgroupId == 2) {
			if (args != null && args.length > 0) {
				cursor = sqliteDatabase
						.rawQuery(
								"select * from article_detail where (alias = ? or alias = ?) and (groups = ? or  groups = ? or  groups = ?)  order by timesort desc"+limit,
								new String[] { args[0].toString(),args[1].toString(), "0", "2", "2:3" });
			} else {
				cursor = sqliteDatabase
						.rawQuery(
								"select * from article_detail where alias = ? and (groups = ? or  groups = ? or  groups = ?)  order by timesort desc"+limit,
								new String[] { category, "0", "2", "2:3" });
			}
		} else if (intgroupId == 3) {
			if (args != null && args.length > 0) {
				cursor = sqliteDatabase
						.rawQuery(
								"select * from article_detail where (alias = ? or alias = ?) and (groups = ? or  groups = ? or  groups = ?)  order by timesort desc"+limit,
								new String[] { args[0].toString(),args[1].toString(), "0", "3", "2:3" });
			} else {
				cursor = sqliteDatabase
						.rawQuery(
								"select * from article_detail where alias = ? and (groups = ? or  groups = ? or  groups = ?)  order by timesort desc"+limit,
								new String[] { category, "0", "3", "2:3" });
			}
		}
		if(cursor != null){
		while (cursor.moveToNext()) {
			ArticleDetail articleDetail = new ArticleDetail();

			articleDetail.setArticle_id(cursor.getInt(cursor
					.getColumnIndex("_id")));
			articleDetail.setTitle(cursor.getString(cursor
					.getColumnIndex("title")));
			articleDetail.setTime(cursor.getString(cursor
					.getColumnIndex("time")));
			articleDetail.setShortcon(cursor.getString(cursor
					.getColumnIndex("shortcon")));
			articleDetail.setImageurl(cursor.getString(cursor
					.getColumnIndex("imageurl")));
			articleDetail.setVideourl(cursor.getString(cursor
					.getColumnIndex("videourl")));
			articleDetail.setPrice(cursor.getString(cursor
					.getColumnIndex("price")));
			articleDetail.setContents(cursor.getString(cursor
					.getColumnIndex("contents")));
			articleDetail.setF_tiaojian(cursor.getString(cursor
					.getColumnIndex("f_tiaojian")));
			articleDetail.setF_chengxu(cursor.getString(cursor
					.getColumnIndex("f_chengxu")));
			articleDetail.setF_shixian(cursor.getString(cursor
					.getColumnIndex("f_shixian")));
			articleDetail.setF_shoufei(cursor.getString(cursor
					.getColumnIndex("f_shoufei")));
			articleDetail.setF_cailiao(cursor.getString(cursor
					.getColumnIndex("f_cailiao")));
			articleDetail.setCategory(cursor.getString(cursor
					.getColumnIndex("category")));
			articleDetail.setAlias(cursor.getString(cursor
					.getColumnIndex("alias")));
			articleDetail.setRecommend(cursor.getInt(cursor
					.getColumnIndex("recommend")));
			articleDetail.setGroups(cursor.getString(cursor
					.getColumnIndex("groups")));
			articleDetail.setTimesort(cursor.getInt(cursor
					.getColumnIndex("timesort")));

			list.add(articleDetail);
		}
		cursor.close();
		}
		sqliteDatabase.setTransactionSuccessful();
		sqliteDatabase.endTransaction();
		return list;
	}

	public ArticleDetail getAllArticleDetailById(int article_id) {
		ArticleDetail articleDetail = new ArticleDetail();
		sqliteDatabase.beginTransaction();
		Cursor cursor = sqliteDatabase.rawQuery(
				"select * from article_detail where _id = ?",
				new String[] { article_id + "" });
		while (cursor.moveToNext()) {

			articleDetail.setArticle_id(cursor.getInt(cursor
					.getColumnIndex("_id")));
			articleDetail.setTitle(cursor.getString(cursor
					.getColumnIndex("title")));
			articleDetail.setTime(cursor.getString(cursor
					.getColumnIndex("time")));
			articleDetail.setShortcon(cursor.getString(cursor
					.getColumnIndex("shortcon")));
			articleDetail.setImageurl(cursor.getString(cursor
					.getColumnIndex("imageurl")));
			articleDetail.setVideourl(cursor.getString(cursor
					.getColumnIndex("videourl")));
			articleDetail.setPrice(cursor.getString(cursor
					.getColumnIndex("price")));
			articleDetail.setContents(cursor.getString(cursor
					.getColumnIndex("contents")));
			articleDetail.setF_tiaojian(cursor.getString(cursor
					.getColumnIndex("f_tiaojian")));
			articleDetail.setF_chengxu(cursor.getString(cursor
					.getColumnIndex("f_chengxu")));
			articleDetail.setF_shixian(cursor.getString(cursor
					.getColumnIndex("f_shixian")));
			articleDetail.setF_shoufei(cursor.getString(cursor
					.getColumnIndex("f_shoufei")));
			articleDetail.setF_cailiao(cursor.getString(cursor
					.getColumnIndex("f_cailiao")));
			articleDetail.setCategory(cursor.getString(cursor
					.getColumnIndex("category")));
			articleDetail.setAlias(cursor.getString(cursor
					.getColumnIndex("alias")));
			articleDetail.setRecommend(cursor.getInt(cursor
					.getColumnIndex("recommend")));
			articleDetail.setGroups(cursor.getString(cursor
					.getColumnIndex("groups")));
			articleDetail.setTimesort(cursor.getInt(cursor
					.getColumnIndex("timesort")));

		}
		cursor.close();
		sqliteDatabase.setTransactionSuccessful();
		sqliteDatabase.endTransaction();
		return articleDetail;
	}

	public int getLastArticle() {
		int lastArticle = 0;
		sqliteDatabase.beginTransaction();
		Cursor cursor = sqliteDatabase.rawQuery(
				"select max(_id) as MAXID  from article_detail", null);
		if (cursor.moveToNext()) {
			lastArticle = cursor.getInt(cursor.getColumnIndex("MAXID"));
		}
		cursor.close();
		sqliteDatabase.setTransactionSuccessful();
		sqliteDatabase.endTransaction();
		return lastArticle;
	}

	public List<ArticleDetail> getRecommendArticleDetail() {
		List<ArticleDetail> list = new ArrayList<ArticleDetail>();
		sqliteDatabase.beginTransaction();
		Cursor cursor = sqliteDatabase
				.rawQuery(
						"select * from article_detail where recommend = 1 order by timesort desc",
						null);
		while (cursor.moveToNext()) {
			ArticleDetail articleDetail = new ArticleDetail();

			articleDetail.setArticle_id(cursor.getInt(cursor
					.getColumnIndex("_id")));
			articleDetail.setTitle(cursor.getString(cursor
					.getColumnIndex("title")));
			articleDetail.setTime(cursor.getString(cursor
					.getColumnIndex("time")));
			articleDetail.setShortcon(cursor.getString(cursor
					.getColumnIndex("shortcon")));
			articleDetail.setImageurl(cursor.getString(cursor
					.getColumnIndex("imageurl")));
			articleDetail.setVideourl(cursor.getString(cursor
					.getColumnIndex("videourl")));
			articleDetail.setPrice(cursor.getString(cursor
					.getColumnIndex("price")));
			articleDetail.setContents(cursor.getString(cursor
					.getColumnIndex("contents")));
			articleDetail.setF_tiaojian(cursor.getString(cursor
					.getColumnIndex("f_tiaojian")));
			articleDetail.setF_chengxu(cursor.getString(cursor
					.getColumnIndex("f_chengxu")));
			articleDetail.setF_shixian(cursor.getString(cursor
					.getColumnIndex("f_shixian")));
			articleDetail.setF_shoufei(cursor.getString(cursor
					.getColumnIndex("f_shoufei")));
			articleDetail.setF_cailiao(cursor.getString(cursor
					.getColumnIndex("f_cailiao")));
			articleDetail.setCategory(cursor.getString(cursor
					.getColumnIndex("category")));
			articleDetail.setAlias(cursor.getString(cursor
					.getColumnIndex("alias")));
			articleDetail.setRecommend(cursor.getInt(cursor
					.getColumnIndex("recommend")));
			articleDetail.setGroups(cursor.getString(cursor
					.getColumnIndex("groups")));
			articleDetail.setTimesort(cursor.getInt(cursor
					.getColumnIndex("timesort")));

			list.add(articleDetail);
		}
		cursor.close();
		sqliteDatabase.setTransactionSuccessful();
		sqliteDatabase.endTransaction();
		return list;
	}

	public List<ArticleDetail> getArticleDetailBySearchKey(String searchKey) {
		List<ArticleDetail> list = new ArrayList<ArticleDetail>();
		sqliteDatabase.beginTransaction();
		Cursor cursor = sqliteDatabase.rawQuery(
				"select * from article_detail where (title like '%" + searchKey
						+ "%' or shortcon like '%" + searchKey
						+ "%' or contents like '%" + searchKey
						+ "%' or category like '%" + searchKey + "%') ", null);
		while (cursor.moveToNext()) {
			ArticleDetail articleDetail = new ArticleDetail();

			articleDetail.setArticle_id(cursor.getInt(cursor
					.getColumnIndex("_id")));
			articleDetail.setTitle(cursor.getString(cursor
					.getColumnIndex("title")));
			articleDetail.setTime(cursor.getString(cursor
					.getColumnIndex("time")));
			articleDetail.setShortcon(cursor.getString(cursor
					.getColumnIndex("shortcon")));
			articleDetail.setImageurl(cursor.getString(cursor
					.getColumnIndex("imageurl")));
			articleDetail.setVideourl(cursor.getString(cursor
					.getColumnIndex("videourl")));
			articleDetail.setPrice(cursor.getString(cursor
					.getColumnIndex("price")));
			articleDetail.setContents(cursor.getString(cursor
					.getColumnIndex("contents")));
			articleDetail.setF_tiaojian(cursor.getString(cursor
					.getColumnIndex("f_tiaojian")));
			articleDetail.setF_chengxu(cursor.getString(cursor
					.getColumnIndex("f_chengxu")));
			articleDetail.setF_shixian(cursor.getString(cursor
					.getColumnIndex("f_shixian")));
			articleDetail.setF_shoufei(cursor.getString(cursor
					.getColumnIndex("f_shoufei")));
			articleDetail.setF_cailiao(cursor.getString(cursor
					.getColumnIndex("f_cailiao")));
			articleDetail.setCategory(cursor.getString(cursor
					.getColumnIndex("category")));
			articleDetail.setAlias(cursor.getString(cursor
					.getColumnIndex("alias")));
			articleDetail.setRecommend(cursor.getInt(cursor
					.getColumnIndex("recommend")));
			articleDetail.setGroups(cursor.getString(cursor
					.getColumnIndex("groups")));
			articleDetail.setTimesort(cursor.getInt(cursor
					.getColumnIndex("timesort")));

			list.add(articleDetail);
		}
		cursor.close();
		sqliteDatabase.setTransactionSuccessful();
		sqliteDatabase.endTransaction();
		return list;
	}

	public boolean updateArticleDetail(ArticleDetail articleDetail) {

		boolean flag = false;
		sqliteDatabase.beginTransaction();
		ContentValues values = new ContentValues();
		values.put("title", articleDetail.getTitle());
		values.put("time", articleDetail.getTime());
		values.put("shortcon", articleDetail.getShortcon());
		values.put("imageurl", articleDetail.getImageurl());
		values.put("videourl", articleDetail.getVideourl());
		values.put("price", articleDetail.getPrice());
		values.put("contents", articleDetail.getContents());
		values.put("f_tiaojian", articleDetail.getF_tiaojian());
		values.put("f_chengxu", articleDetail.getF_chengxu());
		values.put("f_shixian", articleDetail.getF_shixian());
		values.put("f_shoufei", articleDetail.getF_shoufei());
		values.put("f_cailiao", articleDetail.getF_cailiao());
		values.put("category", articleDetail.getCategory());
		values.put("alias", articleDetail.getAlias());
		values.put("recommend", articleDetail.getRecommend());
		values.put("groups", articleDetail.getGroups());
		values.put("timesort", articleDetail.getTimesort());

		try {
			long rowid = sqliteDatabase.update("article_detail", values,
					"_id = ?", new String[] { articleDetail.getArticle_id()
							+ "" });
			if (rowid != 0) {
				flag = true;
			}

		} finally {
			sqliteDatabase.setTransactionSuccessful();
			sqliteDatabase.endTransaction();
		}
		return flag;

	}

	public void deleteAll() {
		sqliteDatabase.beginTransaction();
		sqliteDatabase.delete("article_detail", null, null);
		sqliteDatabase.setTransactionSuccessful();
		sqliteDatabase.endTransaction();
	}
	
	public synchronized void deleteAlls() {
		if (MyApplication.isFirst){
			
			MyApplication.isFirst = false;
		sqliteDatabase.beginTransaction();
		System.out.println(sqliteDatabase.delete("article_detail", null, null));
		sqliteDatabase.setTransactionSuccessful();
		sqliteDatabase.endTransaction();
		}
	}

}
