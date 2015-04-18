package com.lightsensor.controller;

import java.util.ArrayList;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.lightsensor.daos.DatabaseHelper;
import com.lightsensor.model.CalibrationVo;
import com.lightsensor.model.PointVo;
import com.lightsensor.model.SensorVo;
import com.lightsensor.regression.Measurements;

/**
 * Delegowanie zadan do pozostalych kontrolerow. Warstwa posredniczaca miedzy
 * kontrolerami.
 * 
 * @author Kamil Mikusek
 * 
 */
final public class Controller implements ICalibrationController,
		IPointController, IValueController {

	private static final String TAG = Controller.class.getSimpleName();
	private static final String IS_FIRST_RUN = "first_run";
	private static final String NOTE_1 = "Note 1";
	private static final String NOTE_2 = "Note 2";
	private static final String GALAXY_S3 = "Galaxy S3";
	private static final String HTC = "HTC One";
	
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
		
		SharedPreferences prefs = ctx.getSharedPreferences(
				ctx.getPackageName(), Context.MODE_PRIVATE);
		boolean isFirstRun = prefs.getBoolean(IS_FIRST_RUN, true);
		if(isFirstRun){
			prefs.edit().putBoolean(IS_FIRST_RUN, false).commit();
			insertDefaultCalibration(NOTE_1);
			insertDefaultCalibration(NOTE_2);
			insertDefaultCalibration(GALAXY_S3);
			insertDefaultCalibration(HTC);
		}
		Log.d(TAG, "is first run: " + isFirstRun);
	}
	
	private void insertDefaultCalibration(String str){
		insertNewCalibration(str);		
		CalibrationVo note1calibration = getCalibration(str);
		if(note1calibration != null){
			int id = note1calibration.getId();			
			for(int i=0; i<Measurements.LUXOMIERZ.length; i++){
				if(str.contains(NOTE_1)){
					insertNewPoint(Measurements.LUXOMIERZ[i], Measurements.NOTE_1[i], id);
				}else if(str.contains(NOTE_2)){
					insertNewPoint(Measurements.LUXOMIERZ[i], Measurements.NOTE_2[i], id);
				}else if(str.contains(GALAXY_S3)){
					insertNewPoint(Measurements.LUXOMIERZ[i], Measurements.S3[i], id);
				}else if(str.contains(HTC)){
					insertNewPoint(Measurements.LUXOMIERZ[i], Measurements.HTC[i], id);
				}				
			}			
		}
	}
	
	private CalibrationVo getCalibration(String calibration){
		for(CalibrationVo item : getCalibrations()){
			if(item.getLabel().contains(calibration)){
				return item;
			}
		}
		return null;
	}
	
