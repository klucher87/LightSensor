package com.lightsensor;

import com.lightsensor.model.LuxVo;

public interface ISensorObservable {
	public void onValueChanged(LuxVo model);
}
