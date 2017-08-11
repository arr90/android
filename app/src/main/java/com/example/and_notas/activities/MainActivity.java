package com.example.and_notas.activities;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

    private static final String LOG_MAIN_ACTIVITY = MainActivity.class.getSimpleName();

	@Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(LOG_MAIN_ACTIVITY, "LOG ["+Thread.currentThread().getStackTrace()[2].getMethodName()+"] {**********}");

        super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

        dao = new NoteDao(this);
        dao.open();
        notes = dao.getAll();

        noteItemListAdapter = new NoteItemListAdapter(this, notes);
        setListAdapter(noteItemListAdapter);

        createFABaddNote();
        createFABopenMap();
        createFABopenLogin();
        initToolBar();
    }

	@Override
	protected void onResume() {
		dao.open();
		super.onResume();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
        Log.i(LOG_MAIN_ACTIVITY, "LOG ["+Thread.currentThread().getStackTrace()[2].getMethodName()+"] {**********}");
/*
        TextView textView = (TextView) findViewById(R.id.toolbar_text_search);
        textView.setOnClickListener(new TextView.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.i(LOG_MAIN_ACTIVITY, "LOG ["+Thread.currentThread().getStackTrace()[2].getMethodName()+"] {**********}");
                Toast.makeText(MainActivity.this, "text view clicked", Toast.LENGTH_SHORT).show();
            }
        });

        Toolbar toolbarMain = (Toolbar) findViewById(R.id.toolbar_main);
		toolbarMain.inflateMenu(R.menu.menu_main);

        TextView textView = (TextView) findViewById(R.id.toolbar_text_search);
        textView.setOnClickListener(new TextView.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.i(LOG_MAIN_ACTIVITY, "LOG ["+Thread.currentThread().getStackTrace()[2].getMethodName()+"] {**********}");
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
        Log.i(LOG_MAIN_ACTIVITY, "LOG ["+Thread.currentThread().getStackTrace()[2].getMethodName()+"] {**********}");

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
        Log.i(LOG_MAIN_ACTIVITY, "LOG ["+Thread.currentThread().getStackTrace()[2].getMethodName()+"] {**********}");

        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.new_note);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(LOG_MAIN_ACTIVITY, "LOG ["+Thread.currentThread().getStackTrace()[2].getMethodName()+"] {**********}");

                Intent intent = new Intent(MainActivity.this, AddNoteActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    private void createFABopenMap() {
        Log.i(LOG_MAIN_ACTIVITY, "LOG ["+Thread.currentThread().getStackTrace()[2].getMethodName()+"] {**********}");

        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.open_map);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(LOG_MAIN_ACTIVITY, "LOG ["+Thread.currentThread().getStackTrace()[2].getMethodName()+"] {**********}");

                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void createFABopenLogin() {
        Log.i(LOG_MAIN_ACTIVITY, "LOG ["+Thread.currentThread().getStackTrace()[2].getMethodName()+"] {**********}");

        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.open_login);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(LOG_MAIN_ACTIVITY, "LOG ["+Thread.currentThread().getStackTrace()[2].getMethodName()+"] {**********}");

                Intent intent = new Intent(MainActivity.this, MaterialDesignLogInFormActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void initToolBar(){
        Log.i(LOG_MAIN_ACTIVITY, "LOG ["+Thread.currentThread().getStackTrace()[2].getMethodName()+"] {**********}");

        toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setActionBar(toolbar);

        toolbar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.i(LOG_MAIN_ACTIVITY, "LOG ["+Thread.currentThread().getStackTrace()[2].getMethodName()+"] {**********}");

                Toast.makeText(MainActivity.this, "clicking the toolbar!", Toast.LENGTH_SHORT).show();
            }
        });

        toolbar.setNavigationOnClickListener(
        new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(LOG_MAIN_ACTIVITY, "LOG ["+Thread.currentThread().getStackTrace()[2].getMethodName()+"] {**********}");

                Toast.makeText(MainActivity.this, "clicking the navigation toolbar!", Toast.LENGTH_SHORT).show();
            }
        });

    }

}