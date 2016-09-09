package com.example.and_notas.activities;

import android.app.ListActivity;
import android.os.Bundle;
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
		
		
		Button saveButton 		= (Button) 	 findViewById(R.id.save_note_button);
		final EditText noteText = (EditText) findViewById(R.id.note_text);
		
		saveButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String note = noteText.getEditableText().toString();
				dao.create(note);
				finish();
			}
		});
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
