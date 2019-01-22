package com.aceman.moodtracker.model;

import android.app.Activity;
import android.app.Dialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.aceman.moodtracker.R;
import com.aceman.moodtracker.model.MoodSave;

/**
 * Created by Lionel JOFFRAY - on 17/01/2019.
 */

public class NoteMaker extends Dialog {

    public static String mAddNote;
    private EditText mWriteText;
    private Button mValidate, mCancel;
    private String mToastText;
    public static boolean mIsNote;

    // Comment for the day
    public NoteMaker(final Activity mActivity)
    {
        super(mActivity, R.style.CustomDialog);
        setContentView(R.layout.activity_note);
        this.mWriteText = findViewById(R.id.activity_note_edit_txt);
        this.mValidate = findViewById(R.id.activity_note_validate_btn);
        this.mCancel = findViewById(R.id.activity_note_cancel_btn);

        mValidate.setOnClickListener(v -> {

            String mAddNote = mWriteText.getText().toString();
            DayNote(mAddNote);
            mIsNote = true;
            setMText("Note sauvegardée!");
            customToast();
            hide();
        });

        mCancel.setOnClickListener(v -> {
            cancel();
            mIsNote = false;
            setMText("Retour");
            customToast();
        });
    }

    public void buidNotePopup(){

        show();
    }

    private void customToast(){

        Toast.makeText(getContext(),mToastText,Toast.LENGTH_SHORT ).show();
    }

    private void setMText(String mToastText){
        this.mToastText = mToastText;
    }

    // Store the daily note in a String
    private void DayNote(String addNote){


        mAddNote = addNote;

    }

}
