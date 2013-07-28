package com.app.afridge;

import java.io.IOException;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
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
			imageView.setLayoutParams(new GridView.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
			imageView.setPadding(6, 6, 6, 6);
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
					String state = c.getString(6); //true/false/warning/expired

					if (type.equalsIgnoreCase("empty"))
						pics[i] = R.drawable.empty_glass;
					else if (type.equalsIgnoreCase("Milk")){
						if(state.equalsIgnoreCase("false"))
						pics[i] = R.drawable.item_milk;
						else if(state.equalsIgnoreCase("warning"))
						pics[i] = R.drawable.item_milk_warn;
						else if(state.equalsIgnoreCase("expired"))
						pics[i] = R.drawable.item_milk_exp;
					}
						
					else if (type.equalsIgnoreCase("Cheese")){
						if(state.equalsIgnoreCase("false"))
							pics[i] = R.drawable.item_cheese;
						else if(state.equalsIgnoreCase("warning"))
							pics[i] = R.drawable.item_cheese_warn;
						else if(state.equalsIgnoreCase("expired"))
							pics[i] = R.drawable.item_cheese_exp;
					}
					else if (type.equalsIgnoreCase("Eggs")){
						if(state.equalsIgnoreCase("false"))
							pics[i] = R.drawable.item_eggs;
						else if(state.equalsIgnoreCase("warning"))
							pics[i] = R.drawable.item_eggs_warn;
						else if(state.equalsIgnoreCase("expired"))
							pics[i] = R.drawable.item_eggs_exp;
					}
					else if (type.equalsIgnoreCase("Butter"))
					{
						if(state.equalsIgnoreCase("false"))
							pics[i] = R.drawable.item_butter;
						else if(state.equalsIgnoreCase("warning"))
							pics[i] = R.drawable.item_butter_warn;
						else if(state.equalsIgnoreCase("expired"))
							pics[i] = R.drawable.item_butter_exp;
					}
					else if (type.equalsIgnoreCase("Ham"))
					{
						if(state.equalsIgnoreCase("false"))
							pics[i] = R.drawable.item_ham;
						else if(state.equalsIgnoreCase("warning"))
							pics[i] = R.drawable.item_ham_warn;
						else if(state.equalsIgnoreCase("expired"))
							pics[i] = R.drawable.item_ham_exp;
					}
					else if (type.equalsIgnoreCase("Sausage"))
					{
						if(state.equalsIgnoreCase("false"))
							pics[i] = R.drawable.item_sausage;
						else if(state.equalsIgnoreCase("warning"))
							pics[i] = R.drawable.item_sausage_warn;
						else if(state.equalsIgnoreCase("expired"))
							pics[i] = R.drawable.item_sausage_exp;
					}
					else if (type.equalsIgnoreCase("Meat"))
					{
						if(state.equalsIgnoreCase("false"))
							pics[i] = R.drawable.item_meat;
						else if(state.equalsIgnoreCase("warning"))
							pics[i] = R.drawable.item_meat_warn;
						else if(state.equalsIgnoreCase("expired"))
							pics[i] = R.drawable.item_meat_exp;
					}
					else if (type.equalsIgnoreCase("Mayonnaise"))
					{
						if(state.equalsIgnoreCase("false"))
							pics[i] = R.drawable.item_mayo;
						else if(state.equalsIgnoreCase("warning"))
							pics[i] = R.drawable.item_mayo_warn;
						else if(state.equalsIgnoreCase("expired"))
							pics[i] = R.drawable.item_mayo_exp;
					}
					else if (type.equalsIgnoreCase("Ketchup"))
					{
						if(state.equalsIgnoreCase("false"))
							pics[i] = R.drawable.item_ketchup;
						else if(state.equalsIgnoreCase("warning"))
							pics[i] = R.drawable.item_ketchup_warn;
						else if(state.equalsIgnoreCase("expired"))
							pics[i] = R.drawable.item_ketchup_exp;
					}
					else if (type.equalsIgnoreCase("Mustard"))
					{
						if(state.equalsIgnoreCase("false"))
							pics[i] = R.drawable.item_mustard;
						else if(state.equalsIgnoreCase("warning"))
							pics[i] = R.drawable.item_mustard_warn;
						else if(state.equalsIgnoreCase("expired"))
							pics[i] = R.drawable.item_mustard_exp;
					}
					else if (type.equalsIgnoreCase("Leftovers"))
					{
						if(state.equalsIgnoreCase("false"))
							pics[i] = R.drawable.item_leftover;
						else if(state.equalsIgnoreCase("warning"))
							pics[i] = R.drawable.item_leftover_warn;
						else if(state.equalsIgnoreCase("expired"))
							pics[i] = R.drawable.item_leftover_exp;
					}
					else if (type.equalsIgnoreCase("Fruit"))
					{
						if(state.equalsIgnoreCase("false"))
							pics[i] = R.drawable.item_fruit;
						else if(state.equalsIgnoreCase("warning"))
							pics[i] = R.drawable.item_fruit_warn;
						else if(state.equalsIgnoreCase("expired"))
							pics[i] = R.drawable.item_fruit_exp;
					}
					else if (type.equalsIgnoreCase("Vegetables"))
					{
						if(state.equalsIgnoreCase("false"))
							pics[i] = R.drawable.item_veggie;
						else if(state.equalsIgnoreCase("warning"))
							pics[i] = R.drawable.item_veggie_warn;
						else if(state.equalsIgnoreCase("expired"))
							pics[i] = R.drawable.item_veggie_exp;
					}
					else if (type.equalsIgnoreCase("Pickles"))
					{
						if(state.equalsIgnoreCase("false"))
							pics[i] = R.drawable.item_pickles;
						else if(state.equalsIgnoreCase("warning"))
							pics[i] = R.drawable.item_pickles_warn;
						else if(state.equalsIgnoreCase("expired"))
							pics[i] = R.drawable.item_pickles_exp;
					}
					else if (type.equalsIgnoreCase("Cake"))
					{
						if(state.equalsIgnoreCase("false"))
							pics[i] = R.drawable.item_cake;
						else if(state.equalsIgnoreCase("warning"))
							pics[i] = R.drawable.item_cake_warn;
						else if(state.equalsIgnoreCase("expired"))
							pics[i] = R.drawable.item_cake_exp;
					}
					else if (type.equalsIgnoreCase("Cream"))
					{
						if(state.equalsIgnoreCase("false"))
							pics[i] = R.drawable.item_cream;
						else if(state.equalsIgnoreCase("warning"))
							pics[i] = R.drawable.item_cream_warn;
						else if(state.equalsIgnoreCase("expired"))
							pics[i] = R.drawable.item_cream_exp;
					}
					else if (type.equalsIgnoreCase("Jam"))
					{
						if(state.equalsIgnoreCase("false"))
							pics[i] = R.drawable.item_jam;
						else if(state.equalsIgnoreCase("warning"))
							pics[i] = R.drawable.item_jam_warn;
						else if(state.equalsIgnoreCase("expired"))
							pics[i] = R.drawable.item_jam_exp;
					}
					else if (type.equalsIgnoreCase("Ajvar"))
					{
						if(state.equalsIgnoreCase("false"))
							pics[i] = R.drawable.item_ajvar;
						else if(state.equalsIgnoreCase("warning"))
							pics[i] = R.drawable.item_ajvar_warn;
						else if(state.equalsIgnoreCase("expired"))
							pics[i] = R.drawable.item_ajvar_exp;
					}
					else if (type.equalsIgnoreCase("Drinks"))
					{
						if(state.equalsIgnoreCase("false"))
							pics[i] = R.drawable.item_drinks;
						else if(state.equalsIgnoreCase("warning"))
							pics[i] = R.drawable.item_drinks_warn;
						else if(state.equalsIgnoreCase("expired"))
							pics[i] = R.drawable.item_drinks_exp;
					}
					else if (type.equalsIgnoreCase("Other"))
					{
						if(state.equalsIgnoreCase("false"))
							pics[i] = R.drawable.item_other;
						else if(state.equalsIgnoreCase("warning"))
							pics[i] = R.drawable.item_other_warn;
						else if(state.equalsIgnoreCase("expired"))
							pics[i] = R.drawable.item_other_exp;
					}
					else if (type.equalsIgnoreCase("Fish"))
					{
						if(state.equalsIgnoreCase("false"))
							pics[i] = R.drawable.item_fish;
						else if(state.equalsIgnoreCase("warning"))
							pics[i] = R.drawable.item_fish_warn;
						else if(state.equalsIgnoreCase("expired"))
							pics[i] = R.drawable.item_fish_exp;
					}
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
