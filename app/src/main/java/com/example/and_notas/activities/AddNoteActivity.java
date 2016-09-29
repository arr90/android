package com.example.and_notas.activities;

import android.app.ActionBar;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.example.and_notas.dao.NoteDao;
import com.example.and_notas.R;

public class AddNoteActivity extends ListActivity {
	
	private NoteDao dao;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_note);

		dao = new NoteDao(this);
		dao.open();
		
		FloatingActionButton saveFAButton = (FloatingActionButton) findViewById(R.id.save_note_button);
		final EditText noteText = (EditText) findViewById(R.id.note_text);
		
		saveFAButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String note = noteText.getEditableText().toString();
				dao.create(note);
				Intent intent = new Intent(AddNoteActivity.this, MainActivity.class);
				startActivity(intent);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_main,menu);
		return true;
	}

	@Override
	protected void onResume() {
		dao.open();
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		dao.close();
		super.onPause();
	}
	
}
