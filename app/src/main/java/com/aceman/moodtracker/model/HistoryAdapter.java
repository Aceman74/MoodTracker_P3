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

import static com.aceman.moodtracker.model.MoodSave.Today;

/**
 * Adapter for the history view, get MoodSave List to ListView via inflater.
 * @see MoodSave
 * @see com.aceman.moodtracker.controller.HistoryActivity
 *
 * @author Aceman
 * Created by Lionel JOFFRAY - on 18/01/2019.
 */

public class HistoryAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private List<MoodSave> HistoryViewerList;

    public HistoryAdapter(Context context, List<MoodSave> HistoryViewerList){
        this.context = context;
        this.HistoryViewerList = HistoryViewerList;
        this.inflater = LayoutInflater.from(context);
    }

    /**
     * Get the count of the  days in the list.
     * @return only 7 days, the 8th is the actual day in the MoodSave List
     */
    @Override
    public int getCount() {
        return 7;
    } // Gets only 7 items

    /**
     * Get the item position for each day.
     * @param position Position il List
     * @return Position of item
     */
    @Override
    public Object getItem(int position) {
        return HistoryViewerList.get(position);
    }

    /**
     * Get ID of Item
     * @param i iterator
     * @return ItemId
     */
    @Override
    public long getItemId(int i) {
        return 0;
    }

    /**
     * Set the history ListView with personalized cell for each mood and day.
     * @param i
     * @param view
     * @param parent
     * @return
     */
    @Override
    public View getView(int i, View view, ViewGroup parent) {

        view = inflater.inflate(R.layout.adapter_history, null);

        MoodSave actualItem = (MoodSave) getItem(i);
        int Day = actualItem.getDay();
        String Mood = actualItem.getMood();
        boolean Note = actualItem.getNote();
        String NoteWrite = actualItem.getAddNote();

        TextView DayView = view.findViewById(R.id.history_day_text);
        TextView MoodView = view.findViewById(R.id.history_mood_text);
        LinearLayout HistoryBack = view.findViewById(R.id.mood_color);
        ImageButton NoteBtn = view.findViewById(R.id.history_comment_btn);
        SetDayOfWeek(Day,DayView);
        // DayView.setText(Day);     //Used for testing
        // MoodView.setText(Mood);   //Used for testing
        DayView.setHeight(90);
        NoteBtn.setPadding(0,0,50,130);
        NoteShow(Note,NoteWrite,NoteBtn);
        MoodColorBack(Mood,HistoryBack);

        return view;
    }

    /**
     * Set the background color for moods.
     * @param Mood get the mood saved
     * @param HistoryBack set the background color for the mood
     */
    private void MoodColorBack(String Mood, LinearLayout HistoryBack) {

        if(Mood!=null){

            switch (Mood){
                case "happy":
                     HistoryBack.setBackgroundResource(R.color.light_sage);
                    break;
                case "very_happy":
                    HistoryBack.setBackgroundResource(R.color.banana_yellow);
                    break;
                case "normal":
                    HistoryBack.setBackgroundResource(R.color.cornflower_blue_65);
                    break;
                case "bad":
                    HistoryBack.setBackgroundResource(R.color.warm_grey);
                    break;
                case "very_bad":
                    HistoryBack.setBackgroundResource(R.color.faded_red);
                    break;
                default:
                    HistoryBack.setBackgroundResource(R.color.banana_yellow);
            }
        }
    }

    /**
     * Show day note if there is one.
     * @param Note boolean to return a note
     * @param NoteWrite the string containing the note
     * @param NoteBtn show or hide the note button
     */
    private void NoteShow(boolean Note, final String NoteWrite, ImageButton NoteBtn){


        if(Note == false){
            NoteBtn.setVisibility(View.INVISIBLE);
            NoteBtn.setClickable(false);
        }else{ NoteBtn.setOnClickListener(v ->
                Toast.makeText(context,NoteWrite,Toast.LENGTH_LONG).show());

        }

    }

    /**
     * Set the day of each cell for history.
     * @param Day get the day save in the List
     * @param DayView set the day in String
     * @see MoodSave#Today()
     */
    private void SetDayOfWeek(int Day,TextView DayView ){

        if(Day == Today()-1){
            DayView.setText("Hier");
        }if(Day == Today()-2){
            DayView.setText("Avant-hier");
        }if(Day == Today()-3){
            DayView.setText("Il y a 3 jours");
        }if(Day == Today()-4){
            DayView.setText("Il y a 4 jours");
        }if(Day == Today()-5){
            DayView.setText("Il y a 5 jours");
        }if(Day == Today()-6){
            DayView.setText("Il y a 6 jours");
        }if(Day == Today()-7){
            DayView.setText("Il y a une semaine");
        }
    }
}
