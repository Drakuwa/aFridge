package com.app.afridge;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

/**
 * First activity shown in the application, and a splash screen. This class
 * shows a splash screen and dismisses it after a while.
 * 
 * @author drakuwa
 */
public class Splash extends Activity {

	protected boolean _active = true;
	protected int _splashTime = 2000;

	/**
	 * Override of the onCreate method that calls a new runnable after a while.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);

		/**
		 * Starts a new activity given with the novIntent variable.
		 */
		new Handler().postDelayed(new Runnable() {
			public void run() {
				Intent novIntent = new Intent(Splash.this, Main.class);
				startActivity(novIntent);
				finish();

			}
		}, 2000);
	}

}