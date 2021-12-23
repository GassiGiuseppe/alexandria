package com.swgroup.alexandria.ui.fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.swgroup.alexandria.PlayerActivity;
import com.swgroup.alexandria.data.database.EntryType;
import com.swgroup.alexandria.data.database.ShelfEntry;
import com.swgroup.alexandria.data.internal.FileUtil;
import com.swgroup.alexandria.databinding.FragmentBookBinding;
import com.swgroup.alexandria.ui.EntryAdapter;
import com.swgroup.alexandria.ui.ShelfViewModel;
import com.swgroup.alexandria.ui.reader.ReaderActivity;

public class ShelfFragment extends Fragment {

    protected ShelfViewModel shelfViewModel;
    protected FragmentBookBinding binding;
    protected AlertDialog.Builder alertDiag;
    protected EntryAdapter entryAdapter;

    public EntryAdapter setShelfViewModelAndEntryAdapter() {
        entryAdapter = new EntryAdapter();
        binding.recyclerViewBooks.setAdapter(entryAdapter);

        shelfViewModel =
                new  ViewModelProvider(this).get(ShelfViewModel.class);

        shelfViewModel.getAllEntries().observe(getViewLifecycleOwner(),
                shelfEntries -> entryAdapter.setEntries(shelfEntries));

        return entryAdapter;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentBookBinding
                .inflate(inflater, container, false);

        RecyclerView recyclerView = binding.recyclerViewBooks;
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));

        entryAdapter = setShelfViewModelAndEntryAdapter();

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                if(direction == ItemTouchHelper.RIGHT) {
                    alertDiag = new AlertDialog.Builder(getContext());
                    alertDiag.setTitle("Shelf Entry Deletion");
                    alertDiag.setMessage("Do you really want to delete this entry? It will be lost forever!");
                    alertDiag.setIcon(android.R.drawable.ic_dialog_alert);
                    alertDiag.setPositiveButton(android.R.string.yes, (dialog, whichButton) -> {
                        FileUtil.deleteBookFile(entryAdapter.getEntryAt(viewHolder.getAdapterPosition()));
                        //entryAdapter.getEntryAt(viewHolder.getAdapterPosition());///QUI

                        shelfViewModel.delete(entryAdapter.getEntryAt(viewHolder.getAdapterPosition()));
                    });
                    alertDiag.setNegativeButton(android.R.string.no, (dialogInterface, whichButton) -> recyclerView.setAdapter(entryAdapter));
                    alertDiag.show();
                }
                else if (direction == ItemTouchHelper.LEFT) {
                    alertDiag = new AlertDialog.Builder(getContext());
                    alertDiag.setIcon(android.R.drawable.ic_dialog_alert);
                    alertDiag.setNegativeButton(android.R.string.no, (dialogInterface, whichButton) -> recyclerView.setAdapter(entryAdapter));

                    if(entryAdapter.getEntryAt(viewHolder.getAdapterPosition()).getFavorite()) {
                        alertDiag.setTitle("Unmark favorite");
                        alertDiag.setMessage("Do you want to remove this entry from the favorites?");
                        alertDiag.setPositiveButton(android.R.string.yes, (dialog, whichButton) -> {
                            ShelfEntry entry = entryAdapter.getEntryAt(viewHolder.getAdapterPosition());
                            entry.setFavorite(false);
                            shelfViewModel.update(entry);
                        });

                    } else {
                        alertDiag.setTitle("Mark favorite");
                        alertDiag.setMessage("Do you want to add this entry to the favorites?");
                        alertDiag.setPositiveButton(android.R.string.yes, (dialog, whichButton) -> {
                            ShelfEntry entry = entryAdapter.getEntryAt(viewHolder.getAdapterPosition());
                            entry.setFavorite(true);
                            shelfViewModel.update(entry);
                        });
                    }
                    alertDiag.show();

                }
            }

            @Override
            public void onChildDraw (Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive){
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }

        }).attachToRecyclerView(recyclerView);

        entryAdapter.setOnClickListener(shelfEntry -> {
            if (shelfEntry.datatype==EntryType.Book){
                System.out.println("SHELF ENTRY BOOK");
                Intent intent = new Intent(getActivity() , ReaderActivity.class);
                intent.putExtra("epub_location", shelfEntry.getFile());
                System.out.println("FILE:  :::: BOOK" + shelfEntry.getFile());
                startActivity(intent);
            } else if(shelfEntry.datatype==EntryType.Audiobook){
                System.out.println("SHELF ENTRY AUDIO");
                Intent intent = new Intent(getActivity() , PlayerActivity.class);
                intent.putExtra("audio_location", shelfEntry.getFile());
                System.out.println("FILE:  :::: AUDIO" + shelfEntry.getFile());
                startActivity(intent);
            }
        });
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}