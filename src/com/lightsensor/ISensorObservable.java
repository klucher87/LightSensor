package com.lightsensor;

import com.lightsensor.model.SensorVo;


public interface ISensorObservable {
	public void onValueChanged(SensorVo model);
}
