package com.example.jag27.sbv002;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import com.example.jag27.sbv002.database.NoteManager;

public class AddScene extends AppCompatActivity {

    private int notePos;
    private String storyTitle;
    private EditText subTitleEditText;
    private EditText descEditText;

    private NoteManager noteManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Add Scene");
        setContentView(R.layout.activity_add_scene);

        storyTitle = getIntent().getStringExtra("FileName");
        notePos = getIntent().getIntExtra("Position",-1);

        //Set the font type
        Typeface courierFont = Typeface.createFromAsset(getAssets(), "fonts/courier.TTF");
        subTitleEditText = (EditText) findViewById(R.id.sceneTitleText);
        descEditText = (EditText) findViewById(R.id. sceneDescriptionText);
        subTitleEditText.setTypeface(courierFont);
        descEditText.setTypeface(courierFont);

        //Open DB
        noteManager = new NoteManager(this);
        noteManager.open();

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

                Pattern pattern = Pattern.compile("\\s*(\\S.*\\S)\\s*:\\s*(\\S.*\\S)\\s*");
                Matcher matcher = pattern.matcher(subTitle);
                if(matcher.find()){
                    noteManager.insert(storyTitle,matcher.group(1), matcher.group(2),desc, notePos);
                } else {
                    noteManager.insert(storyTitle,subTitle,desc, notePos);
                }

                Toast.makeText(this,"Saving Scene, Returning to Storyboard",
                        Toast.LENGTH_SHORT).show();

                Intent main = new Intent(getApplicationContext(),StoryBoard.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                main.putExtra("FileName",storyTitle);
                startActivity(main);
                break;

            case R.id.menu_delete:
                Intent back = new Intent(getApplicationContext(),StoryBoard.class);
                Toast.makeText(this,"Deleting Scene, Returning to Storyboard",
                        Toast.LENGTH_SHORT).show();
                back.putExtra("FileName",storyTitle);
                startActivity(back);
                break;
        }
        return true;
    }
}
