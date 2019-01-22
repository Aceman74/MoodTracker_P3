package com.aceman.moodtracker.model;

import java.util.Calendar;


/**
 * Created by Lionel JOFFRAY - on 18/01/2019.
 */
public class MoodSave {

    private int mDay;
    private String mMood;
    private boolean mNote;
    private String mAddNote;

    // Setting all informations for the list
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


    //Get the day
    public static int Today(){

        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.DAY_OF_YEAR);
    }
    /*
    public static int Yesterday(){

        Calendar calendar = Calendar.getInstance();
        int Yesterday = calendar.get(Calendar.DAY_OF_YEAR);
        Yesterday--;
        return Yesterday;
    }
    */
}
