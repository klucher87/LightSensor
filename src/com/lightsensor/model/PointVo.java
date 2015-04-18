package com.lightsensor.model;


public class PointVo {
	
	private int mId = -1;
	private int mCalibrationId = -1;
	private float mBeforeCalibration = -1f;
	private float mAfterCalibration = -1f;
	private boolean mIsSelected = false;
	
	public int getId() {
		return mId;
	}
	public void setId(int id) {
		mId = id;
	}	
	
	public int getCalibrationId() {
		return mCalibrationId;
	}
	
	public void setCalibrationId(int calibration_id) {
		mCalibrationId = calibration_id;
	}
	
	public float getBeforeCalibration() {
		return mBeforeCalibration;
	}
	
	public void setBeforeCalibration(float before_calibration) {
		mBeforeCalibration = before_calibration;
	}
	
	public float getAfterCalibration() {
		return mAfterCalibration;
	}
	
	public void setAfterCalibration(float after_calibration) {
		mAfterCalibration = after_calibration;
	}
	
	public boolean isSelected() {
		return mIsSelected;
	}
	
	public void setSelected(boolean isChecked) {
		mIsSelected = isChecked;
	}
	
}
