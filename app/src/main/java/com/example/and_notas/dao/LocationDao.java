package com.example.and_notas.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.and_notas.sql.LocationSQLiteOpenHelper;
import com.example.and_notas.vo.Location;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LocationDao {

	private static final String LOG_NOTE_LIST_ADAPTER = LocationDao.class.getSimpleName();
	private SQLiteDatabase database;
	private String[] columns = { LocationSQLiteOpenHelper.COLUMN_ID, LocationSQLiteOpenHelper.COLUMN_TITLE, LocationSQLiteOpenHelper.COLUMN_DESCRIPTION};
	private LocationSQLiteOpenHelper sqliteOpenHelper;

	public LocationDao(Context context) {
		sqliteOpenHelper = new LocationSQLiteOpenHelper(context);
	}

	public void open() throws SQLException {
		database = sqliteOpenHelper.getWritableDatabase();
	}

	public void close() {
		sqliteOpenHelper.close();
	}

	public Location create(Location location) {
		ContentValues values = new ContentValues();
		values.put(LocationSQLiteOpenHelper.COLUMN_TITLE	   , location.getTitle());
		values.put(LocationSQLiteOpenHelper.COLUMN_DESCRIPTION , location.getDescription());
		values.put(LocationSQLiteOpenHelper.COLUMN_LATITUDE    , location.getLatLng().latitude);
		values.put(LocationSQLiteOpenHelper.COLUMN_LONGITUDE   , location.getLatLng().longitude);
		values.put(LocationSQLiteOpenHelper.COLUMN_CREATE_DATE , new Date().getTime());

		long insertId = database.insert(LocationSQLiteOpenHelper.TABLE_LOCATION, null, values);
		Cursor cursor = database.query(LocationSQLiteOpenHelper.TABLE_LOCATION, columns, LocationSQLiteOpenHelper.COLUMN_ID + " = " + insertId, null, null, null, null);
		cursor.moveToFirst();

		Location newLocation = new Location();
		newLocation.setId(cursor.getLong(0));
		newLocation.setTitle(cursor.getString(1));
		newLocation.setDescription(cursor.getString(2));
		newLocation.setLatLng(new LatLng(cursor.getDouble(3), cursor.getDouble(4)));
		newLocation.setCreateDate(new Date(cursor.getLong(5)));

		cursor.close();
		return newLocation;
	}

	public void delete(Location location) {
		String where = LocationSQLiteOpenHelper.COLUMN_ID + " = " + location.getId();
		database.delete(LocationSQLiteOpenHelper.TABLE_LOCATION, where, null);
		Log.i(LOG_NOTE_LIST_ADAPTER, "DELETE NOTE - id: "+ location.getId() +" ["+Thread.currentThread().getStackTrace()[2].getMethodName()+"] LOG **********");
	}

	public void edit(Location location) {
		ContentValues values = new ContentValues();
		String where = LocationSQLiteOpenHelper.COLUMN_ID + " = " + location.getId();

		values.put(LocationSQLiteOpenHelper.COLUMN_TITLE, location.getTitle());
		values.put(LocationSQLiteOpenHelper.COLUMN_DESCRIPTION, location.getCreateDate().getTime());

		database.update(LocationSQLiteOpenHelper.TABLE_LOCATION, values, where, null);
		database.close();
	}

	public Location getNote(long idNote) {
		Location location = new Location();
		Cursor cursor = database.query(LocationSQLiteOpenHelper.TABLE_LOCATION, columns, null, null, null, null, null);
		cursor.moveToFirst();

		location.setId(cursor.getLong(0));
		location.setTitle(cursor.getString(1));
		location.setCreateDate(new Date(cursor.getLong(2)));

		cursor.close();
		return location;
	}

	public List<Location> getAll() {
		List<Location> locations = new ArrayList<Location>();
		Cursor cursor = database.query(LocationSQLiteOpenHelper.TABLE_LOCATION, columns, null, null, null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Location location = new Location();
			location.setId(cursor.getLong(0));
			location.setTitle(cursor.getString(1));
			location.setCreateDate(new Date(cursor.getLong(2)));
			locations.add(location);
			cursor.moveToNext();
		}
		cursor.close();
		return locations;
	}

	public List<Location> getAllTest() {
		List<Location> locations = new ArrayList<Location>();
		Location location;

		for (int i=1;i<10;i++){
			location = new Location();
			location.setId(i);
			location.setTitle("TEST"+i);
			location.setDescription("DESCRIPTION TEST "+i);

			if(i==1)
			location.setLatLng(new LatLng(53.558, 9.927));
			if(i==2)
			location.setLatLng(new LatLng(-22.902, -43.180));
			if(i==3)
			location.setLatLng(new LatLng(-22.8634671, -43.345479));
			if(i==4)
			location.setLatLng(new LatLng(-22.906337, -43.181129));
			if(i==5)
			location.setLatLng(new LatLng(-22.903538, -43.181600));
			if(i==6)
			location.setLatLng(new LatLng(-22.900824, -43.182013));
			if(i==7)
			location.setLatLng(new LatLng(-22.901457, -43.182540));
			if(i==8)
			location.setLatLng(new LatLng(-22.902657, -43.177279));
			if(i==9)
			location.setLatLng(new LatLng(-22.902706, -43.178519));
			if(i==10)
			location.setLatLng(new LatLng(-22.903286, -43.179802));

			location.setCreateDate(new Date());
			locations.add(location);
		}

		return locations;
	}
}