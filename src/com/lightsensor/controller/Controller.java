package com.lightsensor.controller;

import java.util.ArrayList;

import android.content.Context;
import android.widget.Toast;

import com.lightsensor.R;
import com.lightsensor.daos.CalibrationDao;
import com.lightsensor.daos.DatabaseHelper;
import com.lightsensor.model.CalibrationVo;
import com.lightsensor.model.SensorVo;

final public class Controller {

	private static Controller INSTANCE;

	public interface IOnCalibrationUpdate {
		public void onCalibrationUpdate();
	}

	private ArrayList<CalibrationVo> mItems = new ArrayList<CalibrationVo>();
	private ArrayList<IOnCalibrationUpdate> mListeners = new ArrayList<IOnCalibrationUpdate>();
	private Context mContext;
	// model do odczytu z sensora -> dodac oddzielny kontroller?
	private SensorVo mRead;

	private Controller(Context ctx) {
		mContext = ctx;
		// Create database first time
		new DatabaseHelper(mContext);
		fetchFromDB();
	}

	public static Controller getInstance(Context ctx) {
		if (INSTANCE == null) {
			INSTANCE = new Controller(ctx);
		}
		return INSTANCE;
	}

	public void addlistener(IOnCalibrationUpdate listener) {
		mListeners.add(listener);
	}

	public void removeListener(IOnCalibrationUpdate listener) {
		mListeners.remove(listener);
	}

	public SensorVo getModel() {
		return mRead;
	}

	public void setModel(SensorVo read) {
		mRead = read;
	}
	
	public void onSensorChanged(float f) {
		mRead.setValue(f);
	}

	public ArrayList<CalibrationVo> getItems() {
		return mItems;
	}

	public void setItems(ArrayList<CalibrationVo> items) {
		mItems = items;
		notifyListeners();
	}

	public void insertNewCalibration(final String label) {
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
		fetchFromDB();
	}

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

	public void deleteSelectedCalibration() {
		CalibrationDao dao = new CalibrationDao(mContext);
		CalibrationVo item = getSelecteditem();
		if (item != null) {
			dao.delete(item);
		} else {
			Toast.makeText(
					mContext,
					mContext.getResources().getString(
							R.string.no_selected_item_info), Toast.LENGTH_SHORT)
					.show();
		}
		fetchFromDB();
	}

	public CharSequence getSelectedCalibrationName() {
		CalibrationVo item = getSelecteditem();
		return item != null ? item.getLabel() : "brak";
	}

	private CalibrationVo getSelecteditem() {
		for (CalibrationVo item : mItems) {
			if (item.isSelected()) {
				return item;
			}
		}
		return null;
	}
	
	private void update(final CalibrationVo model) {
		CalibrationDao dao = new CalibrationDao(mContext);
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
	}
	
	private void fetchFromDB() {
		CalibrationDao dao = new CalibrationDao(mContext);
		while (mItems.size() > 0) {
			mItems.remove(0);
		}
		for (CalibrationVo obj : dao.getAll()) {
			mItems.add(obj);
		}
		notifyListeners();
	}

	private void notifyListeners() {
		for (IOnCalibrationUpdate listener : mListeners) {
			listener.onCalibrationUpdate();
		}
	}

}
