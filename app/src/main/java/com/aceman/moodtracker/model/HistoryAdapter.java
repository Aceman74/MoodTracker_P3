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

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

import static com.aceman.moodtracker.model.MoodSave.getToday;

/**
 * Adapter for the history view, get MoodSave ListView via inflater.
 * @see MoodSave
 * @see com.aceman.moodtracker.controller.HistoryActivity
 *
 * @author Aceman
 * Created by Lionel JOFFRAY - on 18/01/2019.
 */
public class HistoryAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private List<MoodSave> mHistoryViewerList;

    public HistoryAdapter(Context context, List<MoodSave> HistoryViewerList){
        this.context = context;
        this.mHistoryViewerList = HistoryViewerList;
        this.inflater = LayoutInflater.from(context);
    }

    /**
     * Get the count of the  days in the list.
     * @return  MoodSave List size
     */
    @Override
    public int getCount() {
        return mHistoryViewerList.size();
    }

    /**
     * Get the item position for each day.
     * @param position Position il List
     * @return Position of item
     */
    @Override
    public Object getItem(int position) {
        return mHistoryViewerList.get(position);
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
     * @param i i
     * @param view view
     * @param parent unused
     * @return the view inflater
     */
    @Override
    public View getView(int i, View view, ViewGroup parent) {
        ViewHolder holder;

        if(view==null) {
            view = inflater.inflate(R.layout.activity_history, null);
            // well set up the ViewHolder
            holder = new ViewHolder(view);
            view.setTag(holder);
        }else
        {
            holder = (ViewHolder) view.getTag();

        }

        MoodSave actualItem = (MoodSave) getItem(i);
        int Day = actualItem.getDay();
        String Mood = actualItem.getMood();
        boolean Note = actualItem.getNote();
        String NoteWrite = actualItem.getAddNote();
        // TextView MoodView = view.findViewById(R.id.history_mood_text); // Used for testing

        // DayView.setText(Day);     // Used for testing
        // MoodView.setText(Mood);   // Used for testing
        setDayOfWeek(Day,holder.DayView,holder);
        holder.DayView.setHeight(90);
        holder.NoteBtn.setPadding(0,0,50,130);
        noteShow(Note,NoteWrite,holder.NoteBtn);
        moodColorBack(Mood,holder.HistoryBack);
        return view;
    }

    /**
     * Binding view for ButterKnife with a ViewHolder
     */
    static final class ViewHolder {
        @BindView(R.id.history_day_text) TextView DayView;
        @BindView(R.id.mood_color) LinearLayout HistoryBack;
        @BindView(R.id.history_comment_btn) ImageButton NoteBtn;
        @BindString(R.string.yesterday) String mYesterday;
        @BindString(R.string.two_days) String mTwoDays;
        @BindString(R.string.three_day) String mThreeDays;
        @BindString(R.string.four_day) String mFourDays;
        @BindString(R.string.five_day) String mFiveDays;
        @BindString(R.string.six_day) String mSixDays;
        @BindString(R.string.one_week) String mOneWeek;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    /**
     * Set the background color for moods.
     * @param Mood get the mood saved
     * @param HistoryBack set the background color for the mood
     */
    private void moodColorBack(String Mood, LinearLayout HistoryBack) {

        if(Mood!=null){
            switch (Mood){
                case "Happy":
                    HistoryBack.setBackgroundResource(R.color.light_sage);
                    break;
                case "Very Happy":
                    HistoryBack.setBackgroundResource(R.color.banana_yellow);
                    break;
                case "Normal":
                    HistoryBack.setBackgroundResource(R.color.cornflower_blue_65);
                    break;
                case "Bad":
                    HistoryBack.setBackgroundResource(R.color.warm_grey);
                    break;
                case "Very Bad":
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
    private void noteShow(boolean Note, final String NoteWrite, ImageButton NoteBtn){

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
     * @see MoodSave#getToday()
     */
    private void setDayOfWeek(int Day, TextView DayView, ViewHolder holder ){

        if(Day == getToday()-1){
            DayView.setText(holder.mYesterday);
        }if(Day == getToday()-2){
            DayView.setText(holder.mTwoDays);
        }if(Day == getToday()-3){
            DayView.setText(holder.mThreeDays);
        }if(Day == getToday()-4){
            DayView.setText(holder.mFourDays);
        }if(Day == getToday()-5){
            DayView.setText(holder.mFiveDays);
        }if(Day == getToday()-6){
            DayView.setText(holder.mSixDays);
        }if(Day == getToday()-7){
            DayView.setText(holder.mOneWeek);
        }
    }
}
