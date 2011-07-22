package com.app.afridge;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Database helper class which overrides and extends some of the
 * SQLiteOpenHelper class functionalities. This model class contains SQL queries
 * written in class methods which are called where needed.
 * 
 * @author drakuwa
 * 
 */
public class DatabaseHelper extends SQLiteOpenHelper {

	// The Android's default system path of your application database.
	private static String DB_PATH = "/data/data/com.app.afridge/databases/";

	// The database name
	private static String DB_NAME = "items.sqlite";

	/**
	 * Set initial variables that will be used in the queries.
	 */
	public static final String KEY_ROWID = "_id";// 0
	public static final String KEY_NAME = "name";// 1
	public static final String KEY_TYPE = "type";// 2
	public static final String KEY_QUANT = "quant";// 3
	public static final String KEY_QTYPE = "qtype";// 4
	public static final String KEY_DETAILS = "details";// 5
	public static final String KEY_ISEMPTY = "isEmpty";// 6
	public static final String KEY_EXPDATE = "exp_date";// 7

	public static final String KEY_NOTE = "note";// 1

	public static final String KEY_TIMESTAMP = "timestamp";// 1
	public static final String KEY_ITEM_ID = "item_id";// 2
	public static final String KEY_CHANGE = "change";// 9

	private static final String DATABASE_TABLE = "fridge";
	private static final String DATABASE_NOTES_TABLE = "notes";
	private static final String DATABASE_HISTORY_TABLE = "history";

	private SQLiteDatabase db;

	private final Context myContext;

	/**
	 * Constructor Takes and keeps a reference of the passed context in order to
	 * access to the application assets and resources.
	 * 
	 * @param context
	 */
	public DatabaseHelper(Context context) {
		super(context, DB_NAME, null, 4);
		this.myContext = context;
	}

	/**
	 * Creates a empty database on the system and rewrites it with your own
	 * database.
	 * */
	public void createDataBase() throws IOException {

		boolean dbExist = checkDataBase();

		if (dbExist) {
			// do nothing - database already exist
		} else {

			// By calling this method and empty database will be created into
			// the default system path
			// of your application so we are going to be able to overwrite that
			// database with our database.
			this.getReadableDatabase();

			try {

				copyDataBase();

			} catch (IOException e) {

				throw new Error("Error copying database");

			}
		}

	}

	/**
	 * Method that checks the existence of a local database in the given path
	 * and returns a boolean value.
	 * 
	 * @return
	 */
	private boolean checkDataBase() {

		SQLiteDatabase checkDB = null;

		try {
			String myPath = DB_PATH + DB_NAME;
			checkDB = SQLiteDatabase.openDatabase(myPath, null,
					SQLiteDatabase.OPEN_READONLY);

		} catch (SQLiteException e) {

		}

		if (checkDB != null) {

			checkDB.close();

		}

		return checkDB != null ? true : false;
	}

	/**
	 * Method that copies the database from the "assets" folder into the created
	 * empty database in the default system path.
	 * 
	 * @throws IOException
	 */
	private void copyDataBase() throws IOException {

		InputStream myInput = myContext.getAssets().open(DB_NAME);

		String outFileName = DB_PATH + DB_NAME;

		OutputStream myOutput = new FileOutputStream(outFileName);

		byte[] buffer = new byte[1024];
		int length;
		while ((length = myInput.read(buffer)) > 0) {
			myOutput.write(buffer, 0, length);
		}

		myOutput.flush();
		myOutput.close();
		myInput.close();

	}

	/**
	 * This method opens the database for read/write.
	 * 
	 * @throws SQLException
	 */
	public void openDataBase() throws SQLException {

		String myPath = DB_PATH + DB_NAME;
		db = SQLiteDatabase.openDatabase(myPath, null,
				SQLiteDatabase.CREATE_IF_NECESSARY); // namesto CREATE_IF_...
		// beshe OPEN_READWRITE

	}

	/**
	 * Method that closes the database connection.
	 */
	@Override
	public synchronized void close() {

		if (db != null)
			db.close();

		super.close();

	}

	@Override
	public void onCreate(SQLiteDatabase db) {

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		db.execSQL("DROP TABLE IF EXISTS notes");
		db.execSQL("DROP TABLE IF EXISTS fridge");
		onCreate(db);

	}

