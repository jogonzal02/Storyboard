package com.example.jag27.sbv002.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.renderscript.Long2;

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

//-------------------------------------NOTE TABLE FUNCTIONS----------------------------------------

    public Cursor findNote(String storyTitle, String subtitle, String subPlot){
        String where;
        String[] whereArgs;
        String[] columns = new String[]{Constants.COLUMN_ID};

        if(subPlot != null) {
            where = Constants.COLUMN_TITLE + " =? AND " +
                    Constants.COLUMN_SUBTITLE + " =? AND " +
                    Constants.COLUMN_SUBPLOT + " =?";

            whereArgs = new String[]{storyTitle, subtitle, subPlot};
        }else{
            where = Constants.COLUMN_TITLE + " =? AND " +
                    Constants.COLUMN_SUBTITLE + " =?";

            whereArgs = new String[]{storyTitle, subtitle};
        }
        Cursor cursor = database.query(Constants.NOTES_TABLE, columns, where, whereArgs,
                null, null, null);

        if(cursor != null){
            cursor.moveToFirst();
        }
        return cursor;
    }

    public Cursor findNoteById(Long noteId){
        String where;
        String[] whereArgs;
        String[] columns = new String[]{ Constants.COLUMN_ID, Constants.COLUMN_TITLE,
                Constants.COLUMN_SUBTITLE, Constants.COLUMN_SUBPLOT, Constants.COLUMN_CONTENT,Constants.COLUMN_POSITION,
                Constants.COLUMN_MODIFIED_TIME, Constants.COLUMN_CREATED_TIME };

            where = Constants.COLUMN_ID + " =?";

            whereArgs = new String[]{Long.toString(noteId)};

        Cursor cursor = database.query(Constants.NOTES_TABLE, columns, where, whereArgs,
                null, null, null);

        if(cursor != null){
            cursor.moveToFirst();
        }
        return cursor;
    }

    //Grab database rows with "title" in the COLUMN_TITLE
    public Cursor fetch(String title){
        String[] columns = new String[] { Constants.COLUMN_ID, Constants.COLUMN_TITLE,
                Constants.COLUMN_SUBTITLE, Constants.COLUMN_SUBPLOT, Constants.COLUMN_CONTENT,Constants.COLUMN_POSITION,
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

    public Cursor fetchSubplots(String title){
        String[] columns = new String[] { Constants.COLUMN_ID, Constants.COLUMN_TITLE,
                Constants.COLUMN_TITLE, Constants.COLUMN_SUBTITLE, Constants.COLUMN_CONTENT,
                Constants.COLUMN_MODIFIED_TIME, Constants.COLUMN_CREATED_TIME };

        String where = Constants.COLUMN_TITLE + " = ? AND " +
                Constants.COLUMN_SUBPLOT + " IS NOT NULL";
        String[] whereArgs = new String[]{title};

        Cursor cursor = database.query(Constants.NOTES_TABLE,columns, where, whereArgs,
                Constants.COLUMN_SUBTITLE, null, null);

        if(cursor != null){
            cursor.moveToFirst();

        }
        return cursor;

    }

    public Cursor fetchStoriesSubPlot(String title, String subPlot){
        String[] columns = new String[] { Constants.COLUMN_ID, Constants.COLUMN_TITLE,
                Constants.COLUMN_TITLE, Constants.COLUMN_SUBTITLE, Constants.COLUMN_SUBPLOT, Constants.COLUMN_CONTENT,Constants.COLUMN_POSITION,
                Constants.COLUMN_MODIFIED_TIME, Constants.COLUMN_CREATED_TIME };

        String where = Constants.COLUMN_TITLE + " = ? AND " +
                Constants.COLUMN_SUBTITLE + " = ?";
        String[] whereArgs = new String[]{title,subPlot};

        Cursor cursor = database.query(Constants.NOTES_TABLE,columns, where, whereArgs,
                null, null, Constants.COLUMN_POSITION);

        if(cursor != null){
            cursor.moveToFirst();

        }
        return cursor;
    }

    public Cursor fetchAfterPosition(String title,int position){
        String[] columns = new String[]{Constants.COLUMN_ID,Constants.COLUMN_POSITION};
        String where = Constants.COLUMN_TITLE + " = ? AND " +
                Constants.COLUMN_POSITION + " > ?";
        String[] whereArgs = new String[] {title,Integer.toString(position)};

        Cursor cursor = database.query(Constants.NOTES_TABLE,columns, where,whereArgs,
                null,null,null);

        if(cursor != null){
            cursor.moveToFirst();

        }
        return cursor;
    }

    public void insert(String title, String subTitle,String content, int position){
        ContentValues contentValues = new ContentValues();
        contentValues.put(Constants.COLUMN_TITLE, title);
        contentValues.put(Constants.COLUMN_SUBTITLE, subTitle);
        contentValues.put(Constants.COLUMN_CONTENT, content);
        contentValues.put(Constants.COLUMN_POSITION, position);
        contentValues.put(Constants.COLUMN_MODIFIED_TIME, System.currentTimeMillis());
        contentValues.put(Constants.COLUMN_CREATED_TIME, System.currentTimeMillis());

        database.insert(Constants.NOTES_TABLE, null, contentValues);
    }

    public void insert(String title, String subTitle, String subPlot,String content, int position){
        ContentValues contentValues = new ContentValues();
        contentValues.put(Constants.COLUMN_TITLE, title);
        contentValues.put(Constants.COLUMN_SUBTITLE, subTitle);
        contentValues.put(Constants.COLUMN_SUBPLOT,subPlot);
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

        return database.update(Constants.NOTES_TABLE, contentValues,
                Constants.COLUMN_ID + "=" + _id, null);
    }

    public int update(long _id, String title, String subTitle, String content){
        ContentValues contentValues = new ContentValues();
        contentValues.put(Constants.COLUMN_TITLE, title);
        contentValues.put(Constants.COLUMN_SUBTITLE, subTitle);
        contentValues.put(Constants.COLUMN_CONTENT, content);
        contentValues.put(Constants.COLUMN_MODIFIED_TIME, System.currentTimeMillis());

        return database.update(Constants.NOTES_TABLE, contentValues,
                Constants.COLUMN_ID + "=" + _id, null);
    }

    public int update(long _id, String title,String subTitle,String subPlot , String content){
        ContentValues contentValues = new ContentValues();
        contentValues.put(Constants.COLUMN_TITLE, title);
        contentValues.put(Constants.COLUMN_SUBTITLE, subTitle);
        contentValues.put(Constants.COLUMN_SUBPLOT,subPlot);
        contentValues.put(Constants.COLUMN_CONTENT, content);
        contentValues.put(Constants.COLUMN_MODIFIED_TIME, System.currentTimeMillis());

        return database.update(Constants.NOTES_TABLE, contentValues,
                Constants.COLUMN_ID + "=" + _id, null);
    }

    public void deleteTitle(String title){
        database.delete(Constants.NOTES_TABLE ,Constants.COLUMN_TITLE + "=?", new String[]{title});
    }

//-------------------------------------CHARACTER TABLE FUNCTIONS------------------------------------

    public Cursor findCharacter(String title,String character){
        String[] columns = new String[]{Constants.COLUMN_ID};

        String where = Constants.COLUMN_TITLE + " =? AND " +
                Constants.COLUMN_CHARACTER + " =?";
        String [] whereArgs = new String[]{title, character};

        Cursor cursor = database.query(Constants.CHARACTER_TABLE,columns, where, whereArgs,
                null, null, null);

        if(cursor != null){
            cursor.moveToFirst();

        }
        return cursor;

    }

    public Cursor findCharacterById(long id){
        String[] columns = new String[]{Constants.COLUMN_CHARACTER, Constants.COLUMN_COLOR};

        String where = Constants.COLUMN_ID + " =?";
        String[] whereArgs = new String[]{Long.toString(id)};

        Cursor cursor = database.query(Constants.CHARACTER_TABLE,columns, where, whereArgs,
                null, null, null);

        if(cursor != null){
            cursor.moveToFirst();

        }
        return cursor;

    }

    public Cursor fetchCharacters(String title){
        String[] columns = new String[] {Constants.COLUMN_ID,Constants.COLUMN_CHARACTER,Constants.COLUMN_COLOR};

        String where = Constants.COLUMN_TITLE + " =?";
        String[] whereArgs = new String[]{title};

        Cursor cursor = database.query(Constants.CHARACTER_TABLE, columns, where, whereArgs,
                null, null, null);

        if(cursor != null){
            cursor.moveToFirst();

        }
        return cursor;
    }

    public void insertCharacter(String title, String character, int color){
        ContentValues contentValues = new ContentValues();
        contentValues.put(Constants.COLUMN_TITLE, title);
        contentValues.put(Constants.COLUMN_CHARACTER, character);
        contentValues.put(Constants.COLUMN_COLOR, color);


        database.insert(Constants.CHARACTER_TABLE, null, contentValues);
    }

//-------------------------------------BRIDGE TABLE FUNCTIONS------------------------------------

    public Cursor findBridge(long noteId, long characterId){
        String[] columns = new String[]{Constants.COLUMN_ID,Constants.COLUMN_NOTEID, Constants.COLUMN_CHARACTERID, Constants.COLUMN_USED_TIME};

        String where = Constants.COLUMN_NOTEID + " =? AND " +
                Constants.COLUMN_CHARACTERID + " =?";
        String[] whereArgs = new String[]{Long.toString(noteId), Long.toString(characterId)};

        Cursor cursor = database.query(Constants.BRIDGE_TABLE,columns, where, whereArgs,
                null, null, null);

        if(cursor != null){
            cursor.moveToFirst();

        }
        return cursor;

    }

    public Cursor findBridgesByNoteId(long noteId){
        String[] columns = new String[]{Constants.COLUMN_ID,Constants.COLUMN_NOTEID,
                Constants.COLUMN_CHARACTERID, Constants.COLUMN_USED_TIME};

        String where = Constants.COLUMN_NOTEID + " =?";
        String[] whereArgs = new String[]{Long.toString(noteId)};

        Cursor cursor = database.query(Constants.BRIDGE_TABLE,columns, where, whereArgs,
                null, null, Constants.COLUMN_USED_TIME+" DESC, " + Constants.COLUMN_CHARACTERID + " ASC");

        if(cursor != null){
            cursor.moveToFirst();

        }
        return cursor;

    }

    public Cursor findBridgesByCharacterId(long characterId){
        String[] columns = new String[]{Constants.COLUMN_ID,Constants.COLUMN_NOTEID,
                Constants.COLUMN_NOTEID, Constants.COLUMN_USED_TIME};

        String where = Constants.COLUMN_CHARACTERID + " =?";
        String[] whereArgs = new String[]{Long.toString(characterId)};

        Cursor cursor = database.query(Constants.BRIDGE_TABLE,columns, where, whereArgs,
                null, null, Constants.COLUMN_USED_TIME+" DESC");

        if(cursor != null){
            cursor.moveToFirst();

        }
        return cursor;

    }

    public void insertBridge(long noteID, long characterID, long time){
        ContentValues contentValues = new ContentValues();
        contentValues.put(Constants.COLUMN_NOTEID, noteID);
        contentValues.put(Constants.COLUMN_CHARACTERID, characterID);
        contentValues.put(Constants.COLUMN_USED_TIME, time);

        database.insert(Constants.BRIDGE_TABLE, null, contentValues);
    }

    public int updateBridge(long _id, long time){
        ContentValues contentValues = new ContentValues();
        contentValues.put(Constants.COLUMN_USED_TIME, time);

        return database.update(Constants.BRIDGE_TABLE, contentValues,
                Constants.COLUMN_ID + "=" + _id, null);
    }

    public  void deleteBridge(long _id){
        database.delete(Constants.BRIDGE_TABLE, Constants.COLUMN_ID + "=" + _id, null);
    }

}
