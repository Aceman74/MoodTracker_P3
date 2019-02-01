package com.aceman.moodtracker.controller;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import com.aceman.moodtracker.R;
import com.aceman.moodtracker.model.MoodSave;
import com.aceman.moodtracker.model.NoteMaker;
import com.aceman.moodtracker.model.OnSwipeTouchListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.aceman.moodtracker.model.MoodSave.getToday;
import static com.aceman.moodtracker.model.NoteMaker.mAddNote;
import static com.aceman.moodtracker.model.NoteMaker.mIsNote;

/**
 ** MoodTracker<br>
 * Version 1.0<br>
 *
 * <b>Main class</b> is where the user choose his mood by swiping and it contains three buttons:<br>
 * - Smiley (center) who save the actual mood<br>
 * - Note (bot left) who add a note with the mood<br>
 * - History for seeing history on 7 days<br>
 *   mMood: 0 Very Bad, 1 Bad, 2 Normal, 3 Happy, 4 Very Happy<br>
 *

 * @author Aceman
 */
public class MainActivity extends AppCompatActivity {

    @BindView(R.id.activity_main_frame) FrameLayout mFrame;
    @BindView(R.id.activity_main_smiley_btn) ImageButton mSmiley;
    @BindView(R.id.activity_main_note_btn) ImageButton mNote;
    @BindView(R.id.activity_main_history_btn) ImageButton mHistory;
    @BindView(R.id.activity_main_share) ImageButton mShare;
    @BindString(R.string.mood_is_saved) String mSavedString;
    @BindString(R.string.sharebody_1) String mShareString_1;
    @BindString(R.string.sharebody_2) String mShareString_2;
    @BindString(R.string.my_mood) String mMyMoodString;
    @BindString(R.string.mood_very_bad) String mMoodVeryBad;
    @BindString(R.string.mood_bad) String mMoodBad;
    @BindString(R.string.mood_normal) String mMoodNormal;
    @BindString(R.string.mood_happy) String mMoodHappy;
    @BindString(R.string.mood_very_happy) String mMoodVeryHappy;
    private List<MoodSave> mMoodSaveList;
    public int mLastDay;
    private int mMood = 3;
    private String mMoodLang;
    private MediaPlayer mSound;
    private Animation mShake;
    private Animation mPulse;
    private Animation mFadeIn;