//
//	double[] x = Measurements.LUXOMIERZ;
//	double[] y = Measurements.NOTE_2;
//
//	// pomiar 18
////	LinearRegression lr1 = new LinearRegression(new double[] { x[18] }, 
////												new double[] { y[18] });
//	//przesunięcie wykresu przy kalibracji z 1 punktem - 18-stym
//	final double shift_1 = x[18] - y[18];
//	
//	// wszystkie 36 pomiarow
//	LinearRegression lr_pre_def = new LinearRegression(x, y);
//	final double alpha_pre_def = lr_pre_def.slope();
//	final double beta_pre_def = lr_pre_def.intercept();
//	Log.e(TAG, ""+lr_pre_def.toString());
//	double avg_before = 0.0f;
//	double avg_1 = 0.0f;
//	double avg_2 = 0.0f;
//	double avg_5 = 0.0f;
//	double avg_10 = 0.0f;
//	double avg_pre_def = 0.0f;
//	
//	int counter = 0;
//	for (int i = 0; i < 100; i++) {
//		if(mCurrentValue!=0){
//			avg_before += mCurrentValue;		
//			avg_1 += mCurrentValue-shift_1;	
//			avg_2 += (mCurrentValue-beta2)/alpha2;	
//			avg_5 += (mCurrentValue-beta5)/alpha5;
//			avg_10 += (mCurrentValue-beta10)/alpha10;	
//			avg_pre_def += (mCurrentValue-beta_pre_def)/alpha_pre_def;	
//			counter++;
//		}
//		try {
//			Thread.sleep(50);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//	}
//	final double avg_before_val = avg_before / counter;
//	final double avg_1_val = avg_1 / counter;
//	final double avg_2_val = avg_2 / counter;
//	final double avg_5_val = avg_5 / counter;
//	final double avg_10_val = avg_10 / counter;
//	final double avg_pre_def_val = avg_pre_def / counter;
//	
//	mHandler.post(new Runnable() {
//		@Override
//		public void run() {
//			DecimalFormat df = new DecimalFormat("#.##");
//			mCurrBefore.setText(df.format(mCurrentValue) + "lux");
//			mCurr1.setText(df.format(mCurrentValue-shift_1) + "lux");
//			mCurr2.setText(df.format((mCurrentValue-beta2)/alpha2) + "lux");
//			mCurr5.setText(df.format((mCurrentValue-beta5)/alpha5) + "lux");
//			mCurr10.setText(df.format((mCurrentValue-beta10)/alpha10) + "lux");
//			mCurrPre.setText(df.format((mCurrentValue-beta_pre_def)/alpha_pre_def) + "lux");
//			mAvgBefore.setText(df.format(avg_before_val) + " lux");
//			mAvg1.setText(df.format(avg_1_val) + " lux");
//			mAvg2.setText(df.format(avg_2_val) + " lux");
//			mAvg5.setText(df.format(avg_5_val) + " lux");
//			mAvg10.setText(df.format(avg_10_val) + " lux");
//			mAvgPre.setText(df.format(avg_pre_def_val) + " lux");
//		}
//	});
//}	
	
	public static Controller getInstance(Context ctx) {
		if (INSTANCE == null) {
			INSTANCE = new Controller(ctx);
		}
		return INSTANCE;
	}

	public SensorVo getModel() {
		return mValueController.getModel();
	}

	// public void setModel(SensorVo read) {
	// mValueController.setModel(read);
	// }
	//
	public void onSensorChanged(float f) {
		mValueController.onSensorChanged(f);
	}

	@Override
	public void addCalibrationListener(IOnCalibrationUpdate listener) {
		mCalibrationController.addCalibrationListener(listener);
	}

	@Override
	public void removeCalibrationListener(IOnCalibrationUpdate listener) {
		mCalibrationController.removeCalibrationListener(listener);
	}

	@Override
	public ArrayList<CalibrationVo> getCalibrations() {
		return mCalibrationController.getCalibrations();
	}

	@Override
	public void setItems(ArrayList<CalibrationVo> items) {
		mCalibrationController.setItems(items);
	}

	@Override
	public void insertNewCalibration(String label) {
		mCalibrationController.insertNewCalibration(label);
	}

	@Override
	public void updateSelectionStates(CalibrationVo selected) {
		mCalibrationController.updateSelectionStates(selected);
	}

	@Override
	public void deleteSelectedCalibration() {
		// usuwa rowniez punkty zwiazane z kalibracja (o tym samym id)
		mPointController.deletePointsConnectedWith(mCalibrationController
				.getSelectedCalibration());
		mCalibrationController.deleteSelectedCalibration();
	}

	@Override
	public CharSequence getSelectedCalibrationName() {
		return mCalibrationController.getSelectedCalibrationName();
	}

	@Override
	public CalibrationVo getSelectedCalibration() {
		return mCalibrationController.getSelectedCalibration();
	}

	@Override
	public void updateSelectionStates(PointVo selected) {
		mPointController.updateSelectionStates(selected);
	}

	@Override
	public void addPointListener(IOnPointsUpdate listener) {
		mPointController.addPointListener(listener);
	}

	@Override
	public void removePointListener(IOnPointsUpdate listener) {
		mPointController.removePointListener(listener);
	}

	@Override
	public PointVo getSelectedPoint() {
		return mPointController.getSelectedPoint();
	}

	@Override
	public ArrayList<PointVo> getPoints() {
		return mPointController.getPoints();
	}

	@Override
	public void deleteSelectedPoint() {
		mPointController.deleteSelectedPoint();
	}

	@Override
	public void updateSelectedPoint(float before, float after) {
		mPointController.updateSelectedPoint(before, after);
	}

	@Override
	public void insertNewPoint(float before, float after, int calibration_id) {
		mPointController.insertNewPoint(before, after, calibration_id);
	}

	public void insertNewPoint(float before, float after) {
		insertNewPoint(before, after, mCalibrationController
				.getSelectedCalibration().getId());
	}

}
