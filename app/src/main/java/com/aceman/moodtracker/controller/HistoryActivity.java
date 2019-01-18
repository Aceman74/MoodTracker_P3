package com.aceman.moodtracker.controller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.aceman.moodtracker.R;

import static java.lang.System.out;

public class HistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        System.out.println("HistoryActivity:onCreate()");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_top_left, R.anim.slide_out_top_left);
    }

    @Override
    protected void onStart() {
        super.onStart();

        out.println("HistoryActivity::onStart()");
    }

    @Override
    protected void onResume() {
        super.onResume();

        out.println("HistoryActivity::onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();

        out.println("HistoryActivity::onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();

        out.println("HistoryActivity::onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        out.println("HistoryActivity::onDestroy()");
    }
}
