package com.example.jag27.sbv002;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.jag27.sbv002.utility.Constants;

/**
 * Created by jag27 on 4/5/2017.
 */

public class DatabaseHelper  extends SQLiteOpenHelper{
    private static final String DATABASE_NAME= "storyboard.db";
    private static int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_NOTE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXIST " + Constants.NOTES_TABLE);
        onCreate(sqLiteDatabase);
    }


    private static final String CREATE_TABLE_NOTE = "create table"
            + Constants.NOTES_TABLE
            +"("
            + Constants.COLUMN_ID + " interger primary key autoincrement, "
            + Constants.COLUMN_TITLE + "text not null,"
            + Constants.COLUMN_CONTENT + " text not null,"
            + Constants.COLUMN_MODIFIED_TIME + " integer not null,"
            + Constants.COLUMN_CREATED_TIME + " integer not null" + ")";
}
