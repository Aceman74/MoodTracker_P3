package com.aceman.moodtracker.controller;

import android.app.Activity;
import android.app.Dialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.aceman.moodtracker.R;

/**
 * Created by Lionel JOFFRAY - on 17/01/2019.
 */

public class NoteActivity extends Dialog {

    EditText mWriteText;
    Button mValidate, mCancel;
    private String mToastText;


    public NoteActivity(final Activity mActivity)
    {
        super(mActivity, R.style.CustomDialog);
        setContentView(R.layout.activity_note);
        this.mWriteText = findViewById(R.id.activity_note_edit_txt);
        this.mValidate = findViewById(R.id.activity_note_validate_btn);
        this.mCancel = findViewById(R.id.activity_note_cancel_btn);

        mWriteText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mValidate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dayNote = mWriteText.getText().toString();
                setMText("Humeur sauvegardée!");
                customToast();
                hide();
            }
        });

        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
                setMText("Retour");
                customToast();
            }
        });
    }

    public void buidNotePopup(){

        show();
    }

    public void customToast(){

        Toast.makeText(getContext(),mToastText,Toast.LENGTH_SHORT ).show();
    }

    public void setMText( String mToastText){
        this.mToastText = mToastText;
    }

}
