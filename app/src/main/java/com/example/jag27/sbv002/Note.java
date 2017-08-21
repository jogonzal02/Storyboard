package com.example.jag27.sbv002;

public class Note{
    private String id;
    private String title;
    private String subPlot;
    private String content;
    private int pos;


    public void setId(String _id){
        id = _id;
    }

    public void setTitle(String title1){
        title = title1;
    }

    public void setSubPlot(String subPlot1) {subPlot = subPlot1;}

    public void setContent(String content1){
        content = content1;
    }

    public void setPos(int pos1){pos = pos1;}

    public String getTitle(){return title;}
    public String getSubPlot(){return subPlot;}
    public String getContent(){return content;}
    public String getId(){return id;}
    public int getPos(){return pos;}
}
