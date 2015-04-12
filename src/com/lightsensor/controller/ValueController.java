package com.lightsensor.controller;

import com.lightsensor.model.SensorVo;

import android.content.Context;

public class ValueController implements IValueController{

	private Context mContext;
	private SensorVo mRead;
	
	public ValueController(Context ctx) {
		mContext = ctx;
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

}
