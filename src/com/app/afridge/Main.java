package com.app.afridge;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;

public class Main extends Activity {

	private static final int MENU_SETTINGS = Menu.FIRST;
	private static final int MENU_SHARE = MENU_SETTINGS + 1;
	private String facebookId = "";
	private String lastTimestamp = "";
	private DatabaseHelper myDb;
	private boolean eraseFirst = false;
	SharedPreferences prefs;
	Model model;

	Facebook facebook = new Facebook("368710909825016");

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		init_db();
		prefs = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());

		int warning_days = Integer.parseInt(prefs
				.getString("PREF_WARNING", "3"));

		facebookId = prefs.getString("facebook_id", "");
		Log.d("xxx", "facebook ID: " + facebookId);

		lastTimestamp = prefs.getString("last_timestamp", "");
		Log.d("xxx", "last Timestamp: " + lastTimestamp);

		model = new Model(this, warning_days);
		model.first_run();

		boolean autocheck = prefs.getBoolean("PREF_EXP_DATE", false);
		if (autocheck)
			model.check_exp_date(false, false);

		ImageView button1 = (ImageView) findViewById(R.id.button1);
		button1.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent myIntent = new Intent(Main.this, Inside.class);
				startActivity(myIntent);
			}
		});

		ImageView button2 = (ImageView) findViewById(R.id.button2);
		button2.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent myIntent = new Intent(Main.this, Notes.class);
				startActivity(myIntent);
			}
		});

		ImageView button3 = (ImageView) findViewById(R.id.button3);
		button3.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent myIntent = new Intent(Main.this, More.class);
				startActivity(myIntent);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(final Menu pMenu) {
		pMenu.add(0, MENU_SETTINGS, Menu.NONE, "Settings").setIcon(
				android.R.drawable.ic_menu_preferences);
		pMenu.add(0, MENU_SHARE, Menu.NONE, "Share").setIcon(
				android.R.drawable.ic_menu_share);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case MENU_SETTINGS:
			Intent settingsActivity = new Intent(getBaseContext(), Prefs.class);
			startActivity(settingsActivity);
			return true;
		case MENU_SHARE:
			shareApp();
			return true;
		default:
			return true;
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		facebook.authorizeCallback(requestCode, resultCode, data);
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (HaveNetworkConnection())
			facebook.extendAccessTokenIfNeeded(this, null);
	}

	public void shareApp() {

		/*
		 * Get existing access_token if any
		 */

		String access_token = prefs.getString("access_token", null);
		long expires = prefs.getLong("access_expires", 0);

		if (access_token != null) {
			facebook.setAccessToken(access_token);
		}
		if (expires != 0) {
			facebook.setAccessExpires(expires);
		}

		final Dialog dialog = new Dialog(this);

		dialog.setContentView(R.layout.share);
		dialog.setTitle("aFridge");

		Button connect = (Button) dialog.findViewById(R.id.connectbtn);
		Button logout = (Button) dialog.findViewById(R.id.logoutbtn);
		Button share = (Button) dialog.findViewById(R.id.sharebtn);
		Button getnotes = (Button) dialog.findViewById(R.id.getnotesbtn);

		connect.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				if (HaveNetworkConnection()) {
					init_fb();
					dialog.dismiss();
				} else {
					createInternetDisabledAlert();
				}
			}
		});

		logout.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				if (facebook.isSessionValid()) {
					try {
						facebook.logout(Main.this);
						facebookId = "";
						lastTimestamp = "";
						SharedPreferences.Editor editor2 = prefs.edit();
						editor2.putString("access_token", null);
						editor2.putLong("expires", 0);
						editor2.putString("facebook_id", facebookId);
						editor2.putString("last_timestamp", "");
						editor2.commit();
					} catch (MalformedURLException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
					dialog.dismiss();
				} else {
					Toast.makeText(getApplicationContext(),
							"You are not connected to Facebook!",
							Toast.LENGTH_SHORT).show();
					dialog.dismiss();
				}
			}
		});

		share.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				if (facebook.isSessionValid()) {
					// post on user's wall.
					facebook.dialog(Main.this, "feed", new DialogListener() {

						public void onFacebookError(FacebookError e) {
							Toast.makeText(getApplicationContext(),
									e.getMessage().toString(),
									Toast.LENGTH_SHORT).show();
							dialog.dismiss();
						}

						public void onError(DialogError e) {
							Toast.makeText(getApplicationContext(),
									e.getMessage().toString(),
									Toast.LENGTH_SHORT).show();
							dialog.dismiss();
						}

						public void onComplete(Bundle values) {
							dialog.dismiss();
						}

						public void onCancel() {
							dialog.dismiss();
						}
					});
				} else {
					Toast.makeText(getApplicationContext(),
							"You have to connect to Facebook first!",
							Toast.LENGTH_SHORT).show();
					dialog.dismiss();
				}
			}
		});

		getnotes.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				if (facebookId.length() > 0) {
					new GetNotes().execute();
					dialog.dismiss();
				} else {
					Toast.makeText(
							getApplicationContext(),
							"You have to connect to the Facebook application first",
							Toast.LENGTH_SHORT).show();
					dialog.dismiss();
				}
			}
		});

		dialog.show();

	}

	public boolean HaveNetworkConnection() {
		boolean HaveConnectedWifi = false;
		boolean HaveConnectedMobile = false;

		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo[] netInfo = cm.getAllNetworkInfo();
		for (NetworkInfo ni : netInfo) {
			if (ni.getTypeName().equalsIgnoreCase("WIFI"))
				if (ni.isConnected())
					HaveConnectedWifi = true;
			if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
				if (ni.isConnected())
					HaveConnectedMobile = true;
		}
		return HaveConnectedWifi || HaveConnectedMobile;
	}

	/**
	 * Create an alert dialog that redirects you to the internet options on the
	 * phone, so you can enable an internet connection
	 */
	public void createInternetDisabledAlert() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(
				"Your internet connection is disabled! Please enable WiFi, or mobile internet")
				.setIcon(R.drawable.icon)
				.setTitle(R.string.app_name)
				.setCancelable(false)
				.setPositiveButton("Internet options",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								showNetOptions();
							}
						});
		AlertDialog alert = builder.create();
		alert.show();
	}

	/**
	 * Start the wireless settings activity
	 */
	public void showNetOptions() {
		Intent netOptionsIntent = new Intent(
				android.provider.Settings.ACTION_WIRELESS_SETTINGS);
		this.startActivity(netOptionsIntent);
	}

	public void init_fb() {
		if (!facebook.isSessionValid()) {
			facebook.authorize(Main.this, new String[] {},
					new DialogListener() {

						public void onComplete(Bundle values) {
							SharedPreferences.Editor editor = prefs.edit();
							editor.putString("access_token",
									facebook.getAccessToken());
							editor.putLong("access_expires",
									facebook.getAccessExpires());
							editor.commit();

							try {
								String response = facebook.request("me");
								JSONObject jArray = null;
								jArray = new JSONObject(response);
								String id = jArray.get("id").toString();

								SharedPreferences.Editor editor2 = prefs.edit();
								editor2.putString("facebook_id", id);
								editor2.commit();

								Log.d("xxx",
										"Na kraj od se izvadeno samo id-to: "
												+ id);
								facebookId = id;
							} catch (MalformedURLException e) {
								e.printStackTrace();
							} catch (IOException e) {
								e.printStackTrace();
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}

						public void onFacebookError(FacebookError error) {
						}

						public void onError(DialogError e) {
						}

						public void onCancel() {
						}
					});
		} else {
			if (facebookId.length() > 0) {
				Toast.makeText(getApplicationContext(),
						"You're already connected to Facebook",
						Toast.LENGTH_SHORT).show();
			} else {
				try {
					String response = facebook.request("me");
					JSONObject jArray = null;
					jArray = new JSONObject(response);
					String id = jArray.get("id").toString();

					SharedPreferences.Editor editor2 = prefs.edit();
					editor2.putString("facebook_id", id);
					editor2.commit();

					Log.d("xxx", "Na kraj od se izvadeno samo id-to: " + id);
					facebookId = id;
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public class GetNotes extends AsyncTask<String, Void, String> {
		ProgressDialog dialog;

		/**
		 * On PreExecute, initialize and show the progress dialog.
		 */
		@Override
		protected void onPreExecute() {
			dialog = new ProgressDialog(Main.this);
			dialog.setTitle("Downloading");
			dialog.setMessage("Please wait...");
			dialog.setIndeterminate(true);
			dialog.show();
		}

		/**
		 * what to do in background while showing the progress dialog.
		 */
		protected String doInBackground(String... vlezni) {

			/**
			 * Get the input parameters.
			 */
			JSONObject jArray = null;

			// initialize
			InputStream is = null;
			String result = "";

			try {
				HttpGet httpget;
				HttpClient httpclient = new DefaultHttpClient();
				if (lastTimestamp.length() > 0) {
					httpget = new HttpGet(
							"http://drakuwa.iriscouch.com/test/_design/android-fridge/_view/notes?key=%22"
									+ facebookId + "%22&startkey_docid="
									+ lastTimestamp);
					eraseFirst = true;
				} else {
					httpget = new HttpGet(
							"http://drakuwa.iriscouch.com/test/_design/android-fridge/_view/notes?key=%22"
									+ facebookId + "%22&");
					eraseFirst = false;
				}
				httpget.setHeader("Content-Type", "application/json");
				httpget.setHeader("Accept", "application/json");
				HttpResponse response = httpclient.execute(httpget);
				HttpEntity entity = response.getEntity();
				is = entity.getContent();

			} catch (Exception e) {
				Log.e("xxx", "Error in http connection " + e.toString());
			}

			// convert response to string
			try {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(is, "iso-8859-1"), 8);
				StringBuilder sb = new StringBuilder();
				String line = null;
				while ((line = reader.readLine()) != null) {
					sb.append(line + "\n");
				}
				is.close();
				result = sb.toString();
			} catch (Exception e) {
				Log.d("xxx", "Error converting result " + e.toString());
			}

			Log.d("xxx", "rezultat: " + result);
			// try parse the string to a JSON object

			try {
				jArray = new JSONObject(result);
				JSONArray jsonArray = new JSONArray(jArray.get("rows")
						.toString());
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jsonObject = jsonArray.getJSONObject(i);

					Cursor c = myDb.getNotes();
					int next_id;
					if (c.getCount() == 0) {
						next_id = 0;
					} else {
						c.moveToLast();
						next_id = c.getInt(0) + 1;
					}

					lastTimestamp = jsonObject.getString("id");
					SharedPreferences.Editor editor2 = prefs.edit();
					editor2.putString("last_timestamp", lastTimestamp);
					editor2.commit();
					
					if (eraseFirst && i == 0) {
						continue;
					} else {
						boolean addnote = myDb.addNote(
								jsonObject.getString("value"),
								Integer.toString(next_id));
					}

					Log.d("xxx", jsonObject.getString("value"));
				}
			} catch (JSONException e) {
				Log.d("xxx", "Error parsing data " + e.toString());
			}

			return "";
		}

		/**
		 * What to do after the calculations are finished.
		 */
		public void onPostExecute(String result) {
			// Remove the progress dialog.
			dialog.dismiss();

		}
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
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		myDb.close();
	}
}
