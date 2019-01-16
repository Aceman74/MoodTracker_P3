package com.aceman.moodtracker.controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;

import com.aceman.moodtracker.R;

public class MainHappyActivity extends AppCompatActivity {


    private float x1, x2, y1, y2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_happy);
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
                if(y2<y1){
                    Intent VeryHappyActivity = new Intent(MainHappyActivity.this, VeryHappyActivity.class);
                    startActivity(VeryHappyActivity);
                }
                break;
        }
        return false;
    }
}
