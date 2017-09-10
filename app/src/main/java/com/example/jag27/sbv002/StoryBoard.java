package com.example.jag27.sbv002;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.jag27.sbv002.database.NoteManager;
import com.example.jag27.sbv002.utility.Constants;
import com.example.jag27.sbv002.utility.OnStartDragListener;
import com.example.jag27.sbv002.utility.SimpleItemTouchHelperCallback;
import com.example.jag27.sbv002.view.RecyclerAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class StoryBoard extends AppCompatActivity implements OnStartDragListener {
    private String storyTitle;
    private NoteManager noteManager;
    private ItemTouchHelper itemTouchHelper;
    private int notePos;

    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_board);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String subPlot = null;
        String character = null;
        notePos = 0;

        storyTitle = getIntent().getStringExtra("FileName");
        subPlot = getIntent().getStringExtra("SubTitle");
        character = getIntent().getStringExtra("Character");
        setTitle(storyTitle);

        //Get note data from database, if story title previously created
        noteManager = new NoteManager(this);
        noteManager.open();
        Cursor cursor;
        if (subPlot == null && character == null)cursor = noteManager.fetch(storyTitle);
        else if(subPlot != null){
            cursor = noteManager.fetchStoriesSubPlot(storyTitle, subPlot);
            notePos = getIntent().getIntExtra("MaxNote",notePos);
        }else{
            cursor = noteManager.findCharacter(storyTitle,character);
            Long characterID = cursor.getLong(cursor.getColumnIndex(Constants.COLUMN_ID));
            cursor.close();
            cursor = noteManager.findBridgesByCharacterId(characterID);
            notePos = getIntent().getIntExtra("MaxNote",notePos);
        }

        //Get the position of the last note
        if(cursor.moveToFirst() && subPlot== null && character == null){
            Cursor lastCursor = cursor;
            lastCursor.moveToLast();
            notePos = lastCursor.getInt(lastCursor.getColumnIndex(Constants.COLUMN_POSITION));

        }

        Log.d("POsition", Integer.toString(notePos));
        //Place all note data into a array list
        ArrayList noteData = new ArrayList<>();
        for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
            if(character == null) {
                //Gather note information from database
                noteData.add(cursorToNote(cursor));
            }else{
                long noteId = cursor.getLong(cursor.getColumnIndex(Constants.COLUMN_NOTEID));
                Cursor c = noteManager.findNoteById(noteId);

                Log.d("Note ID", Long.toString(noteId));
                noteData.add(cursorToNote(c));
            }
        }

        if(character != null){
            //sort array list
            Collections.sort(noteData, new Comparator<Note>() {
                @Override
                public int compare(Note o, Note t1) {
                    return Integer.valueOf(o.getPos()).compareTo(t1.getPos());
                }
            });
        }

        //Make recycler view into a grid
        GridLayoutManager gridLayoutManager = new GridLayoutManager(StoryBoard.this, 2);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(gridLayoutManager);
        RecyclerAdapter mAdapter = new RecyclerAdapter(storyTitle,noteData,getApplicationContext(),this);
        recyclerView.setAdapter(mAdapter);

        //Allow for dragging and dropping of recycler view items
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mAdapter);
        itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createScene();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater= getMenuInflater();
        inflater.inflate(R.menu.storyboard_menu_items, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.new_card:
                createScene();
                break;

            case R.id.load_story:
                Intent loader = new Intent(getApplicationContext(),Load.class);
                startActivity(loader);
                break;

            case R.id.sort_by_subplot:
                Bundle bundle = new Bundle();
                bundle.putString("title", storyTitle);
                bundle.putString("Message", "StoryBoard");
                bundle.putInt("MaxNote", notePos);
                SortBySubplotFragment sortBySubplotFragment = new SortBySubplotFragment();
                sortBySubplotFragment.setArguments(bundle);
                sortBySubplotFragment.show(getFragmentManager(),"create_SBS_fragment");
                break;

            case R.id.sort_by_character:
                Bundle b = new Bundle();
                b.putString("title", storyTitle);
                b.putString("Message", "StoryBoard");
                b.putInt("MaxNote", notePos);
                SortByCharacterFragment sortByCharacter = new SortByCharacterFragment();
                sortByCharacter.setArguments(b);
                sortByCharacter.show(getFragmentManager(),"create_SBC_fragment");
                break;


            case R.id.delete_story:
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                alertDialog.setTitle("Delete Story");

                alertDialog
                        .setMessage("Press ok to delete" + storyTitle)
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent home = new Intent(getApplicationContext(), MainActivity.class);
                                noteManager.deleteTitle(storyTitle);
                                startActivity(home);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                AlertDialog ad = alertDialog.create();

                ad.show();
        }
        return true;
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        itemTouchHelper.startDrag(viewHolder);
    }

    public void createScene(){
        notePos+=1;

        Intent addNote = new Intent(getApplicationContext(), AddScene.class);
        addNote.putExtra("FileName",storyTitle);
        addNote.putExtra("Message","AddScene");
        addNote.putExtra("Position", notePos);

        startActivity(addNote);
    }

    public Note cursorToNote(Cursor c){
        //Gather note information from database
        String titleColumn = c.getString(c.getColumnIndex(Constants.COLUMN_SUBTITLE));
        String subPlotColumn = c.getString(c.getColumnIndex(Constants.COLUMN_SUBPLOT));
        String contentColumn = c.getString(c.getColumnIndex(Constants.COLUMN_CONTENT));
        long idColumn = c.getInt(c.getColumnIndex(Constants.COLUMN_ID));
        int posColumn = c.getInt(c.getColumnIndex(Constants.COLUMN_POSITION));

        //Set database information into note object
        Note note = new Note();
        note.setId(Long.toString(idColumn));
        note.setTitle(titleColumn);
        note.setSubPlot(subPlotColumn);
        note.setContent(contentColumn);
        note.setPos(posColumn);

        return note;
    }
}
