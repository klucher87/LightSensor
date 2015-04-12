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
 * Delegowanie zadan do pozostalych kontrolerow. Warstwa posredniczaca miedzy kontrolerami.
 * 
 * @author kamil
 *
 */
final public class Controller implements ICalibrationController, IPointController, IValueController{

	private static Controller INSTANCE;

	private Context mContext;
	private CalibrationController mCalibrationController;
	private PointController mPointController;
	private ValueController mValueController;

	private Controller(Context ctx) {
		mContext = ctx;
		mCalibrationController = new CalibrationController(ctx);
		mPointController = new PointController(ctx);
		mValueController = new ValueController(ctx);
		// Create database first time
		new DatabaseHelper(mContext);
		mCalibrationController.fetchFromDB();
		mPointController.fetchFromDB();
	}

	public static Controller getInstance(Context ctx) {
		if (INSTANCE == null) {
			INSTANCE = new Controller(ctx);
		}
		return INSTANCE;
	}

	public void addCalibrationListener(IOnCalibrationUpdate listener) {
		mCalibrationController.addlistener(listener);
	}

	public void removeCalibrationListener(IOnCalibrationUpdate listener) {
		mCalibrationController.removeListener(listener);
	}

	public SensorVo getModel() {
		return mValueController.getModel();
	}

	public void setModel(SensorVo read) {
		mValueController.setModel(read);
	}
	
	public void onSensorChanged(float f) {
		mValueController.onSensorChanged(f);
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
	
	public void addPointListener(IOnPointsUpdate listener){
		mPointController.addListener(listener);
	}
	
	public void removePointListener(IOnPointsUpdate listener){
		mPointController.removeListener(listener);
	}
	
	public ArrayList<PointVo> getPoints(){
		return mPointController.getPoints();
	}
	
	public void insertNewPoint(float before, float after){
		mPointController.insertNewPoint(before, after, mCalibrationController.getSelecteditem().getId());
	}
	
}
