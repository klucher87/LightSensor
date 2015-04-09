package com.lightsensor.model;

import java.util.ArrayList;

import com.lightsensor.OnChangeListener;

public class LuxVo {
	
	private final ArrayList<OnChangeListener> mListeners = new ArrayList<OnChangeListener>();
	
	private float mValue;
	
	public float getValue(){
		return mValue;
	}
	
	public void setValue(float val){
		mValue = val;
		notifyListeners();
	}
	
	public void addListener(OnChangeListener listener){
		mListeners.add(listener);
	}
	
	public void removeListener(OnChangeListener listener){
		mListeners.remove(listener);
	}
	
	private void notifyListeners(){
		for(OnChangeListener listener : mListeners){
			listener.onChange(this);
		}
	}
	
}
