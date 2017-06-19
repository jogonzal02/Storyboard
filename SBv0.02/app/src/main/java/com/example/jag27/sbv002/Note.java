package com.example.jag27.sbv002;

import com.example.jag27.sbv002.utility.Constants;

public class Note {
    private Long id;
    private String story;
    private String title;
    private String content;
    private long createDate;
    private long dateModified;


    public static Note getNoteFromCursor(Cursor cursor){
        Note note = new Note();
        note.setId(cursor.getLong(cursor.getColumnIndex(Constants.COLUMN_ID)));
        note.setTitle(cursor.getString(cursor.getColumnIndex(Constants.COLUMN_TITLE)));
        note.setContent(cursor.getString(cursor.getColumnIndex(Constants.COLUMN_CONTENT)));
        note.setCreateDate(cursor.getLong(cursor.getColumnIndex(Constants.COLUMN_CREATED_TIME)));
        note.setDateModified(cursor.getLong(cursor.getColumnIndex(Constants.COLUMN_MODIFIED_TIME)));
        return note;
    }

    public Long getId(){
        return id;
    }
    public void setId(long id){
        this.id = id;
    }

    public String getTitle(){
        return title;
    }
    public void setTitle(String title){
        this.title = title;
    }

    public String getContent(){
        return content;
    }
    public void setContent(String content){
        this.content = content;
    }

    public long getCreateDate(){
        return createDate;
    }
    public void setCreateDate(long createDate){
        this.createDate = createDate;
    }

    public long getDateModified(){
        return dateModified;
    }
    public void setDateModified(long dateModified){
        this.dateModified = dateModified;
    }

}
