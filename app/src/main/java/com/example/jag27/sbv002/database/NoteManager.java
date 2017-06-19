package com.example.jag27.sbv002.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import com.example.jag27.sbv002.utility.Constants;

public class NoteManager {

    //Database fields
    private SQLiteDatabase database;
    private Context context;

    public NoteManager(Context c){
        context = c;
    }

    public NoteManager open() throws SQLException{
        DatabaseHelper dbhelper = new DatabaseHelper(context);
        database = dbhelper.getWritableDatabase();
        return this;
    }

//    public void close(){
//        dbhelper.close();
//    }

    public void insert(String title, String subTitle, String content, int position){
        ContentValues contentValues = new ContentValues();
        contentValues.put(Constants.COLUMN_TITLE, title);
        contentValues.put(Constants.COLUMN_SUBTITLE, subTitle);
        contentValues.put(Constants.COLUMN_CONTENT, content);
        contentValues.put(Constants.COLUMN_POSITION, position);
        contentValues.put(Constants.COLUMN_MODIFIED_TIME, System.currentTimeMillis());
        contentValues.put(Constants.COLUMN_CREATED_TIME, System.currentTimeMillis());

        database.insert(Constants.NOTES_TABLE, null, contentValues);
    }

    public void delete(long _id){
        database.delete(Constants.NOTES_TABLE, Constants.COLUMN_ID + "=" + _id, null);
    }

    public int updatePos(int pos,long _id){
        ContentValues contentValues = new ContentValues();
        contentValues.put(Constants.COLUMN_POSITION,pos);

//        int i = database.update(Constants.NOTES_TABLE, contentValues,
//                Constants.COLUMN_ID + "=" + _id, null);

        return database.update(Constants.NOTES_TABLE, contentValues,
                Constants.COLUMN_ID + "=" + _id, null);

    }

    public int update(long _id, String title, String subTitle, String content){
        ContentValues contentValues = new ContentValues();
        contentValues.put(Constants.COLUMN_TITLE, title);
        contentValues.put(Constants.COLUMN_SUBTITLE, subTitle);
        contentValues.put(Constants.COLUMN_CONTENT, content);
        contentValues.put(Constants.COLUMN_MODIFIED_TIME, System.currentTimeMillis());

//        int i = database.update(Constants.NOTES_TABLE, contentValues,
//                Constants.COLUMN_ID + "=" + _id, null);
        return database.update(Constants.NOTES_TABLE, contentValues,
                Constants.COLUMN_ID + "=" + _id, null);
    }

    //Grab database rows with "title" in the COLUMN_TITLE
    public Cursor fetch(String title){
        String[] columns = new String[] { Constants.COLUMN_ID, Constants.COLUMN_TITLE,
                Constants.COLUMN_TITLE, Constants.COLUMN_SUBTITLE, Constants.COLUMN_CONTENT,Constants.COLUMN_POSITION,
                Constants.COLUMN_MODIFIED_TIME, Constants.COLUMN_CREATED_TIME };

        String whereClause = Constants.COLUMN_TITLE + " = ?";
        String[] whereArgs =  new String[]{title};
        Cursor cursor = database.query(Constants.NOTES_TABLE, columns, whereClause, whereArgs,
                null, null, Constants.COLUMN_POSITION);


        if (cursor != null){
            cursor.moveToFirst();
        }
        return cursor;
    }

    // Grab all stories in COLUMN_TITLE from the database
    public Cursor fetchStories(){
        String[] columns = new String[] { Constants.COLUMN_ID, Constants.COLUMN_TITLE,
                Constants.COLUMN_TITLE, Constants.COLUMN_SUBTITLE, Constants.COLUMN_CONTENT,
                Constants.COLUMN_MODIFIED_TIME, Constants.COLUMN_CREATED_TIME };

        Cursor cursor = database.query(Constants.NOTES_TABLE,columns, null, null,
                Constants.COLUMN_TITLE, null, null);

        if(cursor != null){
            cursor.moveToFirst();

        }
        return cursor;

    }

    public void deleteTitle(String title){
        database.delete(Constants.NOTES_TABLE ,Constants.COLUMN_TITLE + "=?", new String[]{title});
    }










}
