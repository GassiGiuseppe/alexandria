package com.swgroup.alexandria.ui.comics;

import android.os.Bundle;
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
import com.swgroup.alexandria.databinding.FragmentComicBinding;
import com.swgroup.alexandria.ui.EntryAdapter;
import com.swgroup.alexandria.ui.ShelfViewModel;

import java.util.List;

public class ComicFragment extends Fragment {

    private ShelfViewModel shelfViewModel;
    private FragmentComicBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentComicBinding
                .inflate(inflater, container, false);

        RecyclerView recyclerView = binding.recyclerViewComics;
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));

        EntryAdapter entryAdapter = new EntryAdapter();
        recyclerView.setAdapter(entryAdapter);

        shelfViewModel =
                new ViewModelProvider(this).get(ShelfViewModel.class);
        // TODO: Questa è solo una prova, dopo sostituisci per EntryType comic
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