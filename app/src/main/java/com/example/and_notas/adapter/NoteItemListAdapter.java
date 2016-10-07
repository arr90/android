package com.example.and_notas.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.and_notas.R;
import com.example.and_notas.activities.EditNoteActivity;
import com.example.and_notas.dao.NoteDao;
import com.example.and_notas.vo.Note;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by areis on 13/05/2016.
 */

/**
 * This is the java class that is the controller for note_item_list.xml.
 * It keeps references for all of its views,
 * and it also puts these references in tags,
 * extending the ArrayAdapter interface.
 */
public class NoteItemListAdapter extends ArrayAdapter<Note> implements ActionMode.Callback {

    private static final String LOG_NOTE_LIST_ADAPTER = NoteItemListAdapter.class.getSimpleName();
    private Context context;
    private int layoutResourceId;
    private List<Note> notes;
    private NoteDao dao;

    protected Activity activity;
    protected Object mActionMode;
    public int selectedItem = -1;


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


    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        Log.i(LOG_NOTE_LIST_ADAPTER, "init ["+Thread.currentThread().getStackTrace()[2].getMethodName()+"] LOG *************************************************************");
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.note_item_list, parent, false);

        TextView textView = (TextView) view.findViewById(R.id.note_text);
        textView.setText(notes.get(position).getNote().toString());

        //            view.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));// this is a selected position so make it red
        if (mSelection.get(position) != null) {
            System.out.print("****************************** mSelection.get(position) != null *******************************-");
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,EditNoteActivity.class);
                intent.putExtra("noteForEdit", notes.get(position));
                view.getContext().startActivity(intent);
            }
        });

        view.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View view) {
                Log.i(LOG_NOTE_LIST_ADAPTER, "init ["+Thread.currentThread().getStackTrace()[2].getMethodName()+"] LOG *************************************************************");

                if (mActionMode != null) {
                    return false;
                }
//                selectedItem = position;
                //TODO change backgroundColor when clicked

                if (notes.get(position) != null) {
                    view.setBackgroundColor(view.getResources().getColor(R.color.ColorPrimary));// this is a selected position so make it red
                }
/*
                view.setBackgroundColor(getResources().getColor(android.R.color.background_light)); //default color
                if (mSelection.get(position) != null) {
                    view.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));// this is a selected position so make it red
                }
*/

                // Start the CAB using the ActionMode.Callback defined above
//                mActionMode = NoteItemListAdapter.this.startActionMode(NoteItemListAdapter.this);
                mActionMode = ((Activity)context).startActionMode(NoteItemListAdapter.this);
                view.setSelected(true);

                view.setBackgroundResource(R.color.zblue);
//                view.setBackgroundColor(view.getResources().getColor(R.color.zpink));
                view.setDrawingCacheBackgroundColor(view.getResources().getColor(R.color.zyellow));

                return true;
            }
        });

        ImageButton btDelete = (ImageButton) view.findViewById(R.id.bt_delete);
        btDelete.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                dao = new NoteDao(context);
                dao.open();
                dao.delete(notes.get(position));

                notes.remove(position);
                notifyDataSetChanged();
            }
        });

        return view;
    }

    private void show() {
        Toast.makeText(context, String.valueOf(selectedItem), Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        Log.i(LOG_NOTE_LIST_ADAPTER, "init ["+Thread.currentThread().getStackTrace()[2].getMethodName()+"] LOG *************************************************************");
        mode.getMenuInflater().inflate(R.menu.menu_note_selection,menu);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        Log.i(LOG_NOTE_LIST_ADAPTER, "init ["+Thread.currentThread().getStackTrace()[2].getMethodName()+"] LOG *************************************************************");
        mode.setTitle("#");
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        Log.i(LOG_NOTE_LIST_ADAPTER, "init ["+Thread.currentThread().getStackTrace()[2].getMethodName()+"] LOG *************************************************************");
        if (item.getItemId() == R.id.ns_share){
            Toast.makeText(context, "SHARE CLICKED !!!", Toast.LENGTH_LONG).show();
            mode.finish();
            return true;
        }
        if (item.getItemId() == R.id.ns_copy){
            Toast.makeText(context, "COPY CLICKED !!!", Toast.LENGTH_LONG).show();
            mode.finish();
            return true;
        }
        if (item.getItemId() == R.id.ns_delete){
            Toast.makeText(context, "DELETE CLICKED !!!", Toast.LENGTH_LONG).show();
            mode.finish();
            return true;
        }

        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        mActionMode = null;
        selectedItem = -1;
    }

    private HashMap<Integer, Boolean> mSelection = new HashMap<Integer, Boolean>();

    public NoteItemListAdapter(Context context, int resource, int textViewResourceId, List<Note> notes) {
        super(context, R.layout.note_item_list, notes);
    }

    public void setNewSelection(int position, boolean value) {
        mSelection.put(position, value);
        notifyDataSetChanged();
    }

    public boolean isPositionChecked(int position) {
        Boolean result = mSelection.get(position);
        return result == null ? false : result;
    }

    public Set<Integer> getCurrentCheckedPosition() {
        return mSelection.keySet();
    }

    public void removeSelection(int position) {
        mSelection.remove(position);
        notifyDataSetChanged();
    }

    public void clearSelection() {
        mSelection = new HashMap<Integer, Boolean>();
        notifyDataSetChanged();
    }

}