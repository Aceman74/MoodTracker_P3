package com.aceman.moodtracker.ui;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import com.aceman.moodtracker.R;
import com.aceman.moodtracker.utils.OnSwipeTouchListener;
import com.aceman.moodtracker.utils.ListHandler;
import com.aceman.moodtracker.data.MoodSave;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.aceman.moodtracker.utils.ListHandler.mFirstLaunch;
import static com.aceman.moodtracker.data.MoodSave.getToday;

/**
 * * MoodTracker<br>
 * Version 1.2<br>
 *
 * <b>Main class</b> is where the user choose his mood by swiping and contains three buttons:<br>
 * - Smiley (center) who shake and make sound on click <br>
 * - Note (bot left) who add a note with the mood <br>
 * - History for seeing history on 7 days<br>
 * Mood is automatically saved by swiping <br>
 * mMood: 0 Very Bad, 1 Bad, 2 Normal, 3 Happy, 4 Very Happy<br>
 *
 * @author Aceman
 */
public class MainActivity extends AppCompatActivity {

    @BindView(R.id.activity_main_frame)
    FrameLayout mFrame;
    @BindView(R.id.activity_main_smiley_btn)
    ImageButton mSmiley;
    @BindView(R.id.activity_main_note_btn)
    ImageButton mNote;
    @BindView(R.id.activity_main_history_btn)
    ImageButton mHistory;
    @BindView(R.id.activity_main_share)
    ImageButton mShare;
    @BindString(R.string.mood_is_saved)
    String mSavedString;
    @BindString(R.string.sharebody_1)
    String mShareString_1;
    @BindString(R.string.sharebody_2)
    String mShareString_2;
    @BindString(R.string.my_mood)
    String mMyMoodString;
    @BindString(R.string.mood_very_bad)
    String mMoodVeryBad;
    @BindString(R.string.mood_bad)
    String mMoodBad;
    @BindString(R.string.mood_normal)
    String mMoodNormal;
    @BindString(R.string.mood_happy)
    String mMoodHappy;
    @BindString(R.string.mood_very_happy)
    String mMoodVeryHappy;
    @BindString(R.string.commentary)
    String mCommentary;
    @BindString(R.string.Add_Note)
    String mDescriptionNote;
    @BindString(R.string.confirm)
    String mValidate;
    @BindString(R.string.back)
    String mCancel;
    @BindString(R.string.note_save)
    String mNoteIsSaved;
    @BindString(R.string.note_empty)
    String mNoteEmpty;
    @BindString(R.string.history_empty)
    String mHistoryEmpty;
    final static String SPLaunch = "LaunchToday";
    final static String SPLaunchToday = "IsFirstLaunchToday";
    final static String SPMoodSave = "MoodSave";
    final static String SPMoodSaveList = "Mood_List";
    final static String SPMood = "Mood";
    final static String SPmMood = "mMood";
    final static String SPNote = "Note";
    final static String SPNoteSaved = "NoteSaved";
    static String mAddNote = null;
    static boolean mIsNote;
    public static List<MoodSave> mMoodSaveList;
    public static int mLastDay;
    public static int mMood;
    private String mMoodLang;
    private MediaPlayer mSound;
    public static Animation mShake;
    public static Animation mPulse;
    public static Animation mFadeIn;
    public static Animation mFadeOut;
    public static Animation mSlideIn;

    /**
     * Setting the Main mood view, with smileys and buttons.
     *
     * @param savedInstanceState saved instance
     */
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        animSet();
        loadData();
        setMoodOnSwipe();

