package com.lightsensor.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public final class DatabaseHelper extends SQLiteOpenHelper {

	private static final String TAG = DatabaseHelper.class.getSimpleName();
	private static final String DATABASE_NAME = "lightsensor";
	private static final int DATABASE_VERSION = 1;

	public DatabaseHelper(Context ctx) {
		super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
		Log.d("KM", "db created");
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		final String table = "CREATE TABLE " + PhoneDao.TABLE + "("
				+ PhoneDao._ID + " integer primary key, " + PhoneDao.LABEL
				+ " text)";
		database.execSQL(table);
		Log.d("KM", "table created");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// first iteration. do nothing.
	}

}
