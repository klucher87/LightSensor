package com.lightsensor.controller;

import java.util.ArrayList;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.example.luxsensor.R;
import com.lightsensor.daos.CalibrationDao;
import com.lightsensor.model.CalibrationVo;
import com.lightsensor.model.SensorVo;

final public class Controller {

	private static Controller INSTANCE;
	
	public interface IOnCalibrationUpdate{
		public void onCalibrationUpdate();
	}

	private ArrayList<CalibrationVo> mItems = new ArrayList<CalibrationVo>();
	private ArrayList<IOnCalibrationUpdate> mListeners = new ArrayList<IOnCalibrationUpdate>();
	private Context mContext;
	private Handler mHandler;
	// model do odczytu z sensora
	private SensorVo mRead;

	private Controller(Context ctx) {
		mContext = ctx;
		mHandler = new Handler(Looper.getMainLooper());
		fetchFromDB();
	}

	public static Controller getInstance(Context ctx) {
		if (INSTANCE == null) {
			INSTANCE = new Controller(ctx);
		}
		return INSTANCE;
	}
	
	public void addlistener(IOnCalibrationUpdate listener){
		mListeners.add(listener);
	}
	
	public void removeListener(IOnCalibrationUpdate listener){
		mListeners.remove(listener);
	}

	public SensorVo getModel() {
		return mRead;
	}

	public void setModel(SensorVo read) {
		mRead = read;
	}

	public ArrayList<CalibrationVo> getItems() {
		return mItems;
	}

	public void setItems(ArrayList<CalibrationVo> items) {
		mItems = items;
		notifyListeners();
	}

	public void onSensorChanged(float f) {
		mRead.setValue(f);
	}

	private void fetchFromDB() {
		CalibrationDao dao = new CalibrationDao(mContext);
		while (mItems.size() > 0) {
			mItems.remove(0);
		}
		for (CalibrationVo obj : dao.getAll()) {
			mItems.add(obj);
		}
		notifyListeners();
	}

	private void notifyListeners(){
		for(IOnCalibrationUpdate listener : mListeners){
			listener.onCalibrationUpdate();
		}
	}
	
	public void insertNew(final String label) {
		mHandler.post(new Runnable() {
			@Override
			public void run() {
				CalibrationDao dao = new CalibrationDao(mContext);
				final CalibrationVo model = new CalibrationVo();
				model.setLabel(label);
				if (model.getId() > 0) {
					int effected = dao.update(model);
					// this would be the case if
					// item is saved, item is deleted from list, user goes
					// history back,
					// old model still have id value.
					if (effected < 1) {
						long id = dao.insert(model);
						model.setId((int) id);
					}
				} else {
					long id = dao.insert(model);
					model.setId((int) id);
				}
				// mItems.add(model);

				fetchFromDB();
			}
		});
	}

	public void deleteSelectedItem() {
		mHandler.post(new Runnable() {
			@Override
			public void run() {
				CalibrationDao dao = new CalibrationDao(mContext);
				CalibrationVo item = getSelecteditem();
				// synchronized (item) {
				if (item != null) {
					dao.delete(item);
				} else {
					Toast.makeText(
							mContext,
							mContext.getResources().getString(
									R.string.no_selected_item_info),
							Toast.LENGTH_SHORT).show();
				}
				fetchFromDB();
				// }
			}

		});
	}

	public CharSequence getSelecteditemLabel() {
		CalibrationVo item = getSelecteditem();
		return item != null ? item.getLabel() : "brak";
	}

	private CalibrationVo getSelecteditem() {
		// synchronized (mItems) {
		for (CalibrationVo item : mItems) {
			if (item.isSelected()) {
				return item;
			}
		}
		// }
		return null;
	}

}
