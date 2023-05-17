package com.example.techhelm.ui.statistics;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.GoogleMap;

public class StatisticsViewModel extends ViewModel {

    private final MutableLiveData<String> mText;


    public StatisticsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Statistics fragment");

    }

    public LiveData<String> getText() {
        return mText;
    }
}