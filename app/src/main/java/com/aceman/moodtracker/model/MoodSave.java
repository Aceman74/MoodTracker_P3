package com.aceman.moodtracker.model;

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
    private String mMood;
    private boolean mNote;
    private String mAddNote;

    /**
     * Setting the informations to save.
     * @param Day Actual day
     * @param Mood actual screen mood
     * @param Note boolean check for note added
     * @param AddNote note as String
     */
    public MoodSave(int Day , String Mood, boolean Note, String AddNote){

        this.mDay = Day;
        this.mMood = Mood;
        this.mNote = Note;
        this.mAddNote = AddNote;
    }

    int getDay(){

        return mDay;
    }

    String getMood(){

        return mMood;
    }

    boolean getNote(){

        return mNote;
    }

    String getAddNote(){

        return mAddNote;
    }


    /**
     * Get the DAY_OF_YEAR
     * @return the day of the year
     * @see Calendar
     * <mark>(note: may cause problem if launch 1st January)</mark>
     */
    public static int Today(){

        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.DAY_OF_YEAR);
    }
    public static int Yesterday(){

        Calendar calendar = Calendar.getInstance();
        int Yesterday = calendar.get(Calendar.DAY_OF_YEAR);
        Yesterday--;
        return Yesterday;
    }
}
