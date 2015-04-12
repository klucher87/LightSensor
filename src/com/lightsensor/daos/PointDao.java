package com.lightsensor.daos;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.lightsensor.model.PointVo;

public class PointDao {

	protected static final String TABLE = "Point";
	protected static final String _ID = "_id";
	protected static final String BEFORE_CALIBRATION = "before_calibration";
	protected static final String AFTER_CALIBRATION = "after_calibration";
	protected static final String CALIBRATION_ID = "calibration_id";

	private Context mContext;

	public PointDao(Context ctx) {
		mContext = ctx;
	}

	public ArrayList<PointVo> getAll() {
		ArrayList<PointVo> list = new ArrayList<PointVo>();
		SQLiteDatabase db = new DatabaseHelper(mContext).getWritableDatabase();
		Cursor cursor = db.query(TABLE, null, null, null, null, null, null);
		while (cursor.moveToNext()) {
			PointVo vo = new PointVo();
			vo.setId(cursor.getInt(cursor.getColumnIndex(_ID)));
			vo.setBeforeCalibration(cursor.getFloat(cursor
					.getColumnIndex(BEFORE_CALIBRATION)));
			vo.setAfterCalibration(cursor.getFloat(cursor
					.getColumnIndex(AFTER_CALIBRATION)));
			vo.setCalibrationId(cursor.getInt(cursor
					.getColumnIndex(CALIBRATION_ID)));
			list.add(vo);
		}

		cursor.close();
		db.close();
		return list;
	}

	public ArrayList<PointVo> getAllWithCalibrationId(int calibration_id) {
		ArrayList<PointVo> list = new ArrayList<PointVo>();
		for (PointVo point : getAll()) {
			if (point.getCalibrationId() == calibration_id) {
				list.add(point);
			}
		}
		return list;
	}

	public PointVo get(int id) {
		SQLiteDatabase db = new DatabaseHelper(mContext).getWritableDatabase();
		Cursor cursor = db.query(TABLE, null, _ID + "=?",
				new String[] { Integer.toString(id) }, null, null, null);
		PointVo vo = null;
		if (cursor.moveToFirst()) {
			vo = new PointVo();
			vo.setId(cursor.getInt(cursor.getColumnIndex(_ID)));
			vo.setBeforeCalibration(cursor.getFloat(cursor
					.getColumnIndex(BEFORE_CALIBRATION)));
			vo.setAfterCalibration(cursor.getFloat(cursor
					.getColumnIndex(AFTER_CALIBRATION)));
			vo.setCalibrationId(cursor.getInt(cursor
					.getColumnIndex(CALIBRATION_ID)));
		}

		cursor.close();
		db.close();
		return vo;
	}

	public long insert(PointVo model) {
		SQLiteDatabase db = new DatabaseHelper(mContext).getWritableDatabase();
		ContentValues values = new ContentValues();
		if (model.getId() > 0)
			values.put(_ID, model.getId());
		values.put(BEFORE_CALIBRATION, model.getBeforeCalibration());
		values.put(AFTER_CALIBRATION, model.getAfterCalibration());
		values.put(CALIBRATION_ID, model.getCalibrationId());

		long num = db.insert(TABLE, null, values);
		db.close();
		return num;
	}

	public int update(PointVo model) {
		SQLiteDatabase db = new DatabaseHelper(mContext).getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(_ID, model.getId());
		values.put(BEFORE_CALIBRATION, model.getBeforeCalibration());
		values.put(AFTER_CALIBRATION, model.getAfterCalibration());
		values.put(CALIBRATION_ID, model.getCalibrationId());

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

	public void delete(PointVo counterVo) {
		delete(counterVo.getId());
	}

	public void deleteAll() {
		SQLiteDatabase db = new DatabaseHelper(mContext).getWritableDatabase();
		db.delete(TABLE, null, null);
		db.close();
	}

}
