package com.lightsensor;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.lightsensor.controller.Controller;
import com.lightsensor.controller.IOnCalibrationUpdate;
import com.lightsensor.model.CalibrationVo;

public class CalibrationListAdapter extends ArrayAdapter<CalibrationVo> implements
		OnItemClickListener, IOnCalibrationUpdate {

	private LayoutInflater mInflater;
	private ListView mList;
	private ArrayList<CalibrationVo> mItems;
	private Controller mController;

	public CalibrationListAdapter(Context context, ListView list,
			ArrayList<CalibrationVo> items) {
		super(context, R.layout.calibration_list_item, items);
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mList = list;
		mList.setOnItemClickListener(this);
		mItems = items;
		mController = Controller.getInstance(getContext());
		mController.addCalibrationListener(this);
	}

	@Override
	public CalibrationVo getItem(int position) {
		return mItems.get(position);
	}

	@Override
	public int getCount() {
		return mItems.size();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder;
		CalibrationVo model = getItem(position);
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.calibration_list_item, parent, false);
			holder = new Holder();
			holder.model_id = (TextView) convertView
					.findViewById(R.id.model_id);
			holder.label = (TextView) convertView.findViewById(R.id.label);
			holder.checkbox = (CheckBox) convertView
					.findViewById(R.id.checkbox);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}

		int color = ((position % 2) == 0) ? 0xFFF0FFE1 : 0xFFFFFFFF;
		convertView.setBackgroundColor(color);
		holder.model_id.setText(Integer.toString(model.getId()));
		holder.label.setText(model.getLabel());
		holder.checkbox.setChecked(model.isSelected());
		return convertView;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		mController.updateSelectionStates(getItem(position));
	}

	@Override
	public void onCalibrationUpdate() {
		notifyDataSetChanged();
	}

	private class Holder {
		public TextView model_id;
		public TextView label;
		public CheckBox checkbox;
	}

}
