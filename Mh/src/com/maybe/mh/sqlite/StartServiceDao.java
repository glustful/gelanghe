package com.maybe.mh.sqlite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class StartServiceDao {
	

	SQLiteDatabase sqliteDatabase = DatabaseManager.getInstance().openDatabase();

	public void addStart(int start) {
		sqliteDatabase.beginTransaction();
		ContentValues values = new ContentValues();
		values.put("isStart", start);

		try {
			sqliteDatabase.insert("start", null, values);
		} finally {
			sqliteDatabase.setTransactionSuccessful();
			sqliteDatabase.endTransaction();
		}
	}

	public int getStart() {
		sqliteDatabase.beginTransaction();
		Cursor cursor = sqliteDatabase.rawQuery("select * from start", null);
		int flag = 0;
		while (cursor.moveToNext()) {
			flag = cursor.getInt(cursor.getColumnIndex("isStart"));
		}
		cursor.close();
		sqliteDatabase.setTransactionSuccessful();
		sqliteDatabase.endTransaction();
		return flag;
	}



}
