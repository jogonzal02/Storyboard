package com.example.jag27.sbv002.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.jag27.sbv002.utility.Constants;

public class DatabaseHelper extends SQLiteOpenHelper{

    //Database info and version
    private static final String DATABASE_NAME = "storyboard.db";
    private static final int DATABASE_VERSION = 6;


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL(CREATE_NOTE);
        sqLiteDatabase.execSQL(CREATE_CHARACTER);
        sqLiteDatabase.execSQL(CREATE_BRIDGE);
    }

    private static final String DATABASE_ALTER_NOTE_1 = "ALTER TABLE " + Constants.NOTES_TABLE + " ADD COLUMN " +
            Constants.COLUMN_SUBPLOT + " TEXT";

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        if(i < 4) sqLiteDatabase.execSQL(DATABASE_ALTER_NOTE_1);
        if(i < 5) sqLiteDatabase.execSQL(CREATE_CHARACTER);
        if(i < 6) sqLiteDatabase.execSQL(CREATE_BRIDGE);
    }

    //Creating tables
    private static final String CREATE_NOTE = "create table "
            + Constants.NOTES_TABLE
            + "("
            + Constants.COLUMN_ID + " integer primary key autoincrement, "
            + Constants.COLUMN_TITLE + " text not null, "
            + Constants.COLUMN_SUBTITLE + " text not null, "
            + Constants.COLUMN_SUBPLOT + " text, "
            + Constants.COLUMN_CONTENT + " text not null, "
            + Constants.COLUMN_POSITION + " integer not null, "
            + Constants.COLUMN_MODIFIED_TIME + " integer not null, "
            + Constants.COLUMN_CREATED_TIME + " integer not null " + ")";

    private static final String CREATE_CHARACTER = "create table "
            + Constants.CHARACTER_TABLE
            + "("
            + Constants.COLUMN_ID + " integer primary key autoincrement, "
            + Constants.COLUMN_TITLE + " text not null, "
            + Constants.COLUMN_CHARACTER + " text not null, "
            + Constants.COLUMN_COLOR + " integer not null " + ")";

    private static final String CREATE_BRIDGE = "create table "
            + Constants.BRIDGE_TABLE
            +"("
            + Constants.COLUMN_ID + " integer primary key autoincrement, "
            + Constants.COLUMN_NOTEID + " integer not null, "
            + Constants.COLUMN_CHARACTERID + " integer not null " + ")";

}
