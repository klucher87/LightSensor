package com.lightsensor.controller;

import java.util.ArrayList;

import android.content.Context;

import com.lightsensor.daos.CalibrationDao;
import com.lightsensor.model.SensorVo;
import com.lightsensor.model.CalibrationVo;

final public class Controller {

	private static Controller INSTANCE;

	private ArrayList<CalibrationVo> mItems = new ArrayList<CalibrationVo>();
	private Context mContext;
	// model do odczytu z sensora
	private SensorVo mRead;

	private Controller(Context ctx) {
		mContext = ctx;
		fetchFromDB();
	}

	public static Controller getInstance(Context ctx) {
		if (INSTANCE == null) {
			INSTANCE = new Controller(ctx);
		}
		return INSTANCE;
	}

	public SensorVo getModel() {
		return mRead;
	}

	public void setModel(SensorVo read) {
		mRead = read;
	}

	public ArrayList<CalibrationVo> getItems() {
		return mItems;
	}

	public void setItems(ArrayList<CalibrationVo> items) {
		mItems = items;
	}

	public void onSensorChanged(float f) {
		mRead.setValue(f);
	}

	public void fetchFromDB() {
		CalibrationDao dao = new CalibrationDao(mContext);
		while (mItems.size() > 0) {
			mItems.remove(0);
		}
		for (CalibrationVo obj : dao.getAll()) {
			mItems.add(obj);
		}
	}

	public void insertNew(String label) {
		CalibrationDao dao = new CalibrationDao(mContext);
		final CalibrationVo model = new CalibrationVo();
		model.setLabel(label);
		if (model.getId() > 0) {
			int effected = dao.update(model);
			// this would be the case if
			// item is saved, item is deleted from list, user goes
			// history back,
			// old model still have id value.
			if (effected < 1) {
				long id = dao.insert(model);
				model.setId((int) id);
			}
		} else {
			long id = dao.insert(model);
			model.setId((int) id);
		}
		mItems.add(model);
	}

	public CharSequence getSelecteditemLabel() {
		for (CalibrationVo item : mItems) {
			if (item.isSelected()) {
				return item.getLabel();
			}
		}
		return "brak";
	}

}
