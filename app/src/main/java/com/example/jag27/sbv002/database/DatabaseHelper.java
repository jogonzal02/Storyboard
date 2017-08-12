package com.example.jag27.sbv002.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.jag27.sbv002.utility.Constants;

public class DatabaseHelper extends SQLiteOpenHelper{

    //Database info and version
    private static final String DATABASE_NAME = "storyboard.db";
    private static final int DATABASE_VERSION = 4;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_NOTE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        String sql = "ALTER TABLE " + Constants.NOTES_TABLE + " ADD COLUMN " +
                Constants.COLUMN_SUBPLOT + " TEXT";
        sqLiteDatabase.execSQL(sql);


    }

    //Creating table query
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

}
