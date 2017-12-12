package com.pluralsight.notekeeper;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.*;

/**
 * Created by Devendra Bahirat on 12/11/2017.
 */
@RunWith(AndroidJUnit4.class)
public class NoteCreationTest {

    static DataManager dataManager;

    @Rule
    public ActivityTestRule<NoteListActivity> activityTestRule = new ActivityTestRule<NoteListActivity>(NoteListActivity.class);

    @BeforeClass
    public static void setUp() {
        dataManager = DataManager.getInstance();
    }

    @Test
    public void testNoteCreation() {
        CourseInfo courseInfo = dataManager.getCourse("java_lang");
        String noteTitle = "Test Note Title";
        String noteContent = "Test Note Content";
        onView(withId(R.id.fab)).perform(click());
        onView(withId(R.id.spinner_courses)).perform(click());
        onData(allOf(instanceOf(CourseInfo.class),equalTo(courseInfo))).perform(click());
        onView(withId(R.id.text_title)).perform(typeText(noteTitle));
        onView(withId(R.id.text_note_content)).perform(typeText(noteContent), closeSoftKeyboard());
        pressBack();

        int index = dataManager.getNotes().size() - 1;
        NoteInfo noteInfo = dataManager.getNotes().get(index);
        assertEquals(courseInfo,noteInfo.getCourse());
        assertEquals(noteTitle,noteInfo.getTitle());
        assertEquals(noteContent, noteInfo.getText());
    }
}