    /**
     * Setting the Main mood view, with smileys and buttons.
     * @param savedInstanceState saved instance
     */
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mShake = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.shake_anim);    // Smiley onclick animation
        mPulse = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.pulse_effect);    //  Share btn pulse effect
        mFadeIn = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_in);    //  Smoother transition
        setMoodOnSwipe();
        loadData();

        mFrame.setOnTouchListener(new OnSwipeTouchListener(this) {      //  Swipe listener
            @Override
            public void onSwipeDown() {
                if(mMood > 0) {
                    mMood--;
                }
                swipeAnim();
            }
            public void onSwipeUp() {
                if(mMood < 4){
                    mMood++;
                }
                swipeAnim();
            }
        });
    }

    /**
     * Click on Smiley center
     */
    @OnClick(R.id.activity_main_smiley_btn)
    void onClickSmiley(){
        mMoodSaveList.remove(7);
        mMoodSaveList.add(7, new MoodSave(getToday(),mMood, mIsNote,mAddNote)); // Temporary save slot
        saveData();
        mShare.setClickable(true);
        mShare.setVisibility(View.VISIBLE);
        mShare.startAnimation(mPulse);
        mSound.start();    // sound on smiley click
        mSmiley.startAnimation(mShake);  // anim on smiley click
        Toast.makeText(getApplication(),mSavedString,Toast.LENGTH_SHORT ).show();
    }

    /**
     * Click on History button
     * @see HistoryActivity
     */
    @OnClick(R.id.activity_main_history_btn)
    void onClickHistory(){
        Intent HistoryActivity = new Intent(getApplicationContext(), HistoryActivity.class);
        startActivity(HistoryActivity); // show history
        overridePendingTransition(R.anim.slide_in_bot_right, R.anim.slide_out_bot_right);
    }

    /**
     * Click on Note button
     * @see NoteMaker
     */
    @OnClick(R.id.activity_main_note_btn)
    void onClickNote(){
        NoteMaker newNote = new NoteMaker(MainActivity.this);
        newNote.buidNotePopup();    // add a daily comment
    }

    /**
     * Click on Share button after mood selected
     */
    @OnClick(R.id.activity_main_share)
    void onClickShare(){
        shareClick();   // share btn
    }

    /**
     * Method to initiate/stop animations and sound on swipe
     */
    void swipeAnim(){
        mSound.stop();  // stop sound created with swipe movement
        mSound.release();
        setMoodOnSwipe(); // Set the mood view
        mSmiley.startAnimation(mFadeIn);
        mFrame.startAnimation(mFadeIn);
    }

    /**
     * Share method button who shows after a click on the smiley
     */
    private void shareClick() {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = mShareString_1 + " "+mMoodLang+" " + mShareString_2 ;
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, mMyMoodString);
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
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
        editor.putString("Mood_List",json);
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
        String json = mMoodSavePref.getString("Mood_List", null);
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
            mMoodSaveList.add(0,new MoodSave(getToday()-7,0, false,null));
            mMoodSaveList.add(1,new MoodSave(getToday()-6,1, true,"Mauvaise journée!"));
            mMoodSaveList.add(2,new MoodSave(getToday()-5,2, false,null));
            mMoodSaveList.add(3,new MoodSave(getToday()-4,3, false,null));
            mMoodSaveList.add(4,new MoodSave(getToday()-3,4, true,"Belle Journée!!"));
            mMoodSaveList.add(5,new MoodSave(getToday()-2,0, false,null));
            mMoodSaveList.add(6,new MoodSave(getToday()-1,1, true,"Grrr quel mauvais temps..."));
            mMoodSaveList.add(7,new MoodSave(getToday(),3, false,null));
            saveData();
        }
    }

    /**
     * Move items on past 7 day if it is a new day.<br>
     *     + fix (do while) if app is not launched everyday!<br>
     * <mark>(note: works if launch from 1st January to 7th)</mark>
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
                mMoodSaveList.add(7,new MoodSave(today- dayleft,3, false,null));
                mLastDay++;
                dayleft--;
            }
            while(mLastDay < today);
        }
    }

    /**
     * Get the mCurrentPos value to set the mood to show w/ each sound, smiley and background.<br>
     * mMoodLang is only for sharing content.
     */
    protected void setMoodOnSwipe(){
        if(mMood <= 4 && mMood >= 0){

            switch (mMood){

                case 0:
                    mMoodLang = mMoodVeryBad;
                    mFrame.setBackgroundResource(R.color.faded_red);
                    mSmiley.setImageResource(R.drawable.smiley_sad);
                    mSound = MediaPlayer.create(this,R.raw.very_bad);
                    break;
                case 1:
                    mMoodLang = mMoodBad;
                    mFrame.setBackgroundResource(R.color.warm_grey);
                    mSmiley.setImageResource(R.drawable.smiley_disappointed);
                    mSound = MediaPlayer.create(this,R.raw.bad);
                    break;
                case 2:
                    mMoodLang = mMoodNormal;
                    mFrame.setBackgroundResource(R.color.cornflower_blue_65);
                    mSmiley.setImageResource(R.drawable.smiley_normal);
                    mSound = MediaPlayer.create(this,R.raw.normal);
                    break;
                case 3:
                    mMoodLang = mMoodHappy;
                    mFrame.setBackgroundResource(R.color.light_sage);
                    mSmiley.setImageResource(R.drawable.smiley_happy);
                    mSound = MediaPlayer.create(this,R.raw.happy);
                    break;
                case 4:
                    mMoodLang = mMoodVeryHappy;
                    mFrame.setBackgroundResource(R.color.banana_yellow);
                    mSmiley.setImageResource(R.drawable.smiley_super_happy);
                    mSound = MediaPlayer.create(this,R.raw.very_happy);
                    break;

            }
        }
    }
}
