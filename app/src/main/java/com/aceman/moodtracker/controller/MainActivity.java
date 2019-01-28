package com.aceman.moodtracker.controller;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
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
import com.aceman.moodtracker.model.NoteMaker;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.aceman.moodtracker.model.NoteMaker.mAddNote;
import static com.aceman.moodtracker.model.NoteMaker.mIsNote;
import static com.aceman.moodtracker.model.MoodSave.getToday;
import static java.lang.System.out;
/**
 ** MoodTracker<br>
 * Version 1.0<br>
 *
 * <b>Main class</b> is where the user choose his mood by swiping and it contains three buttons:<br>
 * - Smiley (center) who save the actual mood<br>
 * - Note (bot left) who add a note with the mood<br>
 * - History for seeing history on 7 days<br>
 *

 * @author Aceman
 */
public class MainActivity extends AppCompatActivity {

    private float x1, x2, y1, y2;
    @BindView(R.id.activity_main_frame) FrameLayout mFrame;
    @BindView(R.id.activity_main_smiley_btn) ImageButton mSmiley;
    @BindView(R.id.activity_main_note_btn) ImageButton mNote;
    @BindView(R.id.activity_main_history_btn) ImageButton mHistory;
    @BindView(R.id.activity_main_share) ImageButton mShare;
    private List<MoodSave> mMoodSaveList;
    public int mLastDay;
    private String mMood = "happy";
    private String mMoodFr;
    private MediaPlayer mSound;
    private int mCurrentPos = 3;

    /**
     * Setting the Main mood view, with smileys and buttons.
     * @param savedInstanceState saved instance
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        System.out.println("MainActivity:onCreate()");
        final Animation shake = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.shake_anim);    // anim on smiley click
        setMoodSwipe();
        loadData();

        mSmiley.setOnClickListener(v -> {
            mMoodSaveList.remove(7);
            mMoodSaveList.add(7, new MoodSave(getToday(),mMood, mIsNote,mAddNote)); // Temporary save slot
            saveData();
            mShare.setClickable(true);
            mShare.setVisibility(View.VISIBLE);
            Animation pulse = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.pulse_effect);    //  Share btn anim
            mShare.startAnimation(pulse);
            mSound.start();    // sound on smiley click
            mSmiley.startAnimation(shake);  // anim on smiley click
            Toast.makeText(getApplication(),"Humeur sauvegardée!",Toast.LENGTH_SHORT ).show();
        });

        mNote.setOnClickListener(v -> {
            NoteMaker newNote = new NoteMaker(MainActivity.this);
            newNote.buidNotePopup();    // add a daily comment
        });

        mHistory.setOnClickListener(v -> {
            Intent HistoryActivity = new Intent(getApplicationContext(), HistoryActivity.class);
            startActivity(HistoryActivity); // show history
            overridePendingTransition(R.anim.slide_in_bot_right, R.anim.slide_out_bot_right);
        });

        mShare.setOnClickListener(v -> shareClick()); // share btn
    }

    /**
     * Share method button who shows after a click on the smiley with French mood MoodFr
     */
    private void shareClick() {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = "Mon humeur du jour est " +mMoodFr+ " et je voulais partager ça avec toi !" ;
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Mon Humeur");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    /**
     * Swipe event for up and down.
     * @param swipeEvent get the movement
     * @return better mood if swipe up, worst if swipe down
     */
    @Override
    public boolean onTouchEvent(MotionEvent swipeEvent) {   // swipe animations
        switch (swipeEvent.getAction()){
            case MotionEvent.ACTION_DOWN:
                // x1 = swipeEvent.getX();
                y1 = swipeEvent.getY();
                break;

            case MotionEvent.ACTION_UP:
                // x2 = swipeEvent.getX();
                y2 = swipeEvent.getY();
                // if swipe up
                if(y2<y1 && mCurrentPos < 4){
                    mCurrentPos++;
                }
                // if swipe down
                if(y2>y1 && mCurrentPos > 0){
                    mCurrentPos--;
                }
                break;
        }
        mSound.stop();  // stop sound created with swipe movement
        mSound.release();
        setMoodSwipe(); // Set the mood view
        Animation fadein = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_in);    //  Smoother transition
        mSmiley.startAnimation(fadein);
        mFrame.startAnimation(fadein);
        return super.onTouchEvent(swipeEvent);
    }

    /**
     * Check the first launch of the day.
     * @see MoodSave#getToday()
     */
    private void checkLaunchToday() {    // compare date
        if(getToday() != mLastDay){
            mLastDay = getToday();
        }
    }

    /**
     * Save the mood to a List using Gson.
     * @see Gson
     * @see MoodSave
     */
    private void saveData(){    // save SharedPreferences and mLastDay
        SharedPreferences mFirstLaunchToday = getSharedPreferences("LaunchToday",MODE_PRIVATE);
        SharedPreferences.Editor firstLaunch = mFirstLaunchToday.edit();
        firstLaunch.putInt("IsFirstLaunchToday",mLastDay);
        firstLaunch.apply();
        SharedPreferences mMoodSavePref = getSharedPreferences("MoodSave",MODE_PRIVATE);
        SharedPreferences.Editor editor = mMoodSavePref.edit();
        Gson gson = new Gson();
        String json = gson.toJson(mMoodSaveList);
        editor.putString("TestList",json);
        editor.apply();
    }

