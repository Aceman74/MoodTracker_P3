package com.aceman.moodtracker.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
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

import static com.aceman.moodtracker.UI.MainActivity.*;
import static com.aceman.moodtracker.model.MoodSave.getToday;

/**
 * Adapter for the history view, get MoodSave ListView via inflater.
 *
 * @author Aceman
 * Created by Lionel JOFFRAY - on 18/01/2019.
 * @see MoodSave
 * @see com.aceman.moodtracker.UI.HistoryActivity
 */
public class HistoryAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private List<MoodSave> mHistoryViewerList;

    public HistoryAdapter(Context context, List<MoodSave> historyViewerList) {
        this.context = context;
        this.mHistoryViewerList = historyViewerList;
        this.inflater = LayoutInflater.from(context);

    }

    /**
     * Get the count of the  days in the list.
     *
     * @return MoodSave List size
     */
    @Override
    public int getCount() {
        return mHistoryViewerList.size();
    }

    /**
     * Get the item position for each day.
     *
     * @param position Position il List
     * @return Position of item
     */
    @Override
    public Object getItem(int position) {
        return mHistoryViewerList.get(position);
    }

    /**
     * Get ID of Item
     *
     * @param i iterator
     * @return ItemId
     */
    @Override
    public long getItemId(int i) {
        return 0;
    }

    /**
     * Set the history ListView with personalized cell for each mood and day.
     *
     * @param i      i
     * @param view   view
     * @param parent unused
     * @return the view inflater
     */
    @Override
    public View getView(int i, View view, ViewGroup parent) {
        ViewHolder holder;


        if (view == null) {
            view = inflater.inflate(R.layout.activity_history, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        MoodSave actualItem = (MoodSave) getItem(i);
        int Day = actualItem.getDay();
        int Mood = actualItem.getMood();
        boolean Note = actualItem.getNote();
        String NoteWrite = actualItem.getAddNote();
        setDayOfWeek(Day, holder.DayView, holder);
        noteShow(Note, NoteWrite, holder.NoteBtn);
        moodColorBack(Mood, holder.HistoryBack);
        return view;
    }

    /**
     * Set the background color for moods and the size.
     *
     * @param mood        get the mood saved
     * @param historyBack set the background color for the mood
     */
    private void moodColorBack(int mood, LinearLayout historyBack) {

        switch (mood) {
            case 0:
                historyBack.setBackgroundResource(R.color.faded_red);
                historyBack.setLayoutParams(new AbsListView.LayoutParams(mWidth/5, (mHeight-100)/7));
                break;
            case 1:
                historyBack.setBackgroundResource(R.color.warm_grey);
                historyBack.setLayoutParams(new AbsListView.LayoutParams(mWidth/3, (mHeight-100)/7));
                break;
            case 2:
                historyBack.setBackgroundResource(R.color.cornflower_blue_65);
                historyBack.setLayoutParams(new AbsListView.LayoutParams(mWidth/2, (mHeight-100)/7));
                break;
            case 3:
                historyBack.setBackgroundResource(R.color.light_sage);
                historyBack.setLayoutParams(new AbsListView.LayoutParams(mWidth-(mWidth/4), (mHeight-100)/7));
                break;
            case 4:
                historyBack.setBackgroundResource(R.color.banana_yellow);
                historyBack.setLayoutParams(new AbsListView.LayoutParams(mWidth, (mHeight-100)/7));
                break;
            default:
                historyBack.setBackgroundResource(R.color.darker_gray);
                historyBack.setLayoutParams(new AbsListView.LayoutParams(mWidth, (mHeight-100)/7));
        }
    }

    /**
     * Show day note if there is one.
     *
     * @param mNote     boolean to return a note
     * @param noteWrite the string containing the note
     * @param noteBtn   show or hide the note button
     */
    private void noteShow(boolean mNote, final String noteWrite, ImageButton noteBtn) {

        if (!mNote) {
            noteBtn.setVisibility(View.INVISIBLE);
            noteBtn.setClickable(false);
        } else {
            noteBtn.setOnClickListener(v ->
                    Toast.makeText(context, noteWrite, Toast.LENGTH_LONG).show());
        }
    }

    /**
     * Set the day of each cell for history.
     *
     * @param Day     get the day save in the List
     * @param dayView set the day in String
     * @see MoodSave#getToday()
     */
    private void setDayOfWeek(int Day, TextView dayView, ViewHolder holder) {

        if (Day == getToday() - 1) {
            dayView.setText(holder.mYesterday);
        }
        if (Day == getToday() - 2) {
            dayView.setText(holder.mTwoDays);
        }
        if (Day == getToday() - 3) {
            dayView.setText(holder.mThreeDays);
        }
        if (Day == getToday() - 4) {
            dayView.setText(holder.mFourDays);
        }
        if (Day == getToday() - 5) {
            dayView.setText(holder.mFiveDays);
        }
        if (Day == getToday() - 6) {
            dayView.setText(holder.mSixDays);
        }
        if (Day == getToday() - 7) {
            dayView.setText(holder.mOneWeek);
        }
    }

    /**
     * Binding view for ButterKnife with a ViewHolder.
     */
    static final class ViewHolder {
        @BindView(R.id.history_day_text)
        TextView DayView;
        @BindView(R.id.mood_color)
        LinearLayout HistoryBack;
        @BindView(R.id.history_comment_btn)
        ImageButton NoteBtn;
        @BindString(R.string.yesterday)
        String mYesterday;
        @BindString(R.string.two_days)
        String mTwoDays;
        @BindString(R.string.three_day)
        String mThreeDays;
        @BindString(R.string.four_day)
        String mFourDays;
        @BindString(R.string.five_day)
        String mFiveDays;
        @BindString(R.string.six_day)
        String mSixDays;
        @BindString(R.string.one_week)
        String mOneWeek;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
