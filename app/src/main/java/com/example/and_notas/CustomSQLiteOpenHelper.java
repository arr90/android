package com.example.and_notas;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CustomSQLiteOpenHelper extends SQLiteOpenHelper{

	public static final String TABLE_NOTES = "notes";

	public static final String COLUMN_ID 		  = "_id";
	public static final String COLUMN_NOTES 	  = "note";
	public static final String COLUMN_CREATE_DATE = "createDate";

	private static final String DATABASE_NAME	  = "and_notas.db";
	private static final int 	DATABASE_VERSION  = 1;

	private static final String DATABASE_CREATE = "create table " + TABLE_NOTES
			+ "("
			+ COLUMN_ID 	 	 + " integer primary key autoincrement , "
			+ COLUMN_NOTES  	 + " text not null , "
			+ COLUMN_CREATE_DATE + " date not null "
			+ ");";
	
	public CustomSQLiteOpenHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTES);
		onCreate(db);
	}

}
