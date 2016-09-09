package com.example.and_notas.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.and_notas.vo.Note;
import com.example.and_notas.dao.NoteDao;
import com.example.and_notas.R;


public class EditNoteActivity extends Activity {

    private NoteDao dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_note);

        final Note note = (Note) getIntent().getSerializableExtra("noteForEdit");

        dao = new NoteDao(this);
        dao.open();

        final EditText editNoteText = (EditText) findViewById(R.id.edit_note_text);
        Button editButton           = (Button)   findViewById(R.id.edit_note_button);

        editNoteText.setText(note.getNote());

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO
                String noteEdited = editNoteText.getEditableText().toString();

                note.setNote(noteEdited);

                dao.edit(note);

                Intent intent = new Intent(EditNoteActivity.this,MainActivity.class);
                startActivity(intent);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
