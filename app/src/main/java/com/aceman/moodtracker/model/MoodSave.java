package com.aceman.moodtracker.model;

import java.util.Calendar;
import java.util.Date;


/**
 * Created by Lionel JOFFRAY - on 18/01/2019.
 */
public class MoodSave {

    private int mDay;
    private String mMood;
    private boolean mNote;
    private String mAddNote;

    public MoodSave(int Day , String Mood, boolean Note, String AddNote){

        this.mDay = Day;
        this.mMood = Mood;
        this.mNote = Note;
        this.mAddNote = AddNote;
    }

    public int getDay(){

        return mDay;
    }

    public String getMood(){

        return mMood;
    }

    public boolean getNote(){

        return mNote;
    }

    public String getAddNote(){

        return mAddNote;
    }


    public static int Today(){

        Calendar calendar = Calendar.getInstance();
        int DayIs = calendar.get(Calendar.DAY_OF_YEAR);
        return DayIs;
    }
    public static int Yesterday(){

        Calendar calendar = Calendar.getInstance();
        int Yesterday = calendar.get(Calendar.DAY_OF_YEAR);
        Yesterday--;
        return Yesterday;
    }

}
