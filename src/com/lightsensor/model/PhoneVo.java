package com.lightsensor.model;


public class PhoneVo {
	
	private int id = -1;
	private String label = "";
	private boolean mIsChecked = false;
	
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
	
	public boolean isChecked() {
		return mIsChecked;
	}
	public void setChecked(boolean isChecked) {
		mIsChecked = isChecked;
	}
	
	@Override
	synchronized public PhoneVo clone() {
		PhoneVo vo = new PhoneVo();
		vo.setId(id);
		vo.setLabel(label);
		return vo;
	}
	
	synchronized public void consume(PhoneVo vo) {
		this.id = vo.getId();
		this.label = vo.getLabel();
	}
}
