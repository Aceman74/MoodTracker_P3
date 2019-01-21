package com.aceman.moodtracker.controller;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import com.aceman.moodtracker.R;
import com.aceman.moodtracker.model.MoodSave;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static com.aceman.moodtracker.controller.NoteActivity.mAddNote;
import static com.aceman.moodtracker.controller.NoteActivity.mIsNote;
import static com.aceman.moodtracker.model.MoodSave.Today;
import static java.lang.System.out;

public class NormalActivity extends AppCompatActivity {

    private float x1, x2, y1, y2;
    private FrameLayout mMainFrame;
    private ImageButton mSmiley;
    private ImageButton mNote;
    private ImageButton mHistory;
    private NormalActivity mActivity;
    private List<MoodSave> MoodSaveList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal);
        System.out.println("NormalActivity:onCreate()");
        final Animation shake = AnimationUtils.loadAnimation(this,R.anim.shake_anim);
        this.mActivity = this;
        loadData();
        mMainFrame = findViewById(R.id.activity_normal_frame);
        mSmiley = findViewById(R.id.activity_normal_smiley_btn);
        mNote = findViewById(R.id.activity_normal_note_btn);
        mHistory = findViewById(R.id.activity_normal_history_btn);
        mSmiley.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MoodSaveList.add(7, new MoodSave(Today(),"Normal", mIsNote,mAddNote));
                saveData();
                mSmiley.startAnimation(shake);
                Toast.makeText(getApplication(),"Humeur sauvegardée!",Toast.LENGTH_SHORT ).show();
            }
        });

        mNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                NoteActivity newNote = new NoteActivity(NormalActivity.this);
                newNote.buidNotePopup();
            }
        });

        mHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent HistoryActivity = new Intent(getApplicationContext(), HistoryActivity.class);
                startActivity(HistoryActivity);
                overridePendingTransition(R.anim.slide_in_bot_right, R.anim.slide_out_bot_right);
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent swipeEvent) {
        switch (swipeEvent.getAction()){
            case MotionEvent.ACTION_DOWN:
                x1 = swipeEvent.getX();
                y1 = swipeEvent.getY();
                break;
            case MotionEvent.ACTION_UP:
                x2 = swipeEvent.getX();
                y2 = swipeEvent.getY();
                // if swipe up
                if(y2<y1){
                    Intent MainHappyActivity = new Intent(getApplicationContext(), MainHappyActivity.class);
                    startActivity(MainHappyActivity);
                    overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                }
                // if swipe down
                if(y2>y1){
                    Intent BadActivity = new Intent(getApplicationContext(), BadActivity.class);
                    startActivity(BadActivity);
                    overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);
                }
                break;
        }return false;
    }

    private void saveData(){
        SharedPreferences mMoodSavePref = getSharedPreferences("MoodSave",MODE_PRIVATE);
        SharedPreferences.Editor editor = mMoodSavePref.edit();
        Gson gson = new Gson();
        String json = gson.toJson(MoodSaveList);
        editor.putString("TestList",json);
        editor.apply();
    }

    private void loadData(){
        SharedPreferences mMoodSavePref = getSharedPreferences("MoodSave",MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mMoodSavePref.getString("TestList", null);
        Type type = new TypeToken<List<MoodSave>>() {}.getType();
        MoodSaveList = gson.fromJson(json, type);
    }

    @Override
    protected void onStart() {
        super.onStart();

        out.println("Normal::onStart()");
    }

    @Override
    protected void onResume() {
        super.onResume();

        out.println("Normal::onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();

        out.println("Normal::onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();

        out.println("Normal::onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        out.println("Normal::onDestroy()");
    }
}
