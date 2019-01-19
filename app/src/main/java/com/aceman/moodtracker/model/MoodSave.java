package com.aceman.moodtracker.model;

/**
 * Created by Lionel JOFFRAY - on 18/01/2019.
 */
public class MoodSave {

    private String mDay;
    private String mMood;
    private int mNote;

    public MoodSave(String Day , String Mood, int Note){

        this.mDay = Day;
        this.mMood = Mood;
        this.mNote = Note;
    }

    public void setDay(String setDay){
        setDay = mDay;
    }
    public String getDay(){

        return mDay;
    }

    public String getMood(){

        return mMood;
    }

    public int getNote(){

        return mNote;
    }
}
