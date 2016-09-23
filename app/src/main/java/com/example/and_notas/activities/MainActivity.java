package com.example.and_notas.activities;

import java.text.SimpleDateFormat;
import java.util.List;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.and_notas.R;
import com.example.and_notas.adapter.NoteItemListAdapter;
import com.example.and_notas.vo.Note;
import com.example.and_notas.dao.NoteDao;

public class MainActivity extends ListActivity {

	private NoteDao dao;
    List<Note> notes;

    NoteItemListAdapter noteItemListAdapter;

    private final String LOG_ARRAY_ADAPTER = ArrayAdapter.class.getSimpleName();

	@Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
		setContentView(R.layout.main);

        dao = new NoteDao(this);
        dao.open();
        notes = dao.getAll();

        NoteItemListAdapter adapter = new NoteItemListAdapter(this, notes);
        setListAdapter(adapter);

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		toolbar.setLogo(R.drawable.ic_launcher);
    }


	@Override
	protected void onResume() {
		dao.open();
		super.onResume();

		notes = dao.getAll();

		ListView listView = (ListView) findViewById(android.R.id.list);
		noteItemListAdapter = new NoteItemListAdapter(this, notes);
        Log.i(LOG_ARRAY_ADAPTER, "init adapter");

		setListAdapter(noteItemListAdapter);
		listView.setAdapter(noteItemListAdapter);

        createFABaddNote();

	}

    private void createFABaddNote() {
        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.new_note);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddNoteActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    @Override
	protected void onPause() {
		dao.close();
		super.onPause();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	/*public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.add_note) {
			Intent intent = new Intent(this, AddNoteActivity.class);
			startActivity(intent);
		}
		return super.onOptionsItemSelected(item);
	}*/
	public boolean onOptionsItemSelected(MenuItem item) {
		Toast.makeText(this, String.valueOf(getListView().getCheckedItemCount()), Toast.LENGTH_LONG).show();
		return true;
	}

}