package com.lightsensor.controller;

import java.util.ArrayList;

import com.lightsensor.model.PointVo;

public interface IPointController {

	public void addPointListener(IOnPointsUpdate listener);

	public void removePointListener(IOnPointsUpdate listener);

	public ArrayList<PointVo> getPoints();

	public void insertNewPoint(float before, float after, int calibration_id);

	public void updateSelectionStates(PointVo selected);

	public void deleteSelectedPoint();

	public PointVo getSelectedPoint();

	public void updateSelectedPoint(float before, float after);

}
