package com.maybe.mh.sqlite;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.maybe.mh.pojo.DoWorkAttach;

public class DoWorkAttachDao {

	SQLiteDatabase sqliteDatabase = DatabaseManager.getInstance().openDatabase();

	public boolean addDoWorkAttach(DoWorkAttach doWorkAttach) {
		boolean flag = false;
		sqliteDatabase.beginTransaction();
		ContentValues values = new ContentValues();

		values.put("_id", doWorkAttach.getAttach_id());
		values.put("attach", doWorkAttach.getAttach());
		values.put("path", doWorkAttach.getPath());
		values.put("size", doWorkAttach.getSize());
		values.put("type", doWorkAttach.getType());
		values.put("time", doWorkAttach.getTime());
		values.put("article_id", doWorkAttach.getArticle_id());

		try {
			long rowid = sqliteDatabase.insert("do_work_attach", null, values);
			if (rowid != 0) {
				flag = true;
			}

		} finally {
			sqliteDatabase.setTransactionSuccessful();
			sqliteDatabase.endTransaction();
		}
		return flag;
	}

	public DoWorkAttach getDoWorkAttachById(int doWorkAttachId) {

		DoWorkAttach doWorkAttach = new DoWorkAttach();

		sqliteDatabase.beginTransaction();
		Cursor cursor = sqliteDatabase.rawQuery("select * from do_work_attach where _id = ?", new String[] { doWorkAttachId + "" });
		while (cursor.moveToNext()) {

			doWorkAttach.setAttach_id(cursor.getInt(cursor.getColumnIndex("_id")));
			doWorkAttach.setAttach(cursor.getString(cursor.getColumnIndex("attach")));
			doWorkAttach.setPath(cursor.getString(cursor.getColumnIndex("path")));
			doWorkAttach.setSize(cursor.getInt(cursor.getColumnIndex("size")));
			doWorkAttach.setType(cursor.getString(cursor.getColumnIndex("type")));
			doWorkAttach.setTime(cursor.getInt(cursor.getColumnIndex("time")));
			doWorkAttach.setArticle_id(cursor.getInt(cursor.getColumnIndex("article_id")));

		}
		cursor.close();
		sqliteDatabase.setTransactionSuccessful();
		sqliteDatabase.endTransaction();
		return doWorkAttach;
	}

	public List<DoWorkAttach> getDoWorkAttachByArticleId(int articleId) {

		List<DoWorkAttach> list = new ArrayList<DoWorkAttach>();

		sqliteDatabase.beginTransaction();
		Cursor cursor = sqliteDatabase.rawQuery("select * from do_work_attach where article_id = ?", new String[] { articleId + "" });
		while (cursor.moveToNext()) {
			DoWorkAttach doWorkAttach = new DoWorkAttach();

			doWorkAttach.setAttach_id(cursor.getInt(cursor.getColumnIndex("_id")));
			doWorkAttach.setAttach(cursor.getString(cursor.getColumnIndex("attach")));
			doWorkAttach.setPath(cursor.getString(cursor.getColumnIndex("path")));
			doWorkAttach.setSize(cursor.getInt(cursor.getColumnIndex("size")));
			doWorkAttach.setType(cursor.getString(cursor.getColumnIndex("type")));
			doWorkAttach.setTime(cursor.getInt(cursor.getColumnIndex("time")));
			doWorkAttach.setArticle_id(cursor.getInt(cursor.getColumnIndex("article_id")));

			list.add(doWorkAttach);
		}
		cursor.close();
		sqliteDatabase.setTransactionSuccessful();
		sqliteDatabase.endTransaction();
		return list;
	}

	public int getLastDoWorkAttach() {
		int lastDoWorkAttach = 0;
		sqliteDatabase.beginTransaction();
		Cursor cursor = sqliteDatabase.rawQuery("select max(_id) as MAXID  from do_work_attach", null);
		if (cursor.moveToNext()) {
			lastDoWorkAttach = cursor.getInt(cursor.getColumnIndex("MAXID"));
		}
		cursor.close();
		sqliteDatabase.setTransactionSuccessful();
		sqliteDatabase.endTransaction();
		return lastDoWorkAttach;
	}

	public void deleteAll() {
		sqliteDatabase.beginTransaction();
		sqliteDatabase.delete("do_work_attach", null, null);
		sqliteDatabase.setTransactionSuccessful();
		sqliteDatabase.endTransaction();
	}

}
