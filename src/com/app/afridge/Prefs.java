package com.app.afridge;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Prefs extends PreferenceActivity {

	SharedPreferences prefs;
	Model model = new Model(this);

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.prefs);

		addPreferencesFromResource(R.xml.preferences);

		Button button = (Button) findViewById(R.id.add_button);
		button.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});

		Preference showHistory = (Preference) findPreference("PREF_SHOW_HISTORY");
		showHistory
				.setOnPreferenceClickListener(new OnPreferenceClickListener() {

					public boolean onPreferenceClick(Preference preference) {
						Intent novIntent = new Intent(Prefs.this, History.class);
						startActivity(novIntent);
						return true;
					}

				});

		Preference clearHistory = (Preference) findPreference("PREF_CLEAR_HISTORY");
		clearHistory
				.setOnPreferenceClickListener(new OnPreferenceClickListener() {

					public boolean onPreferenceClick(Preference preference) {
						model.clear_history();
						return true;
					}

				});

	}
}