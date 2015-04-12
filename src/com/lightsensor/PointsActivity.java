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

import com.lightsensor.controller.Controller;

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
		mList.setAdapter(new PointListAdapter(getApplicationContext(), mList, mController.getPoints()));

	}

	private void showInsertDialog() {
		new AlertDialog.Builder(PointsActivity.this)
				.setView(
						getLayoutInflater().inflate(
								R.layout.point_insert_dialog_layout, null))
				.setTitle(R.string.insert_dialog_title)
				.setPositiveButton(R.string.positive_btn,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								EditText beforetxt = (EditText) ((AlertDialog) dialog).findViewById(R.id.insert_point_before);
								EditText aftertxt = (EditText) ((AlertDialog) dialog).findViewById(R.id.insert_point_after);
								
								float before = Float.valueOf(beforetxt.getText().toString());
								float after = Float.valueOf(aftertxt.getText().toString());

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
						}).show();
	}
	
}
