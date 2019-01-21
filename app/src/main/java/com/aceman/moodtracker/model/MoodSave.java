package com.aceman.moodtracker.model;

import java.util.Calendar;
import java.util.Date;


/**
 * Created by Lionel JOFFRAY - on 18/01/2019.
 */
public class MoodSave {

    private String mDay;
    private String mMood;
    private boolean mNote;
    private String mAddNote;

    public MoodSave(String Day , String Mood, boolean Note, String AddNote){

        this.mDay = Day;
        this.mMood = Mood;
        this.mNote = Note;
        this.mAddNote = AddNote;
    }

    public String getDay(){

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

public static String getActualDay(){

    Calendar calendar = Calendar.getInstance();
    String Day;
    int getDay = calendar.get(Calendar.DAY_OF_WEEK);

    switch (getDay) {
        case 1:
            Day ="Dimanche";
            break;
        case 2:
            Day ="Lundi";
            break;
        case 3:
            Day = "Mardi";
            break;
        case 4:
            Day = "Mercredi";
            break;
        case 5:
            Day = "Jeudi";
            break;
        case 6:
            Day = "Vendredi";
            break;
        case 7:
            Day = "Samedi";
            break;
            default:
                Day = "Lundi";
    }
        return Day;
}

public static String getActualTime(){
        String Time;
    Date currentTime = Calendar.getInstance().getTime();
    Time = String.valueOf(currentTime);
    return Time;

    }

    public static int CheckDay(){
        Calendar calendar = Calendar.getInstance();
        int getDay = calendar.get(Calendar.DAY_OF_WEEK);
        switch (getDay){
            case 1:
                getDay = 6;
                break;
            case 2:
                getDay = 0;
                break;
            case 3:
                getDay = 1;
                break;
            case 4:
                getDay = 2;
                break;
            case 5:
                getDay =3;
                break;
            case 6:
                getDay = 4;
                break;
            case 7:
                getDay = 5;
                break;
        }
        return getDay;
    }

}
