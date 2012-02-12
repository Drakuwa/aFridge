package com.app.afridge;

import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class Notes extends Activity {

	/** Called when the activity is first created. */
	private static final int MENU_CLEAR = Menu.FIRST;
	private ListView lv;
	private DatabaseHelper myDb;
	private SimpleCursorAdapter cadapter;
	private EditText et;
	private Cursor c;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notes);

		init_db();

		cadapter = new SimpleCursorAdapter(this, R.layout.notes_item, c,
				new String[] { "note" }, new int[] { R.id.notes_text_item });

		lv = (ListView) findViewById(R.id.listViewNotes);
		lv.setAdapter(cadapter);

		lv.setTextFilterEnabled(true);

		et = (EditText) findViewById(R.id.editTextNotes);

		Button add = (Button) findViewById(R.id.notes_button);
		add.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				String value = et.getText().toString();
				int next_id;
				if (c.getCount() == 0) {
					next_id = 0;
				} else {
					c.moveToLast();
					next_id = c.getInt(0) + 1;
				}

				boolean addnote = myDb
						.addNote(value, Integer.toString(next_id));
				init_db();
				cadapter.changeCursor(c);
				cadapter.notifyDataSetChanged();

				et.setText(null);
			}
		});

		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				createClickAlert(position, id);

				//Toast
				//		.makeText(getApplicationContext(),
				//				"id=" + id + " position=" + position,
				//				Toast.LENGTH_LONG).show();
			}
		});
	}

	@Override
	protected void onDestroy() {
		myDb.close();
		super.onDestroy();
	}

	private void createClickAlert(final int position, final long itemid) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(
				"Do you want to delete the selected note: "
						+ cadapter.getCursor().getString(1) + "?").setIcon(
				R.drawable.icon).setTitle(R.string.app_name).setCancelable(
				false).setPositiveButton("Yes",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						boolean delete = myDb.deleteNote(Long.toString(itemid));
						init_db();
						cadapter.changeCursor(c);
						cadapter.notifyDataSetChanged();
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

	public void init_db() {
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
			c = myDb.getNotes();
			if (c.moveToFirst()) {
				do {

				} while (c.moveToNext());
			}
		}
	}
	
	@Override
    public boolean onCreateOptionsMenu(final Menu pMenu) {
    pMenu.add(0, MENU_CLEAR, Menu.NONE, "Clear all notes!").setIcon(android.R.drawable.ic_menu_close_clear_cancel);
    return true;
    }
	
	@Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
            switch(item.getItemId()) {
                    case MENU_CLEAR:
                    	boolean clear = myDb.deleteAllNotes();
                    	init_db();
						cadapter.changeCursor(c);
						cadapter.notifyDataSetChanged();
                    	return true;
                    default:
                    	return true;
            }
	}

}
