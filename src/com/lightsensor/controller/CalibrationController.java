package com.lightsensor.controller;

import java.util.ArrayList;

import android.content.Context;
import android.widget.Toast;

import com.lightsensor.R;
import com.lightsensor.daos.CalibrationDao;
import com.lightsensor.model.CalibrationVo;

final class CalibrationController implements ICalibrationController{

	private ArrayList<CalibrationVo> mItems;
	private ArrayList<IOnCalibrationUpdate> mListeners;
	private Context mContext;
	
	public CalibrationController(Context ctx){
		mItems = new ArrayList<CalibrationVo>();
		mListeners = new ArrayList<IOnCalibrationUpdate>();
		mContext = ctx;
	}

	@Override
	public void addCalibrationListener(IOnCalibrationUpdate listener) {
		mListeners.add(listener);
	}

	@Override
	public void removeCalibrationListener(IOnCalibrationUpdate listener) {
		mListeners.remove(listener);
	}

	@Override
	public ArrayList<CalibrationVo> getCalibrations() {
		return mItems;
	}

	@Override
	public void setItems(ArrayList<CalibrationVo> items) {
		mItems = items;
		notifyListeners();
	}

	@Override
	public void insertNewCalibration(final String label) {
		CalibrationDao dao = new CalibrationDao(mContext);
		final CalibrationVo model = new CalibrationVo();
		model.setLabel(label);
		if (model.getId() > 0) {
			int effected = dao.update(model);
			if (effected < 1) {
				long id = dao.insert(model);
				model.setId((int) id);
			}
		} else {
			long id = dao.insert(model);
			model.setId((int) id);
		}
		fetchFromDB();
	}

	@Override
	public void updateSelectionStates(final CalibrationVo selected) {
		for (int i = 0; i < mItems.size(); i++) {
			CalibrationVo item = mItems.get(i);
			// deselect all others
			if (item.isSelected() && item.getId() != selected.getId()) {
				item.setSelected(false);
				update(item);
			}
		}
		selected.setSelected(!selected.isSelected());
		update(selected);
		fetchFromDB();
	}

	@Override
	public void deleteSelectedCalibration() {
		CalibrationDao dao = new CalibrationDao(mContext);
		CalibrationVo item = getSelectedCalibration();
		if (item != null) {
			dao.delete(item);
		} else {
			Toast.makeText(
					mContext,
					mContext.getResources().getString(
							R.string.no_selected_point_info), Toast.LENGTH_SHORT)
					.show();
		}
		fetchFromDB();
	}

	@Override
	public CharSequence getSelectedCalibrationName() {
		CalibrationVo item = getSelectedCalibration();
		return item != null ? item.getLabel() : "brak";
	}

	@Override
	public CalibrationVo getSelectedCalibration() {
		for (CalibrationVo item : mItems) {
			if (item.isSelected()) {
				return item;
			}
		}
		return null;
	}
	
	public void fetchFromDB() {
		CalibrationDao dao = new CalibrationDao(mContext);
		while (mItems.size() > 0) {
			mItems.remove(0);
		}
		for (CalibrationVo obj : dao.getAll()) {
			mItems.add(obj);
		}
		notifyListeners();
	}
	
	private void update(final CalibrationVo model) {
		CalibrationDao dao = new CalibrationDao(mContext);
		if (model.getId() > 0) {
			int effected = dao.update(model);
			if (effected < 1) {
				long id = dao.insert(model);
				model.setId((int) id);
			}
		} else {
			long id = dao.insert(model);
			model.setId((int) id);
		}
	}
	
	private void notifyListeners() {
		for (IOnCalibrationUpdate listener : mListeners) {
			listener.onCalibrationUpdate();
		}
	}

}
