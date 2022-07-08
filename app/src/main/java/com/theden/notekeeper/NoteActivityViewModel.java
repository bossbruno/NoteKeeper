package com.theden.notekeeper;

import android.os.Bundle;

import androidx.lifecycle.ViewModel;

public class NoteActivityViewModel extends ViewModel  {
    public static final String ORIGINAL_NOTE_COURSE_ID = "com.theden.notekeeper.ORIGINAL_NOTE_COURSE_ID";
    public static final String ORIGINAL_NOTE_TITLE = "com.theden.notekeeper.ORIGINAL_NOTE_TITLE";
    public static final String ORIGINAL_NOTE_TEXT = "com.theden.notekeeper.ORIGINAL_NOTE_TEXT";


    public String mOriginalNoteCourseId;
    public String mOriginalNoteTitle;
    public String mOriginalNoteText;
    public boolean mIsNewlyCreated = true;

    public void saveState(Bundle outState) {

outState.putString(ORIGINAL_NOTE_COURSE_ID, mOriginalNoteCourseId);
        outState.putString(ORIGINAL_NOTE_TEXT, mOriginalNoteText);
        outState.putString(ORIGINAL_NOTE_TITLE, mOriginalNoteTitle);
    }

    public void restoreState(Bundle inState){
        inState.putString(ORIGINAL_NOTE_COURSE_ID, mOriginalNoteCourseId);
        inState.putString(ORIGINAL_NOTE_TEXT, mOriginalNoteText);
        inState.putString(ORIGINAL_NOTE_TITLE, mOriginalNoteTitle);
    }
}
