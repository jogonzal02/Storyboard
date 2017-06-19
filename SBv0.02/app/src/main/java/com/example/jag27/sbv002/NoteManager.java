package com.example.jag27.sbv002;

/**
 * Created by jag27 on 4/9/2017.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.example.jag27.sbv002.Note;
import com.example.jag27.sbv002.utility.Constants;

import java.util.ArrayList;
import java.util.List;

public class NoteManager {

    private SQLiteDatabase database;
    private DatabaseHelper dbhelper;
    private Context mContext;
    private static NoteManager noteManagerInstance = null;

    public static NoteManager newInstance(Context context){
        if(noteManagerInstance == null){
            noteManagerInstance = new NoteManager(context.getApplicationContext());
        }
        return noteManagerInstance;
    }

    private NoteManager(Context context){
        this.mContext = context.getApplicationContext();
    }
    //CRUD
    public long create(Note note){
        ContentValues values = new ContentValues();
        values.put(Constants.COLUMN_TITLE,note.getTitle());
        values.put(Constants.COLUMN_CONTENT,note.getContent());
        values.put(Constants.COLUMN_CREATED_TIME,System.currentTimeMillis());
        values.put(Constants.COLUMN_MODIFIED_TIME,System.currentTimeMillis());
        Uri result  = mContext.getContentResolver().insert(NoteContentProvider.CONTENT_URI,values);
        long id  = Long.parseLong(result.getLastPathSegment());
        return id;
    }

    public void update(Note note){
        ContentValues values = new ContentValues();
        values.put(Constants.COLUMN_TITLE,note.getTitle());
        values.put(Constants.COLUMN_CONTENT,note.getContent());
        values.put(Constants.COLUMN_CREATED_TIME,System.currentTimeMillis());
        values.put(Constants.COLUMN_MODIFIED_TIME,System.currentTimeMillis());
        mContext.getContentResolver().update(NoteContentProvider.CONTENT_URI,
                values, Constants.COLUMN_ID + "=" + note.getId(),null);

    }

    public void delete(Note note){
        mContext.getContentResolver().delete(
                NoteContentProvider.CONTENT_URI,Constants.COLUMN_ID + "=" + note.getId(),null);
    }

    public void open() throws SQLException{
        database = dbhelper.getWritableDatabase();
    }

    private void close(){
        dbhelper.close();
    }

    public List<Note> getAllNotes(){
        List<Note> notes = new ArrayList<Note>();

        Cursor cursor = mContext.getContentResolver().query(NoteContentProvider.CONTENT_URI,
                Constants.COLUMNS,null,null,null);
        if (cursor != null){
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                notes.add(Note.getNoteFromCursor(cursor));
                cursor.moveToNext();
            }
            cursor.close();
        }
        return notes;
    }

    public Note getNote(Long id){
        Note note;
        Cursor cursor = mContext.getContentResolver().query(NoteContentProvider.CONTENT_URI,
                Constants.COLUMNS, Constants.COLUMN_ID + "=" + id, null, null);
        if (cursor != null){
            cursor.moveToFirst();
            note = Note.getNoteFromCursor(cursor);
            return note;
        }
        return null;
    }






}
