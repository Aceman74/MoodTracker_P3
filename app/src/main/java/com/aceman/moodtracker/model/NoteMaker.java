package com.aceman.moodtracker.model;

import android.app.Activity;
import android.app.Dialog;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.aceman.moodtracker.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * NoteMaker is the class who save the note of the day.
 *
 * @author Aceman
 * Created by Lionel JOFFRAY - on 17/01/2019.
 */
public class NoteMaker extends Dialog {

    public static String mAddNote;
    @BindView(R.id.activity_note_edit_txt) EditText mWriteText;
    @BindView(R.id.activity_note_validate_btn) Button mValidate;
    @BindView(R.id.activity_note_cancel_btn) Button mCancel;
    private String mToastText;
    public static boolean mIsNote;

    /**
     * Add a note if clicked on the Note button on all mood activity.<br>
     * Open a custom Dialog layout.
     * @param mActivity the current activity
     */
    public NoteMaker(final Activity mActivity) {
        super(mActivity, R.style.CustomDialog);
        setContentView(R.layout.activity_note);
        ButterKnife.bind(this);

        mValidate.setOnClickListener(v -> {
            String mAddNote = mWriteText.getText().toString();
            dayNote(mAddNote);
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

    /**
     * Create the popup in activity on click on addnote button.
     */
    public void buidNotePopup(){
        show();
    }

    private void customToast(){
        Toast.makeText(getContext(),mToastText,Toast.LENGTH_SHORT ).show();
    }

    private void setMText(String mToastText){
        this.mToastText = mToastText;
    }

    /**
     * Save the Note in a String.
     * @param addNote actual note
     */
    private void dayNote(String addNote){
        mAddNote = addNote;
    }

}
