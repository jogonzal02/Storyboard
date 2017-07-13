package com.example.jag27.sbv002;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    Button createBtn;
    Button loadBtn;
    Button optionBtn;
    Button quitBtn;
    TextView appTitleText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createBtn = (Button) findViewById(R.id.create_button);
        loadBtn = (Button) findViewById(R.id.load_button);
        optionBtn = (Button) findViewById(R.id.options);
        quitBtn = (Button) findViewById(R.id.quit);

        appTitleText = (TextView) findViewById(R.id.app_title);

        //Set a custom font on activity components
        Typeface courierFont = Typeface.createFromAsset(getAssets(), "fonts/courier.TTF");
        appTitleText.setTypeface(courierFont);
        createBtn.setTypeface(courierFont);
        loadBtn.setTypeface(courierFont);
        optionBtn.setTypeface(courierFont);
        quitBtn.setTypeface(courierFont);

        createBtn.setOnClickListener(this);
        loadBtn.setOnClickListener(this);
        optionBtn.setOnClickListener(this);
        quitBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.create_button:
                newContent();
                break;
            case R.id.load_button:
                loadContent();
                break;
            case R.id.options:
                option();
                break;
            case R.id.quit:
                quit();
        }
    }

    void newContent(){
        CreateStoryBoardFragment createStoryBoardFragment = new CreateStoryBoardFragment();
        createStoryBoardFragment.show(getFragmentManager(),"create_storyboard_fragment");
    }

    void loadContent(){
        Intent loader = new Intent(getApplicationContext(),Load.class);
        startActivity(loader);
    }

    void option(){
        Toast.makeText(this,"To Be Implemented",
                Toast.LENGTH_LONG).show();
    }

    void quit(){
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}
