package com.app.afridge;

import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class inside extends Activity {

	private static final int ADD_ITEM = 1;
	Model model;
	ArrayList<String> item = new ArrayList<String>();
	ArrayList<String> item_to_modify = new ArrayList<String>();
	DatabaseHelper myDb;
	int temp = 0;
	ImageAdapter ia;
	SharedPreferences prefs;
	private static final int MENU_CHECK = Menu.FIRST;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.inside);

		prefs = PreferenceManager
		.getDefaultSharedPreferences(getApplicationContext());

		int warning_days = Integer.parseInt(prefs
		.getString("PREF_WARNING", "3"));
		model = new Model(this, warning_days);
		
		ia = new ImageAdapter(this);

		GridView gridview = (GridView) findViewById(R.id.gridview);
		gridview.setAdapter(ia);

		gridview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				handleItem(position);
				temp = position;
			}
		});

		myDb = new DatabaseHelper(this);
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

	}

	@Override
	protected void onDestroy() {
		myDb.close();
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		myDb = new DatabaseHelper(this);
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
		super.onResume();
	}

	public void handleItem(int position) {
		Cursor c = myDb.getItem(Integer.toString(position));
		if (c.moveToFirst()) {
			if (c.getString(6).equalsIgnoreCase("true"))
				if_empty_dialog(position);
			else if (c.getString(6).equalsIgnoreCase("false")) {
				not_empty_dialog(position, c);
			}
		}

	}

	public void not_empty_dialog(int position, final Cursor c) {

		final Dialog dialog = new Dialog(this);

		dialog.setContentView(R.layout.show_item);
		dialog.setTitle("aFridge");

		TextView text = (TextView) dialog.findViewById(R.id.text);
		text.setText("Item description:");
		ImageView image = (ImageView) dialog.findViewById(R.id.image);
		image.setImageResource(ia.getResourcePic(position));

		TextView name = (TextView) dialog.findViewById(R.id.text_name);
		if(!c.getString(1).equalsIgnoreCase(""))name.setText("- " + c.getString(1));
		else name.setText("- name: none");
		
		TextView type = (TextView) dialog.findViewById(R.id.text_type);
		type.setText("- " + c.getString(2));
		
		TextView quant = (TextView) dialog.findViewById(R.id.text_quant);
		if(!c.getString(3).equalsIgnoreCase(""))quant.setText("- "+c.getString(3));
		else quant.setText("- quantity: unknown");
		TextView qtype = (TextView) dialog.findViewById(R.id.text_qtype);
		qtype.setText(c.getString(4));
		
		
		TextView details = (TextView) dialog.findViewById(R.id.text_details);
		if(!c.getString(5).equalsIgnoreCase(""))details.setText("- "+c.getString(5));
		else details.setText("- details: none");
		
		TextView exp = (TextView) dialog.findViewById(R.id.text_exp);
		if(!c.getString(7).equalsIgnoreCase(""))exp.setText("exp. date: "+c.getString(7));
		else exp.setText("exp. date: unknown or none");		
		
		Button change = (Button) dialog.findViewById(R.id.change);
		change.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				item_to_modify.clear();
				item_to_modify.add(c.getString(1));
				item_to_modify.add(c.getString(2));
				item_to_modify.add(c.getString(3));
				item_to_modify.add(c.getString(4));
				item_to_modify.add(c.getString(5));
				item_to_modify.add(c.getString(7));
				addItem(true);
				dialog.dismiss();
			}
		});

		Button delete = (Button) dialog.findViewById(R.id.delete);
		delete.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				item.clear();
				item.add(0, Integer.toString(temp));// id of the item...
				item.add(1, "");
				item.add(2, "empty");
				item.add(3, "");
				item.add(4, "");
				item.add(5, "");
				item.add(6, "true");
				item.add(7, "");

				boolean modify = myDb.modifyItem(item);
				ia.refresh();
				dialog.dismiss();
			}
		});

		dialog.show();

	}

	public void if_empty_dialog(int position) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Do you want to add an item?").setIcon(
				R.drawable.icon).setTitle(R.string.app_name).setCancelable(
				false).setPositiveButton("Yes",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						addItem(false);
					}
				});
		builder.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		AlertDialog alert = builder.create();
		alert.show();
	}

	public void addItem(boolean modify) {
		if(modify){
			//temp init = new temp(); //TODO 
			//init.init(item_to_modify);
			Intent intentAddItem = new Intent(this, temp.class);
			intentAddItem.putStringArrayListExtra("item_to_modify", item_to_modify);
			startActivityForResult(intentAddItem, ADD_ITEM);
			
		} else {
		Intent intentAddItem = new Intent(this, temp.class);
		startActivityForResult(intentAddItem, ADD_ITEM);
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == ADD_ITEM)
			if (resultCode == RESULT_OK) {
				Log.d("xxx", "Vlezeno e vo if-ot");
				item.clear();
				item.add(0, Integer.toString(temp));
				item.addAll(data.getStringArrayListExtra("Item"));

				boolean modify = myDb.modifyItem(item);
				ia.refresh();

				Toast.makeText(
						getApplicationContext(),
						item.get(0) + item.get(1) + item.get(2) + item.get(3)
								+ item.get(4), Toast.LENGTH_LONG).show();
			}
	}
	
	@Override
    public boolean onCreateOptionsMenu(final Menu pMenu) {
    pMenu.add(0, MENU_CHECK, Menu.NONE, "Check expiration dates!").setIcon(android.R.drawable.ic_menu_manage);
    return true;
    }
	
	@Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
            switch(item.getItemId()) {
                    case MENU_CHECK:
                    	model.check_exp_date(true);
                    	return true;
                    default:
                    	return true;
            }
	}
}
