package ru.vladroid.NYTimes.ui.list;

import android.os.AsyncTask;
import java.util.ArrayList;
import java.util.List;

abstract class DataLoadTask<T> extends AsyncTask<Object, Void, Boolean> {

    private List<Subscribable<T>> subscribers = new ArrayList<>();

    void subscribe(Subscribable<T> subscriber) {
        subscribers.add(subscriber);
    }

    @Override
    protected void onPreExecute() {
        notifyLoadingStarted();
    }

    @Override
    protected void onPostExecute(Boolean result) {
        if (result) {
            notifyLoadingCompleted();
        }
        else {
            notifyErrorOccurred(null);
        }
    }

    void notifyLoadingSucceed(T data) {
        for (int i = 0; i < subscribers.size(); i++) {
            subscribers.get(i).onNewsLoad(data);
        }
    }

    void notifyLoadingStarted() {
        notifyLoadingStateChanged(LoadingState.Loading);
    }

    void notifyLoadingCompleted() {
        notifyLoadingStateChanged(LoadingState.DataLoaded);
    }

    void notifyErrorOccurred(Exception e) {
        notifyLoadingStateChanged(LoadingState.NetworkError);
        if (e != null) {
            for (int i = 0; i < subscribers.size(); i++) {
                subscribers.get(i).onLoadingFailed(e);
            }
        }
    }

    private void notifyLoadingStateChanged(LoadingState state) {
        for (int i = 0; i < subscribers.size(); i++) {
            subscribers.get(i).onStateChanged(state);
        }
    }
}