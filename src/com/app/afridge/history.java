package com.app.afridge;

import java.io.IOException;
import java.util.ArrayList;

import android.app.ListActivity;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.widget.ArrayAdapter;

public class history extends ListActivity {

	ArrayList<String> tempArray = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		tempArray.clear();
		DatabaseHelper myDb = new DatabaseHelper(this);
		{
			try {
				myDb.createDataBase();
			} catch (IOException ioe) {
				throw new Error("Unable to create database");
			}
			try {
				myDb.openDataBase();
			} catch (SQLException sqle) {
				throw sqle;
			}
			myDb.getReadableDatabase();
		}
		Cursor c = myDb.getHistory();

		if (c.moveToFirst()) {
			do {
				tempArray.add(c.getString(1) + " " + c.getString(9) + " "
						+ c.getString(2) + " " + c.getString(3) + " "
						+ c.getString(4) + " " + c.getString(5) + " "
						+ c.getString(6) + " " + c.getString(7) + " "
						+ c.getString(8) + ";");
			} while (c.moveToNext());
		}
		myDb.close();

		setListAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, tempArray));
	}
}
