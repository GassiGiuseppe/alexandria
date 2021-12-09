package com.swgroup.alexandria.ui.books;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.swgroup.alexandria.data.database.ShelfEntry;
import com.swgroup.alexandria.data.internal.FileUtil;
import com.swgroup.alexandria.databinding.FragmentBookBinding;
import com.swgroup.alexandria.ui.EntryAdapter;
import com.swgroup.alexandria.ui.ShelfViewModel;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class BookFragment extends Fragment {
    private ShelfViewModel shelfViewModel;
    private FragmentBookBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentBookBinding
                .inflate(inflater, container, false);

        RecyclerView recyclerView = binding.recyclerViewBooks;
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));

        EntryAdapter entryAdapter = new EntryAdapter();
        recyclerView.setAdapter(entryAdapter);

        shelfViewModel =
                new  ViewModelProvider(this).get(ShelfViewModel.class);
        // TODO: Questa è solo una prova, dopo sostituisci per EntryType book
        shelfViewModel.getAllEntries().observe(getViewLifecycleOwner(), new Observer<List<ShelfEntry>>() {
            @Override
            public void onChanged(List<ShelfEntry> shelfEntries) {
                entryAdapter.setEntries(shelfEntries);
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