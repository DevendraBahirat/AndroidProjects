package com.pluralsight.notekeeper;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Devendra Bahirat on 12/12/2017.
 */

public class NoteRecyclerAdapter extends RecyclerView.Adapter<NoteRecyclerAdapter.ViewHoler> {

    private final Context mContext;
    private final LayoutInflater mLayoutInflater;
    private final List<NoteInfo> mNotes;

    public NoteRecyclerAdapter(Context context, List<NoteInfo> notes) {
        this.mContext = context;
        this.mNotes = notes;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public ViewHoler onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mLayoutInflater.inflate(R.layout.item_note_list, parent, false);
        return new ViewHoler(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHoler holder, int position) {
        NoteInfo noteInfo = mNotes.get(position);
        holder.textCourse.setText(noteInfo.getCourse().getTitle());
        holder.textTitle.setText(noteInfo.getTitle());
    }

    @Override
    public int getItemCount() {
        return mNotes.size();
    }

    public class ViewHoler extends RecyclerView.ViewHolder {

        public final TextView textCourse;
        public final TextView textTitle;

        public ViewHoler(View itemView) {
            super(itemView);
            textCourse = itemView.findViewById(R.id.card_text_course);
            textTitle = itemView.findViewById(R.id.card_text_title);
        }
    }
}
