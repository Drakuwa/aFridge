package com.app.afridge;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class main extends Activity {

	private static final int MENU_SETTINGS = Menu.FIRST;
	SharedPreferences prefs;
	Model model;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		prefs = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());

		int warning_days = Integer.parseInt(prefs
				.getString("PREF_WARNING", "3"));

		model = new Model(this, warning_days);
		model.first_run();

		boolean autocheck = prefs.getBoolean("PREF_EXP_DATE", false);
		if (autocheck)
			model.check_exp_date(false, false);

		ImageView button1 = (ImageView) findViewById(R.id.button1);
		button1.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent myIntent = new Intent(main.this, inside.class);
				startActivity(myIntent);
			}
		});

		ImageView button2 = (ImageView) findViewById(R.id.button2);
		button2.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent myIntent = new Intent(main.this, notes.class);
				startActivity(myIntent);
			}
		});

		ImageView button3 = (ImageView) findViewById(R.id.button3);
		button3.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent myIntent = new Intent(main.this, more.class);
				startActivity(myIntent);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(final Menu pMenu) {
		pMenu.add(0, MENU_SETTINGS, Menu.NONE, "Settings").setIcon(
				android.R.drawable.ic_menu_preferences);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case MENU_SETTINGS:
			Intent settingsActivity = new Intent(getBaseContext(), prefs.class);
			startActivity(settingsActivity);
			return true;
		default:
			return true;
		}
	}
}
