package com.swgroup.alexandria.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.swgroup.alexandria.R;
import com.swgroup.alexandria.data.database.ShelfEntry;

import java.io.File;
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
        //qui gli diamo la view
        // curretEntry.cover ->
        //          -> file
        //holder.imageViewCover.;
        if(!(currentEntry.cover==null)){
            if(!currentEntry.cover.equals("ic_cover_not_found.png")) {

                File tmp = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/Alexandria/" + currentEntry.getCover());

                Bitmap bitmap = BitmapFactory.decodeFile(tmp.getPath());

                holder.imageViewCover.setImageBitmap(bitmap);
            }else{
                //
                holder.imageViewCover.setImageResource(R.drawable.ic_cover_not_found);
            }
        }
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

        protected EntryHolder(View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.card_text_view_title);
            textViewAuthor = itemView.findViewById(R.id.card_text_view_author);
            textViewGenre = itemView.findViewById(R.id.card_text_view_genre);
            imageViewCover = itemView.findViewById(R.id.card_image_view_cover);

            itemView.setOnClickListener(view -> {
                int position = getAdapterPosition();
                if(listener != null && position != RecyclerView.NO_POSITION)
                    listener.onItemClick(entries.get(position));
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

