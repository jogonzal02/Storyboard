package com.example.jag27.sbv002;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.ClipData;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.animation.ValueAnimatorCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.DragEvent;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.jag27.sbv002.database.NoteManager;
import com.example.jag27.sbv002.utility.Constants;

import java.util.concurrent.atomic.AtomicBoolean;

public class StoryBoard extends AppCompatActivity {
    private String storyTitle;
    private GridView mGrid;
    private ListView mList;
    private NestedScrollView mScrollView;
    private ValueAnimator mAnimator;
    private NoteManager noteManager;
    private SimpleCursorAdapter adapter;
    private AtomicBoolean mIsScrolling = new AtomicBoolean(false);

    final String[] from = new String[]{Constants.COLUMN_ID,
            Constants.COLUMN_SUBTITLE, Constants.COLUMN_CONTENT};

    final int[] to = new int[]{R.id.id,R.id.subtitle,R.id.content};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_board);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        storyTitle = getIntent().getStringExtra("FileName");
        setTitle(storyTitle);

        noteManager = new NoteManager(this);
        noteManager.open();
        Cursor cursor = noteManager.fetch(storyTitle);

//        mList = (ListView) findViewById(R.id.list_view);
        //mList.setEmptyView(findViewById(R.id.empty));
        mGrid = (GridView) findViewById(R.id.grid_view);
        mGrid.setEmptyView(findViewById(R.id.empty));





        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addNote = new Intent(getApplicationContext(), AddScene.class);
                addNote.putExtra("FileName",storyTitle);
                startActivity(addNote);
            }
        });


        adapter = new SimpleCursorAdapter(this,R.layout.activity_view_note,cursor,
                from,to,0);
        adapter.notifyDataSetChanged();

        mGrid.setAdapter(adapter);

        mGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView idTextView = (TextView) view.findViewById(R.id.id);
                TextView subTitleTextView = (TextView) view.findViewById(R.id.subtitle);
                TextView contentTextView = (TextView) view.findViewById(R.id.content);

                String id = idTextView.getText().toString();
                String subTitle = subTitleTextView.getText().toString();
                String content = contentTextView.getText().toString();

                Intent modify_intent = new Intent(getApplicationContext(),ModifyScene.class);
                modify_intent.putExtra("FileName",storyTitle);
                modify_intent.putExtra("SubTitle",subTitle);
                modify_intent.putExtra("Content",content);
                modify_intent.putExtra("ID",id);
                startActivity(modify_intent);
            }
        });

    }

}
