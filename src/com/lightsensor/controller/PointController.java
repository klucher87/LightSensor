package com.lightsensor.controller;

import java.util.ArrayList;

import com.lightsensor.daos.PointDao;
import com.lightsensor.model.PointVo;

import android.content.Context;

public class PointController implements IPointController {

	private ArrayList<PointVo> mPoints;
	private ArrayList<IOnPointsUpdate> mListeners;
	private Context mContext;

	public PointController(Context ctx) {
		mPoints = new ArrayList<PointVo>();
		mListeners = new ArrayList<IOnPointsUpdate>();
		mContext = ctx;
	}

	public void addListener(IOnPointsUpdate listener) {
		mListeners.add(listener);
	}

	public void removeListener(IOnPointsUpdate listener) {
		mListeners.remove(listener);
	}

	public ArrayList<PointVo> getPoints() {
		return mPoints;
	}

	public void setItems(ArrayList<PointVo> items) {
		mPoints = items;
		notifyListeners();
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

	public void insertNewPoint(float before, float after, int calibration_id) {
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

	private void notifyListeners() {
		for (IOnPointsUpdate listener : mListeners) {
			listener.onPointsUpdate();
		}
	}

}
