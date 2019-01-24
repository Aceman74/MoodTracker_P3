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
import java.util.List;

import static com.aceman.moodtracker.model.NoteMaker.mAddNote;
import static com.aceman.moodtracker.model.NoteMaker.mIsNote;
import static com.aceman.moodtracker.model.MoodSave.Today;
import static java.lang.System.out;
/**
 * <b>Normal class</b> with view Normal Mood and three buttons:<br>
 * - Smiley (center) who save the actual mood<br>
 * - Note (bot left) who add a note with the mood<br>
 * - History for seeing history on 7 days<br>
 *
 * @author Aceman
 */
public class NormalActivity extends AppCompatActivity {

    private float x1, x2, y1, y2;
    private ImageButton mSmiley;
    private ImageButton mNote;
    private ImageButton mHistory;
    private List<MoodSave> MoodSaveList;

    /**
     * Setting the actual mood view, with smiley and buttons.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal);
        System.out.println("NormalActivity:onCreate()");
        final MediaPlayer clickSmiley = MediaPlayer.create(this,R.raw.normal);  // sound on smiley click
        final Animation shake = AnimationUtils.loadAnimation(this,R.anim.shake_anim);   // anim on smiley click
        loadData();

        findViewById(R.id.activity_normal_frame);
        mSmiley = findViewById(R.id.activity_normal_smiley_btn);
        mNote = findViewById(R.id.activity_normal_note_btn);
        mHistory = findViewById(R.id.activity_normal_history_btn);

        mSmiley.setOnClickListener(v -> {
            MoodSaveList.add(7, new MoodSave(Today(),"normal", mIsNote,mAddNote));
            saveData();
            clickSmiley.start();    // sound on smiley click
            mSmiley.startAnimation(shake);  // anim on smiley click
            Toast.makeText(getApplication(),"Humeur sauvegardée!",Toast.LENGTH_SHORT ).show();
        });

        mNote.setOnClickListener(v -> {
            NoteMaker newNote = new NoteMaker(NormalActivity.this);
            newNote.buidNotePopup();    // add a daily comment
        });

        mHistory.setOnClickListener(v -> {
            Intent HistoryActivity = new Intent(getApplicationContext(), HistoryActivity.class);
            startActivity(HistoryActivity); // show history
            overridePendingTransition(R.anim.slide_in_bot_right, R.anim.slide_out_bot_right);
        });
    }
    /**
     * Swipe event for up and down.
     * @param swipeEvent get the movement
     * @return better mood if swipe up, worst if swipe down
     * @see MainHappyActivity swipe up
     * @see BadActivity swipe down
     */
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
    /**
     * Save the mood to a List using Gson.
     * @see Gson
     * @see MoodSave
     */
    private void saveData(){
        SharedPreferences mMoodSavePref = getSharedPreferences("MoodSave",MODE_PRIVATE);
        SharedPreferences.Editor editor = mMoodSavePref.edit();
        Gson gson = new Gson();
        String json = gson.toJson(MoodSaveList);
        editor.putString("TestList",json);
        editor.apply();
    }
    /**
     * Load the mood List.
     */
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
        out.println("normal::onStart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        out.println("normal::onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        out.println("normal::onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        out.println("normal::onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        out.println("normal::onDestroy()");
    }
}
