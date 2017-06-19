package com.example.jag27.sbv002;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.example.jag27.sbv002.utility.Constants;

import java.net.URI;
import java.util.Arrays;
import java.util.HashSet;

public class NoteContentProvider extends ContentProvider {
    private DatabaseHelper dbHelper;
    private static final String BASE_PATH_NOTE = "note";
    private static final String AUTHROITY = "com.example.jag27.provider";
    public static Uri CONTENT_URI = Uri.parse("content://" + AUTHROITY + "/" + BASE_PATH_NOTE);
    private static final int NOTE = 100;
    private static final int NOTES = 101;

    private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        URI_MATCHER.addURI(AUTHROITY, BASE_PATH_NOTE, NOTES);
        URI_MATCHER.addURI(AUTHROITY,BASE_PATH_NOTE + "/#",NOTE);
    }

    private void checkColumns(String[] projection){
        if(projection != null){
            HashSet<String> request = new HashSet<String>(Arrays.asList(projection));
            HashSet<String> available = new HashSet<String>(Arrays.asList(Constants.COLUMNS));
            if (!available.containsAll(request)){
                throw new IllegalArgumentException("Unknown columns in projection");
            }
        }
    }

    @Override
    public boolean onCreate() {
        dbHelper = new DatabaseHelper(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        SQLiteQueryBuilder sqLiteQueryBuilder = new SQLiteQueryBuilder();
        checkColumns(strings);

        int type = URI_MATCHER.match(uri);
        switch (type){
            case NOTES:
                sqLiteQueryBuilder.setTables(Constants.NOTES_TABLE);
                break;
            case NOTE:
                sqLiteQueryBuilder.setTables(Constants.NOTES_TABLE);
                sqLiteQueryBuilder.appendWhere(Constants.COLUMN_ID + "=" + uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = sqLiteQueryBuilder.query(db, strings,s, strings1,null,null,s1);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        int type = URI_MATCHER.match(uri);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Long id;
        switch (type){
            case NOTES:
                id = db.insert(Constants.NOTES_TABLE, null, contentValues);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI:" + contentValues);
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return Uri.parse(BASE_PATH_NOTE + "/" + id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        int type = URI_MATCHER.match(uri);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int affectedRows;
        switch (type){
            case NOTES:
                affectedRows = db.delete(Constants.NOTES_TABLE,s,strings);
                break;
            case NOTE:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(s)){
                    affectedRows = db.delete(Constants.NOTES_TABLE, Constants.COLUMN_ID + "=" + id, null);
                }
                else{
                    affectedRows = db.delete(Constants.NOTES_TABLE, Constants.COLUMN_ID + "=" + id + "and" + s, strings);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return affectedRows;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        int type = URI_MATCHER.match(uri);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int afffectedRows;
        switch (type){
            case NOTES:
                afffectedRows = db.update(Constants.NOTES_TABLE,contentValues,s, strings);
                break;
            case NOTE:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(s)){
                    afffectedRows = db.update(Constants.NOTES_TABLE,contentValues, Constants.COLUMN_ID + "=" + id,null);
                }
                else{
                    afffectedRows = db.update(Constants.NOTES_TABLE,contentValues, Constants.COLUMN_ID + "=" + id + "and" + s, strings);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return afffectedRows;
    }
}
