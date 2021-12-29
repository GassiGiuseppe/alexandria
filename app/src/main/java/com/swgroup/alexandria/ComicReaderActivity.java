package com.swgroup.alexandria;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.swgroup.alexandria.data.internal.EnvDirUtil;
import com.swgroup.alexandria.data.internal.FileUtil;
import com.swgroup.alexandria.databinding.ActivityComicReaderBinding;
import com.swgroup.alexandria.databinding.ActivityMainBinding;
import com.swgroup.alexandria.ui.ComicViewPagerAdapter;
import com.swgroup.alexandria.ui.comic.ComicParser;
import com.swgroup.alexandria.ui.comic.ComicParserFactory;

import java.io.File;
import java.io.IOException;

public class ComicReaderActivity extends AppCompatActivity {
    private ActivityComicReaderBinding binding;
    private ComicViewPagerAdapter adapter;
    private ComicParser comic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getSupportActionBar() != null)
            getSupportActionBar().hide();

        binding = ActivityComicReaderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String comic_location = getIntent().getStringExtra("comic_location");
        comic = ComicParserFactory.create(comic_location);

        adapter = new ComicViewPagerAdapter(this, comic);
        binding.comicViewPager.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        FileUtil.clearTempDir();
        super.onDestroy();
    }
}