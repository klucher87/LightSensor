package com.lightsensor;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.luxsensor.R;
import com.lightsensor.controller.Controller;
import com.lightsensor.dao.DatabaseHelper;
import com.lightsensor.dao.PhoneDao;
import com.lightsensor.model.PhoneVo;

public class ListActivity extends Activity {

	private static final String TAG = "KM";

	private Controller mController = Controller.getInstance();
	private Button mInsert, mDelete, mEdit, mRefresh;
	private ListView mList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.list_layout);
		DatabaseHelper db = new DatabaseHelper(getApplicationContext());

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

		mRefresh = (Button) findViewById(R.id.refresh_btn);
		mList = (ListView) findViewById(R.id.list_view);
		mList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		mList.setAdapter(new ListAdapter(getApplicationContext(), mList,
				mController.getItems()));

		mRefresh.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				fetchFromDB();
				mList.invalidateViews();

			}
		});

		fetchFromDB();

	}

	public void fetchFromDB() {
		PhoneDao dao = new PhoneDao(getApplicationContext());
		ArrayList<PhoneVo> counters = dao.getAll();

		ArrayList<PhoneVo> model = mController.getItems();
		while (model.size() > 0) {
			model.remove(0);
		}
		for (PhoneVo counter : counters) {
			model.add(counter);
		}
	}

	private void insertNewCalibration(AlertDialog dialog) {
		EditText txt = (EditText) dialog.findViewById(R.id.insert_edit_txt);
		PhoneDao dao = new PhoneDao(getApplicationContext());
		// Na razie rozpatruje tylko 1 model
		final PhoneVo model = new PhoneVo();
		model.setLabel(txt.getText().toString());
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
		//TODO dodac model do struktury ??
	}

}
