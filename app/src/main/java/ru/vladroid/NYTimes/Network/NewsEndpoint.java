package ru.vladroid.NYTimes.Network;

import androidx.annotation.NonNull;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import ru.vladroid.NYTimes.DTO.NewsDTO;

public interface NewsEndpoint {

    @GET("topstories/v2/{section}.json")
    Call<NewsDTO> get(@Path("section") @NonNull String section);
}
