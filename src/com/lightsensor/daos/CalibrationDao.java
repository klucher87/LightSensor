package com.lightsensor.daos;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.lightsensor.model.CalibrationVo;

public class CalibrationDao {

	protected static final String TABLE = "Calibration";
	protected static final String _ID = "_id";
	protected static final String LABEL = "label";
	protected static final String SELECTED = "selected";

	private Context mContext;

	public CalibrationDao(Context ctx) {
		mContext = ctx;
	}

	public ArrayList<CalibrationVo> getAll() {
		ArrayList<CalibrationVo> list = new ArrayList<CalibrationVo>();
		SQLiteDatabase db = new DatabaseHelper(mContext).getWritableDatabase();
		Cursor cursor = db.query(TABLE, null, null, null, null, null, null);
		while (cursor.moveToNext()) {
			CalibrationVo vo = new CalibrationVo();
			vo.setId(cursor.getInt(cursor.getColumnIndex(_ID)));
			vo.setLabel(cursor.getString(cursor.getColumnIndex(LABEL)));
			vo.setSelected(cursor.getInt(cursor.getColumnIndex(SELECTED)) == 1);
			list.add(vo);
		}

		cursor.close();
		db.close();
		return list;
	}

	public CalibrationVo get(int id) {
		SQLiteDatabase db = new DatabaseHelper(mContext).getWritableDatabase();
		Cursor cursor = db.query(TABLE, null, _ID + "=?",
				new String[] { Integer.toString(id) }, null, null, null);
		CalibrationVo vo = null;
		if (cursor.moveToFirst()) {
			vo = new CalibrationVo();
			vo.setId(cursor.getInt(cursor.getColumnIndex(_ID)));
			vo.setLabel(cursor.getString(cursor.getColumnIndex(LABEL)));
			vo.setSelected(cursor.getInt(cursor.getColumnIndex(SELECTED)) == 1);
		}

		cursor.close();
		db.close();
		return vo;
	}

	public long insert(CalibrationVo model) {
		SQLiteDatabase db = new DatabaseHelper(mContext).getWritableDatabase();
		ContentValues values = new ContentValues();
		if (model.getId() > 0)
			values.put(_ID, model.getId());
		values.put(LABEL, model.getLabel());
		values.put(SELECTED, model.isSelected());

		long num = db.insert(TABLE, null, values);
		db.close();
		return num;
	}

	public int update(CalibrationVo model) {
		SQLiteDatabase db = new DatabaseHelper(mContext).getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(_ID, model.getId());
		values.put(LABEL, model.getLabel());
		values.put(SELECTED, model.isSelected());

		int num = db.update(TABLE, values, _ID + "=?",
				new String[] { Integer.toString(model.getId()) });
		db.close();
		return num;
	}

	public void delete(int id) {
		SQLiteDatabase db = new DatabaseHelper(mContext).getWritableDatabase();
		db.delete(TABLE, _ID + "=?", new String[] { Integer.toString(id) });
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
