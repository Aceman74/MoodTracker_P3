package com.aceman.moodtracker.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aceman.moodtracker.R;

import java.util.List;


/**
 * Created by Lionel JOFFRAY - on 18/01/2019.
 */
public class HistoryAdapter extends BaseAdapter {

    private Context context;
    LayoutInflater inflater;

    private List<MoodSave> HistoryViewerList;

    public HistoryAdapter(Context context, List<MoodSave> HistoryViewerList){
        this.context = context;
        this.HistoryViewerList = HistoryViewerList;
        this.inflater = LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return 7;
    }

    @Override
    public Object getItem(int position) {
        return HistoryViewerList.get(position);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup parent) {

        view = inflater.inflate(R.layout.adapter_history, null);

        MoodSave actualItem = (MoodSave) getItem(i);
        String Day = actualItem.getDay();
        String Mood = actualItem.getMood();
        Boolean Note = actualItem.getNote();
        String NoteWrite = actualItem.getAddNote();

        TextView DayView = view.findViewById(R.id.history_day_text);
        TextView MoodView = view.findViewById(R.id.history_mood_text);
        LinearLayout HistoryBack = view.findViewById(R.id.mood_color);
        ImageButton NoteBtn = view.findViewById(R.id.history_comment_btn);
        DayView.setText(Day);
        MoodView.setText(Mood);
        DayView.setHeight(200);
        NoteShow(Note,NoteWrite,NoteBtn);
        MoodColorBack(Mood,HistoryBack);

        return view;
    }

    private void MoodColorBack(String Mood, LinearLayout HistoryBack) {

        if(Mood!=null){

            switch (Mood){
                case "Happy":
                     HistoryBack.setBackgroundResource(R.color.light_sage);
                    break;
                case "VeryHappy":
                    HistoryBack.setBackgroundResource(R.color.banana_yellow);
                    break;
                case "Normal":
                    HistoryBack.setBackgroundResource(R.color.cornflower_blue_65);
                    break;
                case "Bad":
                    HistoryBack.setBackgroundResource(R.color.warm_grey);
                    break;
                case "VeryBad":
                    HistoryBack.setBackgroundResource(R.color.faded_red);
                    break;
                default:
                    HistoryBack.setBackgroundResource(R.color.banana_yellow);
            }
        }
    }

    private void NoteShow(boolean Note, final String NoteWrite, ImageButton NoteBtn){


        if(Note == false){
            NoteBtn.setVisibility(View.INVISIBLE);
            NoteBtn.setClickable(false);
        }else{ NoteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,NoteWrite,Toast.LENGTH_LONG).show();
            }
        });

        }

    }
}
