package com.lightsensor.controller;

import android.content.Context;

import com.lightsensor.model.SensorVo;

public class ValueController implements IValueController{

	private Context mContext;
	private SensorVo mSensorVo;
	
	public ValueController(Context ctx) {
		mContext = ctx;
		mSensorVo = new SensorVo();
	}
	
	public SensorVo getModel() {
		return mSensorVo;
	}
	
	public void onSensorChanged(float f) {
		mSensorVo.setValue(f);
	}
	
	

}
