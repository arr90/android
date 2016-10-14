package com.example.and_notas.adapter;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.and_notas.R;
import com.example.and_notas.activities.EditNoteActivity;
import com.example.and_notas.activities.MainActivity;
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

    private SparseBooleanArray mSelectedItemsIds;

    protected Activity activity;
    protected ActionMode mActionMode;
    public int selectedItem = -1;

    public NoteItemListAdapter(Context context, int resource, int textViewResourceId, List<Note> notes) {
        super(context, R.layout.note_item_list, notes);
    }

    public NoteItemListAdapter(Context context, int layoutResourceId,List<Note> notes) {
        super(context, layoutResourceId,notes);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.notes = notes;
    }

    public NoteItemListAdapter(Context context, List<Note> notes) {
        super(context, R.layout.note_item_list, notes);
        mSelectedItemsIds = new SparseBooleanArray();
        this.context = context;
        this.notes = notes;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        Log.i(LOG_NOTE_LIST_ADAPTER, "init ["+Thread.currentThread().getStackTrace()[2].getMethodName()+"] LOG **********");

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.note_item_list, parent, false);

        TextView textView = (TextView) view.findViewById(R.id.note_text);
        textView.setText(notes.get(position).getNote().toString());/*limit character in exhibition*/

        //add on click listener to start edit mode
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mActionMode == null) {// no items selected, so perform item click actions
                    Intent intent = new Intent(context,EditNoteActivity.class);
                    intent.putExtra("noteForEdit", notes.get(position));
                    view.getContext().startActivity(intent);
                } else {// add or remove selection for current list item
                    onListItemCheck(view, position);
                }
            }
        });

        //add on long click listener to start action mode
        view.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View view) {
                Log.i(LOG_NOTE_LIST_ADAPTER, "init ["+Thread.currentThread().getStackTrace()[2].getMethodName()+"] LOG **********");
//                return onListItemCheck(view,position);
                onListItemCheck(view,position);
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

        //change background color if list item is selected
        view.setBackgroundColor(mSelectedItemsIds.get(position) ? context.getResources().getColor(R.color.ColorPrimarySweet) : Color.TRANSPARENT);

        return view;
    }

    private boolean onListItemCheck(View view, int position) {

        toggleSelection(position);
        boolean hasCheckedItems = getSelectedCount() > 0;

        if (hasCheckedItems && mActionMode == null) {
            mActionMode = ((Activity)context).startActionMode(NoteItemListAdapter.this);// there are some selected items, start the actionMode
        } else if (!hasCheckedItems && mActionMode != null) {
            mActionMode.finish();// there no selected items, finish the actionMode
        }

        if(mActionMode != null) {
            mActionMode.setTitle(String.valueOf(getSelectedCount()) + " selected");
        }
        return true;
    }

    /***ADAPTER****/
    public void toggleSelection(int position) {
        selectView(position, !mSelectedItemsIds.get(position));
    }
    public void removeSelection() {
        mSelectedItemsIds = new SparseBooleanArray();
        notifyDataSetChanged();
    }
    public void selectView(int position, boolean value) {
        if(value) {
            mSelectedItemsIds.put(position, value);
        }else {
            mSelectedItemsIds.delete(position);
        }
        notifyDataSetChanged();
    }
    public int getSelectedCount() {
        return mSelectedItemsIds.size();
    }

    public SparseBooleanArray getSelectedIds() {
        return mSelectedItemsIds;
    }
    /****ADAPTER***/

    /***ACTION MODE****/
    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        Log.i(LOG_NOTE_LIST_ADAPTER, "init ["+Thread.currentThread().getStackTrace()[2].getMethodName()+"] LOG **********");
        mode.getMenuInflater().inflate(R.menu.menu_note_selection,menu);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        Log.i(LOG_NOTE_LIST_ADAPTER, "init ["+Thread.currentThread().getStackTrace()[2].getMethodName()+"] LOG **********");
        mode.setTitle("Click to select");
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        Log.i(LOG_NOTE_LIST_ADAPTER, "init ["+Thread.currentThread().getStackTrace()[2].getMethodName()+"] LOG **********");

        SparseBooleanArray selected = getSelectedIds();

        StringBuilder message = new StringBuilder();
        for (int i = 0; i < selected.size(); i++) {
            if (selected.valueAt(i)) {
                Note selectedItem = getItem(selected.keyAt(i));
                message.append(selectedItem.getId() + "\n");
            }
        }

        if (item.getItemId() == R.id.ns_share) {
            Toast.makeText(context, "SHARE CLICKED !!!  \n" + message.toString(), Toast.LENGTH_LONG).show();
            mode.finish();
            return true;
        }
        if (item.getItemId() == R.id.ns_copy) {
            Toast.makeText(context, "COPY CLICKED !!!   \n" + message.toString(), Toast.LENGTH_LONG).show();
            mode.finish();
            return true;
        }
        if (item.getItemId() == R.id.ns_delete) {
            Toast.makeText(context, "DELETE CLICKED !!! \n" + message.toString(), Toast.LENGTH_LONG).show();

            dao = new NoteDao(context);
            dao.open();

            for (int i = 0; i < selected.size(); i++) {
                if (selected.valueAt(i)) {
                    dao.delete(getItem(selected.keyAt(i)));
                    notes.remove(selected.keyAt(i));
                }
            }
            dao.close();

            notifyDataSetChanged();

            mode.finish();
            return true;
        }
        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        removeSelection();
        mActionMode = null;
        selectedItem = -1;
    }
    /****ACTION MODE***/

    private HashMap<Integer, Boolean> mSelection = new HashMap<Integer, Boolean>();

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