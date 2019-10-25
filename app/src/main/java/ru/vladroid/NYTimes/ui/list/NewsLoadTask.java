package ru.vladroid.NYTimes.ui.list;

import android.util.Log;

import java.io.IOException;
import java.util.List;

import ru.vladroid.NYTimes.DTO.NewsDTO;
import ru.vladroid.NYTimes.DTO.Result;
import ru.vladroid.NYTimes.Network.RestAPI;

public class NewsLoadTask extends DataLoadTask<List<Result>> {

    private String section;

    NewsLoadTask(String section) {
        this.section = section;
    }

    void setSection(String section) {
        this.section = section;
    }

    @Override
    protected Boolean doInBackground(Object... voids) {
        try {
            NewsDTO newsDTO = RestAPI.getInstance().newsEndpoint().get(section).execute().body();
            notifyLoadingSucceed(newsDTO.getResults());
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
            notifyErrorOccurred(e);
            return false;
        }
        return true;
    }
}
