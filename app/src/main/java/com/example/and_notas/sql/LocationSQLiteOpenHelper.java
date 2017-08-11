package com.example.and_notas.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class LocationSQLiteOpenHelper extends SQLiteOpenHelper{

	public static final String TABLE_LOCATION = "location";

	public static final String COLUMN_ID 		  = "_id";
	public static final String COLUMN_TITLE 	  = "note";
	public static final String COLUMN_DESCRIPTION = "description";
	public static final String COLUMN_LATITUDE 	  = "latitude";
	public static final String COLUMN_LONGITUDE   = "longitude";
	public static final String COLUMN_CREATE_DATE = "createDate";

	private static final String DATABASE_NAME	  = "and_notas.db";
	private static final int 	DATABASE_VERSION  = 1;

	private static final String DATABASE_CREATE = "create table " + TABLE_LOCATION
			+ "("
			+ COLUMN_ID 	 	 + " integer primary key autoincrement , "
			+ COLUMN_TITLE 		 + " text not null , "
			+ COLUMN_DESCRIPTION + " text not null , "
			+ COLUMN_LATITUDE 	 + " double not null , "
			+ COLUMN_LONGITUDE	 + " double not null , "
			+ COLUMN_CREATE_DATE + " date not null "
			+ ");";

	public LocationSQLiteOpenHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOCATION);
		onCreate(db);
	}

}