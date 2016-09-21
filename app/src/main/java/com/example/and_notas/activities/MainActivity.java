package com.example.and_notas.activities;

import java.text.SimpleDateFormat;
import java.util.List;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
    }

	@Override
	protected void onResume() {
		dao.open();
		super.onResume();

		notes = dao.getAll();

//        createDragToNoteList();

		ListView listView = (ListView) findViewById(android.R.id.list);
		noteItemListAdapter = new NoteItemListAdapter(this, notes);
        Log.i(LOG_ARRAY_ADAPTER, "init adapter");

		setListAdapter(noteItemListAdapter);

		listView.setAdapter(noteItemListAdapter);

		getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        createFABaddNote();


		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> a, View v, int position, long id) {

				AlertDialog.Builder adb = new AlertDialog.Builder(MainActivity.this);
				adb.setTitle("Edit?");
				adb.setMessage("Are you sure you want to edit this item [" + position + "] ?");

				final int positionToEdit = position;
				adb.setNegativeButton("Cancel", null);
				adb.setPositiveButton("Ok", new AlertDialog.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {

						Intent intent = new Intent(MainActivity.this, EditNoteActivity.class);
						intent.putExtra("noteForEdit", notes.get(positionToEdit));

						startActivity(intent);
					}
				});
				adb.show();
			}
		});

		listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

				java.text.SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
				final int positionToRemove = position;

				AlertDialog.Builder adb = new AlertDialog.Builder(MainActivity.this);
				adb.setTitle("Delete?");
				adb.setMessage("Are you sure you want to delete [" + positionToRemove + "]" + " - " + simpleDateFormat.format(notes.get(positionToRemove).getCreateDate()));

				adb.setNegativeButton("Cancel", null);
				adb.setPositiveButton("Ok", new AlertDialog.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						dao.delete(notes.get(positionToRemove));
						notes.remove(positionToRemove);
						noteItemListAdapter.notifyDataSetChanged();
					}
				});

				adb.show();
				return false;
			}
		});

	}
/*
    private void createDragToNoteList() {
        DragLinearLayout dragDropAndroidLinearLayout = (DragLinearLayout) findViewById(R.id.drag_drop_layout);
        for (int i = 0; i < dragDropAndroidLinearLayout.getChildCount(); i++) {
            View child = dragDropAndroidLinearLayout.getChildAt(i);
            dragDropAndroidLinearLayout.setViewDraggable(child, child);
        }
    }
*/
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
		getMenuInflater().inflate(R.menu.main, menu);
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