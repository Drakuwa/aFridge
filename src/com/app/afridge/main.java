package com.app.afridge;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class main extends Activity{

	Model model = new Model(this);
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		model.first_run();
		model.check_exp_date(false);
		
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
}
