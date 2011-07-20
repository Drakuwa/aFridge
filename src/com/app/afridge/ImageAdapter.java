package com.app.afridge;

import java.io.IOException;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter {

	private Context mContext;
	private DatabaseHelper myDb;

	public ImageAdapter(Context ctx) {
		mContext = ctx;
		init_pics();
	}

	public int getCount() {
		return pics.length;
	}

	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getResourcePic(int id) {
		return pics[id];
	}

	// create a new ImageView for each item referenced by the Adapter
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView imageView;
		if (convertView == null) { // if it's not recycled, initialize some
			// attributes
			imageView = new ImageView(mContext);
			imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
			imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
			imageView.setPadding(8, 8, 8, 8);
			notifyDataSetChanged();
		} else {
			imageView = (ImageView) convertView;
			notifyDataSetChanged();
		}

		imageView.setImageResource(pics[position]);
		notifyDataSetChanged();
		return imageView;
	}

	// references to our images

	private Integer[] pics = { R.drawable.empty_glass, R.drawable.empty_glass,
			R.drawable.empty_glass, R.drawable.empty_glass,
			R.drawable.empty_glass, R.drawable.empty_glass,
			R.drawable.empty_glass, R.drawable.empty_glass,
			R.drawable.empty_glass, R.drawable.empty_glass,
			R.drawable.empty_glass, R.drawable.empty_glass,
			R.drawable.empty_glass, R.drawable.empty_glass,
			R.drawable.empty_glass, R.drawable.empty_glass,
			R.drawable.empty_glass, R.drawable.empty_glass,
			R.drawable.empty_glass, R.drawable.empty_glass };

	public void init_pics() {
		int i = 0;
		myDb = new DatabaseHelper(mContext);
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
			Cursor c = myDb.getItems();
			if (c.moveToFirst()) {
				do {
					String type = c.getString(2);

					if (type.equalsIgnoreCase("empty"))
						pics[i] = R.drawable.empty_glass;
					else if (type.equalsIgnoreCase("Milk"))
						pics[i] = R.drawable.item_milk;
					else if (type.equalsIgnoreCase("Cheese"))
						pics[i] = R.drawable.item_cheese;
					else if (type.equalsIgnoreCase("Eggs"))
						pics[i] = R.drawable.item_eggs;
					else if (type.equalsIgnoreCase("Butter"))
						pics[i] = R.drawable.item_butter;
					else if (type.equalsIgnoreCase("Ham"))
						pics[i] = R.drawable.item_ham;
					else if (type.equalsIgnoreCase("Sausage"))
						pics[i] = R.drawable.item_sausage;
					else if (type.equalsIgnoreCase("Meat"))
						pics[i] = R.drawable.item_meat;
					else if (type.equalsIgnoreCase("Mayonnaise"))
						pics[i] = R.drawable.item_mayo;
					else if (type.equalsIgnoreCase("Ketchup"))
						pics[i] = R.drawable.item_ketchup;
					else if (type.equalsIgnoreCase("Mustard"))
						pics[i] = R.drawable.item_mustard;
					else if (type.equalsIgnoreCase("Leftovers"))
						pics[i] = R.drawable.item_leftover;
					else if (type.equalsIgnoreCase("Fruit"))
						pics[i] = R.drawable.item_fruit;
					else if (type.equalsIgnoreCase("Vegetables"))
						pics[i] = R.drawable.item_veggie;
					else if (type.equalsIgnoreCase("Pickles"))
						pics[i] = R.drawable.item_pickles;
					else if (type.equalsIgnoreCase("Cake"))
						pics[i] = R.drawable.item_cake;
					else if (type.equalsIgnoreCase("Cream"))
						pics[i] = R.drawable.item_cream;
					else if (type.equalsIgnoreCase("Jam"))
						pics[i] = R.drawable.item_jam;
					else if (type.equalsIgnoreCase("Ajvar"))
						pics[i] = R.drawable.item_ajvar;
					else if (type.equalsIgnoreCase("Drinks"))
						pics[i] = R.drawable.item_drinks;
					else if (type.equalsIgnoreCase("Other"))
						pics[i] = R.drawable.item_other;
					else if (type.equalsIgnoreCase("Fish"))
						pics[i] = R.drawable.item_fish;
					i++;
					Log.d("xxx", "" + pics[i - 1] + "i=" + (i - 1));
				} while (c.moveToNext());
			}
			myDb.close();
		}
		notifyDataSetChanged();
	}

	public void refresh() {
		init_pics();
		notifyDataSetChanged();
	}
}
