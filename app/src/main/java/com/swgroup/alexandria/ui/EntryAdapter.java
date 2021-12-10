package com.swgroup.alexandria.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.swgroup.alexandria.R;
import com.swgroup.alexandria.data.database.EntryType;
import com.swgroup.alexandria.data.database.ShelfEntry;

import java.util.ArrayList;
import java.util.List;

public class EntryAdapter extends RecyclerView.Adapter<EntryAdapter.EntryHolder> {
    private List<ShelfEntry> entries = new ArrayList<>();
    private onItemClickListener listener;

    @NonNull
    @Override
    public EntryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.entry_card_item, parent,false);
        return new EntryHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull EntryHolder holder, int position) {
        ShelfEntry currentEntry = entries.get(position);
        holder.textViewTitle.setText(currentEntry.getTitle());
        holder.textViewAuthor.setText(currentEntry.getAuthor());
        holder.textViewGenre.setText(currentEntry.getGenre());
    }

    @Override
    public int getItemCount() {
        return entries.size();
    }

    public void setEntries(List<ShelfEntry> entries) {
        this.entries = entries;
        notifyDataSetChanged();  // TODO: MUST CHANGE IT'S OBSOLETE AND INEFFICIENT
    }

    public ShelfEntry getEntryAt(int position) {
        return entries.get(position);
    }

    class EntryHolder extends RecyclerView.ViewHolder {
        private TextView textViewTitle;
        private TextView textViewAuthor;
        private TextView textViewGenre;
        private ImageView imageViewCover;

        public EntryHolder(View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.card_text_view_title);
            textViewAuthor = itemView.findViewById(R.id.card_text_view_author);
            textViewGenre = itemView.findViewById(R.id.card_text_view_genre);
            imageViewCover = itemView.findViewById(R.id.card_image_view_cover);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if(listener != null && position != RecyclerView.NO_POSITION)
                        listener.onItemClick(entries.get(position));
                }
            });
        }
    }

    public interface onItemClickListener {
        void onItemClick(ShelfEntry shelfEntry);
    }

    public void setOnClickListener(onItemClickListener listener) {
        this.listener = listener;
    }
}
