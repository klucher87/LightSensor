package com.lightsensor.controller;

import java.util.ArrayList;

import com.lightsensor.model.CalibrationVo;

public interface ICalibrationController {

	public void addCalibrationListener(IOnCalibrationUpdate listener);

	public void removeCalibrationListener(IOnCalibrationUpdate listener);

	public ArrayList<CalibrationVo> getCalibrations();

	public void setItems(ArrayList<CalibrationVo> items);

	public void insertNewCalibration(final String label);

	public void updateSelectionStates(final CalibrationVo selected);

	public void deleteSelectedCalibration();

	public CharSequence getSelectedCalibrationName();

	public CalibrationVo getSelectedCalibration();
	
}
