package com.lightsensor;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.luxsensor.R;
import com.lightsensor.controller.Controller;
import com.lightsensor.daos.CalibrationDao;
import com.lightsensor.model.CalibrationVo;

public class ListActivity extends Activity {

	private static final String TAG = "KM";

	private Controller mController;
	private Button mInsert, mDelete, mEdit;
	private ListView mList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.list_layout);

		mController = Controller.getInstance(getApplicationContext());
		
		mInsert = (Button) findViewById(R.id.insert_btn);
		mInsert.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new AlertDialog.Builder(ListActivity.this)
						.setView(
								getLayoutInflater().inflate(
										R.layout.insert_dialog_layout, null))
						.setTitle(R.string.insert_dialog_title)
						.setPositiveButton(R.string.positive_btn,
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										insertNewCalibration((AlertDialog) dialog);
									}
								})
						.setNegativeButton(R.string.negative_btn,
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										//do nothing
									}
								})
						.show();
			}
		});
		mDelete = (Button) findViewById(R.id.delete_btn);
		mEdit = (Button) findViewById(R.id.edit_btn);
		mEdit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		});

		mList = (ListView) findViewById(R.id.list_view);
		mList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		mList.setAdapter(new ListAdapter(getApplicationContext(), mList,
				mController.getItems()));


	}

	private void insertNewCalibration(AlertDialog dialog) {
		EditText txt = (EditText) dialog.findViewById(R.id.insert_edit_txt);
		mController.insertNew(txt.getText().toString());
	}

}
