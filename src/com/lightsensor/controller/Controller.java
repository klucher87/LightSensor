package com.lightsensor.controller;

import java.util.ArrayList;

import android.content.Context;
import android.widget.Toast;

import com.lightsensor.R;
import com.lightsensor.daos.CalibrationDao;
import com.lightsensor.daos.DatabaseHelper;
import com.lightsensor.daos.PointDao;
import com.lightsensor.model.CalibrationVo;
import com.lightsensor.model.PointVo;
import com.lightsensor.model.SensorVo;

/**
 * Delegowanie do pozostalych dwoch kontrolerow
 * 
 * @author kamil
 *
 */
final public class Controller implements ICalibrationController, IPointController{

	private static Controller INSTANCE;

	private ArrayList<PointVo> mPoints = new ArrayList<PointVo>();
	private Context mContext;
	private CalibrationController mCalibrationController;
	// model do odczytu z sensora -> dodac oddzielny kontroller?
	private SensorVo mRead;

	private Controller(Context ctx) {
		mContext = ctx;
		mCalibrationController = new CalibrationController(ctx);
		// Create database first time
		new DatabaseHelper(mContext);
		mCalibrationController.fetchFromDB();
	}

	public static Controller getInstance(Context ctx) {
		if (INSTANCE == null) {
			INSTANCE = new Controller(ctx);
		}
		return INSTANCE;
	}

	public void addlistener(IOnCalibrationUpdate listener) {
		mCalibrationController.addlistener(listener);
	}

	public void removeListener(IOnCalibrationUpdate listener) {
		mCalibrationController.removeListener(listener);
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

	public ArrayList<CalibrationVo> getCalibrations() {
		return mCalibrationController.getItems();
	}

	public void setItems(ArrayList<CalibrationVo> items) {
		mCalibrationController.setItems(items);
	}

	public void insertNewCalibration(final String label) {
		mCalibrationController.insertNewCalibration(label);
	}
	
	public void insertNewPoint(int _id, float before, float after, int calibration_id) {
		PointDao dao = new PointDao(mContext);
		final PointVo model = new PointVo();
		model.setId(_id);
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
		
		//move to external method
//		PointDao dao = new PointDao(mContext);
		while (mPoints.size() > 0) {
			mPoints.remove(0);
		}
		for (PointVo obj : dao.getAll()) {
			mPoints.add(obj);
		}
	}

	public void updateSelectionStates(final CalibrationVo selected) {
		mCalibrationController.updateSelectionStates(selected);
	}

	public void deleteSelectedCalibration() {
		mCalibrationController.deleteSelectedCalibration();
	}

	public CharSequence getSelectedCalibrationName() {
		return mCalibrationController.getSelectedCalibrationName();
	}

	public CalibrationVo getSelectedCalibration() {
		return mCalibrationController.getSelecteditem();
	}
	
}
