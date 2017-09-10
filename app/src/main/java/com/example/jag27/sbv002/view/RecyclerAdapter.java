package com.example.jag27.sbv002.view;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jag27.sbv002.AddScene;
import com.example.jag27.sbv002.Note;
import com.example.jag27.sbv002.R;
import com.example.jag27.sbv002.database.NoteManager;
import com.example.jag27.sbv002.utility.Constants;
import com.example.jag27.sbv002.utility.ItemTouchHelperAdapter;
import com.example.jag27.sbv002.utility.ItemTouchHelperViewHolder;
import com.example.jag27.sbv002.utility.OnStartDragListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Handler;


public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>
implements ItemTouchHelperAdapter{

    private String storyTitle;
    private List<Note> notes;
    private LayoutInflater inflater;
    private final OnStartDragListener mDragListener;
    private Context context;
    private NoteManager noteManager;

    public RecyclerAdapter(String storyTitle,List<Note> notes, Context c, OnStartDragListener dragListener) {
        inflater = LayoutInflater.from(c);
        this.context = c;
        this.storyTitle = storyTitle;
        this.notes = notes;
        mDragListener = dragListener;
        noteManager = new NoteManager(context);
        noteManager.open();
    }

    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.activity_view_note, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Typeface courierFont = Typeface.createFromAsset(context.getAssets(), "fonts/courier.TTF");
        Note note = notes.get(position);

        //Set font of TextView
        holder.titleText.setTypeface(courierFont);
        holder.contentText.setTypeface(courierFont);

        if(note.getSubPlot() == null){
            holder.titleText.setText(note.getTitle());
        }else{
            holder.titleText.setText(note.getTitle()+": "+ note.getSubPlot() );
        }
        holder.titleText.setTypeface(null,Typeface.BOLD);

        //Set value of TextViews
        holder.contentText.setText(note.getContent());
        holder.idText.setText(note.getId());
        holder.posText.setText(Integer.toString(note.getPos()));
        ArrayList<Integer> arrayList = getCharacterColorFromDB(Long.parseLong(note.getId()));
        ColorAdapter colorAdapter = new ColorAdapter(context,R.layout.activity_view_color,arrayList);

        for(int i = 0; i< arrayList.size(); i++){
            View item = colorAdapter.getView(i,null,null);
            holder.characterList.addView(item);
        }
        Log.d("Note ID", note.getId());



        //Commences dragging functionality
        final GestureDetector gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener(){
            public void onLongPress(MotionEvent event){
                mDragListener.onStartDrag(holder);
            }

        });


        holder.titleText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return gestureDetector.onTouchEvent(motionEvent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        //Update positions in database and array list when  a note has been moved
        //Selects two note objects at a time and swaps positions values
        Note fromNote = notes.get(fromPosition);
        Note toNote = notes.get(toPosition);
        long fromId = Long.parseLong(fromNote.getId());
        int toPos = toNote.getPos();

        if(fromPosition < toPosition){
            for(int i = fromPosition; i < toPosition; i++){

                Note n1 = notes.get(i);
                Note n2 = notes.get(i+1);
                long n1Id = Long.parseLong(n1.getId());
                long n2Id = Long.parseLong(n2.getId());
                int n1Pos = n1.getPos();
                int n2Pos = n2.getPos();
                n1.setPos(n2Pos);
                n2.setPos(n1Pos);
                noteManager.updatePos(n2Pos, n1Id);
                noteManager.updatePos(n1Pos, n2Id);
                Collections.swap(notes, i, i+1);
            }
        }
        else {
            for (int i  = fromPosition; i> toPosition; i--){

                Note n1 = notes.get(i);
                Note n2 = notes.get(i-1);
                long n1Id = Long.parseLong(n1.getId());
                long n2Id = Long.parseLong(n2.getId());
                int n1Pos = n1.getPos();
                int n2Pos = n2.getPos();
                n1.setPos(n2Pos);
                n2.setPos(n1Pos);
                noteManager.updatePos(n2Pos, n1Id);
                noteManager.updatePos(n1Pos, n2Id);
                Collections.swap(notes, i, i-1);

            }
        }

        //update the position on the database for the from position note card
        noteManager.updatePos(toPos,fromId);
        notifyItemMoved(fromPosition,toPosition);

        return true;
    }

    @Override
    public void onItemDismiss(int position) {
        notes.remove(position);
        notifyItemRemoved(position);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder, View.OnClickListener{

        private TextView titleText;
        private TextView contentText;
        private TextView idText;
        private TextView posText;
        private Handler handler;
        private LinearLayout characterList;


        public ViewHolder(final View itemView) {
            super(itemView);

            titleText = (TextView)itemView.findViewById(R.id.subtitle);
            contentText = (TextView)itemView.findViewById(R.id.content);
            idText = (TextView)itemView.findViewById(R.id.id);
            posText = (TextView) itemView.findViewById(R.id.position);
            characterList = (LinearLayout) itemView.findViewById(R.id.character_list);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY);
        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(0);
        }

        @Override
        public void onClick(View view) {
            //Modify note object
            final Intent modify_intent;
            String id = idText.getText().toString();
            String subTitle = titleText.getText().toString();
            String content = contentText.getText().toString();

            modify_intent = new Intent(context,AddScene.class);
            modify_intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            modify_intent.putExtra("FileName",storyTitle);
            modify_intent.putExtra("Message","ModifyScene");
            modify_intent.putExtra("SubTitle",subTitle);
            modify_intent.putExtra("Content",content);
            modify_intent.putExtra("ID",id);
            context.startActivity(modify_intent);

        }

    }

    public ArrayList<Integer> getCharacterColorFromDB(long _id){

        ArrayList<Integer> colorList = new ArrayList<Integer>();
        Cursor bridgesCursor = noteManager.findBridgesByNoteId(_id);
        long thresholdTime;

        if(bridgesCursor.moveToFirst()) {

            Cursor findCharacter;
            thresholdTime = bridgesCursor.getLong(bridgesCursor.getColumnIndex(Constants.COLUMN_USED_TIME));
            for (bridgesCursor.moveToFirst(); !bridgesCursor.isAfterLast(); bridgesCursor.moveToNext()) {
                long charID = bridgesCursor.getLong(bridgesCursor.getColumnIndex(Constants.COLUMN_CHARACTERID));
                long localTime = bridgesCursor.getLong(bridgesCursor.getColumnIndex(Constants.COLUMN_USED_TIME));

                //If USED_TIME value is less than threshold time value, delete character from bridge table
                if(localTime < thresholdTime) {
                    long bridgeID = bridgesCursor.getLong(bridgesCursor.getColumnIndex(Constants.COLUMN_ID));
                    noteManager.deleteBridge(bridgeID);
                }


                else {
                    findCharacter = noteManager.findCharacterById(charID);
                    String c = findCharacter.getString(findCharacter.getColumnIndex(Constants.COLUMN_CHARACTER));
                    int color = findCharacter.getInt(findCharacter.getColumnIndex(Constants.COLUMN_COLOR));
                    colorList.add(color);
                    findCharacter.close();
                }
            }
        }
        bridgesCursor.close();

        return colorList;
    }

}
