package com.swgroup.alexandria.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import com.swgroup.alexandria.data.database.EntryType;
import com.swgroup.alexandria.ui.EntryAdapter;
import com.swgroup.alexandria.ui.ShelfViewModel;


public class AudiobookFragment extends ShelfFragment {

    @Override
    public EntryAdapter setShelfViewModelAndEntryAdapter() {
        entryAdapter = new EntryAdapter();
        binding.recyclerViewBooks.setAdapter(entryAdapter);

        shelfViewModel =
                new  ViewModelProvider(this).get(ShelfViewModel.class);

        shelfViewModel.getByDataType(EntryType.Audiobook).observe(getViewLifecycleOwner(),
                shelfEntries -> entryAdapter.setEntries(shelfEntries));

        return entryAdapter;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}