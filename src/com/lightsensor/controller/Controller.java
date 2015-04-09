package com.lightsensor.controller;

import java.util.ArrayList;

import android.content.Context;

import com.lightsensor.dao.PhoneDao;
import com.lightsensor.model.LuxVo;
import com.lightsensor.model.PhoneVo;

final public class Controller {

	private static Controller INSTANCE;

	private ArrayList<PhoneVo> mItems = new ArrayList<PhoneVo>();
	private Context mContext;
	// model do odczytu z sensora
	private LuxVo mRead;

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

	public LuxVo getModel() {
		return mRead;
	}

	public void setModel(LuxVo read) {
		mRead = read;
	}

	public ArrayList<PhoneVo> getItems() {
		return mItems;
	}

	public void setItems(ArrayList<PhoneVo> items) {
		mItems = items;
	}

	public void onSensorChanged(float f) {
		mRead.setValue(f);
	}

	public void fetchFromDB() {
		PhoneDao dao = new PhoneDao(mContext);
		while (mItems.size() > 0) {
			mItems.remove(0);
		}
		for (PhoneVo obj : dao.getAll()) {
			mItems.add(obj);
		}
	}

	public void insertNew(String label) {
		PhoneDao dao = new PhoneDao(mContext);
		final PhoneVo model = new PhoneVo();
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
		for (PhoneVo item : mItems) {
			if (item.isSelected()) {
				return item.getLabel();
			}
		}
		return "brak";
	}

}
