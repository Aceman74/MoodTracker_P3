package com.aceman.moodtracker.utils;


import com.aceman.moodtracker.data.MoodSave;

import java.util.ArrayList;

import static com.aceman.moodtracker.data.MoodSave.getToday;
import static com.aceman.moodtracker.ui.MainActivity.mLastDay;
import static com.aceman.moodtracker.ui.MainActivity.mMoodSaveList;
import static com.aceman.moodtracker.ui.MainActivity.resetMood;

/**
 * Class for Mood List related events.<br>
 * <p>
 * Created by Lionel JOFFRAY - on 12/02/2019.
 */
public class ListHandler {

    public static boolean mFirstLaunch = false;
    /**
     * Create empty list on first launch.
     */
    public static void listCreate() {
        if (mMoodSaveList == null) {
            mMoodSaveList = new ArrayList<>();
            mMoodSaveList.add(0, new MoodSave(getToday() - 8, 5, false, null));
            mMoodSaveList.add(1, new MoodSave(getToday() - 8, 5, false, null));
            mMoodSaveList.add(2, new MoodSave(getToday() - 8, 5, false, null));
            mMoodSaveList.add(3, new MoodSave(getToday() - 8, 5, false, null));
            mMoodSaveList.add(4, new MoodSave(getToday() - 8, 5, false, null));
            mMoodSaveList.add(5, new MoodSave(getToday() - 8, 5, false, null));
            mMoodSaveList.add(6, new MoodSave(getToday() - 8, 5, false, null));
            mMoodSaveList.add(7, new MoodSave(getToday(), 3, false, null));
            resetMood();
            mFirstLaunch = true;
        }
    }

    /**
     * Move items on past 7 day if it is a new day.<br>
     * <mark>(note: works if launch from 1st January to 7th)</mark>
     */
    public static void saveMover() {
        int today = getToday();
        int dayLeft = today - mLastDay;

        if (today >= 1 && today <= 7) { //    For the first week of the year
            today = today + 365;
        }
        if (today != mLastDay && mLastDay != 0) { //  Set +1 position in history view
            do {
                mMoodSaveList.set(0, mMoodSaveList.get(1));
                mMoodSaveList.set(1, mMoodSaveList.get(2));
                mMoodSaveList.set(2, mMoodSaveList.get(3));
                mMoodSaveList.set(3, mMoodSaveList.get(4));
                mMoodSaveList.set(4, mMoodSaveList.get(5));
                mMoodSaveList.set(5, mMoodSaveList.get(6));
                mMoodSaveList.set(6, mMoodSaveList.get(7)); //  The last saved mood
                mMoodSaveList.remove(7);
                mMoodSaveList.add(7, new MoodSave((today - dayLeft) + 1, 3, false, null));
                mLastDay++;
                dayLeft--;
            }
            while (mLastDay < today);
            resetMood();
            mFirstLaunch = false;
        }
    }
}
