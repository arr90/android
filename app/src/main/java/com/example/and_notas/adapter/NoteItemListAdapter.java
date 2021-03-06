package com.example.and_notas.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.and_notas.R;
import com.example.and_notas.viewHolder.NoteHolder;
import com.example.and_notas.vo.Note;

import java.util.List;

/**
 * Created by areis on 13/05/2016.
 */

/**
 * This is the java class that is the controller for note_item_list.xml.
 * It keeps references for all of its views,
 * and it also puts these references in tags,
 * extending the ArrayAdapter interface.
 */
public class NoteItemListAdapter extends ArrayAdapter<Note> {

    private List<Note> notes;
    private int layoutResourceId;
    private Context context;


    public NoteItemListAdapter(Context context, int layoutResourceId,List<Note> notes) {
        super(context, layoutResourceId,notes);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.notes = notes;
    }

    @SuppressLint("WrongViewCast")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;
        NoteHolder holder;

        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        row = inflater.inflate(layoutResourceId,parent,false);

        holder = new NoteHolder();
        holder.note = notes.get(position);
        holder.bt_delete = (ImageButton) row.findViewById(R.id.bt_delete);
        holder.bt_delete.setTag(holder.note);

        holder.note_text = (TextView) row.findViewById(R.id.note_text);

        row.setTag(holder);

        setupItem(holder);
        return row;
    }

    private void setupItem(NoteHolder holder) {
        holder.note_text.setText(holder.note.getNote());
    }

}