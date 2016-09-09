package com.example.and_notas.viewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.and_notas.R;
import com.example.and_notas.vo.Note;

/**
 * Created by areis on 10/05/2016.
 */
public class NoteHolder {
    public Note note;
    public TextView note_text;
    public ImageButton bt_delete;

    /*
    public NoteHolder(View v) {
        bt_delete = (Button)   v.findViewById(R.id.bt_delete);
        note_text = (TextView) v.findViewById(R.id.note_text);
    }
    */
}
