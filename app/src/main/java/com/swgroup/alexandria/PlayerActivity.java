package com.swgroup.alexandria;

import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.swgroup.alexandria.data.internal.AudioUtil;
import com.swgroup.alexandria.data.internal.FileUtil;
import com.swgroup.alexandria.databinding.ActivityPlayerBinding;
import com.swgroup.alexandria.databinding.ActivitySplashBinding;
import com.swgroup.alexandria.ui.AudioChapterAdapter;

import java.io.IOException;

public class PlayerActivity extends AppCompatActivity {

    private ActivityPlayerBinding binding;
    private AudioChapterAdapter audioChapterAdapter;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPlayerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        String audio_location = getIntent().getStringExtra("audio_location");
        AudioUtil audioUtil = null;
        try {
            System.out.println("AUDIO LOCATION" + audio_location);
            audioUtil = new AudioUtil(audio_location, this.getApplicationContext());
        RecyclerView recyclerView = binding.itemlist;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        audioChapterAdapter = new AudioChapterAdapter();
        audioChapterAdapter.setEntries(audioUtil.getChapterTitleList());
        binding.itemlist.setAdapter(audioChapterAdapter);
        } catch (IOException e) {
            e.printStackTrace();
        }
        audioChapterAdapter.setOnClickListener(shelfEntry -> {
        });

    }

    @Override
    protected void onDestroy() {
        FileUtil.destroyFile(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/Alexandria" + "temp");
        super.onDestroy();
    }
}