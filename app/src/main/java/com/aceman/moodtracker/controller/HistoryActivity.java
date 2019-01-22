package com.aceman.moodtracker.controller;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.aceman.moodtracker.model.HistoryAdapter;
import com.aceman.moodtracker.R;
import com.aceman.moodtracker.model.MoodSave;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static java.lang.System.out;

public class HistoryActivity extends AppCompatActivity {

    private List<MoodSave> MoodSaveList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loadData();
        getSharedPreferences("mMoodSave", MODE_PRIVATE);
        setContentView(R.layout.adapter_history);
        System.out.println("HistoryActivity:onCreate()");
      //  Collections.reverse(MoodSaveList);
        ListView MoodDayListView = findViewById(R.id.list_view);
        MoodDayListView.setAdapter(new HistoryAdapter(this, MoodSaveList));


    }

    private void loadData(){
        SharedPreferences mMoodSavePref = getSharedPreferences("MoodSave",MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mMoodSavePref.getString("TestList", null);
        Type type = new TypeToken<List<MoodSave>>() {}.getType();
        MoodSaveList = gson.fromJson(json, type);

        if(MoodSaveList == null){
            MoodSaveList = new ArrayList<>();
        }
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
