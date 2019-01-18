package com.aceman.moodtracker.controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.aceman.moodtracker.R;

import static java.lang.System.out;

public class BadActivity extends AppCompatActivity {

    private float x1, x2, y1, y2;
    private FrameLayout mMainFrame;
    private ImageButton mSmiley;
    private ImageButton mNote;
    private ImageButton mHistory;
    private BadActivity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bad);
        System.out.println("BadActivity:onCreate()");
        this.mActivity = this;

        mMainFrame = findViewById(R.id.activity_bad_frame);
        mSmiley = findViewById(R.id.activity_bad_smiley_btn);
        mNote = findViewById(R.id.activity_bad_note_btn);
        mHistory = findViewById(R.id.activity_bad_history_btn);
        final Animation shake = AnimationUtils.loadAnimation(this,R.anim.shake_anim);

        mSmiley.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mSmiley.startAnimation(shake);
            }
        });

        mNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NoteActivity newNote = new NoteActivity(BadActivity.this);
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
                    Intent NormalActivity = new Intent(getApplicationContext(), NormalActivity.class);
                    startActivity(NormalActivity);
                    overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                }
                // if swipe down
                if(y2>y1){
                    Intent VeryBadActivity = new Intent(getApplicationContext(), VeryBadActivity.class);
                    startActivity(VeryBadActivity);
                    overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);
                }
                break;
        }return false;
    }

    @Override
    protected void onStart() {
        super.onStart();

        out.println("BadActivity::onStart()");
    }

    @Override
    protected void onResume() {
        super.onResume();

        out.println("BadActivity::onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();

        out.println("BadActivity::onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();

        out.println("BadActivity::onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        out.println("BadActivity::onDestroy()");
    }
}
