package com.lightsensor;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lightsensor.controller.Controller;
import com.lightsensor.controller.IOnCalibrationUpdate;
import com.lightsensor.controller.IOnPointsUpdate;
import com.lightsensor.model.SensorVo;

public class PointsActivity extends Activity {

	private Controller mController;
	private Button mInsert, mDelete, mEdit;
	private ListView mList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.points_layout);

		mController = Controller.getInstance(getApplicationContext());

		mInsert = (Button) findViewById(R.id.insert_btn);
		mInsert.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showInsertDialog();
			}
		});
		mDelete = (Button) findViewById(R.id.delete_btn);
		mDelete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//
			}
		});
		mEdit = (Button) findViewById(R.id.edit_btn);
		mEdit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//
			}
		});

		mList = (ListView) findViewById(R.id.list_view);
		mList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		mList.setAdapter(new PointListAdapter(getApplicationContext(), mList,
				mController.getPoints()));

	}

	private void showInsertDialog() {

		final View view = getLayoutInflater().inflate(
				R.layout.point_insert_dialog_layout, null);
		final CheckBox check = (CheckBox) view.findViewById(R.id.checkbox);
		final TextView valuetxt = (TextView) view.findViewById(R.id.value);
		final EditText beforetxt = (EditText) view
				.findViewById(R.id.insert_point_before);
		final EditText aftertxt = (EditText) view
				.findViewById(R.id.insert_point_after);

		final ISensorObservable listener = new ISensorObservable() {
			@Override
			public void onValueChanged(SensorVo model) {
				valuetxt.setText(Float.toString(model.getValue()));
			}
		};
		Controller.getInstance(getApplicationContext()).getModel()
				.addListener(listener);
		check.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				beforetxt.setEnabled(isChecked);
				if (isChecked) {
					Controller.getInstance(getApplicationContext()).getModel()
							.removeListener(listener);
					valuetxt.setText("------");
				} else {
					Controller.getInstance(getApplicationContext()).getModel()
							.addListener(listener);
				}
			}
		});

		AlertDialog.Builder builder = new AlertDialog.Builder(
				PointsActivity.this)
				.setView(view)
				.setTitle(R.string.insert_point_dialog_title)
				.setPositiveButton(R.string.positive_btn,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								float before;
								if (beforetxt.isEnabled()) {
									before = Float.valueOf(beforetxt.getText()
											.toString());
								} else {
									before = Float.valueOf(valuetxt.getText()
											.toString());
								}
								float after = Float.valueOf(aftertxt.getText()
										.toString());
								mController.insertNewPoint(before, after);
							}
						})
				.setNegativeButton(R.string.negative_btn,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// do nothing
							}
						});
		builder.show();
	}

}
