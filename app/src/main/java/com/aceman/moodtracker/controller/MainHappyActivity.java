package com.aceman.moodtracker.controller;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import com.aceman.moodtracker.R;
import com.aceman.moodtracker.model.MoodSave;
import com.aceman.moodtracker.model.NoteMaker;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static com.aceman.moodtracker.model.NoteMaker.mAddNote;
import static com.aceman.moodtracker.model.NoteMaker.mIsNote;
import static com.aceman.moodtracker.model.MoodSave.Today;
import static java.lang.System.out;

public class MainHappyActivity extends AppCompatActivity {

    private float x1, x2, y1, y2;
    private ImageButton mSmiley;
    private ImageButton mNote;
    private ImageButton mHistory;
    private List<MoodSave> MoodSaveList;
    public int mLastDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_happy);
        System.out.println("MainHappyActivity:onCreate()");
        final MediaPlayer clickSmiley = MediaPlayer.create(this,R.raw.happy);   // sound on smiley click
        final Animation shake = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.shake_anim);    // anim on smiley click
        loadData();

        findViewById(R.id.activity_main_frame);
        mSmiley = findViewById(R.id.activity_main_happy_smiley_btn);
        mNote = findViewById(R.id.activity_main_happy_note_btn);
        mHistory = findViewById(R.id.activity_main_happy_history_btn);

        mSmiley.setOnClickListener(v -> {
            MoodSaveList.remove(7);
            MoodSaveList.add(7, new MoodSave(Today(),"happy", mIsNote,mAddNote));
            saveData();
            clickSmiley.start();    // sound on smiley click
            mSmiley.startAnimation(shake);  // anim on smiley click
            Toast.makeText(getApplication(),"Humeur sauvegardée!",Toast.LENGTH_SHORT ).show();
        });

        mNote.setOnClickListener(v -> {
            NoteMaker newNote = new NoteMaker(MainHappyActivity.this);
            newNote.buidNotePopup();    // add a daily comment
        });

        mHistory.setOnClickListener(v -> {
            Intent HistoryActivity = new Intent(getApplicationContext(), HistoryActivity.class);
            startActivity(HistoryActivity); // show history
            overridePendingTransition(R.anim.slide_in_bot_right, R.anim.slide_out_bot_right);
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent swipeEvent) {   // swipe animations

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
                    Intent VeryHappyActivity = new Intent(getApplicationContext(), VeryHappyActivity.class);
                    startActivity(VeryHappyActivity);
                    overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                }
                // if swipe down
                if(y2>y1){
                    Intent NormalActivity = new Intent(getApplicationContext(), NormalActivity.class);
                    startActivity(NormalActivity);
                    overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);
                }
                break;
        }
        return false;
    }

    private void CheckLaunchToday() {    // compare date
        if(Today() != mLastDay){
            mLastDay=Today();
        }
    }

    private void saveData(){    // save SharedPreferences and day number
        SharedPreferences mFirstLaunchToday = getSharedPreferences("LaunchToday",MODE_PRIVATE);
        SharedPreferences.Editor firstLaunch = mFirstLaunchToday.edit();
        firstLaunch.putInt("IsFirstLaunchToday",mLastDay);
        firstLaunch.apply();
        SharedPreferences mMoodSavePref = getSharedPreferences("MoodSave",MODE_PRIVATE);
        SharedPreferences.Editor editor = mMoodSavePref.edit();
        Gson gson = new Gson();
        String json = gson.toJson(MoodSaveList);
        editor.putString("TestList",json);
        editor.apply();
    }

    private void loadData(){
        SharedPreferences mFirstLaunchToday = getSharedPreferences("LaunchToday",MODE_PRIVATE);
        mLastDay = mFirstLaunchToday.getInt("IsFirstLaunchToday",mLastDay);
        SharedPreferences mMoodSavePref = getSharedPreferences("MoodSave",MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mMoodSavePref.getString("TestList", null);
        Type type = new TypeToken<List<MoodSave>>() {}.getType();
        MoodSaveList = gson.fromJson(json, type);

        // create a first list if first launch or deleted list
        if(MoodSaveList == null){
            MoodSaveList = new ArrayList<>();
            MoodSaveList.add(0,new MoodSave(Today()-7,"very_bad", false,null));
            MoodSaveList.add(1,new MoodSave(Today()-6,"bad", true,"Mauvaise journée!"));
            MoodSaveList.add(2,new MoodSave(Today()-5,"normal", false,null));
            MoodSaveList.add(3,new MoodSave(Today()-4,"happy", false,null));
            MoodSaveList.add(4,new MoodSave(Today()-3,"very_happy", true,"Belle Journée!!"));
            MoodSaveList.add(5,new MoodSave(Today()-2,"happy", false,null));
            MoodSaveList.add(6,new MoodSave(Today()-1,"very_happy", false,null));
            MoodSaveList.add(7,new MoodSave(Today(),"happy", false,null));
            saveData();
        }
        if(Today() != mLastDay && mLastDay != 0){       // move items on past 7 day if it is a new day
            MoodSaveList.set(0,MoodSaveList.get(1));
            MoodSaveList.set(1,MoodSaveList.get(2));
            MoodSaveList.set(2,MoodSaveList.get(3));
            MoodSaveList.set(3,MoodSaveList.get(4));
            MoodSaveList.set(4,MoodSaveList.get(5));
            MoodSaveList.set(5,MoodSaveList.get(6));
            MoodSaveList.set(6,MoodSaveList.get(7));
        }
        CheckLaunchToday(); // check if first launch of the day
        saveData();
    }

    @Override
    protected void onStart() {
        super.onStart();
        out.println("MainHappyActivity::onStart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        out.println("MainHappyActivity::onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        out.println("MainHappyActivity::onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        out.println("MainHappyActivity::onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        out.println("MainHappyActivity::onDestroy()");
    }
}
