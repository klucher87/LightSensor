package com.lightsensor.controller;

import java.util.ArrayList;

import com.lightsensor.model.LuxVo;
import com.lightsensor.model.PhoneVo;

final public class Controller {

	private static Controller INSTANCE;
	
	private ArrayList<PhoneVo> mItems = new ArrayList<PhoneVo>();	

	private LuxVo mValue;

	private Controller() {
	}

	public static Controller getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new Controller();
		}
		return INSTANCE;
	}
	
	public ArrayList<PhoneVo> getItems() {
		return mItems;
	}

	public void setItems(ArrayList<PhoneVo> items) {
		mItems = items;
	}
	
	public LuxVo getModel(){
		return mValue;
	}
	
	public void setModel(LuxVo model){
		mValue = model;
	}

	public void onSensorChanged(float f) {
		mValue.setValue(f);
	}
	
	

}
