package com.pluralsight.notekeeper;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

public class NoteListActivity extends AppCompatActivity {

    //private ArrayAdapter<NoteInfo> notesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list);
        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(NoteListActivity.this,NoteActivity.class));
            }
        });

        initializeDisplayContent();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //notesAdapter.notifyDataSetChanged();
    }

    private void initializeDisplayContent() {
        /*final ListView listNotes = findViewById(R.id.list_notes);
        List<NoteInfo> notes = DataManager.getInstance().getNotes();
        notesAdapter = new ArrayAdapter<NoteInfo>(this, android.R.layout.simple_list_item_1,notes);
        listNotes.setAdapter(notesAdapter);

        listNotes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(NoteListActivity.this, NoteActivity.class);
                intent.putExtra(NoteActivity.NOTE_POSITION,position);
                startActivity(intent);
            }
        });*/
        RecyclerView notesRecycler = findViewById(R.id.list_notes);
        LinearLayoutManager notesLayoutManager = new LinearLayoutManager(this);
        notesRecycler.setLayoutManager(notesLayoutManager);

        List<NoteInfo> notes = DataManager.getInstance().getNotes();
        final NoteRecyclerAdapter noteRecyclerAdapter = new NoteRecyclerAdapter(this,notes);
        notesRecycler.setAdapter(noteRecyclerAdapter);

    }

}
