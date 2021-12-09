package com.swgroup.alexandria.ui.comics;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ComicViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ComicViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is comic fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}