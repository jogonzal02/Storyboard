package com.example.jag27.sbv002;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import com.example.jag27.sbv002.database.NoteManager;

public class AddScene extends AppCompatActivity {

    private int notePos;
    private long _id;
    private String storyTitle;
    private String message;
    private EditText subTitleEditText;
    private EditText descEditText;

    private NoteManager noteManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Add Scene");
        setContentView(R.layout.activity_add_scene);

        //Set the font type
        Typeface courierFont = Typeface.createFromAsset(getAssets(), "fonts/courier.TTF");
        subTitleEditText = (EditText) findViewById(R.id.sceneTitleText);
        descEditText = (EditText) findViewById(R.id. sceneDescriptionText);
        subTitleEditText.setTypeface(courierFont);
        descEditText.setTypeface(courierFont);

        //Open DB
        noteManager = new NoteManager(this);
        noteManager.open();

        storyTitle = getIntent().getStringExtra("FileName");
        message = getIntent().getStringExtra("Message");
        notePos = getIntent().getIntExtra("Position",-1);

        //destinguish between Adding a note object and Modifying a note object
        if(message.equals("ModifyScene")) {
            String id = getIntent().getStringExtra("ID");
            String subTitle = getIntent().getStringExtra("SubTitle");
            String content = getIntent().getStringExtra("Content");

            _id = Long.parseLong(id);
            subTitleEditText.setText(subTitle);
            descEditText.setText(content);

        } else if(message.equals("AddFromAddScene")) {
            String subTitle = getIntent().getStringExtra("SubTitle");
            String content = getIntent().getStringExtra("Content");

            subTitleEditText.setText(subTitle);
            descEditText.setText(content);

        }

        subTitleEditText.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View view) {


                Bundle bundle = new Bundle();
                bundle.putString("Message","AddScene");
                bundle.putString("title", storyTitle);
                bundle.putString("Content", descEditText.getText().toString());
                bundle.putInt("MaxNote", notePos);

                if(message.equals("ModifyScene")){
                    bundle.putLong("ID",_id);
                }

                SortBySubplotFragment sortBySubplotFragment = new SortBySubplotFragment();
                sortBySubplotFragment.setArguments(bundle);
                sortBySubplotFragment.show(getFragmentManager(),"create_SBS_fragment");

                return true;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_item,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_send:
                final String subTitle = subTitleEditText.getText().toString();
                final String desc = descEditText.getText().toString();

                //Regular expression to destinguish between the subtitle and subplot
                Pattern pattern = Pattern.compile("\\s*(\\S.*\\S)\\s*:\\s*(\\S.*\\S)\\s*");
                Matcher matcher = pattern.matcher(subTitle);

                //Determines whether to update database or insert a new row into
                if(message.equals("ModifyScene")){
                    if(matcher.find()){
                        noteManager.update(_id,storyTitle,matcher.group(1), matcher.group(2),desc);
                    } else {
                        noteManager.update(_id,storyTitle,subTitle,desc);
                    }
                }else {
                    if (matcher.find()) {
                        noteManager.insert(storyTitle, matcher.group(1), matcher.group(2), desc, notePos);
                    } else {
                        noteManager.insert(storyTitle, subTitle, desc, notePos);
                    }
                }

                Toast.makeText(this,"Saving Scene, Returning to Storyboard",
                        Toast.LENGTH_SHORT).show();

                Intent main = new Intent(getApplicationContext(),StoryBoard.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                main.putExtra("FileName",storyTitle);
                startActivity(main);
                break;

            case R.id.menu_delete:

                if(message.equals("ModifyScene")) noteManager.delete(_id);

                Intent back = new Intent(getApplicationContext(),StoryBoard.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                Toast.makeText(this,"Deleting Scene, Returning to Storyboard",
                        Toast.LENGTH_SHORT).show();
                back.putExtra("FileName",storyTitle);
                startActivity(back);
                break;
        }
        return true;
    }
}
