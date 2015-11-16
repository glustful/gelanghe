package com.maybe.mh.sqlite;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.maybe.mh.pojo.DoWork;

public class DoWorkDao {

	SQLiteDatabase sqliteDatabase = DatabaseManager.getInstance().openDatabase();

	public boolean addDoWork(DoWork doWork) {
		boolean flag = false;
		sqliteDatabase.beginTransaction();
		ContentValues values = new ContentValues();
		values.put("_id", doWork.getFeedback_id());
		values.put("category_id", doWork.getCategory_id());
		values.put("article_id", doWork.getArticle_id());
		values.put("title", doWork.getTitle());
		values.put("name", doWork.getName());
		values.put("tel", doWork.getTel());
		values.put("files", doWork.getFiles());
		values.put("contents", doWork.getContents());
		values.put("time", doWork.getTime());

		try {
			long rowid = sqliteDatabase.insert("do_work", null, values);
			if (rowid != 0) {
				flag = true;
			}

		} finally {
			sqliteDatabase.setTransactionSuccessful();
			sqliteDatabase.endTransaction();
		}
		return flag;
	}

	public List<DoWork> getAllDoWork() {
		List<DoWork> list = new ArrayList<DoWork>();
		sqliteDatabase.beginTransaction();
		Cursor cursor = sqliteDatabase.rawQuery("select * from do_work", null);
		while (cursor.moveToNext()) {
			DoWork doWork = new DoWork();

			doWork.setFeedback_id(cursor.getInt(cursor.getColumnIndex("_id")));
			doWork.setCategory_id(cursor.getInt(cursor.getColumnIndex("category_id")));
			doWork.setArticle_id(cursor.getInt(cursor.getColumnIndex("article_id")));
			doWork.setTitle(cursor.getString(cursor.getColumnIndex("title")));
			doWork.setName(cursor.getString(cursor.getColumnIndex("name")));
			doWork.setTel(cursor.getString(cursor.getColumnIndex("tel")));
			doWork.setFiles(cursor.getString(cursor.getColumnIndex("files")));
			doWork.setContents(cursor.getString(cursor.getColumnIndex("contents")));
			doWork.setTime(cursor.getInt(cursor.getColumnIndex("time")));

			list.add(doWork);
		}
		cursor.close();
		sqliteDatabase.setTransactionSuccessful();
		sqliteDatabase.endTransaction();
		return list;
	}

	public DoWork getDoWorkById(int doWorkId) {
		DoWork doWork = new DoWork();
		sqliteDatabase.beginTransaction();
		Cursor cursor = sqliteDatabase.rawQuery("select * from do_work where _id = ?", new String[] { doWorkId + "" });
		while (cursor.moveToNext()) {

			doWork.setFeedback_id(cursor.getInt(cursor.getColumnIndex("_id")));
			doWork.setCategory_id(cursor.getInt(cursor.getColumnIndex("category_id")));
			doWork.setArticle_id(cursor.getInt(cursor.getColumnIndex("article_id")));
			doWork.setTitle(cursor.getString(cursor.getColumnIndex("title")));
			doWork.setName(cursor.getString(cursor.getColumnIndex("name")));
			doWork.setTel(cursor.getString(cursor.getColumnIndex("tel")));
			doWork.setFiles(cursor.getString(cursor.getColumnIndex("files")));
			doWork.setContents(cursor.getString(cursor.getColumnIndex("contents")));
			doWork.setTime(cursor.getInt(cursor.getColumnIndex("time")));
		}
		cursor.close();
		sqliteDatabase.setTransactionSuccessful();
		sqliteDatabase.endTransaction();
		return doWork;
	}

	public int getLastDoWork() {
		int lastId = 0;
		sqliteDatabase.beginTransaction();
		Cursor cursor = sqliteDatabase.rawQuery("select max(_id) as MAXID  from do_work", null);
		if (cursor.moveToNext()) {
			lastId = cursor.getInt(cursor.getColumnIndex("MAXID"));
		}
		cursor.close();
		sqliteDatabase.setTransactionSuccessful();
		sqliteDatabase.endTransaction();
		return lastId;
	}

	public void deleteAll() {
		sqliteDatabase.beginTransaction();
		sqliteDatabase.delete("do_work", null, null);
		sqliteDatabase.setTransactionSuccessful();
		sqliteDatabase.endTransaction();
	}

}
