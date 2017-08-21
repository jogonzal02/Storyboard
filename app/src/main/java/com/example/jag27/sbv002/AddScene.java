package com.example.jag27.sbv002;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
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

import java.util.Random;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import com.example.jag27.sbv002.database.NoteManager;
import com.example.jag27.sbv002.utility.Constants;

public class AddScene extends AppCompatActivity {

    private int notePos;
    private long _id;
    private String storyTitle;
    private String subTitle;
    private String subPlot;
    private String message;
    private EditText subTitleEditText;
    private EditText descEditText;
    private EditText characterEditText;
    private NoteManager noteManager;


    // What I need in general
    //StoryTitle, Message
    //What I need for Add Scene
    //Card position
    //What I need Add Scene SBS & SBC
    //Subtitle, Character, Content, Position
    //What I need for Modify Scene
    //Id,SubTitle, Character, Content,
    //What I need Modify Scene SBS & SBC
    //Id,Subtitle, Character, Content,

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setTitle("Add Scene");
//        setContentView(R.layout.activity_add_scene);
//
//        subTitleEditText = (EditText) findViewById(R.id.sceneTitleText);
//        descEditText = (EditText) findViewById(R.id.sceneDescriptionText);
//        characterEditText = (EditText) findViewById(R.id.characterDescriptionText);
//
//        //Set the font type
//        Typeface courierFont = Typeface.createFromAsset(getAssets(), "fonts/courier.TTF");
//        subTitleEditText.setTypeface(courierFont);
//        descEditText.setTypeface(courierFont);
//        characterEditText.setTypeface(courierFont);
//
//        //Open DB
//        noteManager = new NoteManager(this);
//        noteManager.open();
//
//        storyTitle = getIntent().getStringExtra("FileName");
//        message = getIntent().getStringExtra("Message");
//        notePos = getIntent().getIntExtra("Position",-1);
//
//
//        //Distinguish between Adding a note object and Modifying a note object
//        if(message.equals("ModifyScene")) {
//            String id = getIntent().getStringExtra("ID");
//            String subTitle = getIntent().getStringExtra("SubTitle");
//            String content = getIntent().getStringExtra("Content");
//
//            _id = Long.parseLong(id);
//            subTitleEditText.setText(subTitle);
//            descEditText.setText(content);
//
//            String characterString = "";
//            Cursor bridgesCursor = noteManager.findBridgesByNoteId(_id);
//            long thresholdTime;
//
//            if(bridgesCursor.moveToFirst()) {
//
//                Cursor findCharacter;
//                thresholdTime = bridgesCursor.getLong(bridgesCursor.getColumnIndex(Constants.COLUMN_USED_TIME));
//                for (bridgesCursor.moveToFirst(); !bridgesCursor.isAfterLast(); bridgesCursor.moveToNext()) {
//                    long charID = bridgesCursor.getLong(bridgesCursor.getColumnIndex(Constants.COLUMN_CHARACTERID));
//                    long localTime = bridgesCursor.getLong(bridgesCursor.getColumnIndex(Constants.COLUMN_USED_TIME));
//
//                    //If USED_TIME value is less than threshold time value, delete charater from bridge table
//                    if(localTime < thresholdTime) {
//                        long bridgeID = bridgesCursor.getLong(bridgesCursor.getColumnIndex(Constants.COLUMN_ID));
//                        noteManager.deleteBridge(bridgeID);
//                    }
//
//                    //Else use Character Id to find character name and insert into Character String
//                    else {
//                        findCharacter = noteManager.findCharacterById(charID);
//                        String c = findCharacter.getString(findCharacter.getColumnIndex(Constants.COLUMN_CHARACTER));
//                        int color = findCharacter.getInt(findCharacter.getColumnIndex(Constants.COLUMN_COLOR));
//                        characterString += c + ",";
//                        findCharacter.close();
//                    }
//                }
//            }
//            bridgesCursor.close();
//
//            //Insert all names in charaterString into edit Text
//            characterEditText.setText(characterString);
//
//        } else if(message.equals("AddFromAddScene") || message.equals("ModifySceneFromSBC")) {
//            String subTitle = getIntent().getStringExtra("SubTitle");
//            String character = getIntent().getStringExtra("Characters");
//            String content = getIntent().getStringExtra("Content");
//
//            subTitleEditText.setText(subTitle);
//            characterEditText.setText(character);
//            descEditText.setText(content);
//        }
//
//        subTitleEditText.setOnLongClickListener(new View.OnLongClickListener() {
//
//            @Override
//            public boolean onLongClick(View view) {
//
//                Bundle bundle = new Bundle();
//                bundle.putString("Message","AddScene");
//                bundle.putString("title", storyTitle);
//                bundle.putString("Characters", characterEditText.getText().toString());
//                bundle.putString("Content", descEditText.getText().toString());
//                bundle.putInt("MaxNote", notePos);
//
//                if(message.equals("ModifyScene")){
//                    bundle.putLong("ID",_id);
//                }
//
//                SortBySubplotFragment sortBySubplotFragment = new SortBySubplotFragment();
//                sortBySubplotFragment.setArguments(bundle);
//                sortBySubplotFragment.show(getFragmentManager(),"create_SBS_fragment");
//
//                return true;
//            }
//        });
//
//        characterEditText.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View view) {
//
//                Bundle bundle = new Bundle();
//                bundle.putString("Message","AddScene");
//                bundle.putString("title", storyTitle);
//                bundle.putString("Subtitle", subTitleEditText.getText().toString());
//                bundle.putString("Character", characterEditText.getText().toString());
//
//
//                bundle.putString("Content", descEditText.getText().toString());
//                bundle.putInt("MaxNote", notePos);
//
//                if(message.equals("ModifyScene")){
//                    bundle.putLong("ID",_id);
//                }
//
//                SortByCharacterFragment sortByCharacterFragment = new SortByCharacterFragment();
//                sortByCharacterFragment.setArguments(bundle);
//                sortByCharacterFragment.show(getFragmentManager(),"create_SBC_fragment");
//                return true;
//            }
//        });
//
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Add Scene");
        setContentView(R.layout.activity_add_scene);

