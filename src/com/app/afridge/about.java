package com.app.afridge;

import android.app.Activity;
import android.os.Bundle;

/**
 * Simple activity class that shows the about layout
 * @author drakuwa
 *
 */
public class about extends Activity {

	/**
	 * On create of the activity, show the about layout
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
	}
}
