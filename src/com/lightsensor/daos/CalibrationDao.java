package com.lightsensor.daos;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.lightsensor.model.CalibrationVo;

public class CalibrationDao {
	
	protected static final String TABLE = "Phone";
	protected static final String _ID = "_id";
	protected static final String LABEL = "label";
	
	private Context mContext;
	
	public CalibrationDao(Context ctx) {
		mContext = ctx;
	}
	
	public ArrayList<CalibrationVo> getAll() {
		ArrayList<CalibrationVo> list = new ArrayList<CalibrationVo>();
		SQLiteDatabase db = new DatabaseHelper(mContext).getWritableDatabase();
		Cursor cursor = db.query(TABLE, null, null, null, null, null, null);
		while(cursor.moveToNext()) {
			CalibrationVo vo = new CalibrationVo();
			vo.setId(cursor.getInt(cursor.getColumnIndex(_ID)));
			vo.setLabel(cursor.getString(cursor.getColumnIndex(LABEL)));
			list.add(vo);
		}
		
		cursor.close();
		db.close();
		return list;
	}
	
	public CalibrationVo get(int id) {
		SQLiteDatabase db = new DatabaseHelper(mContext).getWritableDatabase();
		Cursor cursor = db.query(TABLE, null, _ID+"=?", new String[] {Integer.toString(id)}, null, null, null);
		CalibrationVo vo = null;
		if (cursor.moveToFirst()) {
			vo = new CalibrationVo();
			vo.setId(cursor.getInt(cursor.getColumnIndex(_ID)));
			vo.setLabel(cursor.getString(cursor.getColumnIndex(LABEL)));
		}
		
		cursor.close();
		db.close();
		return vo;
	}
	
	
	public long insert(CalibrationVo counterVo) {
		SQLiteDatabase db = new DatabaseHelper(mContext).getWritableDatabase();
		ContentValues values = new ContentValues();
		if (counterVo.getId() > 0) values.put(_ID, counterVo.getId());
		values.put(LABEL, counterVo.getLabel());
		
		long num = db.insert(TABLE, null, values);
		db.close();
		return num;
	}
	
	public int update(CalibrationVo counterVo) {
		SQLiteDatabase db = new DatabaseHelper(mContext).getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(_ID, counterVo.getId());
		values.put(LABEL, counterVo.getLabel());
		
		int num = db.update(TABLE, values, _ID + "=?", new String[]{Integer.toString(counterVo.getId())});
		db.close();
		return num;
	}
	
	public void delete(int id) {
		SQLiteDatabase db = new DatabaseHelper(mContext).getWritableDatabase();
		db.delete(TABLE, _ID+"=?", new String[]{Integer.toString(id)});
		db.close();
	}
	
	public void delete(CalibrationVo counterVo) {
		delete(counterVo.getId());
	}
	
	public void deleteAll() {
		SQLiteDatabase db = new DatabaseHelper(mContext).getWritableDatabase();
		db.delete(TABLE, null, null);
		db.close();
	}

}