        subTitleEditText = (EditText) findViewById(R.id.sceneTitleText);
        descEditText = (EditText) findViewById(R.id.sceneDescriptionText);
        characterEditText = (EditText) findViewById(R.id.characterDescriptionText);

        //Set the font type
        Typeface courierFont = Typeface.createFromAsset(getAssets(), "fonts/courier.TTF");
        subTitleEditText.setTypeface(courierFont);
        descEditText.setTypeface(courierFont);
        characterEditText.setTypeface(courierFont);

        //Open DB
        noteManager = new NoteManager(this);
        noteManager.open();


        storyTitle = getIntent().getStringExtra("FileName");
        message = getIntent().getStringExtra("Message");
        notePos = getIntent().getIntExtra("Position",-1);


        if(getIntent().getExtras().containsKey("SubTitle")){//MOd, SBS, SBC
            String subTitle = getIntent().getStringExtra("SubTitle");
            String content = getIntent().getStringExtra("Content");
            subTitleEditText.setText(subTitle);
            descEditText.setText(content);

        }

        String character = null;
        if(getIntent().getExtras().containsKey("Characters")){ //SBS SBC
            character = getIntent().getStringExtra("Characters");
        }

        //Distinguish between Adding a note object and Modifying a note object
        if(message.equals("ModifyScene")) {
            String id = getIntent().getStringExtra("ID");

            _id = Long.parseLong(id);

            if(character == null){
                character = getCharacterFromDB();
            }
        }
        characterEditText.setText(character);


