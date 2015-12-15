package com.maybe.mh.sqlite;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.maybe.mh.pojo.User;

public class LogInUserDao {

	SQLiteDatabase sqliteDatabase = DatabaseManager.getInstance().openDatabase();

	public boolean addUser(User user) {
		boolean flag = false;
		sqliteDatabase.beginTransaction();
		ContentValues values = new ContentValues();
		values.put("_id", user.getGroup_id());
		values.put("username", user.getUsername());
		values.put("pwd", user.getPwd());
		values.put("role", user.getRole());
		try {
			long rowid = sqliteDatabase.insert("login_user", null, values);
			if (rowid != 0) {
				flag = true;
			}

		} finally {
			sqliteDatabase.setTransactionSuccessful();
			sqliteDatabase.endTransaction();
		}
		return flag;
	}

	public List<User> getAlluser() {
		List<User> list = new ArrayList<User>();
		sqliteDatabase.beginTransaction();
		Cursor cursor = sqliteDatabase.rawQuery("select * from login_user", null);
		while (cursor.moveToNext()) {
			User user = new User();

			user.setGroup_id(cursor.getString(cursor.getColumnIndex("_id")));
			user.setUsername(cursor.getString(cursor.getColumnIndex("username")));
			user.setPwd(cursor.getString(cursor.getColumnIndex("pwd")));
			user.setRole(cursor.getString(cursor.getColumnIndex("role")));
			list.add(user);
		}
		cursor.close();
		sqliteDatabase.setTransactionSuccessful();
		sqliteDatabase.endTransaction();
		return list;
	}

	public void deleteAll() {
		sqliteDatabase.beginTransaction();
		sqliteDatabase.delete("login_user", null, null);
		sqliteDatabase.setTransactionSuccessful();
		sqliteDatabase.endTransaction();
	}

}
