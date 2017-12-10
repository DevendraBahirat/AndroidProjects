package com.pluralsight.notekeeper;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.List;

public class NoteActivity extends AppCompatActivity {

    public static final String NOTE_POSITION = "com.pluralsight.notekeeper.NOTE_POSITION";
    public static final String ORIGINAL_COURSE_ID = "com.pluralsight.notekeeper.ORIGINAL_COURSE_ID";
    public static final String ORIGINAL_TITLE = "com.pluralsight.notekeeper.ORIGINAL_TITLE";
    public static final String ORIGINAL_CONTENT = "com.pluralsight.notekeeper.ORIGINAL_CONTENT";
    public static final int POSITION_NOT_SET = -1;
    private NoteInfo noteInfo;
    private boolean isNewNote;
    private Spinner spinnerCourses;
    private EditText textTitle;
    private EditText textNoteContent;
    private boolean isCancelling;
    private int newNotePosition;
    private String originalContent;
    private String originalTitle;
    private String originalCourseId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        spinnerCourses = findViewById(R.id.spinner_courses);
        textTitle = findViewById(R.id.text_title);
        textNoteContent = findViewById(R.id.text_note_content);

        List<CourseInfo> courses = DataManager.getInstance().getCourses();
        ArrayAdapter<CourseInfo> courseAdapter = new ArrayAdapter<CourseInfo>(this, android.R.layout.simple_spinner_item, courses);
        courseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinnerCourses.setAdapter(courseAdapter);

        readDisplayContent();
        if(savedInstanceState == null) {
            saveOriginalValues();
        } else {
            restoreOriginalNoteState(savedInstanceState);
        }
        if(!isNewNote) {
            displayNote(spinnerCourses, textTitle, textNoteContent);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(ORIGINAL_COURSE_ID,originalCourseId);
        outState.putString(ORIGINAL_TITLE,originalTitle);
        outState.putString(ORIGINAL_CONTENT,originalContent);
    }

    private void restoreOriginalNoteState(Bundle savedInstanceState) {
        originalCourseId = savedInstanceState.getString(ORIGINAL_COURSE_ID);
        originalTitle = savedInstanceState.getString(ORIGINAL_TITLE);
        originalContent = savedInstanceState.getString(ORIGINAL_CONTENT);
    }

    private void saveOriginalValues() {
        if(isNewNote)
            return;
        originalCourseId = noteInfo.getCourse().getCourseId();
        originalTitle = noteInfo.getTitle();
        originalContent = noteInfo.getText();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_note, menu);
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(isCancelling) {
            if(isNewNote) {
                DataManager.getInstance().removeNote(newNotePosition);
            } else {
                storePreviousValues();
            }
        } else {
            saveNote();
        }
    }

    private void storePreviousValues() {
        noteInfo.setCourse(DataManager.getInstance().getCourse(originalCourseId));
        noteInfo.setTitle(originalTitle);
        noteInfo.setText(originalContent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_send_mail) {
            sendEmail();
            return true;
        } else if(id == R.id.action_discard){
            isCancelling = true;
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void displayNote(Spinner spinnerCourses, EditText textTitle, EditText textNoteContent) {
        List<CourseInfo> courses = DataManager.getInstance().getCourses();
        int coursesIndex = courses.indexOf(noteInfo.getCourse());
        spinnerCourses.setSelection(coursesIndex);
        textTitle.setText(noteInfo.getTitle());
        textNoteContent.setText(noteInfo.getText());
    }

    private void readDisplayContent() {
        Intent intent = getIntent();
        int position = intent.getIntExtra(NOTE_POSITION,POSITION_NOT_SET);
        isNewNote = position == POSITION_NOT_SET;
        if(!isNewNote) {
            noteInfo = DataManager.getInstance().getNotes().get(position);
        } else {
            createNewNote();
        }
    }

    private void saveNote() {
        noteInfo.setCourse((CourseInfo) spinnerCourses.getSelectedItem());
        noteInfo.setTitle(textTitle.getText().toString());
        noteInfo.setText(textNoteContent.getText().toString());
    }

    private void createNewNote() {
        newNotePosition = DataManager.getInstance().createNewNote();
        noteInfo = DataManager.getInstance().getNotes().get(newNotePosition);
    }

    private void sendEmail() {
        CourseInfo courseInfo = (CourseInfo) spinnerCourses.getSelectedItem();
        String subject = textTitle.getText().toString();
        String body = "Checkout what I learned from a pluralsight course \"" + courseInfo.getTitle() + "\"\n" + textNoteContent.getText().toString();
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc2822");
        intent.putExtra(Intent.EXTRA_SUBJECT,subject);
        intent.putExtra(Intent.EXTRA_TEXT,body);
        startActivity(intent);
    }
}
