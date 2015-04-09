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

import com.example.luxsensor.R;
import com.lightsensor.model.PhoneVo;

public class ListAdapter extends ArrayAdapter<PhoneVo> implements OnItemClickListener{

	private LayoutInflater mInflater;
	private ListView mList;
	
	public ListAdapter(Context context, ListView list, ArrayList<PhoneVo> items) {
		super(context, R.layout.list_item, items);
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mList = list;
		mList.setOnItemClickListener(this);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder;
		PhoneVo counter = getItem(position);
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.list_item, parent, false);
			holder = new Holder();
			holder.label = (TextView) convertView.findViewById(R.id.label);
			holder.checkbox = (CheckBox) convertView
					.findViewById(R.id.checkbox);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}

		int color = ((position % 2) == 0) ? 0xFFF0FFE1 : 0xFFFFFFFF;
		convertView.setBackgroundColor(color);
		holder.label.setText(counter.getLabel());
		holder.checkbox.setChecked(counter.isChecked());
		return convertView;
	}

	private class Holder {
		public TextView label;
		public CheckBox checkbox;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		boolean isChecked = getItem(position).isChecked();
		for(int i = 0; i<getCount(); i++){
			getItem(i).setChecked(false);
		}
		getItem(position).setChecked(!isChecked);
		notifyDataSetChanged();
	}
}
