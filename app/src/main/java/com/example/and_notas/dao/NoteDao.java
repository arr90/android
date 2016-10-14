package com.example.and_notas.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.and_notas.CustomSQLiteOpenHelper;
import com.example.and_notas.vo.Note;

public class NoteDao {

	private static final String LOG_NOTE_LIST_ADAPTER = NoteDao.class.getSimpleName();
	private SQLiteDatabase database;
	private String[] columns = { CustomSQLiteOpenHelper.COLUMN_ID, CustomSQLiteOpenHelper.COLUMN_NOTES, CustomSQLiteOpenHelper.COLUMN_CREATE_DATE };
	private CustomSQLiteOpenHelper sqliteOpenHelper;

	public NoteDao(Context context) {
		sqliteOpenHelper = new CustomSQLiteOpenHelper(context);
	}

	public void open() throws SQLException {
		database = sqliteOpenHelper.getWritableDatabase();
	}

	public void close() {
		sqliteOpenHelper.close();
	}

	public Note create(String note) {
		ContentValues values = new ContentValues();
		values.put(CustomSQLiteOpenHelper.COLUMN_NOTES, note);
		values.put(CustomSQLiteOpenHelper.COLUMN_CREATE_DATE, new Date().getTime());

		long insertId = database.insert(CustomSQLiteOpenHelper.TABLE_NOTES, null, values);
		Cursor cursor = database.query(CustomSQLiteOpenHelper.TABLE_NOTES, columns, CustomSQLiteOpenHelper.COLUMN_ID + " = " + insertId, null, null, null, null);
		cursor.moveToFirst();

		Note newNote = new Note();
		newNote.setId(cursor.getLong(0));
		newNote.setNote(cursor.getString(1));
		newNote.setCreateDate(new Date(cursor.getLong(2)));

		cursor.close();
		return newNote;
	}

	public void delete(Note note) {
		String where = CustomSQLiteOpenHelper.COLUMN_ID + " = " + note.getId();
		database.delete(CustomSQLiteOpenHelper.TABLE_NOTES, where, null);
		Log.i(LOG_NOTE_LIST_ADAPTER, "DELETE NOTE - id: "+ note.getId() +" ["+Thread.currentThread().getStackTrace()[2].getMethodName()+"] LOG **********");
	}

	public void edit(Note note) {
		ContentValues values = new ContentValues();
		String where = CustomSQLiteOpenHelper.COLUMN_ID + " = " + note.getId();

		values.put(CustomSQLiteOpenHelper.COLUMN_NOTES, note.getNote());
		values.put(CustomSQLiteOpenHelper.COLUMN_CREATE_DATE, note.getCreateDate().getTime());

		database.update(CustomSQLiteOpenHelper.TABLE_NOTES, values, where, null);
		database.close();
	}

	public Note getNote(long idNote) {
		//TODO
		Note note = new Note();
		Cursor cursor = database.query(CustomSQLiteOpenHelper.TABLE_NOTES, columns, null, null, null, null, null);
		cursor.moveToFirst();

		note.setId(cursor.getLong(0));
		note.setNote(cursor.getString(1));
		note.setCreateDate(new Date(cursor.getLong(2)));

		cursor.close();
		return note;
	}

	public List<Note> getAll() {
		List<Note> notes = new ArrayList<Note>();
		Cursor cursor = database.query(CustomSQLiteOpenHelper.TABLE_NOTES, columns, null, null, null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Note note = new Note();
			note.setId(cursor.getLong(0));
			note.setNote(cursor.getString(1));
			note.setCreateDate(new Date(cursor.getLong(2)));
			notes.add(note);
			cursor.moveToNext();
		}
		cursor.close();
		return notes;
	}
}