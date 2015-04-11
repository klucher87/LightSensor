package com.lightsensor;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.lightsensor.controller.Controller;

public class CalibrationsActivity extends Activity {

	private Controller mController;
	private Button mInsert, mDelete, mEdit;
	private ListView mList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.calibrations_layout);

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
				showDeleteDialog();
			}
		});
		mEdit = (Button) findViewById(R.id.edit_btn);
		mEdit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showPointsActivity();
			}
		});

		mList = (ListView) findViewById(R.id.list_view);
		mList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		mList.setAdapter(new CalibrationListAdapter(getApplicationContext(), mList,
				mController.getCalibrations()));

	}

	private void showInsertDialog() {
		new AlertDialog.Builder(CalibrationsActivity.this)
				.setView(
						getLayoutInflater().inflate(
								R.layout.calibration_insert_dialog_layout, null))
				.setTitle(R.string.insert_dialog_title)
				.setPositiveButton(R.string.positive_btn,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								EditText txt = (EditText) ((AlertDialog) dialog)
										.findViewById(R.id.insert_edit_txt);
								mController.insertNewCalibration(txt.getText()
										.toString());
							}
						})
				.setNegativeButton(R.string.negative_btn,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// do nothing
							}
						}).show();
	}

	private void showDeleteDialog() {
		if (mController.getSelectedCalibration() != null) {
			new AlertDialog.Builder(CalibrationsActivity.this)
					.setTitle(R.string.delete_dialog_text)
					.setPositiveButton(R.string.positive_btn,
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									mController.deleteSelectedCalibration();
								}
							})
					.setNegativeButton(R.string.negative_btn,
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// do nothing
								}
							}).show();
		} else {
			Toast.makeText(this,
					getResources().getString(R.string.no_selected_item_info),
					Toast.LENGTH_SHORT).show();
		}

	}

	private void showPointsActivity() {
		if (mController.getSelectedCalibration() != null) {
			startActivity(new Intent(this, PointsActivity.class));
		} else {
			Toast.makeText(this,
					getResources().getString(R.string.no_selected_item_info),
					Toast.LENGTH_SHORT).show();
		}

	}

}
