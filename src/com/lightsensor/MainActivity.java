package com.lightsensor;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.lightsensor.controller.Controller;
import com.lightsensor.model.SensorVo;

public class MainActivity extends Activity implements SensorEventListener,
		IOnSensorChange {

	private static final String TAG = "KM";

	private SensorManager mSensorManager;
	private Sensor mLight;
	private Controller mController;
	private TextView mCurrVal;
	private Button mOpen;
	private TextView mCurrCalibration;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_layout);
		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		mLight = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

		mCurrVal = (TextView) findViewById(R.id.txt_curr);
//		mAddedVal = (TextView) findViewById(R.id.txt_curr_2);
		mCurrCalibration = (TextView) findViewById(R.id.calibrate_txt);
		mOpen = (Button) findViewById(R.id.open_btn);
		mOpen.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(MainActivity.this,
						CalibrationsActivity.class));
			}
		});
		mController = Controller.getInstance(getApplicationContext());
		mSensorManager.registerListener(this, mLight,
				SensorManager.SENSOR_DELAY_NORMAL);
		mController.getModel().addListener(this);
	}

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		switch (arg1) {
		case SensorManager.SENSOR_STATUS_ACCURACY_LOW:
			Log.d(TAG, "SENSOR_STATUS_ACCURACY_LOW");
			break;
		case SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM:
			Log.d(TAG, "SENSOR_STATUS_ACCURACY_MEDIUM");
			break;
		case SensorManager.SENSOR_STATUS_ACCURACY_HIGH:
			Log.d(TAG, "SENSOR_STATUS_ACCURACY_HIGH");
			break;
		default:
			Log.d(TAG, "SENSOR_STATUS_UNRELIABLE");
		}
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		mController.onSensorChanged(event.values[0]);
	}

	@Override
	protected void onResume() {
		super.onResume();
		mCurrCalibration.setText(mController.getSelectedCalibrationName());
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mSensorManager.unregisterListener(this);
		mController.getModel().removeListener(this);
	}

	@Override
	public void onValueChanged(SensorVo model) {
		mCurrVal.setText(Float.toString(model.getValue())+" lx");
//		mAddedVal.setText(Float.toString(model.getValue() + 3));
	}

}
