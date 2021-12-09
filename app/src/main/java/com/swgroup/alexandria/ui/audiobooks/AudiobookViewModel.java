package com.swgroup.alexandria.ui.audiobooks;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AudiobookViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public AudiobookViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is audiobook fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}