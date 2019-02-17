package com.aceman.moodtracker.data;

import java.util.Calendar;

/**
 * Save the user preferences in a List MoodSave:<br>
 * - Day of the year<br>
 * - Mood of the day<br>
 * - Save a note if one is added<br>
 *
 * @author Aceman
 * Created by Lionel JOFFRAY - on 18/01/2019.
 */
public class MoodSave {

    private int mDay;
    private int mMood;
    private boolean mNote;
    private String mAddNote;

    /**
     * Setting the informations to save.
     *
     * @param day     Actual day
     * @param mood    actual screen mood
     * @param note    boolean check for note added
     * @param AddNote note as String
     */
    public MoodSave(int day, int mood, boolean note, String AddNote) {
        this.mDay = day;
        this.mMood = mood;
        this.mNote = note;
        this.mAddNote = AddNote;
    }

    /**
     * Get the DAY_OF_YEAR.
     *
     * @return the day of the year
     * @see Calendar
     */
    public static int getToday() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.DAY_OF_YEAR);
    }

   public int getDay() {
        return mDay;
    }

    public int getMood() {
        return mMood;
    }

    public boolean getNote() {
        return mNote;
    }

    public String getAddNote() {
        return mAddNote;
    }
}
