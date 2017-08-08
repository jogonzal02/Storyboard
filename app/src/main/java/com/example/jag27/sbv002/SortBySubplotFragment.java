package com.example.jag27.sbv002;

import android.app.DialogFragment;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.jag27.sbv002.database.NoteManager;
import com.example.jag27.sbv002.utility.Constants;


public class SortBySubplotFragment extends DialogFragment {
    private String storyTitle;

    final String[] from = new String[]{Constants.COLUMN_SUBTITLE};
    final int[] to = new int[]{R.id.storyTitle};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.sort_by_subplot_fragment,container,false);
        storyTitle = getArguments().getString("title");

        NoteManager noteManager = new NoteManager(getActivity());
        noteManager.open();
        Cursor cursor = noteManager.fetchSubplots(storyTitle);
        ListView mListView = (ListView) root.findViewById(R.id.SBSListView);

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(getActivity(),R.layout.activity_view_story,cursor,
                from,to,0);
        adapter.notifyDataSetChanged();

        mListView.setAdapter(adapter);

        return root;
    }
}
