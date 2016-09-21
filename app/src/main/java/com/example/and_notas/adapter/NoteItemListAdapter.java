package com.example.and_notas.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.and_notas.R;
import com.example.and_notas.activities.MainActivity;
import com.example.and_notas.dao.NoteDao;
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

    private Context context;
    private int layoutResourceId;
    private List<Note> notes;
    private NoteDao dao;

    public NoteItemListAdapter(Context context, int layoutResourceId,List<Note> notes) {
        super(context, layoutResourceId,notes);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.notes = notes;
    }

    public NoteItemListAdapter(Context context, List<Note> notes) {
        super(context, R.layout.note_item_list, notes);
        this.context = context;
        this.notes = notes;
    }

    @SuppressLint("WrongViewCast")
    @Override
    public View getView(final int position, View view, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        view = inflater.inflate(R.layout.note_item_list, parent, false);

        TextView textView = (TextView) view.findViewById(R.id.note_text);
        textView.setText(notes.get(position).getNote().toString());

        Button buttonDelete = (Button) view.findViewById(R.id.bt_delete);
        buttonDelete.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                //TODO implements dao remove note
                dao = new NoteDao(context);
                dao.open();
                dao.delete(notes.get(position));

                notes.remove(position);
                notifyDataSetChanged();
            }
        });

        return view;
    }

}