        mFrame.setOnTouchListener(new OnSwipeTouchListener(this) {      //  Swipe listener
            @Override
            public void onSwipeDown() {
                if (mMood > 0) {
                    mMood--;
                }
                swipeAnim();
            }

            public void onSwipeUp() {
                if (mMood < 4) {
                    mMood++;
                }
                swipeAnim();
            }
        });

    }

    /**
     * Click on Smiley center.
     */
    @OnClick(R.id.activity_main_smiley_btn)
    void onClickSmiley() {
        mSound.start();    // sound on smiley click
        mShare.startAnimation(mPulse);
        mSmiley.startAnimation(mShake);  // anim on smiley click
    }

    /**
     * Click on History button.
     *
     * @see HistoryActivity
     */
    @OnClick(R.id.activity_main_history_btn)
    void onClickHistory() {
        mHistory.startAnimation(mFadeOut);
        Intent HistoryActivity = new Intent(getApplicationContext(), HistoryActivity.class);
        startActivity(HistoryActivity); // show history
        overridePendingTransition(R.anim.slide_in_bot_right, R.anim.slide_out_bot_right);
        if(mFirstLaunch){
            Toast.makeText(this, mHistoryEmpty, Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Click on Note button.
     */
    @OnClick(R.id.activity_main_note_btn)
    void onClickNote() {
        mNote.startAnimation(mFadeOut);
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(mCommentary);
        alert.setMessage(mDescriptionNote);

        final EditText input = new EditText(this);
        alert.setView(input);
        if (mAddNote != null) {
            input.setText(mAddNote);
        }

        alert.setPositiveButton(mValidate, (dialog, whichButton) -> {
            mAddNote = input.getText().toString();

            dayNote(mAddNote);
            Toast.makeText(this, mNoteIsSaved, Toast.LENGTH_LONG).show();
            if (mAddNote == null || mAddNote.isEmpty()) {
                Toast.makeText(this, mNoteEmpty, Toast.LENGTH_LONG).show();
            }
            saveMoodToday();
            saveData();
        });

        alert.setNegativeButton(mCancel, (dialog, whichButton) -> {
            if (mAddNote == null || mAddNote.isEmpty()) {
                Toast.makeText(this, mNoteEmpty, Toast.LENGTH_LONG).show();
            }
            Toast.makeText(this, mCancel, Toast.LENGTH_LONG).show();
            saveMoodToday();
            saveData();
        });
        alert.show();
    }

    /**
     * Click on Share button .
     */
    @OnClick(R.id.activity_main_share)
    void onClickShare() {
        shareClick();
    }

    /**
     * Share method button who shows after a click on the smiley.
     */
    private void shareClick() {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = mShareString_1 + " " + mMoodLang + " " + mShareString_2;
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, mMyMoodString);
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    /**
     * Method to initiate/stop animations and sound on swipe.
     */
    private void swipeAnim() {
        mSound.stop();  // stop sound created with swipe movement
        mSound.release();
        setMoodOnSwipe(); // Set the mood view
        mSmiley.startAnimation(mFadeIn);
        mFrame.startAnimation(mFadeIn);
    }

    /**
     * Saving all necessary values.
     *
     * @see Gson
     * @see MoodSave
     */
    public void saveData() {    // save SharedPreferences and mLastDay
        SharedPreferences mFirstLaunchToday = getSharedPreferences(SPLaunch, MODE_PRIVATE);
        SharedPreferences.Editor firstLaunch = mFirstLaunchToday.edit();
        firstLaunch.putInt(SPLaunchToday, mLastDay);
        firstLaunch.apply();
        SharedPreferences mMoodSP = getSharedPreferences(SPMood, MODE_PRIVATE);
        SharedPreferences.Editor mMoodSave = mMoodSP.edit();
        mMoodSave.putInt(SPmMood, mMood);
        mMoodSave.apply();
        SharedPreferences mMoodSavePref = getSharedPreferences(SPMoodSave, MODE_PRIVATE);
        SharedPreferences.Editor editor = mMoodSavePref.edit();
        Gson gson = new Gson();
        String json = gson.toJson(mMoodSaveList);
        editor.putString(SPMoodSaveList, json);
        editor.apply();
        SharedPreferences noteSave = getSharedPreferences(SPNote, MODE_PRIVATE);
        SharedPreferences.Editor mNoteSave = noteSave.edit();
        mNoteSave.putString(SPNoteSaved, mAddNote);
        mNoteSave.apply();
    }

    /**
     * Load the mood List or create one if first launch ever. <br>
     * Also load mMood (to show last state mood) and mLastDay to check last launch <br>
     * Also move the items in mMoodSaveList everyday for showing 7 past days in history.
     */
    private void loadData() {
        SharedPreferences noteSave = getSharedPreferences(SPNote, MODE_PRIVATE);
        mAddNote = noteSave.getString(SPNoteSaved, mAddNote);
        SharedPreferences mMoodSP = getSharedPreferences(SPMood, MODE_PRIVATE);
        mMood = mMoodSP.getInt(SPmMood, mMood);
        SharedPreferences mFirstLaunchToday = getSharedPreferences(SPLaunch, MODE_PRIVATE);
        mLastDay = mFirstLaunchToday.getInt(SPLaunchToday, mLastDay);
        SharedPreferences mMoodSavePref = getSharedPreferences(SPMoodSave, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mMoodSavePref.getString(SPMoodSaveList, null);
        Type type = new TypeToken<List<MoodSave>>() {
        }.getType();
        mMoodSaveList = gson.fromJson(json, type);
        ListHandler.listCreate();   // create a first list if first launch or deleted list
        ListHandler.saveMover();    // move items on past 7 day if it is a new day + reset mood
        checkLaunchToday();  // update and check the day
        saveData();
    }

    /**
     * Check the first launch of the day.
     *
     * @see MoodSave#getToday()
     */
    private void checkLaunchToday() {    // compare date
        if (getToday() != mLastDay) {
            mLastDay = getToday();
        }
    }

    /**
     * Reset mood view and note when launch on new day
     */
    public static void resetMood() {
        mMood = 3;              // set mood happy
        mAddNote = null;        // reset note
        mIsNote = false;
    }

    /**
     * Get the mMood value to set the mood to show w/ each sound, smiley and background.<br>
     */
    private void setMoodOnSwipe() {

        if (mMood <= 4 && mMood >= 0) {

            switch (mMood) {
                case 0:
                    mMoodLang = mMoodVeryBad;
                    mFrame.setBackgroundResource(R.color.faded_red);
                    mSmiley.setImageResource(R.drawable.smiley_sad);
                    mSound = MediaPlayer.create(this, R.raw.very_bad);
                    break;
                case 1:
                    mMoodLang = mMoodBad;
                    mFrame.setBackgroundResource(R.color.warm_grey);
                    mSmiley.setImageResource(R.drawable.smiley_disappointed);
                    mSound = MediaPlayer.create(this, R.raw.bad);
                    break;
                case 2:
                    mMoodLang = mMoodNormal;
                    mFrame.setBackgroundResource(R.color.cornflower_blue_65);
                    mSmiley.setImageResource(R.drawable.smiley_normal);
                    mSound = MediaPlayer.create(this, R.raw.normal);
                    break;
                case 3:
                    mMoodLang = mMoodHappy;
                    mFrame.setBackgroundResource(R.color.light_sage);
                    mSmiley.setImageResource(R.drawable.smiley_happy);
                    mSound = MediaPlayer.create(this, R.raw.happy);
                    break;
                case 4:
                    mMoodLang = mMoodVeryHappy;
                    mFrame.setBackgroundResource(R.color.banana_yellow);
                    mSmiley.setImageResource(R.drawable.smiley_super_happy);
                    mSound = MediaPlayer.create(this, R.raw.very_happy);
                    break;
            }
        }
        onSmileyView();
    }

    /**
     * Method called for automatic save on swipe.
     */
    void onSmileyView() {
        saveMoodToday(); // Temporary save slot
        saveData();
    }

    /**
     * Get the daynote for MoodSave.
     *
     * @param addNote note written by user
     */
    private void dayNote(String addNote) {
        mAddNote = addNote;
    }

    /**
     * Save slot for the day with note checker.
     */
    private void saveMoodToday() {
        if (mAddNote == null || mAddNote.isEmpty()) {
            mIsNote = false;
        } else {
            mIsNote = true;
        }
        mMoodSaveList.remove(7);
        mMoodSaveList.add(7, new MoodSave(getToday(), mMood, mIsNote, mAddNote));
    }

    /**
     * Initialize animations
     */
    void animSet() {
        mShake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake_anim);    // Smiley onclick animation
        mPulse = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.pulse_effect);    //  Share btn pulse effect
        mFadeIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);    //  Smoother transition
        mFadeOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_out);    //  Click anim effect
        mSlideIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_right);    //  Historty anim
    }
}
