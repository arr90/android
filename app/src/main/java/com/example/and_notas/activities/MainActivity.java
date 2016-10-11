package com.example.and_notas.activities;

import android.app.ActionBar;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.and_notas.R;
import com.example.and_notas.adapter.NoteItemListAdapter;
import com.example.and_notas.dao.NoteDao;
import com.example.and_notas.vo.Note;

import java.util.List;

public class MainActivity extends ListActivity {

	private NoteDao dao;
    List<Note> notes;

    NoteItemListAdapter noteItemListAdapter;
    Toolbar toolbar;

    protected Object mActionMode;
    public int selectedItem = -1;

    private final String LOG_ARRAY_ADAPTER = ArrayAdapter.class.getSimpleName();

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

        dao = new NoteDao(this);
        dao.open();
        notes = dao.getAll();

        noteItemListAdapter = new NoteItemListAdapter(this, notes);
        setListAdapter(noteItemListAdapter);

        /**/
        getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        getListView().setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

            private int nr = 0;

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                noteItemListAdapter.clearSelection();
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                nr = 0;
                MenuInflater inflater = getMenuInflater();
                inflater.inflate(R.menu.menu_edit_note, menu);
                return true;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_delete:
                        nr = 0;
                        noteItemListAdapter.clearSelection();
                        mode.finish();
                }
                return false;
            }

            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                if (checked) {
                    nr++;
                    noteItemListAdapter.setNewSelection(position, checked);
                    getListView().setBackgroundResource(R.color.zgreen);
                } else {
                    nr--;
                    noteItemListAdapter.removeSelection(position);
                }
                mode.setTitle(nr + " selected");

            }
        });
        /**/
        createFABaddNote();
        initToolBar();
    }
/*
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        //TEMP
        super.onPostCreate(savedInstanceState);
        getActionBar().setSubtitle("Long press to start selection");
    }
*/
	@Override
	protected void onResume() {
		dao.open();
		super.onResume();

/*
        notes = dao.getAll();
		noteItemListAdapter = new NoteItemListAdapter(this, notes);
        setListAdapter(noteItemListAdapter);

        Log.i(LOG_ARRAY_ADAPTER, "init adapter LOG *************************************************************");

        ListView listView = (ListView) findViewById(android.R.id.list);
        listView.setAdapter(noteItemListAdapter);
*/
//        createFABaddNote();
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
/*
        Toolbar toolbarMain = (Toolbar) findViewById(R.id.toolbar_main);
		toolbarMain.inflateMenu(R.menu.menu_main);

        TextView textView = (TextView) findViewById(R.id.toolbar_text_search);
        textView.setOnClickListener(new TextView.OnClickListener(){
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "text view clicked", Toast.LENGTH_SHORT).show();
            }
        });

        toolbarMain.setOnClickListener(new Toolbar.OnClickListener(){
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "toolbar click", Toast.LENGTH_SHORT).show();
            }
        });

		toolbarMain.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener(){
					@Override
					public boolean onMenuItemClick(MenuItem item) {
						return onOptionsItemSelected(item);
					}
				});

		*/
        return true;
	}



	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.action_add_note) {
			Intent intent = new Intent(this, AddNoteActivity.class);
			startActivity(intent);
            Toast.makeText(this, "Add Note selected", Toast.LENGTH_SHORT).show();
		}
		if (item.getItemId() == R.id.action_refresh) {
			Toast.makeText(this, "Refresh selected", Toast.LENGTH_SHORT).show();
		}
		if (item.getItemId() == R.id.action_settings) {
			Toast.makeText(this, "Settings selected", Toast.LENGTH_SHORT).show();
		}
		return true;
	}

    @Override
    protected void onPause() {
        dao.close();
        super.onPause();
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

    public void initToolBar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setActionBar(toolbar);



        toolbar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "clicking the toolbar!", Toast.LENGTH_SHORT).show();
            }
        });

        toolbar.setNavigationOnClickListener(
        new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "clicking the navigation toolbar!", Toast.LENGTH_SHORT).show();
            }
        });

    }

}