package com.maybe.mh.sqlite;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.maybe.mh.pojo.FeedbackAttach;

public class FeedbackAttachDao {

	SQLiteDatabase sqliteDatabase = DatabaseManager.getInstance().openDatabase();

	public boolean addFeedbackAttach(FeedbackAttach feedbackAttach) {
		boolean flag = false;
		sqliteDatabase.beginTransaction();
		ContentValues values = new ContentValues();

		values.put("_id", feedbackAttach.getAttach_id());
		values.put("attach", feedbackAttach.getAttach());
		values.put("path", feedbackAttach.getPath());
		values.put("size", feedbackAttach.getSize());
		values.put("type", feedbackAttach.getType());
		values.put("time", feedbackAttach.getTime());
		values.put("feedback_id", feedbackAttach.getFeedback_id());

		try {
			long rowid = sqliteDatabase.insert("feedback_attach", null, values);
			if (rowid != 0) {
				flag = true;
			}

		} finally {
			sqliteDatabase.setTransactionSuccessful();
			sqliteDatabase.endTransaction();
		}
		return flag;
	}

	public FeedbackAttach getFeedbackAttachById(int feedbackAttachId) {

		FeedbackAttach feedbackAttach = new FeedbackAttach();

		sqliteDatabase.beginTransaction();
		Cursor cursor = sqliteDatabase.rawQuery("select * from feedback_attach where _id = ?", new String[] { feedbackAttachId + "" });
		while (cursor.moveToNext()) {

			feedbackAttach.setAttach_id(cursor.getInt(cursor.getColumnIndex("_id")));
			feedbackAttach.setAttach(cursor.getString(cursor.getColumnIndex("attach")));
			feedbackAttach.setPath(cursor.getString(cursor.getColumnIndex("path")));
			feedbackAttach.setSize(cursor.getInt(cursor.getColumnIndex("size")));
			feedbackAttach.setType(cursor.getString(cursor.getColumnIndex("type")));
			feedbackAttach.setTime(cursor.getInt(cursor.getColumnIndex("time")));
			feedbackAttach.setFeedback_id(cursor.getInt(cursor.getColumnIndex("feedback_id")));

		}
		cursor.close();
		sqliteDatabase.setTransactionSuccessful();
		sqliteDatabase.endTransaction();
		return feedbackAttach;
	}

	public List<FeedbackAttach> getFeedbackAttachByFeedbackId(int feedbackId) {

		List<FeedbackAttach> list = new ArrayList<FeedbackAttach>();

		sqliteDatabase.beginTransaction();
		Cursor cursor = sqliteDatabase.rawQuery("select * from feedback_attach where feedback_id = ?", new String[] { feedbackId + "" });
		while (cursor.moveToNext()) {
			FeedbackAttach feedbackAttach = new FeedbackAttach();

			feedbackAttach.setAttach_id(cursor.getInt(cursor.getColumnIndex("_id")));
			feedbackAttach.setAttach(cursor.getString(cursor.getColumnIndex("attach")));
			feedbackAttach.setPath(cursor.getString(cursor.getColumnIndex("path")));
			feedbackAttach.setSize(cursor.getInt(cursor.getColumnIndex("size")));
			feedbackAttach.setType(cursor.getString(cursor.getColumnIndex("type")));
			feedbackAttach.setTime(cursor.getInt(cursor.getColumnIndex("time")));
			feedbackAttach.setFeedback_id(cursor.getInt(cursor.getColumnIndex("feedback_id")));

			list.add(feedbackAttach);
		}
		cursor.close();
		sqliteDatabase.setTransactionSuccessful();
		sqliteDatabase.endTransaction();
		return list;
	}

	public int getLastFeedbackAttach() {
		int lastFeedbackAttach = 1;
		sqliteDatabase.beginTransaction();
		Cursor cursor = sqliteDatabase.rawQuery("select max(_id) as MAXID  from feedback_attach", null);
		if (cursor.moveToNext()) {
			lastFeedbackAttach = cursor.getInt(cursor.getColumnIndex("MAXID"));
		}
		cursor.close();
		sqliteDatabase.setTransactionSuccessful();
		sqliteDatabase.endTransaction();
		if(lastFeedbackAttach==0)lastFeedbackAttach=1;
		return lastFeedbackAttach;
	}

	public void deleteAll() {
		sqliteDatabase.beginTransaction();
		sqliteDatabase.delete("feedback_attach", null, null);
		sqliteDatabase.setTransactionSuccessful();
		sqliteDatabase.endTransaction();
	}

}
