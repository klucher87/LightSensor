package com.lightsensor.model;

import java.util.ArrayList;

import com.lightsensor.ISensorObservable;

public class LuxVo {
	
	private final ArrayList<ISensorObservable> mListeners = new ArrayList<ISensorObservable>();
	
	private float mValue;
	
	public float getValue(){
		return mValue;
	}
	
	public void setValue(float val){
		mValue = val;
		notifyListeners();
	}
	
	public void addListener(ISensorObservable listener){
		mListeners.add(listener);
	}
	
	public void removeListener(ISensorObservable listener){
		mListeners.remove(listener);
	}
	
	private void notifyListeners(){
		for(ISensorObservable listener : mListeners){
			listener.onValueChanged(this);
		}
	}
	
}
