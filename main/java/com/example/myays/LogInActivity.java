package com.example.myays;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.example.myays.databases.MyDBConfiguration.RegisEntry;
import com.example.myays.databases.MyDBHelper;
import com.example.myays.dialogs.LogInConfirm;

public class LogInActivity extends ActionBarActivity {

	private static String TAG = "lch";
	

	private Button buttonLogIn;
	private Button buttonSignUP;
	private EditText usernameEditText;
	private EditText passwordEditText;
	private String usernameString;
	private String passwordString;
	private OnClickListener listener;
	private String infoString;
	private MyDBHelper myDBHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.frag_login);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		buttonLogIn = (Button) findViewById(R.id.button_log_in_in_log_in);
		buttonSignUP = (Button) findViewById(R.id.button_sign_up_in_log_in);
		usernameEditText = (EditText) findViewById(R.id.username_in_log_in);
		passwordEditText = (EditText) findViewById(R.id.password_in_log_in);

		myDBHelper = new MyDBHelper(this, RegisEntry.DB_NAME_STRING);

		listener = new OnClickListener() {

			@Override
			public void onClick(View v) {

				switch (v.getId()) {
				case R.id.button_log_in_in_log_in:
					logInToDB();
					break;
				case R.id.button_sign_up_in_log_in:
					regisInfoToDB();
					break;
				default:
					break;

				}
			}

			private void regisInfoToDB() {

				usernameString = usernameEditText.getText().toString();
				passwordString = passwordEditText.getText().toString();

				Log.i(TAG, "we get username:" + usernameString);
				Log.i(TAG, "we get password:" + passwordString);

				SQLiteDatabase mDatabase = myDBHelper.getWritableDatabase();
				ContentValues values = new ContentValues();
				values.put(RegisEntry.COLUMN_NAME_TITLE, usernameString);
				values.put(RegisEntry.COLUMN_NAME_CONTENT, passwordString);

				mDatabase.insert(RegisEntry.TABLE_NAME, null, values);

				LogInConfirm regisConfirm = new LogInConfirm();
				Bundle args = new Bundle();
				args.putString("info", "You have singed up successfully!!!");
				regisConfirm.setArguments(args);
				regisConfirm.show(getSupportFragmentManager(), "regisConfirm");


			}

			private void logInToDB() {

				usernameString = usernameEditText.getText().toString();
				passwordString = passwordEditText.getText().toString();

				SQLiteDatabase mDatabaseRead = myDBHelper.getReadableDatabase();

				String[] projection = { RegisEntry._ID,
						RegisEntry.COLUMN_NAME_TITLE,
						RegisEntry.COLUMN_NAME_CONTENT,

				};

				// query the database to check if the username and password
				// exist
				String selection = RegisEntry.COLUMN_NAME_TITLE + " = '"
						+ usernameString + "' and "
						+ RegisEntry.COLUMN_NAME_CONTENT + " = '"
						+ passwordString + "'";
				Cursor cursor = mDatabaseRead.query(RegisEntry.TABLE_NAME,
						projection, selection, null, null, null, null);
				Bundle mBundle = new Bundle();
				LogInConfirm mLogInConfirm = new LogInConfirm();

				// if there is no match, moveToLast will return false, cursor is
				// empty
				if (cursor.moveToLast() == false) {
					mBundle.putString("info",
							"You are not in our db, please sign up first!!!");
					mLogInConfirm.setArguments(mBundle);
					mLogInConfirm
							.show(getSupportFragmentManager(), "logInFail");
				} else {
					mBundle.putString("info",
							"You have logged in successfully!!!");
					mBundle.putString("back", "true");
					mLogInConfirm.setArguments(mBundle);
					mLogInConfirm.show(getSupportFragmentManager(),
							"logInSuccess");

				}

				/*
				 * int entriesNum = 0; cursor.moveToFirst(); while
				 * (cursor.isAfterLast() == false) {
				 * 
				 * String titleString = cursor.getString(cursor
				 * .getColumnIndex(RegisEntry.COLUMN_NAME_TITLE)); String
				 * contentString = cursor .getString(cursor
				 * .getColumnIndexOrThrow(RegisEntry.COLUMN_NAME_CONTENT));
				 * Log.i(TAG, "we have title: " + titleString + " and content: "
				 * + contentString); cursor.moveToNext(); entriesNum++; }
				 * 
				 * Log.i(TAG, "We have " + entriesNum + " entries!!");
				 */

			}
		};
		buttonLogIn.setOnClickListener(listener);
		buttonSignUP.setOnClickListener(listener);
	}

}
