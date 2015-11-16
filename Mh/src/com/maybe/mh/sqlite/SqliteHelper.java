package com.maybe.mh.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SqliteHelper extends SQLiteOpenHelper {

	private static final String DataBaseName = "mh";
	private static final int DataBaseVersion = 1;

	private static SqliteHelper mInstance;

	public SqliteHelper(Context context) {
		super(context, DataBaseName, null, DataBaseVersion);
	}

	public static SqliteHelper getInstance(Context context) {
		if (mInstance == null) {
			synchronized (SqliteHelper.class) {
				mInstance = new SqliteHelper(context);
			}

		}
		return mInstance;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		db.execSQL("CREATE TABLE IF NOT EXISTS article_detail(_id INTEGER PRIMARY KEY, title VARCHAR, time VARCHAR, shortcon VARCHAR, imageurl VARCHAR, videourl VARCHAR, price VARCHAR, contents TEXT, f_tiaojian TEXT, f_chengxu TEXT, f_shixian TEXT, f_shoufei TEXT, f_cailiao TEXT, category VARCHAR, alias VARCHAR, recommend VARCHAR, groups VARCHAR, timesort INTEGER)");

		db.execSQL("CREATE TABLE IF NOT EXISTS do_work(_id INTEGER PRIMARY KEY, category_id INTEGER, article_id INTEGER, title VARCHAR, name VARCHAR, tel VARCHAR, files VARCHAR, contents TEXT, time INTEGER)");

		db.execSQL("CREATE TABLE IF NOT EXISTS do_work_attach(_id INTEGER PRIMARY KEY, attach VARCHAR, path VARCHAR, size INTEGER, type VARCHAR, time INTEGER, article_id INTEGER)");

		db.execSQL("CREATE TABLE IF NOT EXISTS feedback_attach(_id INTEGER PRIMARY KEY, attach VARCHAR, path VARCHAR, size INTEGER, type VARCHAR, time INTEGER, feedback_id INTEGER)");

		db.execSQL("CREATE TABLE IF NOT EXISTS category(_id INTEGER PRIMARY KEY, parent_id INTEGER, category VARCHAR, alias VARCHAR , order_num INTEGER)");

		db.execSQL("CREATE TABLE IF NOT EXISTS login_user(_id INTEGER PRIMARY KEY, username VARCHAR, pwd VARCHAR)");
		
		db.execSQL("CREATE TABLE IF NOT EXISTS start(isStart INTEGER)");

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}
}