        subTitleEditText.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View view) {

                Bundle bundle = createBundle();

                SortBySubplotFragment sortBySubplotFragment = new SortBySubplotFragment();
                sortBySubplotFragment.setArguments(bundle);
                sortBySubplotFragment.show(getFragmentManager(),"create_SBS_fragment");

                return true;
            }
        });

        characterEditText.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                Bundle bundle = createBundle();

                SortByCharacterFragment sortByCharacterFragment = new SortByCharacterFragment();
                sortByCharacterFragment.setArguments(bundle);
                sortByCharacterFragment.show(getFragmentManager(),"create_SBC_fragment");
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
                subTitle = subTitleEditText.getText().toString();
                final String desc = descEditText.getText().toString();
                String subplot = null;

                //Regular expression to destinguish between the subtitle and subplot
                Pattern subPlotPattern = Pattern.compile("\\s*(\\S.*\\S)\\s*:\\s*(\\S.*\\S)\\s*");
                final Matcher subPlotMatcher = subPlotPattern.matcher(subTitle);
                if(subPlotMatcher.find()){
                    subTitle = subPlotMatcher.group(1);
                    subPlot = subPlotMatcher.group(2);
                }


                //Determines whether to update note table or insert a new row
                if(message.equals("ModifyScene")){
                    noteManager.update(_id,storyTitle,subTitle, subPlot,desc);

                }else {

                    noteManager.insert(storyTitle, subTitle, subPlot, desc, notePos);
                }

                insertOrUpdateCharacter();

                Toast.makeText(this,"Saving Scene, Returning to Storyboard",
                        Toast.LENGTH_SHORT).show();

                Intent main = new Intent(getApplicationContext(),StoryBoard.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                main.putExtra("FileName",storyTitle);
                startActivity(main);
                break;

            case R.id.menu_delete:

                if(message.equals("ModifyScene")|| message.equals("ModifySceneFromSBC")) noteManager.delete(_id);

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

    public Bundle createBundle(){
        Bundle bundle = new Bundle();
        bundle.putString("Message",message);
        bundle.putString("title", storyTitle);
        bundle.putString("Subtitle", subTitleEditText.getText().toString());
        bundle.putString("Character", characterEditText.getText().toString());
        bundle.putString("Content", descEditText.getText().toString());
        bundle.putInt("MaxNote", notePos);

        if(message.equals("ModifyScene")){
            bundle.putLong("ID",_id);
        }

        return bundle;
    }


    public String getCharacterFromDB(){

        String characterString = "";
        Cursor bridgesCursor = noteManager.findBridgesByNoteId(_id);
        long thresholdTime;

        if(bridgesCursor.moveToFirst()) {

            Cursor findCharacter;
            thresholdTime = bridgesCursor.getLong(bridgesCursor.getColumnIndex(Constants.COLUMN_USED_TIME));
            for (bridgesCursor.moveToFirst(); !bridgesCursor.isAfterLast(); bridgesCursor.moveToNext()) {
                long charID = bridgesCursor.getLong(bridgesCursor.getColumnIndex(Constants.COLUMN_CHARACTERID));
                long localTime = bridgesCursor.getLong(bridgesCursor.getColumnIndex(Constants.COLUMN_USED_TIME));

                //If USED_TIME value is less than threshold time value, delete charater from bridge table
                if(localTime < thresholdTime) {
                    long bridgeID = bridgesCursor.getLong(bridgesCursor.getColumnIndex(Constants.COLUMN_ID));
                    noteManager.deleteBridge(bridgeID);
                }

                //Else use Character Id to find character name and insert into Character String
                else {
                    findCharacter = noteManager.findCharacterById(charID);
                    String c = findCharacter.getString(findCharacter.getColumnIndex(Constants.COLUMN_CHARACTER));
                    int color = findCharacter.getInt(findCharacter.getColumnIndex(Constants.COLUMN_COLOR));
                    characterString += c + ",";
                    findCharacter.close();
                }
            }
        }
        bridgesCursor.close();

        //Insert all names in charaterString into edit Text
        return characterString;
    }



    public void insertOrUpdateCharacter(){

        Cursor noteTable = noteManager.findNote(storyTitle, subTitle, subPlot);
        long noteID = noteTable.getLong(noteTable.getColumnIndex(Constants.COLUMN_ID));

        String  characterString = characterEditText.getText().toString();
        String[] characters = characterString.split(",");

        long currentTime =  System.currentTimeMillis();

        for(String character: characters){
            character = character.trim();
            if(character.isEmpty()) continue;

            //Find if character exist in Character table
            Cursor characterTable = noteManager.findCharacter(storyTitle, character);

            //If exist(grab character ID): check if character id is paired with note id
            if(characterTable.moveToFirst()){
                long characterID = characterTable.getLong(characterTable.getColumnIndex(Constants.COLUMN_ID));
                Cursor bridgeTable = noteManager.findBridge(noteID, characterID);

                //If  doesn't exist: create pairing
                if(!bridgeTable.moveToFirst()){
                    noteManager.insertBridge(noteID, characterID,currentTime);

                }
                //else it exist: update bridge
                else {
                    long  bridgeId = bridgeTable.getLong(bridgeTable.getColumnIndex(Constants.COLUMN_ID));
                    noteManager.updateBridge(bridgeId, currentTime);

                }
            }
            //Else: Insert Character in table & create pairing
            else{
                Random random = new Random();
                //Create random color
                int color = Color.argb(255,random.nextInt(256),
                        random.nextInt(256),random.nextInt(256));

                noteManager.insertCharacter(storyTitle,character,color);
                characterTable = noteManager.findCharacter(storyTitle, character);
                long characterID = characterTable.getLong(characterTable.getColumnIndex(Constants.COLUMN_ID));
                noteManager.insertBridge(noteID, characterID, currentTime);

            }

        }
    }

}
