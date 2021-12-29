package com.swgroup.alexandria;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;

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
    AudioUtil audioUtil = null;
    MediaPlayer mediaPlayer;
    RecyclerView recyclerView;

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
        recyclerView = binding.itemlist;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        audioChapterAdapter = new AudioChapterAdapter();
        audioChapterAdapter.setEntries(audioUtil.getChapterTitleList());
        binding.itemlist.setAdapter(audioChapterAdapter);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //GETTING STARTED WITH MEDIA PLAYER
       /* mediaPlayer = new MediaPlayer();
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
        }*/
        //END OF MEDIA PLAYER
        audioChapterAdapter.setOnClickListener(string -> {
            LightPosition("WHITE");
            CurrentPosition=audioChapterAdapter.getPositionAt(string);
                    if(mediaPlayer!=null){
                        mediaPlayer.release();
                        mediaPlayer = null;}
                mediaPlayer = MediaPlayer.create(this, getUriFromPosition(CurrentPosition));
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener(){
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        if(mediaPlayer != null)
                        {   mediaPlayer.release();
                            mediaPlayer = null; }}});
            System.out.println("CURRENT POSITION" + CurrentPosition);
            LightPosition("GREY");
            mediaPlayer.start();
        });

        binding.nextChapter.setOnClickListener(next -> {
            LightPosition("WHITE");
            CurrentPosition++;
            if (CurrentPosition >= audioChapterAdapter.getItemCount()){
                CurrentPosition=0; }
                    if(mediaPlayer!=null){
                        mediaPlayer.release();
                        mediaPlayer = null;}
                mediaPlayer = MediaPlayer.create(this, getUriFromPosition(CurrentPosition));
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener(){
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        if(mediaPlayer != null) {
                            mediaPlayer.release();
                            mediaPlayer = null; }}});
            LightPosition("GREY");
            mediaPlayer.start();
        });

        binding.prevChapter.setOnClickListener(prev -> {
            LightPosition("WHITE");
            if (CurrentPosition <= 0){
                CurrentPosition=audioChapterAdapter.getItemCount();}
            CurrentPosition--;
            if(mediaPlayer!=null){
                mediaPlayer.release();
                mediaPlayer = null;}
            mediaPlayer = MediaPlayer.create(this, getUriFromPosition(CurrentPosition));
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener(){
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    if(mediaPlayer != null)
                    {
                        mediaPlayer.release();
                        mediaPlayer = null;
                    }}});
            System.out.println("POS" + CurrentPosition);
            LightPosition("GREY");
            mediaPlayer.start();
        });

        binding.play.setOnClickListener(play -> {
            LightPosition("WHITE");
            if(mediaPlayer==null){
                mediaPlayer = MediaPlayer.create(this, getUriFromPosition(CurrentPosition));
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener(){
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                      if(mediaPlayer != null)
                         {
                             mediaPlayer.release();
                             mediaPlayer = null;
                         }}});}
            LightPosition("GREY");
            mediaPlayer.start();
        });

        binding.pause.setOnClickListener(pause -> {
            LightPosition("WHITE");
            if(mediaPlayer!=null)
                mediaPlayer.pause();
            LightPosition("PAUSE");
        });
    }

    @Override
    protected void onPause(){
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //RELEASING RESOURCES
        if(mediaPlayer != null)
        {
            mediaPlayer.release();
            mediaPlayer = null;
        }
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


    @RequiresApi(api = Build.VERSION_CODES.M)
    protected void LightPosition(String code){

        //Code WHITE: toggle to white
        //Code GREY: toggle to grey
        try{
        View View = (binding.itemlist.findViewHolderForLayoutPosition(CurrentPosition)).itemView.findViewById(R.id.background);
        if(code.equals("WHITE")){View.setBackgroundColor(Color.TRANSPARENT);}
        if(code.equals("GREY")){View.setBackgroundColor(Color.LTGRAY);}
        if(code.equals("PAUSE")){View.setBackgroundColor(Color.rgb(233,236,140));}}catch (Exception ignored){}
    }
}