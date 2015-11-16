package com.maybe.mh.sqlite;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.maybe.mh.pojo.Category;

public class CategoryDao {

	SQLiteDatabase sqliteDatabase = DatabaseManager.getInstance().openDatabase();

	public boolean addCategory(Category category) {
		boolean flag = false;
		sqliteDatabase.beginTransaction();
		ContentValues values = new ContentValues();
		values.put("_id", category.getCategory_id());
		values.put("parent_id", category.getParent_id());
		values.put("category", category.getCategory());
		values.put("alias", category.getAlias());
		values.put("order_num", category.getOrder_num());

		try {
			long rowid = sqliteDatabase.insert("category", null, values);
			if (rowid != 0) {
				flag = true;
			}

		} finally {
			sqliteDatabase.setTransactionSuccessful();
			sqliteDatabase.endTransaction();
		}
		return flag;
	}

	public List<Category> getCategoryByParentId(int parentId) {
		List<Category> list = new ArrayList<Category>();
		sqliteDatabase.beginTransaction();
		Cursor cursor = sqliteDatabase.rawQuery("select * from category where parent_id = ? order by order_num", new String[] { parentId + "" });
		while (cursor.moveToNext()) {
			Category category = new Category();

			category.setCategory_id(cursor.getInt(cursor.getColumnIndex("_id")));
			category.setParent_id(cursor.getInt(cursor.getColumnIndex("parent_id")));
			category.setCategory(cursor.getString(cursor.getColumnIndex("category")));
			category.setAlias(cursor.getString(cursor.getColumnIndex("alias")));
			category.setOrder_num(cursor.getInt(cursor.getColumnIndex("order_num")));

			list.add(category);
		}
		cursor.close();
		sqliteDatabase.setTransactionSuccessful();
		sqliteDatabase.endTransaction();
		return list;
	}
	
	public Category getCategoryByAlias(String name) {
		Category category = new Category();
		sqliteDatabase.beginTransaction();
		Cursor cursor = sqliteDatabase.rawQuery("select * from category where alias = ?", new String[] {name});
		while (cursor.moveToNext()) {
			

			category.setCategory_id(cursor.getInt(cursor.getColumnIndex("_id")));
			category.setParent_id(cursor.getInt(cursor.getColumnIndex("parent_id")));
			category.setCategory(cursor.getString(cursor.getColumnIndex("category")));
			category.setAlias(cursor.getString(cursor.getColumnIndex("alias")));
			category.setOrder_num(cursor.getInt(cursor.getColumnIndex("order_num")));

		}
		cursor.close();
		sqliteDatabase.setTransactionSuccessful();
		sqliteDatabase.endTransaction();
		return category;
	}
	

	public int getParentIdByCategory(String category) {

		int parentId = -1;

		sqliteDatabase.beginTransaction();
		Cursor cursor = sqliteDatabase.rawQuery("select parent_id from category where category = ?", new String[] { category });
		while (cursor.moveToNext()) {

			parentId = cursor.getInt(cursor.getColumnIndex("parent_id"));
		}
		cursor.close();
		sqliteDatabase.setTransactionSuccessful();
		sqliteDatabase.endTransaction();
		return parentId;
	}

	public Category getCategoryById(int categoryId) {

		Category category = new Category();

		sqliteDatabase.beginTransaction();
		Cursor cursor = sqliteDatabase.rawQuery("select * from category where _id = ?", new String[] { categoryId + "" });
		while (cursor.moveToNext()) {

			category.setCategory_id(cursor.getInt(cursor.getColumnIndex("_id")));
			category.setParent_id(cursor.getInt(cursor.getColumnIndex("parent_id")));
			category.setCategory(cursor.getString(cursor.getColumnIndex("category")));
			category.setAlias(cursor.getString(cursor.getColumnIndex("alias")));
			category.setOrder_num(cursor.getInt(cursor.getColumnIndex("order_num")));
		}
		cursor.close();
		sqliteDatabase.setTransactionSuccessful();
		sqliteDatabase.endTransaction();
		return category;
	}

	public int getCategoryIdByCategory(String category) {

		int categoryId = -1;

		sqliteDatabase.beginTransaction();
		Cursor cursor = sqliteDatabase.rawQuery("select _id from category where category = ?", new String[] { category });
		while (cursor.moveToNext()) {

			categoryId = cursor.getInt(cursor.getColumnIndex("_id"));
		}
		cursor.close();
		sqliteDatabase.setTransactionSuccessful();
		sqliteDatabase.endTransaction();
		return categoryId;
	}

	public int getLastCategory() {
		int lastCategory = 0;
		sqliteDatabase.beginTransaction();
		Cursor cursor = sqliteDatabase.rawQuery("select max(_id) as MAXID  from category", null);
		if (cursor.moveToNext()) {
			lastCategory = cursor.getInt(cursor.getColumnIndex("MAXID"));
		}
		cursor.close();
		sqliteDatabase.setTransactionSuccessful();
		sqliteDatabase.endTransaction();
		return lastCategory;
	}

	public void deleteAll() {
		sqliteDatabase.beginTransaction();
		sqliteDatabase.delete("category", null, null);
		sqliteDatabase.setTransactionSuccessful();
		sqliteDatabase.endTransaction();
	}

}
