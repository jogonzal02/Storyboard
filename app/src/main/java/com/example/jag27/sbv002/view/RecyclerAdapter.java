package com.example.jag27.sbv002.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;


import com.example.jag27.sbv002.ModifyScene;
import com.example.jag27.sbv002.Note;
import com.example.jag27.sbv002.R;
import com.example.jag27.sbv002.database.NoteManager;
import com.example.jag27.sbv002.utility.ItemTouchHelperAdapter;
import com.example.jag27.sbv002.utility.ItemTouchHelperViewHolder;
import com.example.jag27.sbv002.utility.OnStartDragListener;

import java.util.Collections;
import java.util.List;

/**
 * Created by jag27 on 6/11/2017.
 */

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

        holder.titleText.setText(note.getTitle());
        holder.titleText.setTypeface(courierFont);

        holder.contentText.setText(note.getContent());
        holder.contentText.setTypeface(courierFont);

        holder.idText.setText(note.getId());
        holder.posText.setText(Integer.toString(note.getPos()));

        holder.titleText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(MotionEventCompat.getActionMasked(motionEvent) == MotionEvent.ACTION_DOWN){
                    mDragListener.onStartDrag(holder);
                }
                return false;
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
        Note fromNote = notes.get(fromPosition);
        Note toNote = notes.get(toPosition);
        long fromId = Long.parseLong(fromNote.getId());
        int toPos = toNote.getPos();

        if(fromPosition < toPosition){
            for(int i = fromPosition; i < toPosition; i++){
                Collections.swap(notes, i, i+1);
                Note n1 = notes.get(i);
                Note n2 = notes.get(i);
                long n1Id = Long.parseLong(n1.getId());
                long n2Id = Long.parseLong(n2.getId());
                int n1Pos = n1.getPos() + 1;
                int n2Pos = n1.getPos() - 1;
                noteManager.updatePos(n1Pos, n1Id);
                noteManager.updatePos(n2Pos, n2Id);


            }
        }
        else {
            for (int i  = fromPosition; i> toPosition; i--){
                Collections.swap(notes,i,i-1);
                Note n1 = notes.get(i);
                Note n2 = notes.get(i);
                long n1Id = Long.parseLong(n1.getId());
                long n2Id = Long.parseLong(n2.getId());
                int n1Pos = n1.getPos() - 1;
                int n2Pos = n1.getPos() + 1;
                noteManager.updatePos(n1Pos, n1Id);
                noteManager.updatePos(n2Pos, n2Id);
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

    class ViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder, View.OnClickListener {

        private TextView titleText;
        private TextView contentText;
        private TextView idText;
        private TextView posText;

        public ViewHolder(final View itemView) {
            super(itemView);

            titleText = (TextView)itemView.findViewById(R.id.subtitle);
            contentText = (TextView)itemView.findViewById(R.id.content);
            idText = (TextView)itemView.findViewById(R.id.id);
            posText = (TextView) itemView.findViewById(R.id.position);

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
            final Intent modify_intent;
            String id = idText.getText().toString();
            String subTitle = titleText.getText().toString();
            String content = contentText.getText().toString();


            modify_intent = new Intent(context,ModifyScene.class);
            modify_intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            modify_intent.putExtra("FileName",storyTitle);
            modify_intent.putExtra("SubTitle",subTitle);
            modify_intent.putExtra("Content",content);
            modify_intent.putExtra("ID",id);
            context.startActivity(modify_intent);

        }
    }
}