	/**
	 * SQL query function that returns a cursor showing all the items from the
	 * database.
	 * 
	 * @return
	 */
	public Cursor getItems() {
		return db.query(DATABASE_TABLE, new String[] { KEY_ROWID, KEY_NAME,
				KEY_TYPE, KEY_QUANT, KEY_QTYPE, KEY_DETAILS, KEY_ISEMPTY,
				KEY_EXPDATE }, null, null, null, null, null);
	}

	/**
	 * SQL query function that returns a cursor showing the item with the given
	 * name ID as the parameter "id".
	 * 
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	public Cursor getItem(String id) throws SQLException {
		Cursor mCursor = db.query(true, DATABASE_TABLE, new String[] {
				KEY_ROWID, KEY_NAME, KEY_TYPE, KEY_QUANT, KEY_QTYPE,
				KEY_DETAILS, KEY_ISEMPTY, KEY_EXPDATE }, KEY_ROWID + " LIKE "
				+ "'" + id + "'", null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	/**
	 * SQL query function that modifies the value of the given row with values
	 * passed as parameter of the function - row
	 * 
	 * @param row
	 * @return
	 */
	public boolean modifyItem(ArrayList<String> row) {
		ContentValues updateValues = new ContentValues();
		updateValues.put(KEY_NAME, row.get(1));
		updateValues.put(KEY_TYPE, row.get(2));
		updateValues.put(KEY_QUANT, row.get(3));
		updateValues.put(KEY_QTYPE, row.get(4));
		updateValues.put(KEY_DETAILS, row.get(5));
		updateValues.put(KEY_ISEMPTY, row.get(6));
		updateValues.put(KEY_EXPDATE, row.get(7));

		return db.update(DATABASE_TABLE, updateValues, KEY_ROWID + "="
				+ row.get(0), null) > 0;
	}

	/**
	 * SQL query function that returns a cursor showing all the notes from the
	 * database.
	 * 
	 * @return
	 */
	public Cursor getNotes() {
		return db.query(DATABASE_NOTES_TABLE, new String[] { KEY_ROWID,
				KEY_NOTE }, null, null, null, null, null);
	}

	/**
	 * SQL query function that adds a note to the notes table
	 * 
	 * @param note
	 * @return
	 */
	public boolean addNote(String note, String id) {
		ContentValues values = new ContentValues();
		values.put(KEY_ROWID, id);
		values.put(KEY_NOTE, note);
		return db.insert(DATABASE_NOTES_TABLE, null, values) > 0;
	}

	/**
	 * SQL query function that deletes the note at the given id parameter
	 * 
	 * @param id
	 * @return
	 */
	public boolean deleteNote(String id) {
		return db.delete(DATABASE_NOTES_TABLE, "_id=" + id, null) > 0;
	}

	public boolean deleteAllNotes() {
		return db.delete(DATABASE_NOTES_TABLE, "_id like '%'", null) > 0;
	}

	/**
	 * SQL query function that returns a cursor showing all the history notes
	 * from the database.
	 * 
	 * @return
	 */
	public Cursor getHistory() {
		return db.query(DATABASE_HISTORY_TABLE, new String[] { KEY_ROWID,
				KEY_TIMESTAMP, KEY_ITEM_ID, KEY_NAME, KEY_TYPE, KEY_QUANT,
				KEY_QTYPE, KEY_DETAILS, KEY_EXPDATE, KEY_CHANGE }, null, null,
				null, null, null);
	}

	/**
	 * SQL query function that adds a history note to the history table
	 * 
	 * @param note
	 * @return
	 */
	public boolean addToHistory(String id, ArrayList<String> hist) {
		ContentValues values = new ContentValues();
		values.put(KEY_ROWID, id);
		values.put(KEY_TIMESTAMP, hist.get(0));
		values.put(KEY_ITEM_ID, hist.get(1));
		values.put(KEY_NAME, hist.get(2));
		values.put(KEY_TYPE, hist.get(3));
		values.put(KEY_QUANT, hist.get(4));
		values.put(KEY_QTYPE, hist.get(5));
		values.put(KEY_DETAILS, hist.get(6));
		values.put(KEY_EXPDATE, hist.get(7));
		values.put(KEY_CHANGE, hist.get(8));

		return db.insert(DATABASE_HISTORY_TABLE, null, values) > 0;
	}

	public boolean clearHistory() {
		return db.delete(DATABASE_HISTORY_TABLE, "_id like '%'", null) > 0;
	}

}
