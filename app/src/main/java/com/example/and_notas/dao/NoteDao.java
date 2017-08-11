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

import com.example.and_notas.sql.NoteSQLiteOpenHelper;
import com.example.and_notas.vo.Note;

public class NoteDao {

	private static final String LOG_NOTE_LIST_ADAPTER = NoteDao.class.getSimpleName();
	private SQLiteDatabase database;
	private String[] columns = { NoteSQLiteOpenHelper.COLUMN_ID, NoteSQLiteOpenHelper.COLUMN_NOTES, NoteSQLiteOpenHelper.COLUMN_CREATE_DATE };
	private NoteSQLiteOpenHelper sqliteOpenHelper;

	public NoteDao(Context context) {
		sqliteOpenHelper = new NoteSQLiteOpenHelper(context);
	}

	public void open() throws SQLException {
		database = sqliteOpenHelper.getWritableDatabase();
	}

	public void close() {
		sqliteOpenHelper.close();
	}

	public Note create(String note) {
		ContentValues values = new ContentValues();
		values.put(NoteSQLiteOpenHelper.COLUMN_NOTES, note);
		values.put(NoteSQLiteOpenHelper.COLUMN_CREATE_DATE, new Date().getTime());

		long insertId = database.insert(NoteSQLiteOpenHelper.TABLE_NOTES, null, values);
		Cursor cursor = database.query(NoteSQLiteOpenHelper.TABLE_NOTES, columns, NoteSQLiteOpenHelper.COLUMN_ID + " = " + insertId, null, null, null, null);
		cursor.moveToFirst();

		Note newNote = new Note();
		newNote.setId(cursor.getLong(0));
		newNote.setNote(cursor.getString(1));
		newNote.setCreateDate(new Date(cursor.getLong(2)));

		cursor.close();
		return newNote;
	}

	public void delete(Note note) {
		String where = NoteSQLiteOpenHelper.COLUMN_ID + " = " + note.getId();
		database.delete(NoteSQLiteOpenHelper.TABLE_NOTES, where, null);
		Log.i(LOG_NOTE_LIST_ADAPTER, "DELETE NOTE - id: "+ note.getId() +" ["+Thread.currentThread().getStackTrace()[2].getMethodName()+"] LOG **********");
	}

	public void edit(Note note) {
		ContentValues values = new ContentValues();
		String where = NoteSQLiteOpenHelper.COLUMN_ID + " = " + note.getId();

		values.put(NoteSQLiteOpenHelper.COLUMN_NOTES, note.getNote());
		values.put(NoteSQLiteOpenHelper.COLUMN_CREATE_DATE, note.getCreateDate().getTime());

		database.update(NoteSQLiteOpenHelper.TABLE_NOTES, values, where, null);
		database.close();
	}

	public Note getNote(long idNote) {
		//TODO
		Note note = new Note();
		Cursor cursor = database.query(NoteSQLiteOpenHelper.TABLE_NOTES, columns, null, null, null, null, null);
		cursor.moveToFirst();

		note.setId(cursor.getLong(0));
		note.setNote(cursor.getString(1));
		note.setCreateDate(new Date(cursor.getLong(2)));

		cursor.close();
		return note;
	}

	public List<Note> getAll() {
		List<Note> notes = new ArrayList<Note>();
		Cursor cursor = database.query(NoteSQLiteOpenHelper.TABLE_NOTES, columns, null, null, null, null, null);
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