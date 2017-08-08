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

public class ModifyScene extends AppCompatActivity {

    private String storyTitle;
    private EditText subTitleText;
    private EditText contentText;
    private long _id;

    private NoteManager noteManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.NewAppTheme);
        setContentView(R.layout.activity_modify_scene);

        setTitle("Modify Scene");

        //Open DB
        noteManager = new NoteManager(this);
        noteManager.open();

        //Set Font type
        Typeface courierFont = Typeface.createFromAsset(getAssets(), "fonts/courier.TTF");
        subTitleText = (EditText) findViewById(R.id.modSceneTitleText);
        contentText = (EditText) findViewById(R.id.modSceneDescriptionText);
        subTitleText.setTypeface(courierFont);
        contentText.setTypeface(courierFont);

        Intent intent = getIntent();
        storyTitle = intent.getStringExtra("FileName");
        String id = intent.getStringExtra("ID");
        String subTitle = intent.getStringExtra("SubTitle");
        String content = intent.getStringExtra("Content");

        _id = Long.parseLong(id);
        subTitleText.setText(subTitle);
        contentText.setText(content);


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
                final String subTitle = subTitleText.getText().toString();
                final String desc = contentText.getText().toString();

                Pattern pattern = Pattern.compile("(.*)\\s*:\\s*(.*)");
                Matcher matcher = pattern.matcher(subTitle);
                if(matcher.find()){
                    noteManager.update(_id,storyTitle,matcher.group(1), matcher.group(2),desc);
                } else {
                    noteManager.update(_id,storyTitle,subTitle,desc);
                }

                Toast.makeText(this,"Saving Scene, Returning to Storyboard",
                        Toast.LENGTH_SHORT).show();

                //noteManager.update(_id,storyTitle,subTitle,desc);

                Intent main = new Intent(getApplicationContext(),StoryBoard.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                main.putExtra("FileName",storyTitle);
                startActivity(main);
                break;

            case R.id.menu_delete:

                noteManager.delete(_id);
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