    /**
     * Load the mood List or create one if first launch ever.
     * Also move the items in mMoodSaveList everyday for showing 7 past days in history.
     */
    private void loadData(){
        SharedPreferences mFirstLaunchToday = getSharedPreferences("LaunchToday",MODE_PRIVATE);
        mLastDay = mFirstLaunchToday.getInt("IsFirstLaunchToday",mLastDay);
        SharedPreferences mMoodSavePref = getSharedPreferences("MoodSave",MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mMoodSavePref.getString("Mood_Json", null);
        Type type = new TypeToken<List<MoodSave>>() {}.getType();
        mMoodSaveList = gson.fromJson(json, type);
        listCreate();   // create a first list if first launch or deleted list
        saveMover();    // move items on past 7 day if it is a new day
        checkLaunchToday();  // update and check the day
        saveData();
    }

    /**
     * Create a first list if first launch or deleted list.
     */
    protected void listCreate(){
        if(mMoodSaveList == null){
            mMoodSaveList = new ArrayList<>();
            mMoodSaveList.add(0,new MoodSave(getToday()-7,"very_bad", false,null));
            mMoodSaveList.add(1,new MoodSave(getToday()-6,"baad", true,"Mauvaise journée!"));
            mMoodSaveList.add(2,new MoodSave(getToday()-5,"normal", false,null));
            mMoodSaveList.add(3,new MoodSave(getToday()-4,"happy", false,null));
            mMoodSaveList.add(4,new MoodSave(getToday()-3,"very_happy", true,"Belle Journée!!"));
            mMoodSaveList.add(5,new MoodSave(getToday()-2,"happy", false,null));
            mMoodSaveList.add(6,new MoodSave(getToday()-1,"very_bad", true,"Grrr quel mauvais temps..."));
            mMoodSaveList.add(7,new MoodSave(getToday(),"happy", false,null));
            saveData();
        }
    }

    /**
     * Move items on past 7 day if it is a new day.<br>
     *     + fix (do while) if app is not launched everyday!
     * <mark>(note: may cause problem if launch from 1st January to 7th)</mark>
     */
    protected void saveMover(){
        int today = getToday();
        int dayleft = today - mLastDay;

        if(today >= 1 && today <= 7){
            today = today+365;
        }
        if(today != mLastDay && mLastDay != 0){
            do{
                mMoodSaveList.set(0, mMoodSaveList.get(1));
                mMoodSaveList.set(1, mMoodSaveList.get(2));
                mMoodSaveList.set(2, mMoodSaveList.get(3));
                mMoodSaveList.set(3, mMoodSaveList.get(4));
                mMoodSaveList.set(4, mMoodSaveList.get(5));
                mMoodSaveList.set(5, mMoodSaveList.get(6));
                mMoodSaveList.set(6, mMoodSaveList.get(7));
                mMoodSaveList.remove(7);
                mMoodSaveList.add(7,new MoodSave(today- dayleft,"happy", false,null));
                mLastDay++;
                dayleft--;
            }
            while(mLastDay < today);
        }
    }

    /**
     * Get the mCurrentPos value to set the mood to show w/ each sound, smiley and background.
     */
    protected void setMoodSwipe(){
        if(mCurrentPos <= 4 && mCurrentPos >= 0){

            switch (mCurrentPos){

                case 0:
                    mMood = "very_bad";
                    mMoodFr = "Très Mauvaise";
                    mFrame.setBackgroundResource(R.color.faded_red);
                    mSmiley.setImageResource(R.drawable.smiley_sad);
                    mSound = MediaPlayer.create(this,R.raw.very_bad);
                    break;
                case 1:
                    mMood = "baad";
                    mMoodFr = "Mauvaise";
                    mFrame.setBackgroundResource(R.color.warm_grey);
                    mSmiley.setImageResource(R.drawable.smiley_disappointed);
                    mSound = MediaPlayer.create(this,R.raw.bad);
                    break;
                case 2:
                    mMood = "normal";
                    mMoodFr = "Normale";
                    mFrame.setBackgroundResource(R.color.cornflower_blue_65);
                    mSmiley.setImageResource(R.drawable.smiley_normal);
                    mSound = MediaPlayer.create(this,R.raw.normal);
                    break;
                case 3:
                    mMood = "happy";
                    mMoodFr = "Bonne";
                    mFrame.setBackgroundResource(R.color.light_sage);
                    mSmiley.setImageResource(R.drawable.smiley_happy);
                    mSound = MediaPlayer.create(this,R.raw.happy);
                    break;
                case 4:
                    mMood = "very_happy";
                    mMoodFr = "Très Bonne";
                    mFrame.setBackgroundResource(R.color.banana_yellow);
                    mSmiley.setImageResource(R.drawable.smiley_super_happy);
                    mSound = MediaPlayer.create(this,R.raw.very_happy);
                    break;

            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        out.println("MainActivity::onStart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        out.println("MainActivity::onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        out.println("MainActivity::onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        out.println("MainActivity::onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        out.println("MainActivity::onDestroy()");
    }
}
