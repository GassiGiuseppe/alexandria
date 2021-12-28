package com.swgroup.alexandria;

import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.net.Uri;
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
import com.swgroup.alexandria.ui.AudioChapterAdapter;

import java.io.File;
import java.io.IOException;
import java.net.URI;

public class PlayerActivity extends AppCompatActivity {
    private ActivityPlayerBinding binding;
    private AudioChapterAdapter audioChapterAdapter;
    private int CurrentPosition;
    MediaPlayer mediaPlayer;
    AudioUtil audioUtil = null;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CurrentPosition=0;
        binding = ActivityPlayerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        String audio_location = getIntent().getStringExtra("audio_location");
        try {
            System.out.println("AUDIO LOCATION" + audio_location);
            audioUtil = new AudioUtil(audio_location, this.getApplicationContext(), false);
        RecyclerView recyclerView = binding.itemlist;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        audioChapterAdapter = new AudioChapterAdapter();
        audioChapterAdapter.setEntries(audioUtil.getChapterTitleList());
        binding.itemlist.setAdapter(audioChapterAdapter);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //GETTING STARTED WITH MEDIA PLAYER
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioAttributes(
                new AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
        );
        try {
            mediaPlayer.setDataSource(getApplicationContext(), getUriFromPosition(0));
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.start();
        //END OF MEDIA PLAYER
        audioChapterAdapter.setOnClickListener(string -> {
            CurrentPosition=audioChapterAdapter.getPositionAt(string);
            try {
                if(mediaPlayer.isPlaying())
                    mediaPlayer.stop();
                mediaPlayer.setDataSource(getApplicationContext(), getUriFromPosition(CurrentPosition));
                mediaPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("CURRENT POSITION" +CurrentPosition);
            mediaPlayer.start();
        });

        binding.nextChapter.setOnClickListener(next -> {
            CurrentPosition++;
            if (CurrentPosition > audioChapterAdapter.getItemCount()){
                CurrentPosition=0;}
            try {
                if(mediaPlayer.isPlaying())
                    mediaPlayer.stop();
                mediaPlayer.setDataSource(getApplicationContext(), getUriFromPosition(CurrentPosition));
                mediaPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mediaPlayer.start();
        });

        binding.prevChapter.setOnClickListener(prev -> {
            CurrentPosition--;
            if (CurrentPosition < 0){
                CurrentPosition=audioChapterAdapter.getItemCount();}
            try {
                if(mediaPlayer.isPlaying())
                    mediaPlayer.stop();
                mediaPlayer.setDataSource(getApplicationContext(), getUriFromPosition(CurrentPosition));
                mediaPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mediaPlayer.start();
        });

        binding.play.setOnClickListener(play -> {
            mediaPlayer.start();
        });

        binding.pause.setOnClickListener(pause -> {
            mediaPlayer.pause();
        });
    }

    @Override
    protected void onPause(){
        FileUtil.destroyFile(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/Alexandria" + "temp");
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //mediaPlayer.release();
        //mediaPlayer = null;
    }

    protected Uri getUriFromPosition(int i){
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)+"/Alexandria/temp/"+ audioUtil.getChapterFile(i).getName());
        URI androidUri=file.toURI();
        return new Uri.Builder().scheme(androidUri.getScheme())
                .encodedAuthority(androidUri.getRawAuthority())
                .encodedPath(androidUri.getRawPath())
                .query(androidUri.getRawQuery())
                .fragment(androidUri.getRawFragment())
                .build();
    }
}