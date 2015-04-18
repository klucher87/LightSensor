package com.lightsensor.controller;

import java.util.ArrayList;

import android.content.Context;
import android.widget.Toast;

import com.lightsensor.R;
import com.lightsensor.daos.PointDao;
import com.lightsensor.model.CalibrationVo;
import com.lightsensor.model.PointVo;

final class PointController implements IPointController {

	private ArrayList<PointVo> mPoints;
	private ArrayList<IOnPointsUpdate> mListeners;
	private Context mContext;

	public PointController(Context ctx) {
		mPoints = new ArrayList<PointVo>();
		mListeners = new ArrayList<IOnPointsUpdate>();
		mContext = ctx;
	}

	public void addPointListener(IOnPointsUpdate listener) {
		mListeners.add(listener);
	}

	public void removePointListener(IOnPointsUpdate listener) {
		mListeners.remove(listener);
	}

	public ArrayList<PointVo> getPoints() {
		return mPoints;
	}

//	public void setPoints(ArrayList<PointVo> items) {
//		mPoints = items;
//		notifyListeners();
//	}

	public void insertNewPoint(float before, float after, int calibration_id) {
		before = (float) Math.round(before * 100) / 100;
		after = (float) Math.round(after * 100) / 100;
		PointDao dao = new PointDao(mContext);
		final PointVo model = new PointVo();
		model.setBeforeCalibration(before);
		model.setAfterCalibration(after);
		model.setCalibrationId(calibration_id);
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

	public void deletePointsConnectedWith(CalibrationVo calibrationVo) {
		if (calibrationVo != null) {
			PointDao dao = new PointDao(mContext);
			for (PointVo point : dao.getAllWithCalibrationId(calibrationVo
					.getId())) {
				if (point != null) {
					dao.delete(point);
				}
			}
			fetchFromDB();
		}
	}

//	public void deselectAllPoints() {
//		for (int i = 0; i < mPoints.size(); i++) {
//			PointVo item = mPoints.get(i);
//			if (item.isSelected()) {
//				item.setSelected(false);
//			}
//		}
//	}

	public void updateSelectionStates(PointVo selected) {
		for (int i = 0; i < mPoints.size(); i++) {
			PointVo item = mPoints.get(i);
			if (item.isSelected() && item.getId() != selected.getId()) {
				item.setSelected(false);
			}
		}
		selected.setSelected(!selected.isSelected());
	}

	public void deleteSelectedPoint() {
		PointDao dao = new PointDao(mContext);
		PointVo item = getSelectedPoint();
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

	public PointVo getSelectedPoint() {
		for (PointVo item : mPoints) {
			if (item.isSelected()) {
				return item;
			}
		}
		return null;
	}
	
	public void updateSelectedPoint(float before, float after) {
		before = (float) Math.round(before * 100) / 100;
		after = (float) Math.round(after * 100) / 100;
		PointDao dao = new PointDao(mContext);
		PointVo model = getSelectedPoint();
		model.setBeforeCalibration(before);
		model.setAfterCalibration(after);
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
	
	public void fetchFromDB() {
		PointDao dao = new PointDao(mContext);
		while (mPoints.size() > 0) {
			mPoints.remove(0);
		}
		for (PointVo obj : dao.getAll()) {
			mPoints.add(obj);
		}
		notifyListeners();
	}

	private void notifyListeners() {
		for (IOnPointsUpdate listener : mListeners) {
			listener.onPointsUpdate();
		}
	}

}
