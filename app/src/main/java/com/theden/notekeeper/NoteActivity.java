package com.theden.notekeeper;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

//import com.theden.notekeeper.databinding.ActivityMainBinding;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import java.util.List;

public class NoteActivity extends AppCompatActivity {
public static final String NOTE_POSITION = "com.theden.notekeeper.NOTE_POSITION";
    private NoteInfo mNote;
    private boolean mIsNewNote;
    private Spinner mSpinnercourses;
    private EditText mTextnoteTitle;
    private EditText mTextnoteText;
    public Button mButton_mail;
    public Button mButton_Cancel;
    private int mNotePosition;
    private boolean mIsCancelling;
private NoteActivityViewModel mViewModel;


//  private AppBarConfiguration appBarConfiguration;
    //private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

      //  binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_note);
        Toolbar toolbar = findViewById(R.id.toolbar);
       setSupportActionBar(toolbar);

       ViewModelProvider viewModelProvider = new ViewModelProvider(getViewModelStore(), ViewModelProvider.
               AndroidViewModelFactory.getInstance(getApplication()));

       mViewModel= viewModelProvider.get(NoteActivityViewModel.class);
       if(mViewModel.mIsNewlyCreated && savedInstanceState !=null  )
           mViewModel.restoreState(savedInstanceState );
       mViewModel.mIsNewlyCreated = false;

         mSpinnercourses = findViewById(R.id.spinner_courses);
        mButton_mail = findViewById(R.id.button);
        mButton_Cancel = findViewById(R.id.button3);
        List<CourseInfo> courses = DataManager.getInstance().getCourses();

        ArrayAdapter<CourseInfo> adapterCourses =
                new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, courses);
        adapterCourses.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnercourses.setAdapter(adapterCourses);
readDisplayStateValues();
saveOriginalStateValues();

        mTextnoteTitle = findViewById(R.id.text_note_title);
        mTextnoteText = findViewById(R.id.text_note_text);
        if(!mIsNewNote)
displayNote(mSpinnercourses, mTextnoteTitle, mTextnoteText);

        mButton_mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NoteActivity.this, NoteListActivity.class);
                Toast.makeText(NoteActivity.this, "Note Saved", Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });

        mButton_Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIsCancelling = true;
                finish();
            }
        });
    }

    private void saveOriginalStateValues() {
        if (mIsNewNote)
            return;
       mViewModel.mOriginalNoteCourseId  = mNote.getCourse().getCourseId();
       mViewModel. mOriginalNoteTitle = mNote.getTitle();
       mViewModel. mOriginalNoteText = mNote.getText();


    }


    @Override
    protected void onPause() {
        super.onPause();
        if(mIsCancelling){
            if(mIsNewNote) {
                DataManager.getInstance().removeNote(mNotePosition);
            }
            else
                {
                storePreviousNoteValues();
        }
            }
        else {
            saveNote();
        }
    }


    private void storePreviousNoteValues() {
        CourseInfo course = DataManager.getInstance().getCourse(mViewModel.mOriginalNoteCourseId);
        mNote.setCourse(course);
        mNote.setTitle(mViewModel.mOriginalNoteTitle);
        mNote.setText(mViewModel.mOriginalNoteText);

    }

    private void saveNote() {
mNote.setCourse((CourseInfo) mSpinnercourses.getSelectedItem());
mNote.setTitle(mTextnoteTitle.getText().toString());
mNote.setText(mTextnoteText.getText().toString());
    }

    private void displayNote(Spinner spinnercourses, EditText textnoteTitle, EditText textnoteText) {
List<CourseInfo> courses = DataManager.getInstance().getCourses();
int courseindex = courses.indexOf(mNote.getCourse());
spinnercourses.setSelection(courseindex);
        textnoteTitle.setText(mNote.getTitle());
textnoteText.setText(mNote.getText());
    }

    private void readDisplayStateValues() {
        Intent intent = getIntent();
         int position = intent.getIntExtra(NOTE_POSITION, POSITION_NOT_SET());
        mIsNewNote = position == POSITION_NOT_SET();
        if(mIsNewNote){
            createNewNote();
        }
        else
            mNote = DataManager.getInstance().getNotes().get(position);

    }

    private void createNewNote() {
        DataManager dm = DataManager.getInstance();
        mNotePosition = dm.createNewNote();
        mNote = dm.getNotes().get(mNotePosition);
    }

    public static  int POSITION_NOT_SET() {
        return -1;
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        if(outState !=null)
        mViewModel.saveState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_note,menu);
        return super.onCreateOptionsMenu(menu);
    }

    public  boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        if (id==R.id.action_send_mail){
            sendEmail();
            return true;
        }
        else if (id==R.id.action_cancel){
            mIsCancelling = true;
            finish();
        }

        return super.onOptionsItemSelected(item);

    }

    private void sendEmail() {
CourseInfo course = (CourseInfo) mSpinnercourses.getSelectedItem();
String subject = mTextnoteTitle.getText().toString();
String text = "Checkout what i learned today in this course \"" + course.getTitle() + "\" \n" +
        mTextnoteText.getText().toString();
String email ="kwakye105@gmail.com";
Intent intent = new Intent(Intent.ACTION_SEND);
intent.setType("message/rfc2822");
intent.putExtra(Intent.EXTRA_EMAIL,email);
intent.putExtra(Intent.EXTRA_SUBJECT, subject);
intent. putExtra(Intent.EXTRA_TEXT,text);
startActivity(intent);
    }




}