package com.swgroup.alexandria.ui.comics;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.swgroup.alexandria.databinding.FragmentComicBinding;

public class ComicFragment extends Fragment {

    private ComicViewModel comicViewModel;
    private FragmentComicBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        comicViewModel =
                new ViewModelProvider(this).get(ComicViewModel.class);

        binding = FragmentComicBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textComic;
        comicViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}