package com.example.jag27.sbv002;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by jag27 on 4/5/2017.
 */

public class CreateStoryBoardFragment extends DialogFragment implements View.OnClickListener {

    EditText titleTxt;
    Button submitBtn;
    Button cancelBtn;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.new_story_fragment,container,false);
        getDialog().setTitle(getResources().getString(R.string.create_storyboard));
        titleTxt = (EditText) rootView.findViewById(R.id.fragmentEditText);
        submitBtn = (Button) rootView.findViewById(R.id.fragmentSubmitButton);
        cancelBtn = (Button) rootView.findViewById(R.id.fragmentCanelButton);

        submitBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);


        return rootView;

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fragmentSubmitButton:
                Intent intent = new Intent(getActivity(),StoryBoard.class);
                intent.putExtra("FileName",titleTxt.getText().toString());
                startActivity(intent);

            case R.id.fragmentCanelButton:
                dismiss();
        }
    }
}
