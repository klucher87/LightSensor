package com.lightsensor.model;


public class PhoneVo {
	
	private int id = -1;
	private String label = "";
	private boolean mIsSelected = false;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}	
	
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	
	public boolean isSelected() {
		return mIsSelected;
	}
	public void setSelected(boolean isChecked) {
		mIsSelected = isChecked;
	}
	
}
