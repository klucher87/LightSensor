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
import com.lightsensor.controller.IOnPointsUpdate;
import com.lightsensor.model.PointVo;

public class PointListAdapter extends ArrayAdapter<PointVo> implements
		OnItemClickListener, IOnPointsUpdate {

	private LayoutInflater mInflater;
	private ListView mList;
	private ArrayList<PointVo> mItems;
	private Controller mController;

	public PointListAdapter(Context context, ListView list,
			ArrayList<PointVo> arrayList) {
		super(context, R.layout.point_list_item, arrayList);
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mList = list;
		mList.setOnItemClickListener(this);
		mItems = arrayList;
		mController = Controller.getInstance(getContext());
		mController.addPointListener(this);
	}

	@Override
	public PointVo getItem(int position) {
		return mItems.get(position);
	}

	@Override
	public int getCount() {
		return mItems.size();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder;
		PointVo model = getItem(position);
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.point_list_item, parent, false);
			holder = new Holder();
			holder.point_id = (TextView) convertView.findViewById(R.id.point_id);
			holder.before = (TextView) convertView.findViewById(R.id.before);
			holder.after =  (TextView) convertView.findViewById(R.id.after);
			holder.calibration_id = (TextView) convertView.findViewById(R.id.calibration_id);
			holder.checkbox = (CheckBox) convertView.findViewById(R.id.checkbox);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}

		int color = ((position % 2) == 0) ? 0xFFF0FFE1 : 0xFFFFFFFF;
		convertView.setBackgroundColor(color);
		holder.point_id.setText(Integer.toString(model.getId()));
		holder.before.setText(Float.toString(model.getBeforeCalibration()));
		holder.after.setText(Float.toString(model.getAfterCalibration()));
		holder.calibration_id.setText(Integer.toString(model.getCalibrationId()));
//		holder.checkbox.setChecked(model.);
		return convertView;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
//		mController.updateSelectionStates(getItem(position));
	}

	@Override
	public void onPointsUpdate() {
		notifyDataSetChanged();
	}

	private class Holder {
		public TextView point_id;
		public TextView before;
		public TextView after;
		public TextView calibration_id;
		public CheckBox checkbox;
	}

}
