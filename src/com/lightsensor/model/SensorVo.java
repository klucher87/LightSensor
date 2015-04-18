package com.lightsensor.model;

import java.util.ArrayList;

import android.util.Log;

import com.lightsensor.IOnSensorChange;

public class SensorVo {
	
	private final ArrayList<IOnSensorChange> mListeners = new ArrayList<IOnSensorChange>();
	
	private float mValue;
	
	public float getValue(){
		return mValue;
	}
	
	public void setValue(float val){
		mValue = val;
		notifyListeners();
	}
	
	public void addListener(IOnSensorChange listener){
		mListeners.add(listener);
	}
	
	public void removeListener(IOnSensorChange listener){
		mListeners.remove(listener);
	}
	
	private void notifyListeners(){
		for(IOnSensorChange listener : mListeners){
//			Log.e("KM", "notify "+mListeners.size());
			listener.onValueChanged(this);
		}
	}
	
}
