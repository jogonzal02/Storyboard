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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.jag27.sbv002.database.NoteManager;
import com.example.jag27.sbv002.utility.Constants;
import com.example.jag27.sbv002.utility.OnStartDragListener;
import com.example.jag27.sbv002.utility.SimpleItemTouchHelperCallback;
import com.example.jag27.sbv002.view.RecyclerAdapter;

import java.util.ArrayList;


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
        notePos = 0;

        storyTitle = getIntent().getStringExtra("FileName");
        subPlot = getIntent().getStringExtra("SubTitle");
        setTitle(storyTitle);

        //Get note data from database, if story title previously created
        noteManager = new NoteManager(this);
        noteManager.open();
        Cursor cursor;
        if (subPlot == null)cursor = noteManager.fetch(storyTitle);
        else {
            cursor = noteManager.fetchStoriesSubPlot(storyTitle, subPlot);
            notePos = getIntent().getIntExtra("MaxNote",notePos);
        }

        //Get the position of the last note
        if(cursor.moveToFirst() && subPlot== null){
            Cursor lastCursor = cursor;
            lastCursor.moveToLast();
            notePos = lastCursor.getInt(lastCursor.getColumnIndex(Constants.COLUMN_POSITION));

        }

        //Place all note data into a array list
        ArrayList noteData = new ArrayList<>();
        for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
            String titleColumn = cursor.getString(cursor.getColumnIndex(Constants.COLUMN_SUBTITLE));
            String subPlotColumn = cursor.getString(cursor.getColumnIndex(Constants.COLUMN_SUBPLOT));
            String contentColumn = cursor.getString(cursor.getColumnIndex(Constants.COLUMN_CONTENT));
            int idColumn = cursor.getInt(cursor.getColumnIndex(Constants.COLUMN_ID));
            int posColumn = cursor.getInt(cursor.getColumnIndex(Constants.COLUMN_POSITION));

            Note note = new Note();
            note.setId(Integer.toString(idColumn));
            note.setTitle(titleColumn);
            note.setSubPlot(subPlotColumn);
            note.setContent(contentColumn);
            note.setPos(posColumn);
            noteData.add(note);
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
                bundle.putInt("MaxNote", notePos);
                SortBySubplotFragment sortBySubplotFragment = new SortBySubplotFragment();
                sortBySubplotFragment.setArguments(bundle);
                sortBySubplotFragment.show(getFragmentManager(),"create_SBS_fragment");
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
        addNote.putExtra("Position", notePos);

        startActivity(addNote);
    }
}
