package com.example.jag27.sbv002;

import android.content.Intent;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.jag27.sbv002.database.NoteManager;
import com.example.jag27.sbv002.utility.Constants;

public class Load extends AppCompatActivity {
    private NoteManager noteManager;
    private ListView mList;
    private SimpleCursorAdapter adapter;

    final String[] from = new String[]{Constants.COLUMN_TITLE};
    final int[] to = new int[]{R.id.storyTitle};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);

        noteManager = new NoteManager(this);
        noteManager.open();
        Cursor cursor = noteManager.fetchStories();

        mList = (ListView) findViewById(R.id.list_view);

        adapter = new SimpleCursorAdapter(this,R.layout.activity_view_story,cursor,
                from,to,0);
        adapter.notifyDataSetChanged();

        mList.setAdapter(adapter);

        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView titleText = (TextView) view.findViewById(R.id.storyTitle);
                String title = titleText.getText().toString();

                Intent intent = new Intent(getApplicationContext(), StoryBoard.class);
                intent.putExtra("FileName",title);
                startActivity(intent);
            }
        });


    }
}
