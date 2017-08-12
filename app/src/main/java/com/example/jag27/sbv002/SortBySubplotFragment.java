package com.example.jag27.sbv002;

import android.app.DialogFragment;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.jag27.sbv002.database.NoteManager;
import com.example.jag27.sbv002.utility.Constants;


public class SortBySubplotFragment extends DialogFragment {
    private String storyTitle;
    private String message;
    private String content;
    private int maxNote;

    final String[] from = new String[]{Constants.COLUMN_SUBTITLE};
    final int[] to = new int[]{R.id.storyTitle};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.sort_by_subplot_fragment,container,false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        //Get values from bundle
        storyTitle = getArguments().getString("title");
        message = getArguments().getString("Message");
        maxNote = getArguments().getInt("MaxNote");

        //Get and open DB
        NoteManager noteManager = new NoteManager(getActivity());
        noteManager.open();

        //Gather all subtitle that are recognized as a subplot and insert onto ListView
        Cursor cursor = noteManager.fetchSubplots(storyTitle);
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(getActivity(),R.layout.activity_view_story,cursor,
                from,to,0);
        adapter.notifyDataSetChanged();
        ListView mListView = (ListView) root.findViewById(R.id.SBSListView);
        mListView.setAdapter(adapter);

        //Launch activity base on the message value and send necessary information
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView subTitleText = (TextView) view.findViewById(R.id.storyTitle);
                String subTitle = subTitleText.getText().toString();

                Intent intent;
                if (message.equals("StoryBoard")){
                    intent = new Intent(getActivity(), StoryBoard.class);
                    intent.putExtra("SubTitle",subTitle);
                    intent.putExtra("MaxNote", maxNote);
                }
                else {
                    intent = new Intent(getActivity(), AddScene.class);

                    if(getArguments().containsKey("ID")){
                        long temp = getArguments().getLong("ID");
                        intent.putExtra("Message", "ModifyScene");
                        intent.putExtra("ID", Long.toString(temp));
                    }
                    else intent.putExtra("Message", "AddFromAddScene");

                    intent.putExtra("SubTitle",subTitle+":");
                    intent.putExtra("Content", getArguments().getString("Content"));
                    intent.putExtra("Position", maxNote);

                }

                intent.putExtra("FileName",storyTitle);



                startActivity(intent);
            }
        });

        TextView noneText = (TextView) root.findViewById(R.id.SBSTextView);
        if (message.equals("AddScene")){
            noneText.setVisibility(root.GONE);
        }else {
            noneText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), StoryBoard.class);
                    intent.putExtra("FileName", storyTitle);
                    startActivity(intent);
                }
            });
        }
        return root;
    }
}
