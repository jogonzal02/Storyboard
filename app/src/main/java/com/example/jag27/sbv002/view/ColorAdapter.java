package com.example.jag27.sbv002.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.jag27.sbv002.R;

import java.util.ArrayList;
import java.util.List;

public class ColorAdapter extends ArrayAdapter {
    private ArrayList<Integer> colors;
    private int resource;

    public ColorAdapter(Context context,int resource,ArrayList<Integer> colors){
        super(context,resource, colors);
        this.resource = resource;
        this.colors = colors;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder = null;
        int color = colors.get(position);
        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(resource,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.colorImage = (ImageView)convertView.findViewById(R.id.color);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }
        viewHolder.colorImage.setBackgroundColor(color);
        return convertView;
    }

    @Override
    public int getCount() {
        return colors.size();
    }

    private static class ViewHolder{
        private ImageView colorImage;
    }
}
