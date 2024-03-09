package com.sutd.t4app.ui.reviews;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ReviewsViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public ReviewsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is reviews fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}