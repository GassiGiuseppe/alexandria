package com.swgroup.alexandria.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.swgroup.alexandria.R;
import com.swgroup.alexandria.data.database.ShelfEntry;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AudioChapterAdapter  extends RecyclerView.Adapter<AudioChapterAdapter.EntryHolder>{

    private List<String> entries = new ArrayList<>();
    private AudioChapterAdapter.onItemClickListener listener;

    @NonNull
    @Override
    public AudioChapterAdapter.EntryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.audio_player_chapter_item, parent,false);
        return new AudioChapterAdapter.EntryHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AudioChapterAdapter.EntryHolder holder, int position) {
        String currentEntry = entries.get(position);
        holder.chapter_title.setText(currentEntry);
    }

    public void setEntries(List<String> entries) {
        this.entries = entries;
    }

    public String getEntryAt(int position) {
        return entries.get(position);
    }

    public int getPositionAt(String string) {
        return entries.indexOf(string);
    }

    public interface onItemClickListener {
        void onChapterClick(String string) throws IOException;
    }

    @Override
    public int getItemCount() {
        return entries.size();
    }

    class EntryHolder extends RecyclerView.ViewHolder {
        private TextView chapter_title;

        protected EntryHolder(View itemView) {
            super(itemView);
            chapter_title = itemView.findViewById(R.id.chapter_title);

            itemView.setOnClickListener(view -> {
                int position = getAdapterPosition();
                if(listener != null && position != RecyclerView.NO_POSITION) {
                    try {
                        listener.onChapterClick(entries.get(position));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
    public void setOnClickListener(AudioChapterAdapter.onItemClickListener listener) {
        this.listener = listener;
    }
}
